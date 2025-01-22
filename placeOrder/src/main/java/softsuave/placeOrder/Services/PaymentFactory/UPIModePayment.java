package softsuave.placeOrder.Services.PaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Model.PaymentDetails;
import softsuave.placeOrder.Repository.OrdersTabelRepository;
import softsuave.placeOrder.Repository.PaymentDetailsRepo;
import softsuave.placeOrder.Services.PlacesOrderServices;
import softsuave.placeOrder.config.Customer;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class UPIModePayment implements PaymentModeFactory {

    private PlacesOrderServices PlacesOrder;

    private OrdersTabelRepository odersTabelRepo;

    private PaymentDetailsRepo paymentDetailsRepo;

    private RestTemplate restTemplate;

    @Autowired
    public UPIModePayment(PlacesOrderServices placesOrder, OrdersTabelRepository odersTabelRepo, PaymentDetailsRepo paymentDetailsRepo
    ,RestTemplate restTemplate) {
        PlacesOrder = placesOrder;
        this.odersTabelRepo = odersTabelRepo;
        this.paymentDetailsRepo = paymentDetailsRepo;
        this.restTemplate=restTemplate;
    }


    @Override
    public boolean payment(PaymentDetails paymentDetails, int LoanTenure) throws PlacedOrderException {
        try {
            Customer Customer = PlacesOrder.RestTemplateCall(paymentDetails.getCustomerID());
            if (Customer == null) {
                throw new PlacedOrderException("invalid customer id");
            }
            List<OrdersTabel> ordersTabel = odersTabelRepo.findByCustomerId(paymentDetails.getCustomerID());
            paymentDetailsRepo.save(paymentDetails);
            return isPaymentCompleted();
        } catch (Exception message) {
            throw new PlacedOrderException(message.getMessage());
        }

    }


    public static byte[] createWordData(List<OrdersTabel> ordersTabel, StringBuilder wordFileSize) throws IOException, PlacedOrderException {

        if (ordersTabel != null) {
            // Load the image from classpath or filesystem
            ClassPathResource imgFile = new ClassPathResource(wordFileSize.toString());

            // Convert the image to byte array
            byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());

            // Create the response with image as byte array
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return imageBytes;
        } else {
            throw new PlacedOrderException("wrong customer id");
        }
    }

    public boolean isPaymentCompleted(){
        String paymenturl="http://localhost:8082/AdsCosting-api/is-payment/completed";
        String paymentDone= this.restTemplate.getForObject(paymenturl, String.class);
        boolean response=paymentDone.equals("true")?true:false;
        return response;
    }

//    public static  byte[]getPicture(List<OrdersTabel> ordersTabel ,StringBuilder imgPath) throws FileNotFoundException {
//        File imgFile = new File(imgPath);
//
//        // InputStream to read the image file
//        FileInputStream fis = new FileInputStream(imgFile);
//
//        // Prepare HTTP headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG); // You can change to IMAGE_PNG for PNG files
//        headers.setContentLength(imgFile.length());
//        return new InputStreamResource(fis);
//    }
}