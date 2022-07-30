package com.natour.natour.openapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfiguration {
    
    public OpenAPI customOpenAPI(
        @Value("${app.description}") String appDescription,
        @Value("${app.version}") String appVersion
    ) {
        Info info = new Info();

        info
            .title("NaTour API")
            .version(appVersion)
            .description(appDescription)
            .termsOfService("http://swagger.io/terms/")
            .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI().info(info);
    }
}
