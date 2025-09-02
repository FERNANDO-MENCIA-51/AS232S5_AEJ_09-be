package pe.edu.vallegrande.apis_ai_demos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NasaApodResponse {
    
    private String copyright;
    
    private String date;
    
    private String explanation;
    
    @JsonProperty("hdurl")
    private String hdUrl;
    
    @JsonProperty("media_type")
    private String mediaType;
    
    @JsonProperty("service_version")
    private String serviceVersion;
    
    private String title;
    
    private String url;
    
    // Campos adicionales para nuestro control
    private String status;
}