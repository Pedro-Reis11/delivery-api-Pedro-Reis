package com.delivery_api.Projeto.Delivery.API.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("DeliveryTech API").version("1.0.0").description("API for managing delivery operations").contact(new Contact().name("DeliveryTech Support").email("dev@deliverytech.com").url("https://www.deliverytech.com/support")).license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))).servers(List.of(new Server().url("http://localhost:8080").description("Local Development Server"), new Server().url("https://api.deliverytech.com/v1").description("Production Server")));
    }
}
