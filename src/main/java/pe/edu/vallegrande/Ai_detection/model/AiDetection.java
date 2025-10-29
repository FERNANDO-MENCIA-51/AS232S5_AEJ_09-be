package pe.edu.vallegrande.Ai_detection.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un análisis de detección de contenido generado por IA
 * Mapea a la tabla 'ai_detections' en PostgreSQL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ai_detections")
public class AiDetection {

    /**
     * Identificador único del análisis
     */
    @Id
    private Long id;

    /**
     * Texto analizado para detectar si fue generado por IA
     */
    @Column("text_content")
    private String textContent;

    /**
     * Código de idioma del texto (ISO 639-1): es, en, fr, etc.
     */
    @Column("lang")
    private String lang;

    /**
     * Indica si el texto fue generado por IA
     */
    @Column("is_ai_generated")
    private Boolean isAiGenerated;

    /**
     * Probabilidad de que el texto sea generado por IA (0.0000 a 1.0000)
     */
    @Column("ai_probability")
    private Double aiProbability;

    /**
     * Nivel de confianza del análisis (0.0 - 1.0)
     */
    @Column("confidence_score")
    private Double confidenceScore;

    /**
     * Clasificación del contenido: AI_GENERATED, HUMAN_WRITTEN, MIXED_CONTENT,
     * UNCERTAIN
     */
    @Column("classification")
    private String classification;

    /**
     * Fecha y hora en que se realizó el análisis
     */
    @Column("analysis_date")
    private LocalDateTime analysisDate;

    /**
     * Estado del registro: 'A' = Activo, 'I' = Inactivo (eliminado lógicamente)
     */
    @Column("status")
    private String status;

    /**
     * Fecha de creación del registro
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización del registro
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
