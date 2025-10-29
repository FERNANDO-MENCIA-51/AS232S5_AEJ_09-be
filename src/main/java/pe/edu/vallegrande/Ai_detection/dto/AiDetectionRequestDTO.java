package pe.edu.vallegrande.Ai_detection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de creación/actualización de análisis de detección de IA
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDetectionRequestDTO {

    /**
     * Texto a analizar para detectar si fue generado por IA
     */
    private String textContent;

    /**
     * Código de idioma del texto (ISO 639-1): es, en, fr, etc.
     * Por defecto: 'en'
     */
    private String lang;
}
