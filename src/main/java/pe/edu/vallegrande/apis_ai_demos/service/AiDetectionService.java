package pe.edu.vallegrande.apis_ai_demos.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.apis_ai_demos.dto.AiDetectionRequest;
import pe.edu.vallegrande.apis_ai_demos.dto.AiDetectionResponse;
import pe.edu.vallegrande.apis_ai_demos.entity.AiDetectionQuery;
import pe.edu.vallegrande.apis_ai_demos.repository.AiDetectionRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiDetectionService {

    private final WebClient aiDetectionWebClient;
    private final AiDetectionRepository aiDetectionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<AiDetectionResponse> detectAi(String text, String language) {
        log.info("Calling AI Detection API for text: {}", text.substring(0, Math.min(text.length(), 50)) + "...");
        
        AiDetectionRequest request = new AiDetectionRequest(text, language);
        log.info("Request payload: {}", request);
        
        return aiDetectionWebClient
                .post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class) // Primero obtenemos String para ver la respuesta cruda
                .doOnSuccess(rawResponse -> {
                    log.info("=== RAW API RESPONSE ===");
                    log.info("{}", rawResponse);
                    log.info("=========================");
                })
                .doOnError(error -> {
                    log.error("=== API ERROR ===");
                    log.error("Error calling AI Detection API: {}", error.getMessage());
                    log.error("Error class: {}", error.getClass().getSimpleName());
                    if (error.getCause() != null) {
                        log.error("Caused by: {}", error.getCause().getMessage());
                    }
                    log.error("=================");
                })
                .map(rawResponse -> {
                    try {
                        log.info("Processing rawResponse: {}", rawResponse);
                        
                        // Intentar parsear la respuesta JSON de RapidAPI
                        JsonNode jsonNode = objectMapper.readTree(rawResponse);
                        
                        // Extraer los datos reales de la respuesta
                        String responseText = text; // El texto original
                        Double aiProbability = null;
                        String classification = "UNKNOWN";
                        
                        // Buscar el ai_score (que va de 0 a 100, lo convertimos a 0-1)
                        if (jsonNode.has("ai_score")) {
                            double score = jsonNode.get("ai_score").asDouble();
                            aiProbability = score / 100.0; // Convertir de 0-100 a 0-1
                        }
                        
                        // Determinar la clasificación basada en el summary
                        if (jsonNode.has("summary")) {
                            JsonNode summary = jsonNode.get("summary");
                            double aiScore = summary.has("ai") ? summary.get("ai").asDouble() : 0.0;
                            double humanScore = summary.has("human") ? summary.get("human").asDouble() : 0.0;
                            double mixedScore = summary.has("mixed") ? summary.get("mixed").asDouble() : 0.0;
                            
                            if (aiScore > humanScore && aiScore > mixedScore) {
                                classification = "AI_GENERATED";
                            } else if (humanScore > aiScore && humanScore > mixedScore) {
                                classification = "HUMAN_WRITTEN";
                            } else if (mixedScore > 0) {
                                classification = "MIXED_CONTENT";
                            } else {
                                classification = "UNCERTAIN";
                            }
                        }
                        
                        // Si no pudimos extraer la probabilidad, usar un valor por defecto
                        if (aiProbability == null) {
                            aiProbability = 0.0;
                            classification = "PARSE_ERROR";
                        }
                        
                        log.info("Parsed - aiProbability: {}, classification: {}", aiProbability, classification);
                        
                        AiDetectionResponse response = new AiDetectionResponse(responseText, aiProbability, classification, language);
                        
                        // Guardar en la base de datos de forma asíncrona
                        AiDetectionQuery query = new AiDetectionQuery(text, language, aiProbability, classification);
                        aiDetectionRepository.save(query)
                            .doOnSuccess(saved -> log.info("Query saved to database with ID: {}", saved.getId()))
                            .doOnError(error -> log.error("Error saving to database: {}", error.getMessage()))
                            .subscribe(); // No bloqueamos la respuesta por la BD
                        
                        return response;
                        
                    } catch (Exception e) {
                        log.error("Error parsing response: {}", e.getMessage());
                        log.error("Raw response that failed to parse: {}", rawResponse);
                        return new AiDetectionResponse(text, 0.0, "PARSE_ERROR", language);
                    }
                })
                .onErrorReturn(new AiDetectionResponse(text, 0.0, "CONNECTION_ERROR", language));
    }

    public Mono<AiDetectionResponse> detectAi(String text) {
        return detectAi(text, "en"); // Default to English
    }
}
