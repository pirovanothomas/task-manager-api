package com.example.taskmanagerapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .version("1.0.0")
                        .description(
                                "REST API for managing tasks. " +
                                        "Provides CRUD operations with request validation."
                        )
                        .contact(new Contact()
                                .name("Thomas Pirovano")));
    }
}