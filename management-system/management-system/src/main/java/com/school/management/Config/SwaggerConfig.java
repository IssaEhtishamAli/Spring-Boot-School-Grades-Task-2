package com.school.management.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8087}")
    private String serverPort;

    @Bean
    public OpenAPI minimalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("School Management System API")
                        .version("1.0.0")
                        .description("RESTful APIs for student enrollments, courses, and grades"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server")
                ));
    }
}