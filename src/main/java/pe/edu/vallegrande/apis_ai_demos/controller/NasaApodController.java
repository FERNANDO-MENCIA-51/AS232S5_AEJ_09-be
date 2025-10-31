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
import pe.edu.vallegrande.apis_ai_demos.dto.NasaApodRequest;
import pe.edu.vallegrande.apis_ai_demos.dto.NasaApodResponse;
import pe.edu.vallegrande.apis_ai_demos.entity.NasaApodQuery;
import pe.edu.vallegrande.apis_ai_demos.repository.NasaApodRepository;
import pe.edu.vallegrande.apis_ai_demos.service.NasaApodService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/nasa-apod")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "NASA APOD", description = "Endpoints para obtener la Imagen Astronómica del Día de la NASA")
public class NasaApodController {

        private final NasaApodService nasaApodService;
        private final NasaApodRepository nasaApodRepository;

        @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Obtener imagen astronómica del día actual", description = "Obtiene la imagen astronómica del día actual de la NASA", responses = {
                        @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public Mono<NasaApodResponse> getTodayApod() {
                log.info("=== NASA APOD TODAY REQUEST ===");
                log.info("Getting today's APOD");
                log.info("===============================");

                return nasaApodService.getTodayApod()
                                .doOnSuccess(response -> log.info("Successful today APOD retrieval: {}",
                                                response.getTitle()))
                                .doOnError(error -> log.error("Error getting today APOD: {}", error.getMessage()));
        }

        @GetMapping(value = "/date", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Obtener imagen astronómica por fecha", description = "Obtiene la imagen astronómica de una fecha específica", responses = {
                        @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Fecha inválida"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public Mono<NasaApodResponse> getApodByDate(
                        @Parameter(description = "Fecha en formato YYYY-MM-DD", required = true, example = "2023-12-25") @RequestParam String date) {

                log.info("=== NASA APOD BY DATE REQUEST ===");
                log.info("Requested date: {}", date);
                log.info("=================================");

                return nasaApodService.getApod(date)
                                .doOnSuccess(
                                                response -> log.info("Successful APOD retrieval for date {}: {}", date,
                                                                response.getTitle()))
                                .doOnError(error -> log.error("Error getting APOD for date {}: {}", date,
                                                error.getMessage()));
        }

        @PostMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Obtener imagen astronómica (método POST)", description = "Obtiene la imagen astronómica usando un objeto de solicitud", responses = {
                        @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public Mono<NasaApodResponse> getApod(
                        @Parameter(description = "Datos de la solicitud para obtener APOD") @RequestBody NasaApodRequest request) {

                log.info("=== NASA APOD POST REQUEST ===");
                log.info("Request: {}", request);
                log.info("==============================");

                String date = request != null ? request.getDate() : null;

                return nasaApodService.getApod(date)
                                .doOnSuccess(response -> log.info("Successful APOD retrieval: {}", response.getTitle()))
                                .doOnError(error -> log.error("Error in APOD retrieval: {}", error.getMessage()));
        }

