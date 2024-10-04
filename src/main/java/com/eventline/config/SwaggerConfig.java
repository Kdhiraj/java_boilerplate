package com.eventline.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "EventLine Backend API";
    private static final String API_VERSION = "1.0";
    private static final String API_DESCRIPTION = "Use this documentation to understand how to interact with the API, including all available resources and their operations.";

    private static final String CONTACT_NAME = "Dhiraj Kumar";
    private static final String CONTACT_EMAIL = "dhiraj.email@example.com";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServers())
                .components(createComponents());
    }

    private Info createApiInfo() {
        return new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .contact(createContact());
    }

    private Contact createContact() {
        return new Contact()
                .name(CONTACT_NAME)
                .email(CONTACT_EMAIL);
    }

    private List<Server> createServers() {
        return List.of(
                createServer("http://localhost:8080", "Local Server"),
                createServer("http://dev.eventline.com", "Development Server"),
                createServer("http://qa.eventline.com", "QA Server"),
                createServer("http://prod.eventline.com", "Production Server")
        );
    }

    private Server createServer(String url, String description) {
        return new Server().url(url).description(description);
    }

    private Components createComponents() {
        return new Components()
                .addSecuritySchemes("basicAuth", createBasicAuthScheme())
                .addSecuritySchemes("jwtAuth", createJwtAuthScheme());
    }

    private SecurityScheme createBasicAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("Basic Authentication");
    }

    private SecurityScheme createJwtAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Authorization header using the Bearer scheme. Example: 'Authorization: Bearer {token}'");
    }


}
