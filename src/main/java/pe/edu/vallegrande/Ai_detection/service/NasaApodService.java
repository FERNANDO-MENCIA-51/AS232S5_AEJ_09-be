package pe.edu.vallegrande.Ai_detection.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.Ai_detection.dto.NasaApodRequestDTO;
import pe.edu.vallegrande.Ai_detection.dto.NasaApodResponseDTO;
import pe.edu.vallegrande.Ai_detection.exception.ResourceNotFoundException;
import pe.edu.vallegrande.Ai_detection.model.NasaApod;
import pe.edu.vallegrande.Ai_detection.repository.NasaApodRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio para gestionar imágenes astronómicas del día (APOD) de NASA
 * Implementa operaciones CRUD con eliminación lógica
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NasaApodService {

    private final NasaApodRepository repository;
    private final NasaApiClient nasaApiClient;

    /**
     * Crea un nuevo registro APOD
     * Llama a NASA API para obtener los datos y los guarda
     *
     * @param request DTO con la fecha opcional
     * @return Mono con el registro APOD creado
     */
    public Mono<NasaApodResponseDTO> create(NasaApodRequestDTO request) {
        LocalDate date = request.getDate() != null ? request.getDate() : LocalDate.now();
        log.info("Creando nuevo registro APOD para la fecha: {}", date);

        return nasaApiClient.getApod(date)
                .flatMap(apiResponse -> {
                    NasaApod entity = NasaApod.builder()
                            .requestedDate(date.toString())
                            .title(apiResponse.getTitle())
                            .explanation(apiResponse.getExplanation())
                            .url(apiResponse.getUrl())
                            .hdurl(apiResponse.getHdurl())
                            .mediaType(apiResponse.getMediaType())
                            .apodDate(LocalDate.parse(apiResponse.getDate()))
                            .copyright(apiResponse.getCopyright())
                            .serviceVersion(apiResponse.getServiceVersion())
                            .queryStatus("SUCCESS")
                            .status("A")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return repository.save(entity);
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Registro APOD creado exitosamente con ID: {}", response.getId()))
                .doOnError(error -> log.error("Error al crear registro APOD: {}", error.getMessage()));
    }

    /**
     * Obtiene todos los registros APOD activos
     *
     * @return Flux con todos los registros activos
     */
    public Flux<NasaApodResponseDTO> findAll() {
        log.info("Obteniendo todos los registros APOD activos");
        return repository.findByStatus("A")
                .map(this::toResponseDTO)
                .doOnComplete(() -> log.info("Registros APOD activos obtenidos exitosamente"));
    }

    /**
     * Obtiene un registro APOD por ID (solo si está activo)
     *
     * @param id Identificador del registro
     * @return Mono con el registro si existe y está activo
     */
    public Mono<NasaApodResponseDTO> findById(Long id) {
        log.info("Buscando registro APOD con ID: {}", id);
        return repository.findByIdAndStatus(id, "A")
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Registro APOD encontrado: {}", id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Registro APOD no encontrado con ID: " + id)));
    }

    /**
     * Actualiza un registro APOD existente
     *
     * @param id      Identificador del registro
     * @param request DTO con los nuevos datos
     * @return Mono con el registro actualizado
     */
    public Mono<NasaApodResponseDTO> update(Long id, NasaApodRequestDTO request) {
        log.info("Actualizando registro APOD con ID: {}", id);

        return repository.findByIdAndStatus(id, "A")
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Registro APOD no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    LocalDate date = request.getDate() != null ? request.getDate() : existing.getApodDate();
                    return nasaApiClient.getApod(date)
                            .flatMap(apiResponse -> {
                                existing.setRequestedDate(date.toString());
                                existing.setTitle(apiResponse.getTitle());
                                existing.setExplanation(apiResponse.getExplanation());
                                existing.setUrl(apiResponse.getUrl());
                                existing.setHdurl(apiResponse.getHdurl());
                                existing.setMediaType(apiResponse.getMediaType());
                                existing.setApodDate(LocalDate.parse(apiResponse.getDate()));
                                existing.setCopyright(apiResponse.getCopyright());
                                existing.setServiceVersion(apiResponse.getServiceVersion());
                                existing.setQueryStatus("SUCCESS");
                                existing.setUpdatedAt(LocalDateTime.now());
                                return repository.save(existing);
                            });
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Registro APOD actualizado exitosamente: {}", id))
                .doOnError(error -> log.error("Error al actualizar registro APOD {}: {}", id, error.getMessage()));
    }

    /**
     * Elimina lógicamente un registro APOD (cambia status a 'I')
     *
     * @param id Identificador del registro
     * @return Mono vacío cuando se completa
     */
    public Mono<Void> delete(Long id) {
        log.info("Eliminando lógicamente registro APOD con ID: {}", id);

        return repository.findByIdAndStatus(id, "A")
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Registro APOD no encontrado con ID: " + id)))
                .flatMap(entity -> {
                    entity.setStatus("I");
                    entity.setUpdatedAt(LocalDateTime.now());
                    return repository.save(entity);
                })
                .then()
                .doOnSuccess(v -> log.info("Registro APOD eliminado lógicamente: {}", id))
                .doOnError(error -> log.error("Error al eliminar registro APOD {}: {}", id, error.getMessage()));
    }

    /**
     * Restaura un registro APOD eliminado lógicamente (cambia status a 'A')
     *
     * @param id Identificador del registro
     * @return Mono con el registro restaurado
     */
    public Mono<NasaApodResponseDTO> restore(Long id) {
        log.info("Restaurando registro APOD con ID: {}", id);

        return repository.findByIdAndStatus(id, "I")
                .switchIfEmpty(Mono
                        .error(new ResourceNotFoundException("Registro APOD eliminado no encontrado con ID: " + id)))
                .flatMap(entity -> {
                    entity.setStatus("A");
                    entity.setUpdatedAt(LocalDateTime.now());
                    return repository.save(entity);
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("Registro APOD restaurado exitosamente: {}", id))
                .doOnError(error -> log.error("Error al restaurar registro APOD {}: {}", id, error.getMessage()));
    }

    /**
     * Obtiene la imagen astronómica del día actual desde NASA API y la guarda en BD
     *
     * @return Mono con el registro guardado
     */
    public Mono<NasaApodResponseDTO> fetchTodayApodFromApi() {
        log.info("Obteniendo APOD del día actual desde NASA API y guardando en BD");
        return nasaApiClient.getTodayApod()
                .flatMap(apiResponse -> {
                    // Verificar si ya existe un registro para esta fecha
                    LocalDate date = LocalDate.parse(apiResponse.getDate());
                    return repository.findByApodDateAndStatus(date, "A")
                            .flatMap(existing -> {
                                // Si existe, actualizar
                                log.info("Registro APOD ya existe para {}, actualizando", date);
                                existing.setRequestedDate(date.toString());
                                existing.setTitle(apiResponse.getTitle());
                                existing.setExplanation(apiResponse.getExplanation());
                                existing.setUrl(apiResponse.getUrl());
                                existing.setHdurl(apiResponse.getHdurl());
                                existing.setMediaType(apiResponse.getMediaType());
                                existing.setCopyright(apiResponse.getCopyright());
                                existing.setServiceVersion(apiResponse.getServiceVersion());
                                existing.setQueryStatus("SUCCESS");
                                existing.setUpdatedAt(LocalDateTime.now());
                                return repository.save(existing);
                            })
                            .switchIfEmpty(
                                    // Si no existe, crear nuevo
                                    Mono.defer(() -> {
                                        log.info("Creando nuevo registro APOD para {}", date);
                                        NasaApod entity = NasaApod.builder()
                                                .requestedDate(date.toString())
                                                .title(apiResponse.getTitle())
                                                .explanation(apiResponse.getExplanation())
                                                .url(apiResponse.getUrl())
                                                .hdurl(apiResponse.getHdurl())
                                                .mediaType(apiResponse.getMediaType())
                                                .apodDate(date)
                                                .copyright(apiResponse.getCopyright())
                                                .serviceVersion(apiResponse.getServiceVersion())
                                                .queryStatus("SUCCESS")
                                                .status("A")
                                                .createdAt(LocalDateTime.now())
                                                .updatedAt(LocalDateTime.now())
                                                .build();
                                        return repository.save(entity);
                                    }));
                })
                .map(this::toResponseDTO)
                .doOnSuccess(response -> log.info("APOD del día guardado exitosamente con ID: {}", response.getId()))
                .doOnError(error -> log.error("Error al obtener/guardar APOD del día: {}", error.getMessage()));
    }

    /**
     * Obtiene la imagen astronómica de una fecha específica desde NASA API y la
     * guarda en BD
     *
     * @param dateStr Fecha en formato YYYY-MM-DD
     * @return Mono con el registro guardado
     */
    public Mono<NasaApodResponseDTO> fetchApodFromApiByDate(String dateStr) {
        log.info("Obteniendo APOD de la fecha {} desde NASA API y guardando en BD", dateStr);
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return nasaApiClient.getApod(date)
                    .flatMap(apiResponse -> {
                        // Verificar si ya existe un registro para esta fecha
                        return repository.findByApodDateAndStatus(date, "A")
                                .flatMap(existing -> {
                                    // Si existe, actualizar
                                    log.info("Registro APOD ya existe para {}, actualizando", date);
                                    existing.setRequestedDate(dateStr);
                                    existing.setTitle(apiResponse.getTitle());
                                    existing.setExplanation(apiResponse.getExplanation());
                                    existing.setUrl(apiResponse.getUrl());
                                    existing.setHdurl(apiResponse.getHdurl());
                                    existing.setMediaType(apiResponse.getMediaType());
                                    existing.setCopyright(apiResponse.getCopyright());
                                    existing.setServiceVersion(apiResponse.getServiceVersion());
                                    existing.setQueryStatus("SUCCESS");
                                    existing.setUpdatedAt(LocalDateTime.now());
                                    return repository.save(existing);
                                })
                                .switchIfEmpty(
                                        // Si no existe, crear nuevo
                                        Mono.defer(() -> {
                                            log.info("Creando nuevo registro APOD para {}", date);
                                            NasaApod entity = NasaApod.builder()
                                                    .requestedDate(dateStr)
                                                    .title(apiResponse.getTitle())
                                                    .explanation(apiResponse.getExplanation())
                                                    .url(apiResponse.getUrl())
                                                    .hdurl(apiResponse.getHdurl())
                                                    .mediaType(apiResponse.getMediaType())
                                                    .apodDate(date)
                                                    .copyright(apiResponse.getCopyright())
                                                    .serviceVersion(apiResponse.getServiceVersion())
                                                    .queryStatus("SUCCESS")
                                                    .status("A")
                                                    .createdAt(LocalDateTime.now())
                                                    .updatedAt(LocalDateTime.now())
                                                    .build();
                                            return repository.save(entity);
                                        }));
                    })
                    .map(this::toResponseDTO)
                    .doOnSuccess(response -> log.info("APOD de fecha {} guardado exitosamente con ID: {}", dateStr,
                            response.getId()))
                    .doOnError(error -> log.error("Error al obtener/guardar APOD de fecha {}: {}", dateStr,
                            error.getMessage()));
        } catch (Exception e) {
            log.error("Formato de fecha inválido: {}", dateStr);
            return Mono.error(new IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM-DD"));
        }
    }

    /**
     * Convierte una entidad NasaApod a DTO de respuesta
     */
    private NasaApodResponseDTO toResponseDTO(NasaApod entity) {
        return NasaApodResponseDTO.builder()
                .id(entity.getId())
                .requestedDate(entity.getRequestedDate())
                .title(entity.getTitle())
                .explanation(entity.getExplanation())
                .url(entity.getUrl())
                .hdurl(entity.getHdurl())
                .mediaType(entity.getMediaType())
                .apodDate(entity.getApodDate())
                .copyright(entity.getCopyright())
                .serviceVersion(entity.getServiceVersion())
                .queryStatus(entity.getQueryStatus())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
