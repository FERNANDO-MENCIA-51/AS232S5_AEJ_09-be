package pe.edu.vallegrande.apis_ai_demos.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springboot-public")
                .pathsToMatch("/v1/api/**", "/api/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API - AI Detection Demo")
                        .description("API para testear detección de contenido generado por IA")
                        .license(new License().name("Valle Grande").url("https://vallegrande.edu.pe"))
                        .version("1.0.0"));
    }

}
