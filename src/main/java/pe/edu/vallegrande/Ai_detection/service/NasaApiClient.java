package pe.edu.vallegrande.Ai_detection.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.Ai_detection.dto.NasaApiResponse;
import reactor.core.publisher.Mono;

/**
 * Cliente para consumir NASA APOD API
 * Proporciona métodos para obtener imágenes astronómicas del día
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NasaApiClient {

        @Qualifier("nasaApiWebClient")
        private final WebClient nasaApiWebClient;

        @Value("${nasa.apikey}")
        private String nasaApiKey;

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /**
         * Obtiene la imagen astronómica del día (APOD) de NASA
         *
         * @param date Fecha de la imagen (opcional, si es null usa la fecha actual)
         * @return Mono con la respuesta de NASA API
         */
        public Mono<NasaApiResponse> getApod(LocalDate date) {
                // Validar que la fecha no sea futura
                LocalDate requestDate = date != null ? date : LocalDate.now();
                LocalDate today = LocalDate.now();

                if (requestDate.isAfter(today)) {
                        String errorMsg = String.format(
                                        "La fecha solicitada (%s) es futura. NASA APOD solo tiene datos hasta hoy (%s)",
                                        requestDate, today);
                        log.error(errorMsg);
                        return Mono.error(new IllegalArgumentException(errorMsg));
                }

                // Validar que la fecha no sea anterior al inicio de APOD (16 de junio de 1995)
                LocalDate apodStartDate = LocalDate.of(1995, 6, 16);
                if (requestDate.isBefore(apodStartDate)) {
                        String errorMsg = String.format(
                                        "La fecha solicitada (%s) es anterior al inicio de NASA APOD (%s)",
                                        requestDate, apodStartDate);
                        log.error(errorMsg);
                        return Mono.error(new IllegalArgumentException(errorMsg));
                }

                String dateStr = requestDate.format(DATE_FORMATTER);
                log.info("Llamando a NASA APOD API para la fecha: {}", dateStr);

                return nasaApiWebClient
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/planetary/apod")
                                                .queryParam("api_key", nasaApiKey)
                                                .queryParam("date", dateStr)
                                                .build())
                                .retrieve()
                                .bodyToMono(NasaApiResponse.class)
                                .doOnSuccess(response -> log.info(
                                                "Respuesta exitosa de NASA API: title={}, mediaType={}",
                                                response.getTitle(), response.getMediaType()))
                                .doOnError(WebClientResponseException.class,
                                                error -> log.error(
                                                                "Error al llamar a NASA API: status={}, body={}, url={}",
                                                                error.getStatusCode(), error.getResponseBodyAsString(),
                                                                error.getRequest().getURI()))
                                .onErrorResume(WebClientResponseException.NotFound.class, error -> {
                                        String errorMsg = String.format(
                                                        "No se encontraron datos de NASA APOD para la fecha %s. La fecha puede no tener datos disponibles.",
                                                        dateStr);
                                        log.error(errorMsg);
                                        return Mono.error(new RuntimeException(errorMsg));
                                })
                                .onErrorResume(WebClientResponseException.class, error -> {
                                        String errorMsg = String.format(
                                                        "Error al obtener datos de NASA APOD: %s (Status: %d)",
                                                        error.getStatusText(), error.getStatusCode().value());
                                        log.error(errorMsg);
                                        return Mono.error(new RuntimeException(errorMsg));
                                })
                                .onErrorResume(Exception.class, error -> {
                                        log.error("Error inesperado al llamar a NASA API: {}", error.getMessage(),
                                                        error);
                                        return Mono.error(new RuntimeException(
                                                        "Error inesperado al conectar con NASA API: "
                                                                        + error.getMessage()));
                                });
        }

        /**
         * Obtiene la imagen astronómica del día actual
         * Si la fecha del sistema es futura (posterior a 2024-12-31), usa una fecha
         * válida conocida
         *
         * @return Mono con la respuesta de NASA API para hoy
         */
        public Mono<NasaApiResponse> getTodayApod() {
                LocalDate today = LocalDate.now();
                LocalDate maxValidDate = LocalDate.of(2024, 12, 31);

                // Si la fecha del sistema es futura (probablemente mal configurada), usar una
                // fecha válida
                if (today.isAfter(maxValidDate)) {
                        log.warn("La fecha del sistema ({}) parece ser futura. Usando fecha válida conocida: {}",
                                        today, maxValidDate);
                        return getApod(maxValidDate);
                }

                return getApod(today);
        }
}
