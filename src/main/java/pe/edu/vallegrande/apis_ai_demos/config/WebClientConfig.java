package pe.edu.vallegrande.apis_ai_demos.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Configuración para AI Detection API
    @Value("${rapidapi.url}")
    private String rapidapiUrl;

    @Value("${rapidapi.host}")
    private String rapidapiHost;

    @Value("${rapidapi.apikey}")
    private String rapidapiApikey;



    // Configuración para NASA API
    @Value("${nasa.url}")
    private String nasaUrl;

    @Value("${nasa.apikey}")
    private String nasaApikey;



    @Bean
    public WebClient aiDetectionWebClient() {
        return WebClient.builder()
                .baseUrl(rapidapiUrl)
                .defaultHeader("x-rapidapi-host", rapidapiHost)
                .defaultHeader("x-rapidapi-key", rapidapiApikey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


    @Bean
    public WebClient nasaWebClient() {
        return WebClient.builder()
                .baseUrl(nasaUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


    
}