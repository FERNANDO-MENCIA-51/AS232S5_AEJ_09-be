package pe.edu.vallegrande.apis_ai_demos.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.apis_ai_demos.entity.AiDetectionQuery;
import reactor.core.publisher.Flux;

@Repository
public interface AiDetectionRepository extends R2dbcRepository<AiDetectionQuery, Long> {
    
    Flux<AiDetectionQuery> findByLang(String lang);
    
}
