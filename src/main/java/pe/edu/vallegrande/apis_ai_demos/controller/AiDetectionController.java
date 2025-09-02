package pe.edu.vallegrande.apis_ai_demos.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.apis_ai_demos.dto.AiDetectionRequest;
import pe.edu.vallegrande.apis_ai_demos.dto.AiDetectionResponse;
import pe.edu.vallegrande.apis_ai_demos.entity.AiDetectionQuery;
import pe.edu.vallegrande.apis_ai_demos.repository.AiDetectionRepository;
import pe.edu.vallegrande.apis_ai_demos.service.AiDetectionService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/ai-detection")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AI Detection", description = "Endpoints para detectar contenido generado por IA")
public class AiDetectionController {

    private final AiDetectionService aiDetectionService;
    private final AiDetectionRepository aiDetectionRepository;

    @PostMapping(value = "/detect", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Detectar contenido generado por IA",
        description = "Analiza un texto para determinar si fue generado por inteligencia artificial",
        responses = {
            @ApiResponse(responseCode = "200", description = "Análisis completado exitosamente",
                content = @Content(schema = @Schema(implementation = AiDetectionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public Mono<AiDetectionResponse> detectAi(
            @Parameter(description = "Datos de la solicitud de detección de IA", required = true)
            @RequestBody AiDetectionRequest request) {
        
        // Logging para debug
        log.info("=== POST REQUEST RECEIVED ===");
        log.info("Request: {}", request);
        log.info("Text: {}", request != null ? request.getText() : "NULL");
        log.info("Lang: {}", request != null ? request.getLang() : "NULL");
        log.info("=============================");
        
        // Validación básica
        if (request == null || request.getText() == null || request.getText().trim().isEmpty()) {
            log.error("Invalid request: text is null or empty");
            return Mono.error(new IllegalArgumentException("Text is required"));
        }
        
        String lang = request.getLang() != null ? request.getLang() : "en";
        
        return aiDetectionService.detectAi(request.getText(), lang)
                .doOnSuccess(response -> log.info("Successful detection: {}", response))
                .doOnError(error -> log.error("Error in detection: {}", error.getMessage()));
    }

    @GetMapping(value = "/detect-simple", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Detectar contenido generado por IA (método simple)",
        description = "Analiza un texto para determinar si fue generado por IA usando parámetros de consulta",
        responses = {
            @ApiResponse(responseCode = "200", description = "Análisis completado exitosamente",
                content = @Content(schema = @Schema(implementation = AiDetectionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Texto es requerido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public Mono<AiDetectionResponse> detectAiSimple(
            @Parameter(description = "Texto a analizar", required = true, example = "This is a sample text to analyze")
            @RequestParam String text,
            @Parameter(description = "Idioma del texto", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        return aiDetectionService.detectAi(text, lang);
    }

    @GetMapping(value = "/test-database", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Probar conexión con base de datos",
        description = "Endpoint para verificar que la conexión con PostgreSQL funciona correctamente"
    )
    public Mono<String> testDatabase() {
        AiDetectionQuery testQuery = new AiDetectionQuery("Test message", "en", 0.5, "TEST");
        
        return aiDetectionRepository.save(testQuery)
                .map(saved -> "Database connection: SUCCESS - Saved record with ID: " + saved.getId())
                .onErrorReturn("Database connection: FAILED");
    }

    @GetMapping(value = "/test-rapidapi", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Probar conexión con RapidAPI",
        description = "Endpoint para verificar que la conexión con RapidAPI funciona correctamente"
    )
    public Mono<String> testRapidApi() {
        return aiDetectionService.detectAi("This is a test message", "en")
                .map(response -> "RapidAPI connection: SUCCESS - Classification: " + response.getClassification())
                .onErrorReturn("RapidAPI connection: FAILED");
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Verificar estado del servicio",
        description = "Endpoint para verificar que el servicio de detección de IA está funcionando"
    )
    public Mono<String> healthCheck() {
        return Mono.just("AI Detection Service is running!");
    }
}
