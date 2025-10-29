package pe.edu.vallegrande.Ai_detection.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de registros NASA APOD
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NasaApodResponseDTO {

    /**
     * Identificador único del registro
     */
    private Long id;

    /**
     * Fecha solicitada en formato YYYY-MM-DD
     */
    private String requestedDate;

    /**
     * Título de la imagen astronómica
     */
    private String title;

    /**
     * Explicación detallada de la imagen
     */
    private String explanation;

    /**
     * URL de la imagen o video
     */
    private String url;

    /**
     * URL de la imagen en alta definición (HD) - solo para imágenes
     */
    private String hdurl;

    /**
     * Tipo de medio: 'image' o 'video'
     */
    private String mediaType;

    /**
     * Fecha del contenido astronómico (fecha real de la imagen/video)
     */
    private LocalDate apodDate;

    /**
     * Información de derechos de autor
     */
    private String copyright;

    /**
     * Versión del servicio NASA APOD API utilizada
     */
    private String serviceVersion;

    /**
     * Estado de la consulta: SUCCESS, ERROR, PENDING, TIMEOUT
     */
    private String queryStatus;

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
