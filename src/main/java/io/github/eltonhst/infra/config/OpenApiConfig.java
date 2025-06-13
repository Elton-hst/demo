package io.github.eltonhst.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .addOpenApiCustomizer(openApi -> {
                    Components components = openApi.getComponents() != null ? openApi.getComponents() : new Components();
                    components.addSecuritySchemes(
                            "BearerAuth",
                            new SecurityScheme()
                                    .name("BearerAuth")
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .description("Autenticação com token JWT")
                    );
                    openApi.addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
                    openApi.setComponents(components);
                }).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("eltonhst API")
                        .version("1.0.0")
                        .description("Documentação da API eltonhst")
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));

        Components components = openAPI.getComponents() != null ? openAPI.getComponents() : new Components();
        components.addSecuritySchemes(
                "BearerAuth",
                new SecurityScheme()
                        .name("BearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Insira seu token JWT")
        );
        openAPI.setComponents(components);
        return openAPI;
    }

}
