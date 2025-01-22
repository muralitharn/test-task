package softsuave.placeOrder.Services.RoleBasedImplementation;
import aj.org.objectweb.asm.ConstantDynamic;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Repository.OrdersTabelRepository;
import softsuave.placeOrder.Services.PlacesOrderServices;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AdminImplementation implements FactortyResponse {

    public final Logger LOG = LoggerFactory.getLogger(AdminImplementation.class);

    @Autowired
    PlacesOrderServices placesOrder;

    @Autowired
    OrdersTabelRepository placedordersRepository;

    @Override
    public byte[] Placeorder(Object object) throws Exception {
        OrdersTabel.orderDto dto = (OrdersTabel.orderDto) object;
        // PlacedOrdersTabel placedOrdersTabel = placesOrder.PlacedOrderDto(dto);
        Date time = Calendar.getInstance().getTime();
        List<OrdersTabel> PlacedOrdersTabelDb = placedordersRepository.findByCreationDate(time);
        return createExcelData(PlacedOrdersTabelDb, new StringBuilder("2.5"), "test task");
    }


    public byte[] createExcelData(List<OrdersTabel> placeOrderlist, StringBuilder excelFileSize,
                                  String password) throws Exception {

        try (Workbook workbook = new XSSFWorkbook()) {


            ConstantDynamic time;
            Sheet sheet = workbook.createSheet("orders places on  " + Calendar.getInstance().getTime());
            Row headerRow = sheet.createRow(0);
            String[] headers = new String[]{"orderNo", "OrderStatus", "orderCancellation", "customerId",
                    " numberOfAdsChosen", "totalCost", "days", "customerName", "isUpdatedtoSuperuser", "creationDate"};


            CellStyle headerStyle = workbook.createCellStyle();
            ((CellStyle) headerStyle).setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);


            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            for (int i = 0; i < placeOrderlist.size(); i++) {
                OrdersTabel placedOrders = placeOrderlist.get(i);
                Row row = sheet.createRow(i + 1);


                createAndSetCell(row, 0, placedOrders.getOrderNo());
                createAndSetCell(row, 1, placedOrders.isOrderStatus());
                createAndSetCell(row, 2, placedOrders.isOrderCancellation());
                createAndSetCell(row, 3, placedOrders.getCustomerId());
                createAndSetCell(row, 4, placedOrders.getAdsCost().size());
                createAndSetCell(row, 5, placedOrders.getTotalCost());
                createAndSetCell(row, 6, placedOrders.getDays());
                createAndSetCell(row, 7, placedOrders.getCustomerName());
                createAndSetCell(row, 8, placedOrders.isUpdatedtoSuperuser());
                createAndSetCell(row, 9, placedOrders.getCreationDate());


                for (int j = 0; j < headers.length; j++) {
                    sheet.autoSizeColumn(j);
                }
            }

            byte[] workbookBytes;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                workbookBytes = out.toByteArray();
            }

            try (POIFSFileSystem fs = new POIFSFileSystem()) {
                EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
                Encryptor encryptor = info.getEncryptor();

                encryptor.confirmPassword(password);

                try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(workbookBytes));
                     OutputStream os = encryptor.getDataStream(fs)) {
                    opc.save(os);
                }


                byte[] encryptedExcelBytes;
                try (ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream()) {
                    fs.writeFilesystem(encryptedOut);
                    encryptedExcelBytes = encryptedOut.toByteArray();
                }
                excelFileSize.append(encryptedExcelBytes.length);
                LOG.info("encrypted file generated "+Calendar.getInstance().getTime());
                return encryptedExcelBytes;
            }

        } catch (Exception e) {
            LOG.info(e.getMessage());
            return new byte[0];

        }
    }

    private void createAndSetCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            // Optional: Format the date to your desired format
            CellStyle dateCellStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper creationHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            cell.setCellStyle(dateCellStyle);
            cell.setCellValue((Date) value);
        } else if (value != null) {
            cell.setCellValue(value.toString());
        }
    }

}
