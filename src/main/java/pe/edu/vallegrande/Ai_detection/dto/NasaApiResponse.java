package pe.edu.vallegrande.Ai_detection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mapear la respuesta de NASA APOD API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NasaApiResponse {

    /**
     * Título de la imagen astronómica
     */
    @JsonProperty("title")
    private String title;

    /**
     * Explicación detallada de la imagen
     */
    @JsonProperty("explanation")
    private String explanation;

    /**
     * URL de la imagen o video
     */
    @JsonProperty("url")
    private String url;

    /**
     * URL de la imagen en alta definición (opcional)
     */
    @JsonProperty("hdurl")
    private String hdurl;

    /**
     * Tipo de medio: 'image' o 'video'
     */
    @JsonProperty("media_type")
    private String mediaType;

    /**
     * Fecha de la imagen en formato YYYY-MM-DD
     */
    @JsonProperty("date")
    private String date;

    /**
     * Información de derechos de autor (opcional)
     */
    @JsonProperty("copyright")
    private String copyright;

    /**
     * Versión del servicio
     */
    @JsonProperty("service_version")
    private String serviceVersion;
}
