package softsuave.placeOrder.Services.PaymentFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Model.PaymentDetails;
import softsuave.placeOrder.Repository.OrdersTabelRepository;
import softsuave.placeOrder.Repository.PaymentDetailsRepo;
import softsuave.placeOrder.Services.DocumentProvider;
import softsuave.placeOrder.config.Customer;
import java.util.List;

import static softsuave.placeOrder.Services.DocumentProvider.createExcelData;


@Service
public class DebitcardModePayment implements PaymentModeFactory {



    private OrdersTabelRepository odersTabelRepo;

    private PaymentDetailsRepo paymentDetailsRepo;

    private RestTemplate restTemplate;

    private DocumentProvider documentMaker;


    @Autowired
    public DebitcardModePayment(OrdersTabelRepository  ordersTabelRepository, RestTemplate restTemplate, PaymentDetailsRepo paymentDetailsRepo
            ,DocumentProvider documentMaker) {
        this.odersTabelRepo = ordersTabelRepository;
        this.restTemplate = restTemplate;
        this.paymentDetailsRepo = paymentDetailsRepo;
        this.documentMaker=documentMaker;
    }


    @Override
    public boolean payment(PaymentDetails paymentDetails,int LoanTenure) throws PlacedOrderException {
        try {
            Customer Customer = getCustomerfromId(paymentDetails.getCustomerID());
            if (Customer == null) {
                throw new PlacedOrderException("invalid customer id");
            }
            List<OrdersTabel> ordersTabel = odersTabelRepo.findByCustomerId(paymentDetails.getCustomerID());
            paymentDetails.setFileData(createExcelData(ordersTabel, new StringBuilder("3.5"), ordersTabel.get(0).getCustomerName()));
            paymentDetailsRepo.save(paymentDetails);
            documentMaker.saveToSystem(ordersTabel
                    ,createExcelData(ordersTabel, new StringBuilder("3.5"), ordersTabel.get(0).getCustomerName())
            ,"xlsx");
            return isPaymentCompleted();

        } catch (Exception message) {
            throw new PlacedOrderException(message.getMessage());
        }
    }


    public Customer getCustomerfromId(int customerID) throws JsonProcessingException {
        String urlForSaveOrder = "http://localhost:8081/customer-api/get-customer/" + customerID;
        String customer = this.restTemplate.getForObject(urlForSaveOrder, String.class);
        Customer Response = new ObjectMapper().readValue(customer, Customer.class);
        return Response;
    }

    public boolean isPaymentCompleted(){
        String paymenturl="http://localhost:8082/AdsCosting-api/is-payment/completed";
        String paymentDone= this.restTemplate.getForObject(paymenturl, String.class);
        boolean response=paymentDone.equals("true")?true:false;
        return response;
    }




}
