package customerServices.softsuave.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import customerServices.softsuave.Repository.CustomerRepository;
import customerServices.softsuave.cuustomerServicse.CustomerException;
import customerServices.softsuave.dtos.AuthenticationRequest;
import customerServices.softsuave.jwt.JwtUtil;
import customerServices.softsuave.newModel.Customer;
import customerServices.softsuave.cuustomerServicse.CustomerServices;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(value = "/customer-api")
@Tag(name = "customer APIs", description = "customer APIs for basic crud")
@SecurityRequirement(name = "Bearer Authentication")
public class CustomerController {

    @Autowired
    private final CustomerServices customerServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public CustomerController(CustomerServices customerServices) {
        this.customerServices = customerServices;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> saveCustomerDetailsApi(@RequestBody Customer.CustomerRequestDto dto) throws CustomerException {
        Customer customer = customerServices.saveCustomerDetails(dto);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginCustomerDetails(@RequestBody AuthenticationRequest authenticationRequest) throws CustomerException {
        String response = customerServices.GenerateTokenfromUserDetails(authenticationRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping ("/customerId/{customerId}")
    public ResponseEntity<String> GetTokenFromCustomerId(@PathVariable(required = true)  int customerId) throws Exception {
        String token=customerServices.GetTokenFromId(customerId);
return new ResponseEntity<>(token,HttpStatus.OK);
    }

    //*****@PreAuthorize("#username == authentication.name")*****//
//    @PreAuthorize("hasAuthority('ADMIN') or hasRole('ADMIN')||hasAuthority('USER') or hasRole('USER')")
//    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-customer/{customerId}")
    public ResponseEntity<Object> getCustomerApi(@PathVariable(name = "customerId") int customerId) throws Exception {
        Customer response = customerServices.getCustomer(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')or hasRole('ADMIN')")
    @DeleteMapping("/delete-customer")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Object> softDeleteCustomerApi(@RequestParam(name = "customerId") int customerId) throws CustomerException {
        String response = customerServices.softDeleteCustomers(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
