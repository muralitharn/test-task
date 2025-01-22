package customerServices.softsuave.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import customerServices.softsuave.jwt.JwtAuthenticationFilter;
import customerServices.softsuave.jwt.JwtAuthorizationFilter;
import customerServices.softsuave.jwt.JwtUtil;

@Configuration
//EnableWebSecurity //--> for web mvc
//@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/customer-api/register"
                                        , "/customer-api/login"
                                ,"/customer-api/get-customer/**"
                                        ,"/customer-api/customerId/**"
                                        , "/h2-console/**"
                                ,"/swagger-ui.html"
                                ,"/swagger-ui/**"
                                        ,"/v3/api-docs/**","/customer-api?**").permitAll()
                                .requestMatchers("/AdsCosting-api/**","/placed-orders-api/**").permitAll()
                            .requestMatchers("/get-customer/").hasRole("SUPERUSER")
                             .requestMatchers("/delete-customer").hasRole("ADMIN")
                                .anyRequest()



                                .authenticated()

                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)  // Allow frames from the same origin
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, userDetailsService));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
