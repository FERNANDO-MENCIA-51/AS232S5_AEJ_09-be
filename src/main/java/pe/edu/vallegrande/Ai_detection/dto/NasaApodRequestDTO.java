package pe.edu.vallegrande.Ai_detection.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de creación/actualización de registros NASA APOD
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NasaApodRequestDTO {

    /**
     * Fecha de la imagen astronómica (opcional)
     * Si no se proporciona, se usa la fecha actual
     */
    private LocalDate date;
}
