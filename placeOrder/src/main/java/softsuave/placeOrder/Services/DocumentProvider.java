package softsuave.placeOrder.Services;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.OrdersTabel;

import java.io.*;
import java.util.List;

@Service
public class DocumentProvider {

    public static byte[] wordTypeData(List<OrdersTabel> ordersList, StringBuilder wordFileSize, String password) throws Exception {
        try (XWPFDocument document = new XWPFDocument()) {
            for (OrdersTabel Orders : ordersList) {
                // Create Title for each order
                XWPFParagraph title = document.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                titleRun.setText("Ads Cost Detail of "+ Orders.getCustomerName());
                titleRun.setBold(true);
                titleRun.setFontSize(16);

                // Adding some space after the title
                XWPFParagraph spacer = document.createParagraph();
                spacer.createRun().addBreak();

                // Add bill-like details for each order
                addBillDetail(document, "Order Number: ", String.valueOf(Orders.getOrderNo()));
                addBillDetail(document, "Order Status: ", Orders.isOrderStatus() ? "Confirmed" : "Pending");
                addBillDetail(document, "Order Cancellation: ", Orders.isOrderCancellation() ? "Yes" : "No");
                addBillDetail(document, "Customer ID: ", String.valueOf(Orders.getCustomerId()));
                addBillDetail(document, "Number of Ads Chosen: ", String.valueOf(Orders.getAdsCost().size()));
                addBillDetail(document, "Total Cost: ", String.valueOf(Orders.getTotalCost()));
                addBillDetail(document, "Days: ", String.valueOf(Orders.getDays()));
                addBillDetail(document, "Customer Name: ", Orders.getCustomerName());
                addBillDetail(document, "Updated to Superuser: ", Orders.isUpdatedtoSuperuser() ? "Yes" : "No");
                addBillDetail(document, "Creation Date: ", Orders.getCreationDate().toString());

                // Add space between orders in the document
                XWPFParagraph paragraphBreak = document.createParagraph();
                paragraphBreak.createRun().addBreak();
                paragraphBreak.createRun().addBreak(); // Extra break for space
            }

            // Write the document to a byte array
            byte[] documentBytes;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                document.write(out);
                documentBytes = out.toByteArray();
            }

            // Password protection
            try (POIFSFileSystem fs = new POIFSFileSystem()) {
                EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
                Encryptor encryptor = info.getEncryptor();
                encryptor.confirmPassword(password);

                try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(documentBytes));
                     OutputStream os = encryptor.getDataStream(fs)) {
                    opc.save(os);
                }

                // Write the encrypted file to a byte array
                byte[] encryptedDocBytes;
                try (ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream()) {
                    fs.writeFilesystem(encryptedOut);
                    encryptedDocBytes = encryptedOut.toByteArray();
                }

