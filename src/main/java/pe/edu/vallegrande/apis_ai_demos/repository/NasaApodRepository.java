package pe.edu.vallegrande.apis_ai_demos.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.apis_ai_demos.entity.NasaApodQuery;
import reactor.core.publisher.Flux;

@Repository
public interface NasaApodRepository extends R2dbcRepository<NasaApodQuery, Long> {

    // Buscar por fecha solicitada
    @Query("SELECT * FROM nasa_apod_queries WHERE requested_date = :date ORDER BY created_at DESC")
    Flux<NasaApodQuery> findByRequestedDate(String date);

    // Buscar por tipo de media
    @Query("SELECT * FROM nasa_apod_queries WHERE media_type = :mediaType ORDER BY created_at DESC")
    Flux<NasaApodQuery> findByMediaType(String mediaType);

    // Buscar por status
    @Query("SELECT * FROM nasa_apod_queries WHERE status = :status ORDER BY created_at DESC")
    Flux<NasaApodQuery> findByStatus(String status);

    // Buscar las más recientes
    @Query("SELECT * FROM nasa_apod_queries ORDER BY created_at DESC LIMIT :limit")
    Flux<NasaApodQuery> findRecentQueries(int limit);

    // Buscar por título (búsqueda parcial)
    @Query("SELECT * FROM nasa_apod_queries WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY created_at DESC")
    Flux<NasaApodQuery> findByTitleContaining(String title);
}