package pe.edu.vallegrande.Ai_detection.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.Ai_detection.model.AiDetection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para operaciones CRUD sobre la entidad AiDetection
 * Proporciona métodos para consultas por estado y eliminación lógica
 */
@Repository
public interface AiDetectionRepository extends ReactiveCrudRepository<AiDetection, Long> {

    /**
     * Busca todos los análisis de detección de IA por estado
     *
     * @param status Estado del registro ('A' = Activo, 'I' = Inactivo)
     * @return Flux de análisis con el estado especificado
     */
    Flux<AiDetection> findByStatus(String status);

    /**
     * Busca un análisis de detección de IA por ID y estado
     *
     * @param id     Identificador del análisis
     * @param status Estado del registro ('A' = Activo, 'I' = Inactivo)
     * @return Mono con el análisis si existe y tiene el estado especificado
     */
    Mono<AiDetection> findByIdAndStatus(Long id, String status);
}
