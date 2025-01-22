package customerServices.softsuave.cuustomerServicse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customerServices.softsuave.config.PaymentDetails;
import customerServices.softsuave.dtos.jwtWrapper;
import customerServices.softsuave.jwt.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import customerServices.softsuave.Repository.RolesReprository;
import customerServices.softsuave.dtos.AuthenticationRequest;
import customerServices.softsuave.jwt.JwtUtil;
import customerServices.softsuave.newModel.Customer;
import customerServices.softsuave.Repository.CustomerRepository;
import customerServices.softsuave.newModel.Roles;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CustomerServices {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    RolesReprository rolesReprository;

    @Autowired
    RestTemplate restTemplate;

    public final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public Customer CustomerDtoConverter(Customer.CustomerRequestDto dto) {
        if(dto.getId()>=0){
            return new Customer( dto.getId(),dto.getName(), dto.getEmail()
                    , dto.getAddress(), dto.isActive(), dto.getPassword(), dto.getRole());
        }
        return new Customer(dto.getName(), dto.getEmail()
                , dto.getAddress(), dto.isActive(), dto.getPassword(), dto.getRole());
    }


    public Customer saveCustomerDetails(Customer.CustomerRequestDto dto) throws CustomerException {
        Roles rolesSample = rolesReprository.findByRoles("USER");
        if (rolesSample == null) {
            List<Roles> allRoles = new ArrayList<>();
            allRoles.add(new Roles(UUID.randomUUID(), "USER"));
            allRoles.add(new Roles(UUID.randomUUID(), "SUPERUSER"));
            allRoles.add(new Roles(UUID.randomUUID(), "ADMIN"));
            rolesReprository.saveAll(allRoles);
        }

        if (dto == null) {
            throw new CustomerException("fill your details to register as an fellow customer");
        } else if (dto.getEmail() == null || dto.getPassword() == null || dto.getName() == null || dto.getAddress() == null || dto.getRole() == null) {
            throw new CustomerException("fill the  all details mentioned in form");
        } else if (customerRepository.findByCustomerName(dto.getName()) != null) {
            throw new CustomerException("Customer already present");
        } else {

            Customer customer = CustomerDtoConverter(dto);
            customer.toString();
            String roles = customer.getRole();
            customer.toString();
            if (!(roles.equalsIgnoreCase("USER")
                    || roles.equalsIgnoreCase("SUPERUSER")
                    || roles.equalsIgnoreCase("ADMIN"))) {
                throw new CustomerException("select the role correctly");
            }
            customer.setCreationDate(Calendar.getInstance().getTime());
            customer.setLastUpdatedDate(Calendar.getInstance().getTime());
            Date time = Calendar.getInstance().getTime();

            customer.setPassword(passwordEncoder.encode(dto.getPassword()));
           Customer savedCustomer= customerRepository.save(customer);
            return savedCustomer;

        }

    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoles())).collect(Collectors.toList());
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }


    public String GenerateTokenfromUserDetails(AuthenticationRequest authenticationRequest) throws CustomerException {
        try {
            Customer customer = customerRepository.findByCustomerName(authenticationRequest.getUsername());

            try {
                Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        customer.getEmail(),
                        authenticationRequest.getPassword(),
                        mapRoleToAuthorities(customer.getRole())));
                SecurityContextHolder.getContext().setAuthentication(authenticate);
            } catch (BadCredentialsException exception) {
                LOG.info("enter a vaild mail id and password");
                throw new CustomerException("enter a vaild mail id and password");
            }
            final UserDetails userDetails = userDetailsService.loadUserByUsername(customer.getEmail());
            jwtWrapper jwtwrapper= new jwtWrapper();

            final String jwt = jwtUtil.generateToken(userDetails);
            jwtwrapper.setAccessToken(jwt);
            jwtwrapper.setAcc_expire_time(jwtUtil.extractTimeFromToken(jwt));

            return jwt;
        } catch (BadCredentialsException exception) {
            LOG.info(exception.getMessage());
            return " went wrong here  ";
        }
    }

    public String GetTokenFromId( int customerId)throws Exception {
        Customer customer=customerRepository.findById(customerId).get();
        if(customer==null){
            LOG.info("invalid customer id");
            throw new CustomerException("invalid customer id");
        }
        try {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(customer.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
            LOG.info(jwt);
        return jwt;
        }catch (BadCredentialsException exception) {
            LOG.info("invalid customer id");
            return "invalid customer id";
        }

    }


    public  Customer getCustomer(int customerid) throws Exception {
        try {
            if (customerid <= 0) {
                LOG.info("invalid customer id");
                throw new CustomerException("invalid customer id");
            }
            Customer customer = customerRepository.findById(customerid).get();
            return customer;
        } catch (CustomerException exception) {
            LOG.info("give a valid customer id");
            throw new CustomerException("give a valid customer id");
        }
        catch (Exception exception) {
            LOG.info("Access denied");
            throw new CustomerException("Access denied");
        }
    }

    public String softDeleteCustomers(int customerid) throws CustomerException {
        try {
            Customer customer = getCustomer(customerid);
            customer.setActive(false);
            customerRepository.save(customer);
            LOG.info("customer deleted ");
            return "customer deleted";
        } catch (Exception exception) {
            LOG.info("Access denied");
            throw new CustomerException("Access denied");
        }
    }

    public PaymentDetails getpaymentModeByRest(int id) throws JsonProcessingException {
        String url="http://localhost:8083/placed-orders-api/get/paymentDetails?customerId="+id;
                String Details = restTemplate.getForObject(url, String.class);
        PaymentDetails PaymentDetails = new ObjectMapper().readValue(Details, PaymentDetails.class);
        return PaymentDetails;
    }


}