                wordFileSize.append(encryptedDocBytes.length);
                return encryptedDocBytes;
            }

        } catch (Exception e) {
            throw new Exception("Error while generating the document", e);
        }
    }

    private static void addBillDetail(XWPFDocument document, String label, String value) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        run.setBold(true);
        run.setText(label);
        run = paragraph.createRun();
        run.setText(value);
        run.addBreak();
    }

    public static byte[] createExcelData(List<OrdersTabel> ordersList, StringBuilder fileSize, String password) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Create a sheet in the workbook
            Sheet sheet = workbook.createSheet("Orders Data");

            // Create header row
            String[] headers = new String[]{"Order No", "Order Status", "Order Cancellation", "Customer ID",
                    "Number of Ads Chosen", "Total Cost", "Days", "Customer Name", "Updated to Superuser", "Creation Date"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                createAndSetCell(headerRow, i, headers[i]);
            }

            // Fill data rows
            int rowNum = 1;
            for (OrdersTabel ordersTabel : ordersList) {
                Row row = sheet.createRow(rowNum++);
                createAndSetCell(row, 0, ordersTabel.getOrderNo());
                createAndSetCell(row, 1, ordersTabel.isOrderStatus());
                createAndSetCell(row, 2, ordersTabel.isOrderCancellation());
                createAndSetCell(row, 3, ordersTabel.getCustomerId());
                createAndSetCell(row, 4, ordersTabel.getAdsCost().size());
                createAndSetCell(row, 5, ordersTabel.getTotalCost());
                createAndSetCell(row, 6, ordersTabel.getDays());
                createAndSetCell(row, 7, ordersTabel.getCustomerName());
                createAndSetCell(row, 8, ordersTabel.isUpdatedtoSuperuser());
                createAndSetCell(row, 9, ordersTabel.getCreationDate());
            }

            // Autosize all columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to a byte array
            byte[] excelBytes;
            try (org.apache.commons.io.output.ByteArrayOutputStream out = new org.apache.commons.io.output.ByteArrayOutputStream()) {
                workbook.write(out);
                excelBytes = out.toByteArray();
            }

            // Set file size in StringBuilder
            fileSize.append(excelBytes.length);
            return excelBytes;

        } catch (Exception e) {
            throw new Exception("Error generating Excel file", e);
        }
    }

    private static void createAndSetCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            CellStyle dateCellStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper creationHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            cell.setCellStyle(dateCellStyle);
            cell.setCellValue((java.util.Date) value);
        } else if (value != null) {
            cell.setCellValue(value.toString());
        }
    }

    public static byte[] wordTypeDataForEMI(List<OrdersTabel> ordersList, StringBuilder wordFileSize, String password, int cost, int intrest, int tenureYear, int EMI) throws Exception {
        try (XWPFDocument document = new XWPFDocument()) {
            for (OrdersTabel Orders : ordersList) {

                XWPFParagraph title = document.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                titleRun.setText("Ads Cost Detail of "+ Orders.getCustomerName());
                titleRun.setBold(true);
                titleRun.setFontSize(16);

                // Adding some space after the title
                XWPFParagraph spacer = document.createParagraph();
                spacer.createRun().addBreak();

                // Add bill-like details for each order
                addBillDetailEMI(document, "Order Number: ", String.valueOf(Orders.getOrderNo()));
                addBillDetailEMI(document, "Order Status: ", Orders.isOrderStatus() ? "Confirmed" : "Pending");
                addBillDetailEMI(document, "Order Cancellation: ", Orders.isOrderCancellation() ? "Yes" : "No");
                addBillDetailEMI(document, "Customer ID: ", String.valueOf(Orders.getCustomerId()));
                addBillDetailEMI(document, "Number of Ads Chosen: ", String.valueOf(Orders.getAdsCost().size()));
                addBillDetailEMI(document, "Total Cost: ", String.valueOf(Orders.getTotalCost()));
                addBillDetailEMI(document, "Days: ", String.valueOf(Orders.getDays()));
                addBillDetailEMI(document, "Customer Name: ", Orders.getCustomerName());
                addBillDetailEMI(document, "Updated to Superuser: ", Orders.isUpdatedtoSuperuser() ? "Yes" : "No");
                addBillDetailEMI(document, "Creation Date: ", Orders.getCreationDate().toString());
                addBillDetailEMI(document, "Total Cost: ", String.valueOf(cost));
                addBillDetailEMI(document, "Intrest rate: ", String.valueOf(intrest));
                addBillDetailEMI(document, "chosen tenure years : ", String.valueOf(tenureYear));
                addBillDetailEMI(document, "EMI: ", String.valueOf(EMI));

                // Add space between orders in the document
                XWPFParagraph paragraphBreak = document.createParagraph();
                paragraphBreak.createRun().addBreak();
                paragraphBreak.createRun().addBreak(); // Extra break for space
            }

            // Write the document to a byte array
            byte[] documentBytes;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                document.write(out);
                documentBytes = out.toByteArray();
            }

            // Password protection
            try (POIFSFileSystem fs = new POIFSFileSystem()) {
                EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
                Encryptor encryptor = info.getEncryptor();
                encryptor.confirmPassword(password);

                try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(documentBytes));
                     OutputStream os = encryptor.getDataStream(fs)) {
                    opc.save(os);
                }

                // Write the encrypted file to a byte array
                byte[] encryptedDocBytes;
                try (ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream()) {
                    fs.writeFilesystem(encryptedOut);
                    encryptedDocBytes = encryptedOut.toByteArray();
                }

                wordFileSize.append(encryptedDocBytes.length);
                return encryptedDocBytes;
            }

        } catch (Exception e) {
            throw new Exception("Error while generating the document", e);
        }
    }

    private static void addBillDetailEMI(XWPFDocument document, String label, String value) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        run.setBold(true);
        run.setText(label);
        run = paragraph.createRun();
        run.setText(value);
        run.addBreak();
    }

    public void saveToSystem(List<OrdersTabel> ordersList,byte[] DocumentType,String docxType) throws PlacedOrderException {
        String dotExtesion=docxType.equalsIgnoreCase("docx")?".docx":".xlsx";
        File file = new File("C:/Users/softsuave/Desktop/all doc for project test task"+ File.separator +ordersList.get(0).getCustomerName() +dotExtesion);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(DocumentType);
            fos.flush();
        } catch (IOException e) {
            throw new PlacedOrderException("could not save it in your system");
        }
    }
