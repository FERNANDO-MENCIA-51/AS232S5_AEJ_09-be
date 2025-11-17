package pe.edu.vallegrande.apis_ai_demos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${rapidapi.url:https://ai-detection4.p.rapidapi.com/v1/ai-detection-rapid-api}")
    private String rapidapiUrl;

    @Value("${rapidapi.host:ai-detection4.p.rapidapi.com}")
    private String rapidapiHost;

    @Value("${rapidapi.apikey}")
    private String rapidapiApikey;

    @Value("${nasa.url:https://api.nasa.gov}")
    private String nasaUrl;


    @Bean
    public WebClient aiDetectionWebClient() {
        return WebClient.builder()
                .baseUrl(java.util.Objects.requireNonNull(rapidapiUrl))
                .defaultHeader("x-rapidapi-host", rapidapiHost)
                .defaultHeader("x-rapidapi-key", rapidapiApikey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient nasaWebClient() {
        return WebClient.builder()
                .baseUrl(java.util.Objects.requireNonNull(nasaUrl))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}