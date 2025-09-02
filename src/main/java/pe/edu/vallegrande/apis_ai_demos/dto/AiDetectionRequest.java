package pe.edu.vallegrande.apis_ai_demos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDetectionRequest {
    
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("lang")
    private String lang; // "en" for English
}
