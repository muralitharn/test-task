package customerServices.softsuave;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customerServices.softsuave.controller.CustomerController;
import customerServices.softsuave.cuustomerServicse.CustomerException;
import customerServices.softsuave.cuustomerServicse.CustomerServices;
import customerServices.softsuave.newModel.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
public class controllerTest {

    @InjectMocks
    CustomerController customerController;

   @Mock
    CustomerServices customerServices;

    Customer customer;
    Customer.CustomerRequestDto customerDto;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        customerDto = new Customer.CustomerRequestDto();


        customerDto.setId(1);
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@gmail.com");
        customerDto.setAddress("123 Main St, City, Country");
        customerDto.setCreationDate(new Date());
        customerDto.setLastUpdatedDate(new Date());
        customerDto.setActive(true);
        customerDto.setRole("USER");
        customerDto.setPassword("password123");


        customer = new Customer();

        customer.setCustomerId(1);
        customer.setCustomerName("John Doe");
        customer.setEmail("john.doe@gmail.com");
        customer.setAddress("123 Main St, Springfield");
        customer.setCreationDate(new Date());
        customer.setLastUpdatedDate(new Date());
        customer.setActive(true);
        customer.setPassword("password123");
        customer.setRole("USER");
    }
@Test
    void saveCustomerDetailsTest() throws CustomerException, JsonProcessingException {
   // Customer customer =new Customer();

    Mockito.when( customerServices.saveCustomerDetails(any())).thenReturn(customer);
    ResponseEntity<Customer>  response = (ResponseEntity<Customer>) customerController.saveCustomerDetailsApi(customerDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mapper.writeValueAsString(response.getBody()), mapper.writeValueAsString(this.customer));

}

@Test
    void getCustomerTest() throws Exception {
        Customer  customer =new Customer();
        Mockito.when(customerServices.getCustomer(1)).thenReturn(customer);
    ResponseEntity<Object> response = (ResponseEntity<Object>) customerController.getCustomerApi(1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mapper.writeValueAsString(response.getBody()), mapper.writeValueAsString(this.customer));
}

@Test
    void softDeleteCustomerTest() throws CustomerException, JsonProcessingException {
        Customer deletedCustomer = new Customer();
        Mockito.when(customerServices.softDeleteCustomers(1)).thenReturn("customer deleted");
    ResponseEntity<Object>  response = (ResponseEntity<Object>) customerController.softDeleteCustomerApi(1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mapper.writeValueAsString(response.getBody()), mapper.writeValueAsString(customer));
}
}
