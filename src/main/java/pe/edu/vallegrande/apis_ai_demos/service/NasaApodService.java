package pe.edu.vallegrande.apis_ai_demos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.apis_ai_demos.dto.NasaApodResponse;
import pe.edu.vallegrande.apis_ai_demos.entity.NasaApodQuery;
import pe.edu.vallegrande.apis_ai_demos.repository.NasaApodRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasaApodService {

    private final WebClient nasaWebClient;
    private final NasaApodRepository nasaApodRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${nasa.apikey}")
    private String nasaApiKey;

    public Mono<NasaApodResponse> getApod(String date) {
        log.info("Calling NASA APOD API for date: {}", date != null ? date : "today");

        return nasaWebClient
                .get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/planetary/apod");
                    uriBuilder.queryParam("api_key", nasaApiKey);
                    if (date != null && !date.trim().isEmpty()) {
                        uriBuilder.queryParam("date", date);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(rawResponse -> {
                    log.info("=== RAW NASA APOD API RESPONSE ===");
                    log.info("{}", rawResponse);
                    log.info("==================================");
                })
                .doOnError(error -> {
                    log.error("=== NASA APOD API ERROR ===");
                    log.error("Error calling NASA APOD API: {}", error.getMessage());
                    log.error("Error class: {}", error.getClass().getSimpleName());
                    if (error.getCause() != null) {
                        log.error("Caused by: {}", error.getCause().getMessage());
                    }
                    log.error("===========================");
                })
                .map(rawResponse -> {
                    try {
                        log.info("Processing NASA APOD rawResponse: {}", rawResponse);

                        // Parsear la respuesta JSON
                        NasaApodResponse response = objectMapper.readValue(rawResponse, NasaApodResponse.class);
                        response.setStatus("SUCCESS");

                        log.info("Parsed NASA APOD response: {}", response);

                        // Guardar en la base de datos de forma asíncrona
                        NasaApodQuery query = new NasaApodQuery(
                            response.getDate(),
                            response.getTitle(),
                            response.getExplanation(),
                            response.getUrl(),
                            response.getHdUrl(),
                            response.getMediaType(),
                            response.getCopyright(),
                            "SUCCESS"
                        );
                        
                        nasaApodRepository.save(query)
                            .doOnSuccess(saved -> log.info("NASA APOD query saved to database with ID: {}", saved.getId()))
                            .doOnError(error -> log.error("Error saving NASA APOD query to database: {}", error.getMessage()))
                            .subscribe(); // No bloqueamos la respuesta por la BD

                        return response;

                    } catch (Exception e) {
                        log.error("Error parsing NASA APOD response: {}", e.getMessage());
                        log.error("Raw response that failed to parse: {}", rawResponse);
                        
                        NasaApodResponse errorResponse = new NasaApodResponse();
                        errorResponse.setStatus("PARSE_ERROR");
                        errorResponse.setTitle("Error parsing response");
                        return errorResponse;
                    }
                })
                .onErrorReturn(createErrorResponse("CONNECTION_ERROR"));
    }

    public Mono<NasaApodResponse> getTodayApod() {
        return getApod(null); // null = today
    }

    private NasaApodResponse createErrorResponse(String errorType) {
        NasaApodResponse response = new NasaApodResponse();
        response.setStatus("ERROR");
        response.setTitle(errorType + " - Could not connect to NASA APOD API");
        return response;
    }
}