package pe.edu.vallegrande.Ai_detection.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuración de OpenAPI/Swagger para documentación interactiva de la API
 * Accesible en: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Detection & NASA APOD API")
                        .version("1.0.0")
                        .description(
                                "API REST reactiva que integra servicios de detección de contenido generado por IA (RapidAPI) "
                                        +
                                        "y el servicio de imágenes astronómicas del día de NASA (APOD). " +
                                        "Proporciona operaciones CRUD completas con eliminación lógica sobre PostgreSQL.")
                        .contact(new Contact()
                                .name("Valle Grande")
                                .email("soporte@vallegrande.edu.pe")
                                .url("https://www.vallegrande.edu.pe"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.production.com")
                                .description("Servidor de Producción")));
    }
}
