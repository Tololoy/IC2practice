package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Manager API")
                        .version("1.0.0")
                        .description("API para la gestión de libros: creación, búsqueda, filtrado y estadísticas.")
                        .contact(new Contact()
                                .name("Eloy Colirio")
                                .email("eloy.tu.email@example.com"))
                );
    }
}
