package ir.azki.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .schemaRequirement("bearerAuth",
                        new SecurityScheme()
                                .name("bearerAuth")
                                .description("JWT Auth authentication")
                                .scheme("bearer")
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER));
    }
}
