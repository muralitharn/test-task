package customerServices.softsuave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import customerServices.softsuave.Repository.CustomerRepository;
import customerServices.softsuave.newModel.Customer;
import customerServices.softsuave.newModel.Roles;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class UserDetailsServiceConfig {

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoles())).collect(Collectors.toList());
    }



    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));}

        @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository) {
        return customerName -> {
            Optional<Customer> customer = Optional.ofNullable(customerRepository.findByEmail(customerName));
            if (customer.isEmpty()) {
                throw new UsernameNotFoundException("User not found for any jwt related activities");
            }

            return new org.springframework.security.core.userdetails.User(
                    customer.get().getEmail(),
                    customer.get().getPassword(),
                    mapRoleToAuthorities(customer.get().getRole()));
        };
    }
}

