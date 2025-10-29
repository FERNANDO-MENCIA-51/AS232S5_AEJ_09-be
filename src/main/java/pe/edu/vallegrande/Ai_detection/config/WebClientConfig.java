package pe.edu.vallegrande.Ai_detection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de WebClients para consumir APIs externas
 * - RapidAPI: Servicio de detección de contenido generado por IA
 * - NASA API: Servicio de imágenes astronómicas del día (APOD)
 */
@Configuration
public class WebClientConfig {

    @Value("${rapidapi.url}")
    private String rapidApiUrl;

    @Value("${rapidapi.host}")
    private String rapidApiHost;

    @Value("${rapidapi.apikey}")
    private String rapidApiKey;

    @Value("${nasa.url}")
    private String nasaUrl;

    /**
     * WebClient configurado para RapidAPI AI Detection
     * Incluye headers requeridos: x-rapidapi-key y x-rapidapi-host
     */
    @Bean(name = "rapidApiWebClient")
    public WebClient rapidApiWebClient() {
        return WebClient.builder()
                .baseUrl(rapidApiUrl)
                .defaultHeader("x-rapidapi-key", rapidApiKey)
                .defaultHeader("x-rapidapi-host", rapidApiHost)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * WebClient configurado para NASA APOD API
     */
    @Bean(name = "nasaApiWebClient")
    public WebClient nasaApiWebClient() {
        return WebClient.builder()
                .baseUrl(nasaUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
