package pe.edu.vallegrande.Ai_detection.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una imagen astronómica del día (APOD) de NASA
 * Mapea a la tabla 'nasa_apod' en PostgreSQL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("nasa_apod")
public class NasaApod {

    /**
     * Identificador único del registro APOD
     */
    @Id
    private Long id;

    /**
     * Fecha solicitada en formato YYYY-MM-DD
     */
    @Column("requested_date")
    private String requestedDate;

    /**
     * Título de la imagen astronómica
     */
    @Column("title")
    private String title;

    /**
     * Explicación detallada de la imagen
     */
    @Column("explanation")
    private String explanation;

    /**
     * URL de la imagen o video
     */
    @Column("url")
    private String url;

    /**
     * URL de la imagen en alta definición (HD) - solo para imágenes
     */
    @Column("hdurl")
    private String hdurl;

    /**
     * Tipo de medio: 'image' o 'video'
     */
    @Column("media_type")
    private String mediaType;

    /**
     * Fecha del contenido astronómico (fecha real de la imagen/video)
     */
    @Column("apod_date")
    private LocalDate apodDate;

    /**
     * Información de derechos de autor
     */
    @Column("copyright")
    private String copyright;

    /**
     * Versión del servicio NASA APOD API utilizada
     */
    @Column("service_version")
    private String serviceVersion;

    /**
     * Estado de la consulta: SUCCESS, ERROR, PENDING, TIMEOUT
     */
    @Column("query_status")
    private String queryStatus;

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
