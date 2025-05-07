package com.gestaoprojetos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

//    TODO: Adicionar configuração do Swagger para a documentação da API

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("Documentação da API usando Springdoc OpenAPI")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@example.com")));
    }
}