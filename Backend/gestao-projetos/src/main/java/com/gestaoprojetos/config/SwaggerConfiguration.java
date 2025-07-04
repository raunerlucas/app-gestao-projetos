package com.gestaoprojetos.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para expor o botão "Authorize"
 * e permitir que o usuário cole seu JWT Bearer ali.
 */
@Configuration
public class SwaggerConfiguration {


    /**
     * Nomear o nosso esquema de segurança como "bearerAuth" (poderia usar outro nome,
     * mas em convenção usamos "bearerAuth").
     * <p>
     * O SecurityScheme diz:
     * - type: HTTP
     * - scheme: bearer
     * - bearerFormat: JWT
     * <p>
     * O SecurityRequirement faz com que TODOS os endpoints (por padrão)
     * exijam esse esquema. Se quiser liberar alguns métodos, dá para detalhar via @Operation no Controller.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // 1) define o título, versão e descrição da API
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("Documentação da API usando Springdoc OpenAPI")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@example.com")))
                // 2) define globalmente que todos os endpoints usam este esquema
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 3) registra o esquema de segurança dentro dos components
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)       // HTTP authentication
                                        .scheme("bearer")                     // indica que é Bearer
                                        .bearerFormat("JWT")                  // formato: JWT
                        )
                );
    }
}