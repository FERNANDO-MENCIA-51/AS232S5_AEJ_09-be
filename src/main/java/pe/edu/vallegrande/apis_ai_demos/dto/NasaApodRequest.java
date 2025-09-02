package pe.edu.vallegrande.apis_ai_demos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NasaApodRequest {
    private String date; // Fecha en formato YYYY-MM-DD (opcional)
    private String startDate; // Fecha de inicio para rango (opcional)
    private String endDate; // Fecha de fin para rango (opcional)
    private Integer count; // Número de imágenes aleatorias (opcional)
    private Boolean thumbs; // Incluir URLs de thumbnails (opcional)
}