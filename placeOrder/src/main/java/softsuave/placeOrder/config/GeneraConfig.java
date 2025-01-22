package softsuave.placeOrder.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeneraConfig {

    @Bean
    public RestTemplate restTemplateBean(){
        return new RestTemplate();
    }

    @Bean
    OpenAPI springOpenAPI() {
        return new OpenAPI().info(new Info().title("test task ")
                        .description("some test task apis are there")
                        .version("3-6 month").license(new License().name("softsuave").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation().description("no url found here")
                        .url("https://springboot.wiki.github.org/docs"));

    }

//    @Configuration
//    public class RestTemplateConfig {
//        @Bean
//        public RestTemplate restTemplate() {
//            return new RestTemplate();
//        }
//    }
}

