package pe.edu.vallegrande.apis_ai_demos.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Permitir orígenes de Angular (desarrollo y producción)
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://localhost:4201",
                "http://localhost:4202",
                "http://127.0.0.1:4200",
                "https://your-production-domain.com" // Cambia esto por tu dominio en producción
        ));

        // Permitir todos los métodos HTTP
        corsConfig.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"));

        // Permitir todos los headers
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciales
        corsConfig.setAllowCredentials(true);

        // Tiempo de cache para preflight requests
        corsConfig.setMaxAge(3600L);

        // Exponer headers adicionales si es necesario
        corsConfig.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