        @GetMapping(value = "/test-nasa-api", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Probar conexión con NASA APOD API", description = "Endpoint para verificar que la conexión con NASA APOD API funciona correctamente")
        public Mono<String> testNasaApi() {
                return nasaApodService.getTodayApod()
                                .map(response -> "NASA APOD API connection: SUCCESS - Today's title: "
                                                + response.getTitle())
                                .onErrorReturn("NASA APOD API connection: FAILED");
        }

        @GetMapping(value = "/test-nasa-database", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Probar conexión con base de datos NASA APOD", description = "Endpoint para verificar que la conexión con PostgreSQL funciona correctamente para NASA APOD")
        public Mono<String> testNasaDatabase() {
                NasaApodQuery testQuery = new NasaApodQuery(
                                "2023-01-01",
                                "Test APOD Entry",
                                "This is a test entry for database connection",
                                "https://test.com/image.jpg",
                                "https://test.com/hd_image.jpg",
                                "image",
                                "Test Copyright",
                                "SUCCESS");

                return nasaApodRepository.save(testQuery)
                                .map(saved -> "NASA APOD Database connection: SUCCESS - Saved record with ID: "
                                                + saved.getId())
                                .onErrorReturn("NASA APOD Database connection: FAILED");
        }

        @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Verificar estado del servicio NASA APOD", description = "Endpoint para verificar que el servicio de NASA APOD está funcionando")
        public Mono<String> healthCheck() {
                return Mono.just("NASA APOD Service is running!");
        }

        // ==================== CRUD ENDPOINTS ====================

        @GetMapping(value = "/queries", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Listar consultas NASA APOD con paginación", description = "Obtiene consultas de NASA APOD con paginación", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(schema = @Schema(implementation = NasaApodQuery.class)))
        })
        public Flux<NasaApodQuery> getQueries(
                        @Parameter(description = "Número de página (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Tamaño de página", example = "12") @RequestParam(defaultValue = "12") int size,
                        @Parameter(description = "Filtrar por tipo de media (opcional)", example = "image") @RequestParam(required = false) String mediaType,
                        @Parameter(description = "Filtrar por status (opcional)", example = "SUCCESS") @RequestParam(required = false) String status) {

                log.info("=== GET NASA APOD QUERIES WITH PAGINATION ===");
                log.info("Page: {}, Size: {}, MediaType: {}, Status: {}", page, size, mediaType, status);

                long skip = (long) page * size;

                if (mediaType != null && !mediaType.trim().isEmpty()) {
                        return nasaApodRepository.findByMediaType(mediaType)
                                        .skip(skip)
                                        .take(size);
                }

                if (status != null && !status.trim().isEmpty()) {
                        return nasaApodRepository.findByStatus(status)
                                        .skip(skip)
                                        .take(size);
                }

                return nasaApodRepository.findAll()
                                .skip(skip)
                                .take(size)
                                .doOnComplete(() -> log.info("Queries retrieved with pagination"));
        }

        @PostMapping(value = "/queries", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Guardar consulta NASA APOD", description = "Guarda una nueva consulta de NASA APOD", responses = {
                        @ApiResponse(responseCode = "200", description = "Consulta guardada exitosamente", content = @Content(schema = @Schema(implementation = NasaApodQuery.class)))
        })
        public Mono<NasaApodQuery> saveQuery(
                        @Parameter(description = "Datos de la consulta a guardar", required = true) @RequestBody NasaApodQuery query) {

                log.info("=== SAVE NASA APOD QUERY ===");
                log.info("Query: {}", query);

                return nasaApodRepository.save(query)
                                .doOnSuccess(saved -> log.info("Query saved with ID: {}", saved.getId()));
        }

