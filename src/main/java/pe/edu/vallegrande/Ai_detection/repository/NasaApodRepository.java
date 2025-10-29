package pe.edu.vallegrande.Ai_detection.repository;

import java.time.LocalDate;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.Ai_detection.model.NasaApod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para operaciones CRUD sobre la entidad NasaApod
 * Proporciona métodos para consultas por estado, fecha y eliminación lógica
 */
@Repository
public interface NasaApodRepository extends ReactiveCrudRepository<NasaApod, Long> {

    /**
     * Busca todos los registros APOD por estado
     *
     * @param status Estado del registro ('A' = Activo, 'I' = Inactivo)
     * @return Flux de registros APOD con el estado especificado
     */
    Flux<NasaApod> findByStatus(String status);

    /**
     * Busca un registro APOD por ID y estado
     *
     * @param id     Identificador del registro
     * @param status Estado del registro ('A' = Activo, 'I' = Inactivo)
     * @return Mono con el registro si existe y tiene el estado especificado
     */
    Mono<NasaApod> findByIdAndStatus(Long id, String status);

    /**
     * Busca un registro APOD por fecha y estado
     *
     * @param apodDate Fecha de la imagen astronómica
     * @param status   Estado del registro ('A' = Activo, 'I' = Inactivo)
     * @return Mono con el registro si existe para esa fecha y estado
     */
    Mono<NasaApod> findByApodDateAndStatus(LocalDate apodDate, String status);
}
