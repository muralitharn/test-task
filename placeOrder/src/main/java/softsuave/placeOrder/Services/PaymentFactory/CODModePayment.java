package softsuave.placeOrder.Services.PaymentFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Model.PaymentDetails;
import softsuave.placeOrder.Repository.OrdersTabelRepository;
import softsuave.placeOrder.Repository.PaymentDetailsRepo;
import softsuave.placeOrder.Services.DocumentProvider;
import softsuave.placeOrder.config.Customer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;



@Service
@NoArgsConstructor
public class CODModePayment implements PaymentModeFactory {

    private OrdersTabelRepository odersTabelRepo;

    private RestTemplate restTemplate;

    private PaymentDetailsRepo paymentDetailsRepo;

    private DocumentProvider documentMaker;

    @Autowired
    public CODModePayment(OrdersTabelRepository  ordersTabelRepository,RestTemplate restTemplate,PaymentDetailsRepo paymentDetailsRepo,DocumentProvider documentMaker) {
        this.odersTabelRepo = ordersTabelRepository;
        this.restTemplate = restTemplate;
        this.paymentDetailsRepo = paymentDetailsRepo;
        this.documentMaker=documentMaker;
    }



    @Override
    public boolean payment(PaymentDetails paymentDetails, int LoanTenure) throws PlacedOrderException {
        try {

            Customer customer = makeRequestWithHeaders1(paymentDetails.getCustomerID());
            if (customer == null) {
                throw new PlacedOrderException("invalid customer id");
            }
           List<OrdersTabel> ordersList= odersTabelRepo.findByCustomerId(paymentDetails.getCustomerID());

            paymentDetails.setFileData(DocumentProvider.wordTypeData(ordersList, new StringBuilder("3.5"),ordersList.get(0).getCustomerName() ));
            PaymentDetails paymentDetails1 = paymentDetailsRepo.save(paymentDetails);
            documentMaker.saveToSystem(ordersList
                    ,DocumentProvider.wordTypeData(ordersList, new StringBuilder("3.5"), ordersList.get(0).getCustomerName())
                    ,"docx");

            return isPaymentCompleted();
        } catch (Exception message) {
            throw new PlacedOrderException(message.getMessage());
        }
    }

    public Customer makeRequestWithHeaders1(int customerID) throws JsonProcessingException {
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
