package pe.edu.vallegrande.Ai_detection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.Ai_detection.dto.ErrorResponse;
import pe.edu.vallegrande.Ai_detection.dto.NasaApodRequestDTO;
import pe.edu.vallegrande.Ai_detection.dto.NasaApodResponseDTO;
import pe.edu.vallegrande.Ai_detection.service.NasaApodService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para gestionar imágenes astronómicas del día (APOD) de NASA
 * Proporciona endpoints para operaciones CRUD con eliminación lógica
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/nasa-apod")
@RequiredArgsConstructor
@Tag(name = "NASA APOD", description = "API para gestionar imágenes astronómicas del día de NASA")
public class NasaApodController {

        private final NasaApodService service;

        @Operation(summary = "Crear nuevo registro APOD", description = "Obtiene datos de NASA APOD API para una fecha específica (o la actual) y los guarda")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Registro APOD creado exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "502", description = "Error al comunicarse con NASA API", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping
        public Mono<ResponseEntity<NasaApodResponseDTO>> create(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fecha de la imagen astronómica (opcional, por defecto fecha actual)", required = true, content = @Content(schema = @Schema(implementation = NasaApodRequestDTO.class))) @RequestBody NasaApodRequestDTO request) {
                log.info("POST /api/v1/nasa-apod - Crear nuevo registro APOD");
                return service.create(request)
                                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
        }

        @Operation(summary = "Listar todos los registros APOD activos", description = "Obtiene todos los registros de imágenes astronómicas que están activos (no eliminados)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de registros APOD obtenida exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class)))
        })
        @GetMapping
        public Flux<NasaApodResponseDTO> findAll() {
                log.info("GET /api/v1/nasa-apod - Listar todos los registros APOD activos");
                return service.findAll();
        }

        @Operation(summary = "Obtener registro APOD por ID", description = "Obtiene un registro específico por su identificador (solo si está activo)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Registro APOD encontrado", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Registro APOD no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/{id}")
        public Mono<ResponseEntity<NasaApodResponseDTO>> findById(
                        @Parameter(description = "ID del registro APOD", required = true) @PathVariable Long id) {
                log.info("GET /api/v1/nasa-apod/{} - Obtener registro APOD por ID", id);
                return service.findById(id)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Actualizar registro APOD existente", description = "Actualiza un registro existente, obteniendo nuevos datos de NASA API")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Registro APOD actualizado exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Registro APOD no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PutMapping("/{id}")
        public Mono<ResponseEntity<NasaApodResponseDTO>> update(
                        @Parameter(description = "ID del registro APOD", required = true) @PathVariable Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del registro APOD", required = true, content = @Content(schema = @Schema(implementation = NasaApodRequestDTO.class))) @RequestBody NasaApodRequestDTO request) {
                log.info("PUT /api/v1/nasa-apod/{} - Actualizar registro APOD", id);
                return service.update(id, request)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Eliminar registro APOD (eliminación lógica)", description = "Elimina lógicamente un registro cambiando su estado a inactivo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Registro APOD eliminado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Registro APOD no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @DeleteMapping("/{id}")
        public Mono<ResponseEntity<Void>> delete(
                        @Parameter(description = "ID del registro APOD", required = true) @PathVariable Long id) {
                log.info("DELETE /api/v1/nasa-apod/{} - Eliminar registro APOD", id);
                return service.delete(id)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
        }

        @Operation(summary = "Restaurar registro APOD eliminado", description = "Restaura un registro previamente eliminado cambiando su estado a activo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Registro APOD restaurado exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Registro APOD eliminado no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PatchMapping("/{id}/restore")
        public Mono<ResponseEntity<NasaApodResponseDTO>> restore(
                        @Parameter(description = "ID del registro APOD", required = true) @PathVariable Long id) {
                log.info("PATCH /api/v1/nasa-apod/{}/restore - Restaurar registro APOD", id);
                return service.restore(id)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Obtener APOD del día actual desde NASA API y guardar", description = "Consume la API de NASA para obtener la imagen astronómica del día actual y la guarda en base de datos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Datos APOD obtenidos y guardados exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class))),
                        @ApiResponse(responseCode = "502", description = "Error al comunicarse con NASA API", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/fetch/today")
        public Mono<ResponseEntity<NasaApodResponseDTO>> fetchTodayApod() {
                log.info("GET /api/v1/nasa-apod/fetch/today - Obtener APOD del día actual desde NASA API y guardar");
                return service.fetchTodayApodFromApi()
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Obtener APOD de una fecha específica desde NASA API y guardar", description = "Consume la API de NASA para obtener la imagen astronómica de una fecha específica y la guarda en base de datos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Datos APOD obtenidos y guardados exitosamente", content = @Content(schema = @Schema(implementation = NasaApodResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Fecha inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "502", description = "Error al comunicarse con NASA API", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/fetch/date/{date}")
        public Mono<ResponseEntity<NasaApodResponseDTO>> fetchApodByDate(
                        @Parameter(description = "Fecha en formato YYYY-MM-DD (ejemplo: 2024-01-15)", required = true) @PathVariable String date) {
                log.info("GET /api/v1/nasa-apod/fetch/date/{} - Obtener APOD de fecha específica desde NASA API y guardar",
                                date);
                return service.fetchApodFromApiByDate(date)
                                .map(ResponseEntity::ok);
        }
}
