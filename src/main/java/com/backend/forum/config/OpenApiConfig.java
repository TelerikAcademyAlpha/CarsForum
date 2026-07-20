package com.backend.forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI communityForumApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Community Forum API")
                        .description("REST API for Community Forum")
                        .version("1.0"));
    }
}
