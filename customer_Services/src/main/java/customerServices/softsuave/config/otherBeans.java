package customerServices.softsuave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class otherBeans {

    @Bean
    org.springframework.web.client.RestTemplate restTemplateBean(){
        return new org.springframework.web.client.RestTemplate();
    }

}
