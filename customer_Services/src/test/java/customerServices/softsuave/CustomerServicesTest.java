package customerServices.softsuave;
import customerServices.softsuave.Repository.CustomerRepository;
import customerServices.softsuave.Repository.RolesReprository;
import customerServices.softsuave.cuustomerServicse.CustomerException;
import customerServices.softsuave.cuustomerServicse.CustomerServices;
import customerServices.softsuave.newModel.Customer;
import customerServices.softsuave.newModel.Roles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServicesTest {


    @InjectMocks
    CustomerServices customerServices;

    @Mock
    PasswordEncoder passwordEncoder;


    @Mock
    RolesReprository rolesReprository;

    @Mock
    CustomerRepository customerRepository;


    Customer.CustomerRequestDto customerdto;
    Customer customersetup;

    @BeforeEach
    public void setup() {
        customerdto = new Customer.CustomerRequestDto();
        customerdto.setId(1);
        customerdto.setName("John Doe");
        customerdto.setEmail("john.doe@gmail.com");
        customerdto.setAddress("123 Main St, City, Country");
        customerdto.setCreationDate(new Date());
        customerdto.setLastUpdatedDate(new Date());
        customerdto.setActive(true);
        customerdto.setRole("USER");
        customerdto.setPassword("password123");


        customersetup = new Customer();
        customersetup.setCustomerId(1);
        customersetup.setCustomerName("John Doe");
        customersetup.setEmail("john.doe@gmail.com");
        customersetup.setAddress("123 Main St, Springfield");
        customersetup.setCreationDate(new Date());  // Current date and time
        customersetup.setLastUpdatedDate(new Date());  // Current date and time
        customersetup.setActive(true);
        customersetup.setPassword("password123");
        customersetup.setRole("USER");}


    @AfterEach
    void tearDown() {
        customerdto=null;
        customersetup=null;
    }


    @Test
    void getCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("dfsfsf");
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        Customer actual = customerServices.getCustomer(1);
        Assertions.assertEquals(customer, actual);

    }
    @Test
    void getCustomer_whenIdIsZero() throws CustomerException {

        CustomerException exception =  Assertions.assertThrows(CustomerException.class, () ->
               customerServices.getCustomer(-1));
        Assertions.assertEquals("give a valid customer id", exception.getMessage());

    }

    @Test
    void getCustomer_errorCase() throws CustomerException {
        when(customerRepository.findById(101)).thenReturn(null);
        CustomerException exception = Assertions.assertThrows(CustomerException.class, () ->
                customerServices.getCustomer(101));
        Assertions.assertEquals("Access denied", exception.getMessage());
    }

    @Test
    void deleteCustomerTest() throws Exception {

//        Customer customer = new Customer();
//        customer.setCustomerId(2);
//        customer.setCustomerName("john de pal");
//        customer.setActive(true);
      when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customersetup));
   // Mockito.when(customerServices.getCustomer(1)).thenReturn(customersetup);
       //Customer actual= customerServices.getCustomer(2);
        when(customerRepository.save(customersetup)).thenReturn(customersetup);
       // customerServices.softDeleteCustomers(2);
     Assertions.assertEquals(customerServices.softDeleteCustomers(1),"customer deleted");
        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(customersetup);
        //assertFalse(customer.isActive(), "Customer should be inactive after soft delete");
   //  Assertions.assertEquals("customer deleted", act);

    }

    @Test
    void  softDeleteCustomersTest() throws Exception {
        //Exception error = null;
//Mockito.when(customerServices.getCustomer(1)).thenReturn(customersetup );
        //when(customerRepository.findById(1)).thenReturn(Optional.ofNullable(customersetup));
     //   when(customerRepository.save(customersetup)).thenReturn(customersetup);
        Exception exception=Assertions.assertThrows(CustomerException.class,
                () -> customerServices.getCustomer(-1));
        Assertions.assertEquals("give a valid customer id", exception.getMessage());


    }

    @Test
    void saveCustomerDetailsTest() throws CustomerException {
        UUID manualUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Roles role = new Roles(manualUuid, "USER");
        when(rolesReprository.findByRoles("USER")).thenReturn(role);

//        if (rolesReprository.findByRoles("USER") == null) {
//            List<Roles> allRoles = new ArrayList<>();
//            allRoles.add(new Roles(UUID.randomUUID(), "USER"));
//            allRoles.add(new Roles(UUID.randomUUID(), "SUPERUSER"));
//            allRoles.add(new Roles(UUID.randomUUID(), "ADMIN"));   // for separate result
//            when(rolesReprository.saveAll(allRoles)).thenReturn(allRoles);
//
//        }

        Customer.CustomerRequestDto customerdtoforConverter = new Customer.CustomerRequestDto(6, "sakar", "sakar@gmail.com"
                , "123 Main Street City Country", true, "USER", "USER");

        Customer customerConverter = new Customer(6,"sakar", "sakar@gmail.com", "123 Main St, City, Country"
                , true, "USER", "USER");
        when(passwordEncoder.encode("USER")).thenReturn("lqQ1WmRpmopsbYqJk3FCP.8DwMrep1AHa.4seYk2XpkD.57br8lYi");
        Mockito.when(customerRepository.save(any())).thenReturn(customerConverter);
      //  Mockito.when(customerRepository.findByCustomerName(customerConverter.getCustomerName())).thenReturn(customerConverter);
      //  when(customerServices.CustomerDtoConverter(customerdtoforConverter)).thenReturn(customerConverter);
       Customer customer= customerServices.saveCustomerDetails(customerdtoforConverter);
Assertions.assertEquals(customer.getCustomerName(), customerdtoforConverter.getName());
         verify(rolesReprository, times(1)).findByRoles(any());
        verify(customerRepository, times(1)).save(any());
    }



    @Test
    void saveCustomerDetailsError()throws CustomerException {
        // similar to all case;
        Exception message=Assertions.assertThrows(Exception.class,
                () -> customerServices.saveCustomerDetails(null));
        Assertions.assertEquals("fill your details to register as an fellow customer",message.getMessage());
    }
}





