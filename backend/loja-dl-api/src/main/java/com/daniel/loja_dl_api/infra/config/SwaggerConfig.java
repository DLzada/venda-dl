package com.daniel.loja_dl_api.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loja DL - API de E-commerce")
                        .version("1.0")
                        .description("API robusta para gerenciamento de e-commerce, integração com Webhooks, Pix e rotinas automatizadas.")
                        .contact(new Contact()
                                .name("Daniel")
                                .email("daniel@exemplo.com")));
    }
}