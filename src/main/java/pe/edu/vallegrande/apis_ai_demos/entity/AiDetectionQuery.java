package pe.edu.vallegrande.apis_ai_demos.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("ai_detection_queries")
public class AiDetectionQuery {

    @Id
    private Long id;

    @Column("text")
    private String text;

    @Column("lang")
    private String lang;

    @Column("ai_probability")
    private Double aiProbability;

    @Column("classification")
    private String classification;

    @Column("created_at")
    private LocalDateTime createdAt;

    // Constructor sin ID para crear nuevas instancias
    public AiDetectionQuery(String text, String lang, Double aiProbability, String classification) {
        this.text = text;
        this.lang = lang;
        this.aiProbability = aiProbability;
        this.classification = classification;
        this.createdAt = LocalDateTime.now();
    }
}