        @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Listar historial de consultas NASA APOD", description = "Obtiene todas las consultas de NASA APOD realizadas", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(schema = @Schema(implementation = NasaApodQuery.class)))
        })
        public Flux<NasaApodQuery> getAllHistory(
                        @Parameter(description = "Filtrar por tipo de media (opcional)", example = "image") @RequestParam(required = false) String mediaType,
                        @Parameter(description = "Filtrar por status (opcional)", example = "SUCCESS") @RequestParam(required = false) String status,
                        @Parameter(description = "Límite de resultados (opcional)", example = "10") @RequestParam(required = false) Integer limit) {

                log.info("=== GET ALL NASA APOD HISTORY ===");
                log.info("MediaType filter: {}, Status filter: {}, Limit: {}", mediaType, status, limit);

                if (mediaType != null && !mediaType.trim().isEmpty()) {
                        return nasaApodRepository.findByMediaType(mediaType)
                                        .doOnComplete(() -> log.info("History retrieved with mediaType filter: {}",
                                                        mediaType));
                }

                if (status != null && !status.trim().isEmpty()) {
                        return nasaApodRepository.findByStatus(status)
                                        .doOnComplete(() -> log.info("History retrieved with status filter: {}",
                                                        status));
                }

                if (limit != null && limit > 0) {
                        return nasaApodRepository.findRecentQueries(limit)
                                        .doOnComplete(() -> log.info("Recent {} queries retrieved", limit));
                }

                return nasaApodRepository.findAll()
                                .doOnComplete(() -> log.info("All NASA APOD history retrieved"));
        }

        @GetMapping(value = "/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar consulta NASA APOD por ID", description = "Obtiene una consulta específica de NASA APOD por su ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Consulta encontrada", content = @Content(schema = @Schema(implementation = NasaApodQuery.class))),
                        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
        })
        public Mono<NasaApodQuery> getHistoryById(
                        @Parameter(description = "ID de la consulta", required = true) @PathVariable Long id) {

                log.info("=== GET NASA APOD BY ID ===");
                log.info("ID: {}", id);

                return nasaApodRepository.findById(id)
                                .doOnSuccess(query -> log.info("NASA APOD query found: {}", query))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "NASA APOD query not found with ID: " + id)));
        }

        @GetMapping(value = "/history/search", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar por título", description = "Busca consultas de NASA APOD por título (búsqueda parcial)", responses = {
                        @ApiResponse(responseCode = "200", description = "Búsqueda completada", content = @Content(schema = @Schema(implementation = NasaApodQuery.class)))
        })
        public Flux<NasaApodQuery> searchByTitle(
                        @Parameter(description = "Título a buscar", required = true, example = "Moon") @RequestParam String title) {

                log.info("=== SEARCH NASA APOD BY TITLE ===");
                log.info("Title: {}", title);

                return nasaApodRepository.findByTitleContaining(title)
                                .doOnComplete(() -> log.info("Search completed for title: {}", title));
        }

        @GetMapping(value = "/history/by-date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar por fecha", description = "Busca consultas de NASA APOD por fecha específica", responses = {
                        @ApiResponse(responseCode = "200", description = "Búsqueda completada", content = @Content(schema = @Schema(implementation = NasaApodQuery.class)))
        })
        public Flux<NasaApodQuery> getHistoryByDate(
                        @Parameter(description = "Fecha en formato YYYY-MM-DD", required = true, example = "2023-12-25") @PathVariable String date) {

                log.info("=== GET NASA APOD HISTORY BY DATE ===");
                log.info("Date: {}", date);

                return nasaApodRepository.findByRequestedDate(date)
                                .doOnComplete(() -> log.info("History retrieved for date: {}", date));
        }

        @DeleteMapping(value = "/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Eliminar consulta NASA APOD por ID", description = "Elimina una consulta específica de NASA APOD", responses = {
                        @ApiResponse(responseCode = "200", description = "Consulta eliminada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
        })
        public Mono<String> deleteHistoryById(
                        @Parameter(description = "ID de la consulta a eliminar", required = true) @PathVariable Long id) {

                log.info("=== DELETE NASA APOD BY ID ===");
                log.info("ID: {}", id);

                return nasaApodRepository.findById(id)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "NASA APOD query not found with ID: " + id)))
                                .flatMap(query -> nasaApodRepository.deleteById(id)
                                                .then(Mono.just("NASA APOD query deleted successfully with ID: " + id)))
                                .doOnSuccess(result -> log.info("NASA APOD query deleted: {}", id));
        }

        @DeleteMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Eliminar todo el historial NASA APOD", description = "Elimina todas las consultas de NASA APOD", responses = {
                        @ApiResponse(responseCode = "200", description = "Historial eliminado exitosamente")
        })
        public Mono<String> deleteAllHistory() {
                log.info("=== DELETE ALL NASA APOD HISTORY ===");

                return nasaApodRepository.deleteAll()
                                .then(Mono.just("All NASA APOD history deleted successfully"))
                                .doOnSuccess(result -> log.info("All NASA APOD history deleted"));
        }

        @GetMapping(value = "/history/count", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Contar consultas NASA APOD", description = "Obtiene el número total de consultas de NASA APOD realizadas", responses = {
                        @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
        })
        public Mono<Long> countHistory() {
                log.info("=== COUNT NASA APOD HISTORY ===");

                return nasaApodRepository.count()
                                .doOnSuccess(count -> log.info("Total NASA APOD queries: {}", count));
        }
}