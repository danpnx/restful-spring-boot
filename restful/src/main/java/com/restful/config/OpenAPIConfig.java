package com.restful.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {@Bean
public OpenAPI customOpenApi() {
    return new OpenAPI().info(
            new Info()
                    .title("RESTful API with Spring Boot")
                    .version("v1")
                    .description("RESTful API using Spring Framework")
                    .termsOfService("https://github.com/danpnx")
                    .license(
                            new License()
                                    .name("Apache License 2.0")
                                    .url("https://www.apache.org/licenses/LICENSE-2.0")
                    )
                    .contact(
                            new Contact()
                                    .name("Daniel Augusto")
                                    .email("danielpn23@outlook.com")
                                    .url("https://www.linkedin.com/in/daniel-augusto-nunes/")
                    )
    );
}


}
