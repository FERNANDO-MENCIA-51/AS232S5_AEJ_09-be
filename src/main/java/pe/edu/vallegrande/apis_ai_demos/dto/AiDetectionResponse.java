package pe.edu.vallegrande.apis_ai_demos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDetectionResponse {
    
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("aiProbability")
    private Double aiProbability;
    
    @JsonProperty("classification")
    private String classification;
    
    @JsonProperty("lang")
    private String lang;
}
