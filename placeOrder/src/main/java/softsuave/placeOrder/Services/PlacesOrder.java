package softsuave.placeOrder.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import softsuave.placeOrder.Exception.PlacedOrderException;
import softsuave.placeOrder.Model.AdsCostTabel;
import softsuave.placeOrder.Model.PlacedOrdersTabel;
import softsuave.placeOrder.Repository.placedOrdersRepository;
import softsuave.placeOrder.config.Customer;

import java.util.*;

@Service
public class PlacesOrder {

    private static final Logger LOG = LoggerFactory.getLogger(PlacesOrder.class);
    @Autowired
    placedOrdersRepository placedOrders;

    @Autowired
    RestTemplate restTemplate;

    public PlacedOrdersTabel PlacedOrderDto(PlacedOrdersTabel.orderDto dto) {
        Set<AdsCostTabel> adsCost = new HashSet<>();
        for (String sub : dto.getAdsCost()) {
            adsCost.add(findAds(sub));
        }
        return new PlacedOrdersTabel(dto.isOrderStatus(), dto.getCustomerId(), dto.getDays(), adsCost,dto.isUpdatedtoSuperuser());
    }

    public AdsCostTabel findAds(String Ads) {
        String url = "http://localhost:8082/AdsCosting-api/find-ads/"+Ads.toUpperCase() ;
        return  restTemplate.getForEntity(url, AdsCostTabel.class).getBody();
    }

    public  byte[] saveOrder(PlacedOrdersTabel.orderDto dto) throws Exception {
        PlacedOrdersTabel placedOrdersTabel = PlacedOrderDto(dto);
        if (!placedOrdersTabel.isOrderStatus()) {
            throw new PlacedOrderException("Order is not placed");
        }
        if (placedOrdersTabel.getCustomerId() > 0) {

            //Customer customer1 = CustomerRepository.findById(placedOrdersTabel.getCustomerId()).get();
            Customer customer = makeRequestWithHeaders(placedOrdersTabel.getCustomerId());
            if (customer.getRole().equalsIgnoreCase("admin")) {
                 return factorySwitcher(customer.getRole()).Placeorder(dto);
            }
            else {
                if (customer != null)
                    placedOrdersTabel.setCustomerId(customer.getCustomerId());
                placedOrdersTabel.setCustomerName(customer.getCustomerName());
                placedOrdersTabel.setOrderCancellation(false);
                placedOrdersTabel.setOrderNo(UUID.randomUUID());
            }
            placedOrdersTabel.setDays(placedOrdersTabel.getDays());
            Set<AdsCostTabel> adsCost = placedOrdersTabel.getAdsCost();
            int totalcost = 0;
            for (AdsCostTabel adCost : adsCost) {
                totalcost = (int) adCost.getCost();
            }
            placedOrdersTabel.setTotalCost(totalcost);
            PlacedOrdersTabel savedPlacedOrdersTabel = placedOrders.save(placedOrdersTabel);
            LOG.info(savedPlacedOrdersTabel.toString() + "   " + Calendar.getInstance().getTime().toString() + "   "
                    + savedPlacedOrdersTabel.getCustomerId());
            return factorySwitcher(customer.getRole()).Placeorder(dto);
        }
return new byte[0];
    }

    public Customer makeRequestWithHeaders(int customerId ) {
        String urlForSaveOrder = "http://localhost:8081/customer-api/get-customer";
        String urlToken ="http://localhost:8081/customer-api/customerId/"+customerId;
        Map<String, String> params = new HashMap<>();
        params.put("customerId", String.valueOf(customerId));
        String token= restTemplate.getForObject(urlToken, String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+token.toString());
        HttpEntity<String> entity = new HttpEntity<>(headers);

//        ResponseEntity<String> customer = restTemplate.exchange(urlForSaveOrder, HttpMethod.GET, entity, String.class, params);
        ResponseEntity<String> customer = restTemplate.exchange(urlForSaveOrder, HttpMethod.GET, entity, String.class);
      //  ResponseEntity<Customer> response=restTemplate.getForEntity(urlForSaveOrder, Customer.class);
        customer.getBody();
        return null;
    }


    public FactortyResponse factorySwitcher(String role) {

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

}

