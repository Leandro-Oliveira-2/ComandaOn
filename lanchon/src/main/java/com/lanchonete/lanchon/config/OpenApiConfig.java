package com.lanchonete.lanchon.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LanchOn API",
                version = "1.0.0",
                description = "Backend da lanchonete digital. Use os grupos de endpoints e exemplos para executar o fluxo completo de pedido, item e pagamento.",
                contact = @Contact(name = "Leandro de Oliveira Leite", email = "leandro@example.com"),
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente local")
        }
)
public class OpenApiConfig {
    @Bean
    public OpenAPI lanchonOpenAPI() {
        final String bearerKey = "bearerAuth";

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("LanchOn API")
                        .version("1.0.0")
                        .description("Colecao Swagger para explorar rapidamente as rotas do sistema. Sempre crie um usuario, categoria e produto antes de abrir pedidos e itens.")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Leandro de Oliveira Leite")
                                .email("leandro@example.com"))
                        .license(new io.swagger.v3.oas.models.info.License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Script de teste ponta a ponta disponivel em scripts/test-flow.js")
                        .url("/scripts/test-flow.js"))
                .components(new Components()
                        .addSecuritySchemes(bearerKey,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(bearerKey));
    }

    @Bean
    public GroupedOpenApi catalogApi() {
        return GroupedOpenApi.builder()
                .group("Catalogo")
                .pathsToMatch("/api/categories/**", "/api/products/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("Pedidos e Itens")
                .pathsToMatch("/api/order/**", "/api/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("Usuarios e Login")
                .pathsToMatch("/api/users/**", "/api/auth/**")
                .build();
    }
}
