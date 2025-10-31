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
@Table("nasa_apod_queries")
public class NasaApodQuery {

    @Id
    private Long id;

    @Column("requested_date")
    private String requestedDate;

    @Column("title")
    private String title;

    @Column("explanation")
    private String explanation;

    @Column("image_url")
    private String imageUrl;

    @Column("hd_image_url")
    private String hdImageUrl;

    @Column("media_type")
    private String mediaType;

    @Column("copyright")
    private String copyright;

    @Column("status")
    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;

    // Constructor sin ID para crear nuevas instancias
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
