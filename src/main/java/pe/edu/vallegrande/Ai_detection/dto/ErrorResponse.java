package pe.edu.vallegrande.Ai_detection.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de error estandarizadas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp del error
     */
    private LocalDateTime timestamp;

    /**
     * Código de estado HTTP
     */
    private Integer status;

    /**
     * Tipo de error
     */
    private String error;

    /**
     * Mensaje descriptivo del error
     */
    private String message;

    /**
     * Ruta del endpoint donde ocurrió el error
     */
    private String path;
}
