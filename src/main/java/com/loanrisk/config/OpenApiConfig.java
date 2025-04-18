package com.loanrisk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanRiskOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Risk Scoring Engine API")
                        .description("API for evaluating loan applications based on risk scoring rules")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Loan Risk Team")
                                .email("support@loanrisk.com")
                                .url("https://loanrisk.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("/").description("Default Server URL")
                ));
    }
}