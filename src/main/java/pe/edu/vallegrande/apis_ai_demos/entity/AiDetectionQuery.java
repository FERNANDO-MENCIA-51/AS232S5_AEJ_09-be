package pe.edu.vallegrande.apis_ai_demos.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("ai_detection_queries")
public class AiDetectionQuery {
    
    @Id
    private Long id;
    
    private String text;
    private String lang;
    private Double aiProbability;
    private String classification;
    private LocalDateTime createdAt;
    
    public AiDetectionQuery(String text, String lang, Double aiProbability, String classification) {
        this.text = text;
        this.lang = lang;
        this.aiProbability = aiProbability;
        this.classification = classification;
        this.createdAt = LocalDateTime.now();
    }
}
