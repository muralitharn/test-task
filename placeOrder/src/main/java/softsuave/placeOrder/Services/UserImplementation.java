package softsuave.placeOrder.Services;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import softsuave.placeOrder.Model.PlacedOrdersTabel;
import softsuave.placeOrder.Repository.placedOrdersRepository;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserImplementation implements FactortyResponse {

    @Autowired
    placedOrdersRepository placedordersRepository;

    @Autowired
    PlacesOrder placesOrder;

    @Override
    public byte[] Placeorder(Object object) {
        PlacedOrdersTabel.orderDto dto = (PlacedOrdersTabel.orderDto) object;
        PlacedOrdersTabel placedOrdersTabel = placesOrder.PlacedOrderDto(dto);
       return  createWordData(placedOrdersTabel,new StringBuilder("2.5"),placedOrdersTabel.getCustomerName().toUpperCase());
    }


    public static byte[] createWordData(PlacedOrdersTabel placedOrdersTabel, StringBuilder wordFileSize, String password) {
        try (XWPFDocument document = new XWPFDocument()) {

            // Create Title
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("ads cost detail for "+placedOrdersTabel.getCustomerName());
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            // Create table
            XWPFTable table = document.createTable();

            // Create header row
            String[] headers = new String[]{"orderNo", "OrderStatus", "orderCancellation", "customerId",
                    " numberOfAdsChosen", "totalCost", "days", "customerName", "isUpdatedtoSuperuser", "creationDate"};

            XWPFTableRow headerRow = table.getRow(0); // First row is header
            for (String header : headers) {
                headerRow.addNewTableCell().setText(header);
            }

            // Fill data rows

                XWPFTableRow row = table.createRow();

                row.getCell(0).setText(String.valueOf(placedOrdersTabel.getOrderNo()));
                row.getCell(1).setText(String.valueOf(placedOrdersTabel.isOrderStatus()));
                row.getCell(2).setText(String.valueOf(placedOrdersTabel.isOrderCancellation()));
                row.getCell(3).setText(String.valueOf(placedOrdersTabel.getCustomerId()));
                row.getCell(4).setText(String.valueOf(placedOrdersTabel.getAdsCost().size()));
                row.getCell(5).setText(String.valueOf(placedOrdersTabel.getTotalCost()));
                row.getCell(6).setText(String.valueOf(placedOrdersTabel.getDays()));
                row.getCell(7).setText(placedOrdersTabel.getCustomerName());
                row.getCell(8).setText(String.valueOf(placedOrdersTabel.isUpdatedtoSuperuser()));
                row.getCell(9).setText(placedOrdersTabel.getCustomerName());
                // and so on for other columns


            // Write the document to a byte array
            byte[] documentBytes;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                document.write(out);
                documentBytes = out.toByteArray();
            }

            // password - security
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
            //LOG.error("** UGRO EOD SCHEDULER ** Exception : ", e);
        }
        return new byte[0];
    }
}
