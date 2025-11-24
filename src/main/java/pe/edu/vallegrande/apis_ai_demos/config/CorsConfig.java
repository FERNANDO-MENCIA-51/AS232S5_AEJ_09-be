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

                // Permitir todos los orígenes (cualquier puerto)
                corsConfig.addAllowedOriginPattern("*");

                // Permitir todos los métodos HTTP
                corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

                // Permitir todos los headers
                corsConfig.addAllowedHeader("*");

                // Exponer todos los headers
                corsConfig.addExposedHeader("*");

                // Permitir credenciales
                corsConfig.setAllowCredentials(true);

                // Tiempo de cache para preflight requests (1 hora)
                corsConfig.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", corsConfig);

                return new CorsWebFilter(source);
        }
}
