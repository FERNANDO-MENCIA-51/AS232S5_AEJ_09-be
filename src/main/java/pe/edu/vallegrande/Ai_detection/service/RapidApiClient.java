package pe.edu.vallegrande.Ai_detection.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.Ai_detection.dto.RapidApiResponse;
import reactor.core.publisher.Mono;

/**
 * Cliente para consumir RapidAPI AI Detection
 * Proporciona métodos para detectar contenido generado por IA
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RapidApiClient {

    @Qualifier("rapidApiWebClient")
    private final WebClient rapidApiWebClient;

    /**
     * Detecta si un texto fue generado por IA usando RapidAPI
     *
     * @param text Texto a analizar
     * @return Mono con la respuesta de RapidAPI
     */
    public Mono<RapidApiResponse> detectAiContent(String text) {
        log.info("Llamando a RapidAPI para analizar texto");

        return rapidApiWebClient
                .post()
                .uri("/v1/ai-detection-rapid-api")
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(RapidApiResponse.class)
                .doOnSuccess(response -> log.info("Respuesta exitosa de RapidAPI: isAiGenerated={}, confidence={}",
                        response.getIsAiGenerated(), response.getConfidence()))
                .doOnError(WebClientResponseException.class,
                        error -> log.error("Error al llamar a RapidAPI: status={}, body={}",
                                error.getStatusCode(), error.getResponseBodyAsString()))
                .onErrorResume(WebClientResponseException.class, error -> {
                    log.error("Error en RapidAPI, retornando respuesta por defecto");
                    // Retornar respuesta por defecto en caso de error
                    return Mono.just(RapidApiResponse.builder()
                            .isAiGenerated(false)
                            .confidence(0.0)
                            .message("Error al procesar la solicitud: " + error.getMessage())
                            .build());
                })
                .onErrorResume(Exception.class, error -> {
                    log.error("Error inesperado al llamar a RapidAPI: {}", error.getMessage());
                    return Mono.just(RapidApiResponse.builder()
                            .isAiGenerated(false)
                            .confidence(0.0)
                            .message("Error inesperado: " + error.getMessage())
                            .build());
                });
    }
}
