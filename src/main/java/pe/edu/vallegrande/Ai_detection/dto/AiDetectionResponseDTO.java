package pe.edu.vallegrande.Ai_detection.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de análisis de detección de IA
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDetectionResponseDTO {

    /**
     * Identificador único del análisis
     */
    private Long id;

    /**
     * Texto analizado
     */
    private String textContent;

    /**
     * Código de idioma del texto (ISO 639-1): es, en, fr, etc.
     */
    private String lang;

    /**
     * Indica si el texto fue generado por IA
     */
    private Boolean isAiGenerated;

    /**
     * Probabilidad de que el texto sea generado por IA (0.0000 a 1.0000)
     */
    private Double aiProbability;

    /**
     * Nivel de confianza del análisis (0.0 - 1.0)
     */
    private Double confidenceScore;

    /**
     * Clasificación del contenido: AI_GENERATED, HUMAN_WRITTEN, MIXED_CONTENT,
     * UNCERTAIN
     */
    private String classification;

    /**
     * Fecha y hora del análisis
     */
    private LocalDateTime analysisDate;

    /**
     * Estado del registro: 'A' = Activo, 'I' = Inactivo
     */
    private String status;

    /**
     * Fecha de creación del registro
     */
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     */
    private LocalDateTime updatedAt;
}
