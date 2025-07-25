package dev.ayushbadoni.MyEcom.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(new Info().title("MyEcom API's")
                .description("MyEcom website is my project")
                .version("1.0")
                .contact(new Contact().name("Ayush Badoni")));
    }
}
