package softsuave.AdsCost.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI springOpenAPI() {
        return new OpenAPI().info(new Info().title("test task ")
                        .description("some test task apis are there")
                        .version("3-6 month").license(new License().name("softsuave").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation().description("no url found here")
                        .url("https://springboot.wiki.github.org/docs"));

    }
}