//    public static byte[] createExcelData(OrdersTabel placedOrdersTabel, StringBuilder excelFileSize, String password) throws Exception {
//        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
//            // Create Sheet
//            Sheet sheet = workbook.createSheet("Ads Cost Detail");
//
//            // Create Title Row
//            Row titleRow = sheet.createRow(0);
//            Cell titleCell = titleRow.createCell(0);
//            titleCell.setCellValue("Ads Cost Detail for " + placedOrdersTabel.getCustomerName());
//
//            // Apply styles to title if needed
//            CellStyle titleStyle = workbook.createCellStyle();
//            Font titleFont = workbook.createFont();
//            titleFont.setBold(true);
//            titleFont.setFontHeightInPoints((short) 16);
//            titleStyle.setFont(titleFont);
//            titleCell.setCellStyle(titleStyle);
//
//            // Skip a row after the title
//            Row emptyRow = sheet.createRow(1);
//
//            // Create Header Row
//            String[] headers = new String[]{"Order No", "Order Status", "Order Cancellation", "Customer ID",
//                    "Number of Ads Chosen", "Total Cost", "Days", "Customer Name", "Updated to Superuser", "Creation Date"};
//            Row headerRow = sheet.createRow(2);
//
//            for (int i = 0; i < headers.length; i++) {
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(headers[i]);
//                // Optionally style the headers
//                CellStyle headerStyle = workbook.createCellStyle();
//                Font headerFont = workbook.createFont();
//                headerFont.setColor(IndexedColors.GREY_25_PERCENT.getIndex());
//                headerFont.setBold(true);
//                headerStyle.setFont(headerFont);
//                cell.setCellStyle(headerStyle);
//            }
//
//            // Create Data Row
//            Row dataRow = sheet.createRow(3);
//            dataRow.createCell(0).setCellValue(String.valueOf(placedOrdersTabel.getOrderNo()));
//            dataRow.createCell(1).setCellValue(placedOrdersTabel.isOrderStatus() ? "Confirmed" : "Pending");
//            dataRow.createCell(2).setCellValue(placedOrdersTabel.isOrderCancellation() ? "Yes" : "No");
//            dataRow.createCell(3).setCellValue(placedOrdersTabel.getCustomerId());
//            dataRow.createCell(4).setCellValue(placedOrdersTabel.getAdsCost().size());
//            dataRow.createCell(5).setCellValue(placedOrdersTabel.getTotalCost());
//            dataRow.createCell(6).setCellValue(placedOrdersTabel.getDays());
//            dataRow.createCell(7).setCellValue(placedOrdersTabel.getCustomerName());
//            dataRow.createCell(8).setCellValue(placedOrdersTabel.isUpdatedtoSuperuser() ? "Yes" : "No");
//            dataRow.createCell(9).setCellValue(placedOrdersTabel.getCreationDate().toString());
//
//            // Auto-size all columns
//            for (int i = 0; i < headers.length; i++) {
//                sheet.autoSizeColumn(i);
//            }
//
//            // Write workbook to a byte array
//            byte[] documentBytes;
//            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                workbook.write(out);
//                documentBytes = out.toByteArray();
//            }
//
//            // Encrypt the file with a password
//            try (POIFSFileSystem fs = new POIFSFileSystem()) {
//                EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
//                Encryptor encryptor = info.getEncryptor();
//                encryptor.confirmPassword(password);
//
//                try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(documentBytes));
//                     OutputStream os = encryptor.getDataStream(fs)) {
//                    opc.save(os);
//                }
//
//                // Write the encrypted file to a byte array
//                byte[] encryptedDocBytes;
//                try (ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream()) {
//                    fs.writeFilesystem(encryptedOut);
//                    encryptedDocBytes = encryptedOut.toByteArray();
//                }
//
//                excelFileSize.append(encryptedDocBytes.length);
//                return encryptedDocBytes;
//            }
//
//        } catch (Exception e) {
//            throw new Exception(" arumai da ..niee dhan da driver hu!!.... unaiayy nami dhan da project kudukanumm", e);
//        }
//    }
}
