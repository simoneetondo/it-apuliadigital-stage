package it.exprivia.Scuola.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // lucchetto per autorizzazzione con jwt nelle api
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Scuola Exprivia")
                        .version("1.0")
                        .description("Documentazione delle API per il progetto Scuola"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi studentiApi() {
        return GroupedOpenApi.builder()
                .group("studenti-api")
                .pathsToMatch("/api/students/**") // Filtra per path
                .build();
    }

    @Bean
    public GroupedOpenApi docentiApi() {
        return GroupedOpenApi.builder()
                .group("docenti-api")
                .pathsToMatch("/api/teachers/**") // Filtra per path
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("tutte-le-api")
                .pathsToMatch("/**")
                .build();
    }


}