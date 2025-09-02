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
import pe.edu.vallegrande.apis_ai_demos.dto.NasaApodRequest;
import pe.edu.vallegrande.apis_ai_demos.dto.NasaApodResponse;
import pe.edu.vallegrande.apis_ai_demos.entity.NasaApodQuery;
import pe.edu.vallegrande.apis_ai_demos.repository.NasaApodRepository;
import pe.edu.vallegrande.apis_ai_demos.service.NasaApodService;
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
    @Operation(
        summary = "Obtener imagen astronómica del día actual",
        description = "Obtiene la imagen astronómica del día actual de la NASA",
        responses = {
            @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = NasaApodResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public Mono<NasaApodResponse> getTodayApod() {
        log.info("=== NASA APOD TODAY REQUEST ===");
        log.info("Getting today's APOD");
        log.info("===============================");
        
        return nasaApodService.getTodayApod()
                .doOnSuccess(response -> log.info("Successful today APOD retrieval: {}", response.getTitle()))
                .doOnError(error -> log.error("Error getting today APOD: {}", error.getMessage()));
    }

    @GetMapping(value = "/date", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Obtener imagen astronómica por fecha",
        description = "Obtiene la imagen astronómica de una fecha específica",
        responses = {
            @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = NasaApodResponse.class))),
            @ApiResponse(responseCode = "400", description = "Fecha inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public Mono<NasaApodResponse> getApodByDate(
            @Parameter(description = "Fecha en formato YYYY-MM-DD", required = true, example = "2023-12-25")
            @RequestParam String date) {
        
        log.info("=== NASA APOD BY DATE REQUEST ===");
        log.info("Requested date: {}", date);
        log.info("=================================");
        
        return nasaApodService.getApod(date)
                .doOnSuccess(response -> log.info("Successful APOD retrieval for date {}: {}", date, response.getTitle()))
                .doOnError(error -> log.error("Error getting APOD for date {}: {}", date, error.getMessage()));
    }

    @PostMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Obtener imagen astronómica (método POST)",
        description = "Obtiene la imagen astronómica usando un objeto de solicitud",
        responses = {
            @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = NasaApodResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public Mono<NasaApodResponse> getApod(
            @Parameter(description = "Datos de la solicitud para obtener APOD")
            @RequestBody NasaApodRequest request) {
        
        log.info("=== NASA APOD POST REQUEST ===");
        log.info("Request: {}", request);
        log.info("==============================");
        
        String date = request != null ? request.getDate() : null;
        
        return nasaApodService.getApod(date)
                .doOnSuccess(response -> log.info("Successful APOD retrieval: {}", response.getTitle()))
                .doOnError(error -> log.error("Error in APOD retrieval: {}", error.getMessage()));
    }

    @GetMapping(value = "/test-nasa-api", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Probar conexión con NASA APOD API",
        description = "Endpoint para verificar que la conexión con NASA APOD API funciona correctamente"
    )
    public Mono<String> testNasaApi() {
        return nasaApodService.getTodayApod()
                .map(response -> "NASA APOD API connection: SUCCESS - Today's title: " + response.getTitle())
                .onErrorReturn("NASA APOD API connection: FAILED");
    }

    @GetMapping(value = "/test-nasa-database", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Probar conexión con base de datos NASA APOD",
        description = "Endpoint para verificar que la conexión con PostgreSQL funciona correctamente para NASA APOD"
    )
    public Mono<String> testNasaDatabase() {
        NasaApodQuery testQuery = new NasaApodQuery(
            "2023-01-01",
            "Test APOD Entry",
            "This is a test entry for database connection",
            "https://test.com/image.jpg",
            "https://test.com/hd_image.jpg",
            "image",
            "Test Copyright",
            "SUCCESS"
        );
        
        return nasaApodRepository.save(testQuery)
                .map(saved -> "NASA APOD Database connection: SUCCESS - Saved record with ID: " + saved.getId())
                .onErrorReturn("NASA APOD Database connection: FAILED");
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Verificar estado del servicio NASA APOD",
        description = "Endpoint para verificar que el servicio de NASA APOD está funcionando"
    )
    public Mono<String> healthCheck() {
        return Mono.just("NASA APOD Service is running!");
    }
}