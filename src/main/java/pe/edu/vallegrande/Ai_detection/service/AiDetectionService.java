package pe.edu.vallegrande.Ai_detection.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.Ai_detection.dto.AiDetectionRequestDTO;
import pe.edu.vallegrande.Ai_detection.dto.AiDetectionResponseDTO;
import pe.edu.vallegrande.Ai_detection.exception.ResourceNotFoundException;
import pe.edu.vallegrande.Ai_detection.model.AiDetection;
import pe.edu.vallegrande.Ai_detection.repository.AiDetectionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio para gestionar análisis de detección de contenido generado por IA
 * Implementa operaciones CRUD con eliminación lógica
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDetectionService {

    private final AiDetectionRepository repository;
    private final RapidApiClient rapidApiClient;

    /**
     * Crea un nuevo análisis de detección de IA
     * Llama a RapidAPI para analizar el texto y guarda el resultado
     *
     * @param request DTO con el texto a analizar
     * @return Mono con el análisis creado
     */
    public Mono<AiDetectionResponseDTO> create(AiDetectionRequestDTO request) {
        log.info("Creando nuevo análisis de detección de IA");

        String lang = request.getLang() != null ? request.getLang() : "en";

        return rapidApiClient.detectAiContent(request.getTextContent())
                .flatMap(apiResponse -> {
                    AiDetection entity = AiDetection.builder()
                            .textContent(request.getTextContent())
                            .lang(lang)
                            .isAiGenerated(apiResponse.getIsAiGenerated())
                            .aiProbability(apiResponse.getConfidence())
                            .confidenceScore(apiResponse.getConfidence())
                            .classification(determineClassification(apiResponse.getIsAiGenerated(),
                                    apiResponse.getConfidence()))
                            .analysisDate(LocalDateTime.now())
                            .status("A")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return repository.save(entity);
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Análisis creado exitosamente con ID: {}", response.getId()))
                .doOnError(error -> log.error("Error al crear análisis: {}", error.getMessage()));
    }

    /**
     * Obtiene todos los análisis activos
     *
     * @return Flux con todos los análisis activos
     */
    public Flux<AiDetectionResponseDTO> findAll() {
        log.info("Obteniendo todos los análisis activos");
        return repository.findByStatus("A")
                .map(this::toResponseDTO)
                .doOnComplete(() -> log.info("Análisis activos obtenidos exitosamente"));
    }

    /**
     * Obtiene un análisis por ID (solo si está activo)
     *
     * @param id Identificador del análisis
     * @return Mono con el análisis si existe y está activo
     */
    public Mono<AiDetectionResponseDTO> findById(Long id) {
        log.info("Buscando análisis con ID: {}", id);
        return repository.findByIdAndStatus(id, "A")
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Análisis encontrado: {}", id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Análisis no encontrado con ID: " + id)));
    }

    /**
     * Actualiza un análisis existente
     *
     * @param id      Identificador del análisis
     * @param request DTO con los nuevos datos
     * @return Mono con el análisis actualizado
     */
    public Mono<AiDetectionResponseDTO> update(Long id, AiDetectionRequestDTO request) {
        log.info("Actualizando análisis con ID: {}", id);

        return repository.findByIdAndStatus(id, "A")
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Análisis no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    String lang = request.getLang() != null ? request.getLang() : existing.getLang();
                    return rapidApiClient.detectAiContent(request.getTextContent())
                            .flatMap(apiResponse -> {
                                existing.setTextContent(request.getTextContent());
                                existing.setLang(lang);
                                existing.setIsAiGenerated(apiResponse.getIsAiGenerated());
                                existing.setAiProbability(apiResponse.getConfidence());
                                existing.setConfidenceScore(apiResponse.getConfidence());
                                existing.setClassification(determineClassification(apiResponse.getIsAiGenerated(),
                                        apiResponse.getConfidence()));
                                existing.setAnalysisDate(LocalDateTime.now());
                                existing.setUpdatedAt(LocalDateTime.now());
                                return repository.save(existing);
                            });
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Análisis actualizado exitosamente: {}", id))
                .doOnError(error -> log.error("Error al actualizar análisis {}: {}", id, error.getMessage()));
    }

    /**
     * Elimina lógicamente un análisis (cambia status a 'I')
     *
     * @param id Identificador del análisis
     * @return Mono vacío cuando se completa
     */
    public Mono<Void> delete(Long id) {
        log.info("Eliminando lógicamente análisis con ID: {}", id);

        return repository.findByIdAndStatus(id, "A")
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Análisis no encontrado con ID: " + id)))
                .flatMap(entity -> {
                    entity.setStatus("I");
                    entity.setUpdatedAt(LocalDateTime.now());
                    return repository.save(entity);
                })
                .then()
                .doOnSuccess(v -> log.info("Análisis eliminado lógicamente: {}", id))
                .doOnError(error -> log.error("Error al eliminar análisis {}: {}", id, error.getMessage()));
    }

    /**
     * Restaura un análisis eliminado lógicamente (cambia status a 'A')
     *
     * @param id Identificador del análisis
     * @return Mono con el análisis restaurado
     */
    public Mono<AiDetectionResponseDTO> restore(Long id) {
        log.info("Restaurando análisis con ID: {}", id);

        return repository.findByIdAndStatus(id, "I")
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Análisis eliminado no encontrado con ID: " + id)))
                .flatMap(entity -> {
                    entity.setStatus("A");
                    entity.setUpdatedAt(LocalDateTime.now());
                    return repository.save(entity);
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Análisis restaurado exitosamente: {}", id))
                .doOnError(error -> log.error("Error al restaurar análisis {}: {}", id, error.getMessage()));
    }

    /**
     * Analiza un texto con RapidAPI y guarda el resultado en BD
     *
     * @param text Texto a analizar
     * @return Mono con el análisis guardado
     */
    public Mono<AiDetectionResponseDTO> analyzeTextDirectly(String text) {
        log.info("Analizando texto con RapidAPI y guardando en BD");

        if (text == null || text.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El texto no puede estar vacío"));
        }

        return rapidApiClient.detectAiContent(text)
                .flatMap(apiResponse -> {
                    AiDetection entity = AiDetection.builder()
                            .textContent(text)
                            .lang("en")
                            .isAiGenerated(apiResponse.getIsAiGenerated())
                            .aiProbability(apiResponse.getConfidence())
                            .confidenceScore(apiResponse.getConfidence())
                            .classification(determineClassification(apiResponse.getIsAiGenerated(),
                                    apiResponse.getConfidence()))
                            .analysisDate(LocalDateTime.now())
                            .status("A")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return repository.save(entity);
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Análisis guardado exitosamente con ID: {}", response.getId()))
                .doOnError(error -> log.error("Error al analizar/guardar texto: {}", error.getMessage()));
    }

    /**
     * Determina la clasificación del contenido basado en el resultado del análisis
     */
    private String determineClassification(Boolean isAiGenerated, Double confidence) {
        if (isAiGenerated == null || confidence == null) {
            return "UNCERTAIN";
        }

        if (confidence < 0.5) {
            return "UNCERTAIN";
        } else if (confidence >= 0.5 && confidence < 0.7) {
            return "MIXED_CONTENT";
        } else if (isAiGenerated) {
            return "AI_GENERATED";
        } else {
            return "HUMAN_WRITTEN";
        }
    }

    /**
     * Convierte una entidad AiDetection a DTO de respuesta
     */
    private AiDetectionResponseDTO toResponseDTO(AiDetection entity) {
        return AiDetectionResponseDTO.builder()
                .id(entity.getId())
                .textContent(entity.getTextContent())
                .lang(entity.getLang())
                .isAiGenerated(entity.getIsAiGenerated())
                .aiProbability(entity.getAiProbability())
                .confidenceScore(entity.getConfidenceScore())
                .classification(entity.getClassification())
                .analysisDate(entity.getAnalysisDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
