package org.example.hobbycatalog.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(){
        return new OpenAPI().
            servers(List.of(new Server().url("http://localhost:5000")))
                .info(new Info()
                        .title ("Hobby Catalog Documantation")
                        .description("Back end app for describing catalog of different hobby games, that user can buy")
                        .version("1.0"));
    }
}
