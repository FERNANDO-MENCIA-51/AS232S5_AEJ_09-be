package pe.edu.vallegrande.Ai_detection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mapear la respuesta de RapidAPI AI Detection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RapidApiResponse {

    /**
     * Indica si el contenido fue generado por IA
     */
    @JsonProperty("is_ai_generated")
    private Boolean isAiGenerated;

    /**
     * Nivel de confianza del análisis
     */
    @JsonProperty("confidence")
    private Double confidence;

    /**
     * Mensaje adicional de la API
     */
    @JsonProperty("message")
    private String message;

    /**
     * Detalles adicionales del análisis
     */
    @JsonProperty("details")
    private String details;
}
