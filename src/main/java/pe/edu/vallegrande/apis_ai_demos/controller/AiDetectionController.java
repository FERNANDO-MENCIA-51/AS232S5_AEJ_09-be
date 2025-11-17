package pe.edu.vallegrande.apis_ai_demos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
import reactor.core.publisher.Flux;
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
        @Operation(summary = "Detectar contenido generado por IA", description = "Analiza un texto para determinar si fue generado por inteligencia artificial", responses = {
                        @ApiResponse(responseCode = "200", description = "Análisis completado exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public Mono<AiDetectionResponse> detectAi(
                        @Parameter(description = "Datos de la solicitud de detección de IA", required = true) @RequestBody AiDetectionRequest request) {

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
        @Operation(summary = "Detectar contenido generado por IA (método simple)", description = "Analiza un texto para determinar si fue generado por IA usando parámetros de consulta", responses = {
                        @ApiResponse(responseCode = "200", description = "Análisis completado exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Texto es requerido"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public Mono<AiDetectionResponse> detectAiSimple(
                        @Parameter(description = "Texto a analizar", required = true, example = "This is a sample text to analyze") @RequestParam String text,
                        @Parameter(description = "Idioma del texto", example = "en") @RequestParam(defaultValue = "en") String lang) {
                return aiDetectionService.detectAi(text, lang);
        }

        @GetMapping(value = "/test-database", produces = MediaType.TEXT_PLAIN_VALUE)
        @Operation(summary = "Probar conexión con base de datos", description = "Endpoint para verificar que la conexión con PostgreSQL funciona correctamente")
        public Mono<String> testDatabase() {
                AiDetectionQuery testQuery = new AiDetectionQuery("Test message", "en", 0.5, "TEST");

                return aiDetectionRepository.save(testQuery)
                                .map(saved -> "Database connection: SUCCESS - Saved record with ID: " + saved.getId())
                                .onErrorReturn("Database connection: FAILED");
        }

        @GetMapping(value = "/test-rapidapi", produces = MediaType.TEXT_PLAIN_VALUE)
        @Operation(summary = "Probar conexión con RapidAPI", description = "Endpoint para verificar que la conexión con RapidAPI funciona correctamente")
        public Mono<String> testRapidApi() {
                return aiDetectionService.detectAi("This is a test message", "en")
                                .map(response -> "RapidAPI connection: SUCCESS - Classification: "
                                                + response.getClassification())
                                .onErrorReturn("RapidAPI connection: FAILED");
        }

        @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
        @Operation(summary = "Verificar estado del servicio", description = "Endpoint para verificar que el servicio de detección de IA está funcionando")
        public Mono<String> healthCheck() {
                return Mono.just("AI Detection Service is running!");
        }

        // ==================== CRUD ENDPOINTS ====================

        @GetMapping(value = "/queries", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Listar consultas con paginación", description = "Obtiene consultas de detección de IA con paginación", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionQuery.class)))
        })
        public Flux<AiDetectionQuery> getQueries(
                        @Parameter(description = "Número de página (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Filtrar por idioma (opcional)", example = "en") @RequestParam(required = false) String lang) {

                log.info("=== GET AI DETECTION QUERIES WITH PAGINATION ===");
                log.info("Page: {}, Size: {}, Language filter: {}", page, size, lang);

                long skip = (long) page * size;

                if (lang != null && !lang.trim().isEmpty()) {
                        return aiDetectionRepository.findByLang(lang)
                                        .skip(skip)
                                        .take(size)
                                        .doOnComplete(() -> log
                                                        .info("Queries retrieved with pagination and language filter"));
                }

                return aiDetectionRepository.findAll()
                                .skip(skip)
                                .take(size)
                                .doOnComplete(() -> log.info("Queries retrieved with pagination"));
        }

        @GetMapping(value = "/queries/search", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar consultas con paginación", description = "Busca consultas de detección de IA con paginación", responses = {
                        @ApiResponse(responseCode = "200", description = "Búsqueda completada", content = @Content(schema = @Schema(implementation = AiDetectionQuery.class)))
        })
        public Flux<AiDetectionQuery> searchQueries(
                        @Parameter(description = "Número de página (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Filtrar por idioma (opcional)", example = "en") @RequestParam(required = false) String lang) {

                log.info("=== SEARCH AI DETECTION QUERIES ===");
                log.info("Page: {}, Size: {}, Language: {}", page, size, lang);

                long skip = (long) page * size;

                if (lang != null && !lang.trim().isEmpty()) {
                        return aiDetectionRepository.findByLang(lang)
                                        .skip(skip)
                                        .take(size);
                }

                return aiDetectionRepository.findAll()
                                .skip(skip)
                                .take(size);
        }

        @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Listar historial de consultas", description = "Obtiene todas las consultas de detección de IA realizadas", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionQuery.class)))
        })
        public Flux<AiDetectionQuery> getAllHistory(
                        @Parameter(description = "Filtrar por idioma (opcional)", example = "en") @RequestParam(required = false) String lang) {

                log.info("=== GET ALL AI DETECTION HISTORY ===");
                log.info("Language filter: {}", lang);

                if (lang != null && !lang.trim().isEmpty()) {
                        return aiDetectionRepository.findByLang(lang)
                                        .doOnComplete(() -> log.info("History retrieved with language filter: {}",
                                                        lang));
                }

                return aiDetectionRepository.findAll()
                                .doOnComplete(() -> log.info("All history retrieved"));
        }

        @GetMapping(value = "/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar consulta por ID", description = "Obtiene una consulta específica de detección de IA por su ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Consulta encontrada", content = @Content(schema = @Schema(implementation = AiDetectionQuery.class))),
                        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
        })
        public Mono<AiDetectionQuery> getHistoryById(
                        @Parameter(description = "ID de la consulta", required = true) @PathVariable Long id) {

                log.info("=== GET AI DETECTION BY ID ===");
                log.info("ID: {}", id);

                return aiDetectionRepository.findById(id != null ? id : 0L)
                                .doOnSuccess(query -> log.info("Query found: {}", query))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "AI Detection query not found with ID: " + id)));
        }

        @DeleteMapping(value = "/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Eliminar consulta por ID", description = "Elimina una consulta específica de detección de IA", responses = {
                        @ApiResponse(responseCode = "200", description = "Consulta eliminada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
        })
        public Mono<String> deleteHistoryById(
                        @Parameter(description = "ID de la consulta a eliminar", required = true) @PathVariable Long id) {

                log.info("=== DELETE AI DETECTION BY ID ===");
                log.info("ID: {}", id);

                return aiDetectionRepository.findById(id != null ? id : 0L)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "AI Detection query not found with ID: " + id)))
                                .flatMap(query -> aiDetectionRepository.deleteById(query.getId() != null ? query.getId() : 0L)
                                                .then(Mono.just("AI Detection query deleted successfully with ID: "
                                                                + id)))
                                .doOnSuccess(result -> log.info("Query deleted: {}", id));
        }

        @DeleteMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Eliminar todo el historial", description = "Elimina todas las consultas de detección de IA", responses = {
                        @ApiResponse(responseCode = "200", description = "Historial eliminado exitosamente")
        })
        public Mono<String> deleteAllHistory() {
                log.info("=== DELETE ALL AI DETECTION HISTORY ===");

                return aiDetectionRepository.deleteAll()
                                .then(Mono.just("All AI Detection history deleted successfully"))
                                .doOnSuccess(result -> log.info("All history deleted"));
        }

        @GetMapping(value = "/history/count", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Contar consultas", description = "Obtiene el número total de consultas realizadas", responses = {
                        @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
        })
        public Mono<Long> countHistory() {
                log.info("=== COUNT AI DETECTION HISTORY ===");

                return aiDetectionRepository.count()
                                .doOnSuccess(count -> log.info("Total queries: {}", count));
        }
}
