package pe.edu.vallegrande.apis_ai_demos.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("nasa_apod_queries")
public class NasaApodQuery {

    @Id
    private Long id;
    
    private String requestedDate;
    private String title;
    private String explanation;
    private String imageUrl;
    private String hdImageUrl;
    private String mediaType;
    private String copyright;
    private String status;
    
    @CreatedDate
    private LocalDateTime createdAt;

    public NasaApodQuery(String requestedDate, String title, String explanation, 
                        String imageUrl, String hdImageUrl, String mediaType, 
                        String copyright, String status) {
        this.requestedDate = requestedDate;
        this.title = title;
        this.explanation = explanation;
        this.imageUrl = imageUrl;
        this.hdImageUrl = hdImageUrl;
        this.mediaType = mediaType;
        this.copyright = copyright;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
}