package com.backend.carsforum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carsForumApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Cars Forum API")
                        .description("REST API for Cars Forum")
                        .version("1.0"));
    }
}
