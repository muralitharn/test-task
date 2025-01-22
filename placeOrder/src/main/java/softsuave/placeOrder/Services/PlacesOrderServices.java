package softsuave.placeOrder.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.AdsCostTabel;
import softsuave.placeOrder.Model.OrdersTabel;
import softsuave.placeOrder.Model.PaymentDetails;
import softsuave.placeOrder.Repository.OrdersTabelRepository;
import softsuave.placeOrder.Repository.PaymentDetailsRepo;
import softsuave.placeOrder.Services.PaymentFactory.*;
import softsuave.placeOrder.Services.RoleBasedImplementation.AdminImplementation;
import softsuave.placeOrder.Services.RoleBasedImplementation.FactortyResponse;
import softsuave.placeOrder.Services.RoleBasedImplementation.SuperUserImplementation;
import softsuave.placeOrder.Services.RoleBasedImplementation.UserImplementation;
import softsuave.placeOrder.config.Customer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import softsuave.placeOrder.enums.paymentMode;

import java.util.*;

@Service()
public class PlacesOrderServices {

    private OrdersTabelRepository ordersRepo;

    private RestTemplate restTemplate;

    private PaymentDetailsRepo paymentDetailsRepo;

    private DocumentProvider documentMaker;



    @Autowired
    public PlacesOrderServices(RestTemplate restTemplate, PaymentDetailsRepo paymentDetailsRepo, OrdersTabelRepository ordersRepo
            ,DocumentProvider documentMaker) {
        this.restTemplate = restTemplate;
        this.paymentDetailsRepo = paymentDetailsRepo;
        this.ordersRepo = ordersRepo;
        this.documentMaker=documentMaker;
    }

    public OrdersTabel PlacedOrderDto(OrdersTabel.orderDto dto) throws JsonProcessingException {
        List<AdsCostTabel> adsCost = new ArrayList<>();
        Set<String > duplicates=new HashSet<>();
        for (String sub : dto.getAdsCost()) {
            if (duplicates.add(sub)){
            adsCost.add(findAds(sub));}
        }
        return new OrdersTabel(dto.isOrderStatus(), dto.getCustomerId(), dto.getDays(), adsCost, dto.isUpdatedtoSuperuser());
    }

    public AdsCostTabel findAds(String Ads) throws JsonProcessingException {
        String url = "http://localhost:8082/AdsCosting-api/find-ads/" + Ads.toUpperCase();
        String userJson = restTemplate.getForObject(url, String.class);
        AdsCostTabel adsCostTabel = new ObjectMapper().readValue(userJson, AdsCostTabel.class);
        return adsCostTabel;
    }


    public String saveOrder(OrdersTabel.orderDto dto) throws Exception {
        int id;
        OrdersTabel placedOrdersTabel = PlacedOrderDto(dto);
        if (!placedOrdersTabel.isOrderStatus()) {
            throw new PlacedOrderException("Order is not placed");
        }
        if (placedOrdersTabel.getCustomerId() > 0) {

            Customer customer = RestTemplateCall(placedOrdersTabel.getCustomerId());
            if (customer.getRole() == "USER") {
                saveCustomerWithRest(customer);
            }
            if (customer != null)
                placedOrdersTabel.setCustomerId(customer.getCustomerId());
            placedOrdersTabel.setCustomerName(customer.getCustomerName());
            placedOrdersTabel.setOrderCancellation(false);

            List<AdsCostTabel> adsCostList = placedOrdersTabel.getAdsCost();
            int totalcost = 0;
            for (AdsCostTabel adCost : adsCostList) {
                totalcost += (int) adCost.getCost();
            }

            placedOrdersTabel.setTotalCost(totalcost);

        }
        ordersRepo.save(placedOrdersTabel);

        String Response = (ordersRepo.save(placedOrdersTabel) != null) ? "congrats! your order has been placed " +
                "....complete the payment process track your order " : "something is wrong";
        return Response;

    }


    public void saveCustomerWithRest( Customer customer){
        String url = "http://localhost:8081/customer-api/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        customer.setRole("SUPERUSER");
        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        String response = restTemplate.postForObject(url, requestEntity, String.class);

    }


    public Customer RestTemplateCall(int customerId) throws JsonProcessingException {
        String urlForSaveOrder = "http://localhost:8081/customer-api/get-customer/" + customerId;
        String customer = restTemplate.getForObject(urlForSaveOrder, String.class);
        Customer response = new ObjectMapper().readValue(customer, Customer.class);
        return response;
    }


    public FactortyResponse orderFactorySwitcher(String role) {

        FactortyResponse factortyResponse;
        if (role.equalsIgnoreCase("user")) {
            return factortyResponse = new UserImplementation();
        } else if (role.equalsIgnoreCase("admin")) {
            return factortyResponse = new AdminImplementation();
        } else if (role.equalsIgnoreCase("superuser")) {
            return factortyResponse = new SuperUserImplementation();
        }
        return null;
    }


    public PaymentModeFactory FactoryPaymentSwitcher(String paymentMethod) throws PlacedOrderException {

        if (Objects.equals(paymentMode.COD.toString(), paymentMethod)){
            return new CODModePayment(ordersRepo,restTemplate, paymentDetailsRepo,documentMaker);
        } else if (Objects.equals(paymentMode.UPI.toString(), paymentMethod)) {
            return  new UPIModePayment(new PlacesOrderServices(restTemplate,paymentDetailsRepo,ordersRepo,documentMaker),ordersRepo,paymentDetailsRepo,restTemplate);
        } else if (Objects.equals(paymentMode.DEBITCARD.toString(), paymentMethod)) {
            return new DebitcardModePayment(ordersRepo,restTemplate, paymentDetailsRepo,documentMaker);
        } else if (Objects.equals(paymentMode.EMI.toString(), paymentMethod)) {
            return new EMIModePayment(paymentDetailsRepo,restTemplate,ordersRepo,documentMaker);
        }
        throw new PlacedOrderException("invalid payment method");
    }


    public boolean  GenerateBill(PaymentDetails paymentDetails,int LoanTenure) throws Exception {
        return FactoryPaymentSwitcher(String.valueOf(paymentDetails.getPaymentType())).payment(paymentDetails,LoanTenure);
    }


    public PaymentDetails getPaymentDetails(int  customerId) throws PlacedOrderException {
        PaymentDetails Details;
        if (customerId >= 0) {
            Details = paymentDetailsRepo.findByCustomerID(customerId);
        } else {
            throw new PlacedOrderException("invalid customer id", new Exception());
        }
        return Details;
    }



}

