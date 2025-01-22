package customerServices.softsuave.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
@Configuration
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
//for authentication jwy for all api
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
