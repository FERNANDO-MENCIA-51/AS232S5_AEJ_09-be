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
import pe.edu.vallegrande.Ai_detection.dto.AiDetectionRequestDTO;
import pe.edu.vallegrande.Ai_detection.dto.AiDetectionResponseDTO;
import pe.edu.vallegrande.Ai_detection.dto.ErrorResponse;
import pe.edu.vallegrande.Ai_detection.service.AiDetectionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para gestionar análisis de detección de contenido generado
 * por IA
 * Proporciona endpoints para operaciones CRUD con eliminación lógica
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai-detections")
@RequiredArgsConstructor
@Tag(name = "AI Detection", description = "API para detección de contenido generado por IA usando RapidAPI")
public class AiDetectionController {

        private final AiDetectionService service;

        @Operation(summary = "Crear nuevo análisis de detección de IA", description = "Analiza un texto usando RapidAPI para determinar si fue generado por IA y guarda el resultado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Análisis creado exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "502", description = "Error al comunicarse con RapidAPI", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping
        public Mono<ResponseEntity<AiDetectionResponseDTO>> create(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del texto a analizar", required = true, content = @Content(schema = @Schema(implementation = AiDetectionRequestDTO.class))) @RequestBody AiDetectionRequestDTO request) {
                log.info("POST /api/v1/ai-detections - Crear nuevo análisis");
                return service.create(request)
                                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
        }

        @Operation(summary = "Listar todos los análisis activos", description = "Obtiene todos los análisis de detección de IA que están activos (no eliminados)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de análisis obtenida exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponseDTO.class)))
        })
        @GetMapping
        public Flux<AiDetectionResponseDTO> findAll() {
                log.info("GET /api/v1/ai-detections - Listar todos los análisis activos");
                return service.findAll();
        }

        @Operation(summary = "Obtener análisis por ID", description = "Obtiene un análisis específico por su identificador (solo si está activo)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Análisis encontrado", content = @Content(schema = @Schema(implementation = AiDetectionResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Análisis no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/{id}")
        public Mono<ResponseEntity<AiDetectionResponseDTO>> findById(
                        @Parameter(description = "ID del análisis", required = true) @PathVariable Long id) {
                log.info("GET /api/v1/ai-detections/{} - Obtener análisis por ID", id);
                return service.findById(id)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Actualizar análisis existente", description = "Actualiza un análisis existente, re-analizando el texto con RapidAPI")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Análisis actualizado exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Análisis no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PutMapping("/{id}")
        public Mono<ResponseEntity<AiDetectionResponseDTO>> update(
                        @Parameter(description = "ID del análisis", required = true) @PathVariable Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del análisis", required = true, content = @Content(schema = @Schema(implementation = AiDetectionRequestDTO.class))) @RequestBody AiDetectionRequestDTO request) {
                log.info("PUT /api/v1/ai-detections/{} - Actualizar análisis", id);
                return service.update(id, request)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Eliminar análisis (eliminación lógica)", description = "Elimina lógicamente un análisis cambiando su estado a inactivo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Análisis eliminado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Análisis no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @DeleteMapping("/{id}")
        public Mono<ResponseEntity<Void>> delete(
                        @Parameter(description = "ID del análisis", required = true) @PathVariable Long id) {
                log.info("DELETE /api/v1/ai-detections/{} - Eliminar análisis", id);
                return service.delete(id)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
        }

        @Operation(summary = "Restaurar análisis eliminado", description = "Restaura un análisis previamente eliminado cambiando su estado a activo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Análisis restaurado exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Análisis eliminado no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PatchMapping("/{id}/restore")
        public Mono<ResponseEntity<AiDetectionResponseDTO>> restore(
                        @Parameter(description = "ID del análisis", required = true) @PathVariable Long id) {
                log.info("PATCH /api/v1/ai-detections/{}/restore - Restaurar análisis", id);
                return service.restore(id)
                                .map(ResponseEntity::ok);
        }

        @Operation(summary = "Analizar texto con RapidAPI y guardar", description = "Analiza un texto usando RapidAPI para detectar si fue generado por IA y guarda el resultado en base de datos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Análisis completado y guardado exitosamente", content = @Content(schema = @Schema(implementation = AiDetectionResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Texto inválido o vacío", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "502", description = "Error al comunicarse con RapidAPI", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/analyze")
        public Mono<ResponseEntity<AiDetectionResponseDTO>> analyzeText(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Texto a analizar", required = true, content = @Content(schema = @Schema(implementation = AiDetectionRequestDTO.class))) @RequestBody AiDetectionRequestDTO request) {
                log.info("POST /api/v1/ai-detections/analyze - Analizar texto con RapidAPI y guardar");
                return service.analyzeTextDirectly(request.getTextContent())
                                .map(ResponseEntity::ok);
        }
}
