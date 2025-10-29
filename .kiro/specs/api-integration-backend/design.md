# Documento de Diseño

## Overview

Sistema backend reactivo basado en Spring WebFlux 3.5.7 que integra dos APIs externas (RapidAPI AI Detection y NASA APOD) y proporciona operaciones CRUD completas con eliminación lógica. El sistema utiliza PostgreSQL 17 en Neon con R2DBC para conectividad reactiva, expone endpoints REST documentados con Swagger, y sigue una arquitectura MVC limpia.

## Architecture

### Arquitectura General

```
┌─────────────────┐
│   Swagger UI    │
└────────┬────────┘
         │
┌────────▼────────────────────────────────────┐
│         Controller Layer                    │
│  - AiDetectionController                    │
│  - NasaApodController                       │
└────────┬────────────────────────────────────┘
         │
┌────────▼────────────────────────────────────┐
│          Service Layer                      │
│  - AiDetectionService                       │
│  - NasaApodService                          │
│  - RapidApiClient                           │
│  - NasaApiClient                            │
└────────┬────────────────────────────────────┘
         │
┌────────▼────────────────────────────────────┐
│        Repository Layer                     │
│  - AiDetectionRepository                    │
│  - NasaApodRepository                       │
└────────┬────────────────────────────────────┘
         │
┌────────▼────────────────────────────────────┐
│      PostgreSQL (Neon) via R2DBC            │
└─────────────────────────────────────────────┘

External APIs:
┌──────────────────┐       ┌──────────────────┐
│  RapidAPI        │       │   NASA APOD      │
│  AI Detection    │       │   API            │
└──────────────────┘       └──────────────────┘
```

### Flujo Reactivo

Todas las operaciones siguen el patrón reactivo de Project Reactor:

- Controladores retornan `Mono<T>` o `Flux<T>`
- Servicios procesan streams reactivos sin bloqueo
- Repositorios usan `ReactiveCrudRepository`
- Clientes HTTP usan `WebClient` para llamadas no bloqueantes

## Components and Interfaces

### 1. Configuration Layer

#### DatabaseConfig

```java
@Configuration
public class DatabaseConfig {
    // Configuración de R2DBC ConnectionFactory
    // URL: r2dbc:postgresql://ep-long-grass-adsq34n7-pooler.c-2.us-east-1.aws.neon.tech:5432/neondb
    // Usuario: neondb_owner
    // SSL: require
}
```

#### WebClientConfig

```java
@Configuration
public class WebClientConfig {
    // WebClient para RapidAPI
    @Bean("rapidApiWebClient")
    WebClient rapidApiWebClient() {
        // Base URL: https://ai-detection4.p.rapidapi.com
        // Headers: x-rapidapi-key, x-rapidapi-host
    }

    // WebClient para NASA API
    @Bean("nasaApiWebClient")
    WebClient nasaApiWebClient() {
        // Base URL: https://api.nasa.gov
    }
}
```

#### SwaggerConfig

```java
@Configuration
public class SwaggerConfig {
    // Configuración de OpenAPI 3.0
    // Título: AI Detection & NASA APOD API
    // Versión: 1.0
    // Descripción del API
}
```

### 2. Model Layer (Entities)

#### AiDetection

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ai_detections")
public class AiDetection {
    @Id
    private Long id;
    private String textContent;
    private String lang; // Código de idioma (ISO 639-1): es, en, fr, etc.
    private Boolean isAiGenerated;
    private Double aiProbability; // Probabilidad de que sea generado por IA (0.0000 a 1.0000)
    private Double confidenceScore;
    private String classification; // AI_GENERATED, HUMAN_WRITTEN, MIXED_CONTENT, UNCERTAIN
    private LocalDateTime analysisDate;
    private String status; // 'A' = Active, 'I' = Inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### NasaApod

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("nasa_apod")
public class NasaApod {
    @Id
    private Long id;
    private String requestedDate; // Fecha solicitada en formato YYYY-MM-DD
    private String title;
    private String explanation;
    private String url;
    private String hdurl; // URL de la imagen en alta definición
    private String mediaType;
    private LocalDate apodDate; // Fecha real del contenido astronómico
    private String copyright;
    private String serviceVersion; // Versión del servicio NASA APOD API
    private String queryStatus; // SUCCESS, ERROR, PENDING, TIMEOUT
    private String status; // 'A' = Active, 'I' = Inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 3. DTO Layer

#### AiDetectionRequestDTO

```java
@Data
@Builder
public class AiDetectionRequestDTO {
    @NotBlank
    private String textContent;
    private String lang; // Opcional, por defecto 'en'
}
```

#### AiDetectionResponseDTO

```java
@Data
@Builder
public class AiDetectionResponseDTO {
    private Long id;
    private String textContent;
    private String lang;
    private Boolean isAiGenerated;
    private Double aiProbability;
    private Double confidenceScore;
    private String classification;
    private LocalDateTime analysisDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### NasaApodRequestDTO

```java
@Data
@Builder
public class NasaApodRequestDTO {
    private LocalDate date; // Opcional, si no se proporciona usa la fecha actual
}
```

#### NasaApodResponseDTO

```java
@Data
@Builder
public class NasaApodResponseDTO {
    private Long id;
    private String requestedDate;
    private String title;
    private String explanation;
    private String url;
    private String hdurl;
    private String mediaType;
    private LocalDate apodDate;
    private String copyright;
    private String serviceVersion;
    private String queryStatus;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### RapidApiResponse

```java
@Data
public class RapidApiResponse {
    private Boolean isAiGenerated;
    private Double confidence;
    // Otros campos según la respuesta real de la API
}
```

#### NasaApiResponse

```java
@Data
public class NasaApiResponse {
    private String title;
    private String explanation;
    private String url;
    @JsonProperty("media_type")
    private String mediaType;
    private String date;
    private String copyright;
}
```

### 4. Repository Layer

#### AiDetectionRepository

```java
@Repository
public interface AiDetectionRepository extends ReactiveCrudRepository<AiDetection, Long> {
    Flux<AiDetection> findByStatus(String status);
    Mono<AiDetection> findByIdAndStatus(Long id, String status);
}
```

#### NasaApodRepository

```java
@Repository
public interface NasaApodRepository extends ReactiveCrudRepository<NasaApod, Long> {
    Flux<NasaApod> findByStatus(String status);
    Mono<NasaApod> findByIdAndStatus(Long id, String status);
    Mono<NasaApod> findByDateAndStatus(LocalDate date, String status);
}
```

### 5. Service Layer

#### RapidApiClient

```java
@Service
public class RapidApiClient {
    private final WebClient rapidApiWebClient;

    public Mono<RapidApiResponse> detectAiContent(String text) {
        // POST a /v1/ai-detection-rapid-api
        // Body: { "text": text }
        // Retorna RapidApiResponse
    }
}
```

#### NasaApiClient

```java
@Service
public class NasaApiClient {
    private final WebClient nasaApiWebClient;

    public Mono<NasaApiResponse> getApod(LocalDate date) {
        // GET a /planetary/apod
        // Params: api_key, date (opcional)
        // Retorna NasaApiResponse
    }
}
```

#### AiDetectionService

```java
@Service
public class AiDetectionService {
    private final AiDetectionRepository repository;
    private final RapidApiClient rapidApiClient;

    public Mono<AiDetectionResponseDTO> create(AiDetectionRequestDTO request) {
        // 1. Llamar a RapidAPI para analizar el texto
        // 2. Crear entidad con resultado
        // 3. Guardar en BD
        // 4. Convertir a DTO y retornar
    }

    public Flux<AiDetectionResponseDTO> findAll() {
        // Buscar todos con status='A'
    }

    public Mono<AiDetectionResponseDTO> findById(Long id) {
        // Buscar por id y status='A'
    }

    public Mono<AiDetectionResponseDTO> update(Long id, AiDetectionRequestDTO request) {
        // Actualizar registro existente
        // Actualizar updatedAt
    }

    public Mono<Void> delete(Long id) {
        // Eliminación lógica: cambiar status a 'I'
    }

    public Mono<AiDetectionResponseDTO> restore(Long id) {
        // Restaurar: cambiar status a 'A'
    }
}
```

#### NasaApodService

```java
@Service
public class NasaApodService {
    private final NasaApodRepository repository;
    private final NasaApiClient nasaApiClient;

    public Mono<NasaApodResponseDTO> create(NasaApodRequestDTO request) {
        // 1. Llamar a NASA API con la fecha (o fecha actual)
        // 2. Crear entidad con resultado
        // 3. Guardar en BD
        // 4. Convertir a DTO y retornar
    }

    public Flux<NasaApodResponseDTO> findAll() {
        // Buscar todos con status='A'
    }

    public Mono<NasaApodResponseDTO> findById(Long id) {
        // Buscar por id y status='A'
    }

    public Mono<NasaApodResponseDTO> update(Long id, NasaApodRequestDTO request) {
        // Actualizar registro existente
    }

    public Mono<Void> delete(Long id) {
        // Eliminación lógica: cambiar status a 'I'
    }

    public Mono<NasaApodResponseDTO> restore(Long id) {
        // Restaurar: cambiar status a 'A'
    }
}
```

### 6. Controller Layer

#### AiDetectionController

```java
@RestController
@RequestMapping("/api/v1/ai-detections")
public class AiDetectionController {
    private final AiDetectionService service;

    @PostMapping
    public Mono<ResponseEntity<AiDetectionResponseDTO>> create(@RequestBody AiDetectionRequestDTO request);

    @GetMapping
    public Flux<AiDetectionResponseDTO> findAll();

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AiDetectionResponseDTO>> findById(@PathVariable Long id);

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AiDetectionResponseDTO>> update(@PathVariable Long id, @RequestBody AiDetectionRequestDTO request);

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id);

    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<AiDetectionResponseDTO>> restore(@PathVariable Long id);
}
```

#### NasaApodController

```java
@RestController
@RequestMapping("/api/v1/nasa-apod")
public class NasaApodController {
    private final NasaApodService service;

    @PostMapping
    public Mono<ResponseEntity<NasaApodResponseDTO>> create(@RequestBody NasaApodRequestDTO request);

    @GetMapping
    public Flux<NasaApodResponseDTO> findAll();

    @GetMapping("/{id}")
    public Mono<ResponseEntity<NasaApodResponseDTO>> findById(@PathVariable Long id);

    @PutMapping("/{id}")
    public Mono<ResponseEntity<NasaApodResponseDTO>> update(@PathVariable Long id, @RequestBody NasaApodRequestDTO request);

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id);

    @PatchMapping("/{id}/restore")
    public Mono<ResponseEntity<NasaApodResponseDTO>> restore(@PathVariable Long id);
}
```

### 7. Exception Handling

#### GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(ResourceNotFoundException ex);

    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleExternalApiError(WebClientResponseException ex);

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneral(Exception ex);
}
```

#### ErrorResponse

```java
@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
```

## Data Models

### Database Schema

#### Tabla: ai_detections

```sql
CREATE TABLE ai_detections (
    id SERIAL PRIMARY KEY,
    text_content TEXT NOT NULL,
    is_ai_generated BOOLEAN,
    confidence_score DOUBLE PRECISION,
    analysis_date TIMESTAMP,
    status VARCHAR(1) DEFAULT 'A' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ai_detections_status ON ai_detections(status);
CREATE INDEX idx_ai_detections_analysis_date ON ai_detections(analysis_date);
```

#### Tabla: nasa_apod

```sql
CREATE TABLE nasa_apod (
    id SERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    explanation TEXT,
    url TEXT NOT NULL,
    media_type VARCHAR(50),
    date DATE NOT NULL,
    copyright VARCHAR(255),
    status VARCHAR(1) DEFAULT 'A' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_nasa_apod_status ON nasa_apod(status);
CREATE INDEX idx_nasa_apod_date ON nasa_apod(date);
CREATE UNIQUE INDEX idx_nasa_apod_date_unique ON nasa_apod(date) WHERE status = 'A';
```

## Error Handling

### Estrategia de Manejo de Errores

1. **Errores de APIs Externas (502 Bad Gateway)**

   - Timeout en llamadas HTTP
   - Respuestas 4xx/5xx de APIs externas
   - Problemas de conectividad

2. **Errores de Validación (400 Bad Request)**

   - Datos de entrada inválidos
   - Campos requeridos faltantes

3. **Errores de Recursos No Encontrados (404 Not Found)**

   - ID no existe en la base de datos
   - Registro con status='I' (eliminado)

4. **Errores de Base de Datos (500 Internal Server Error)**
   - Problemas de conectividad con PostgreSQL
   - Violaciones de constraints
   - Errores de transacción

### Formato de Respuesta de Error

```json
{
  "timestamp": "2025-10-29T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "AI Detection with id 123 not found",
  "path": "/api/v1/ai-detections/123"
}
```

## Testing Strategy

### Unit Tests

- Servicios: Mockear repositorios y clientes externos
- Controladores: Mockear servicios
- Validar lógica de negocio y transformaciones DTO

### Integration Tests

- Repositorios: Usar base de datos embebida H2 con R2DBC
- Endpoints: Usar WebTestClient para probar flujo completo
- Validar operaciones CRUD y eliminación lógica

### External API Tests

- Mockear respuestas de RapidAPI y NASA API
- Probar manejo de errores y timeouts
- Validar transformación de respuestas externas

## Configuration

### application.yml

```yaml
server:
  port: 8080

spring:
  r2dbc:
    url: r2dbc:postgresql://ep-long-grass-adsq34n7-pooler.c-2.us-east-1.aws.neon.tech:5432/neondb
    username: neondb_owner
    password: npg_oKi7MeRL5hqf
    properties:
      ssl: true
      sslMode: require

rapidapi:
  url: https://ai-detection4.p.rapidapi.com
  host: ai-detection4.p.rapidapi.com
  apikey: 3f797b008emsh2e4ad644606c6b8p18678cjsn053f92651618

nasa:
  url: https://api.nasa.gov
  apikey: JR8yjMmoORyUHGaKOzLOGeWpNblx335ZXjFMXwRd

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## Decisiones de Diseño

### 1. Uso de R2DBC en lugar de JPA

**Razón**: R2DBC proporciona conectividad reactiva completa con PostgreSQL, evitando bloqueos y maximizando el rendimiento en un entorno WebFlux.

### 2. Eliminación Lógica con campo status

**Razón**: Permite mantener historial completo de datos, facilita auditorías y permite restauración de registros sin complejidad adicional.

### 3. Separación de DTOs de Request y Response

**Razón**: Proporciona control fino sobre qué campos se aceptan en entrada vs qué se retorna, mejora seguridad y claridad de la API.

### 4. WebClient para APIs Externas

**Razón**: Cliente HTTP reactivo nativo de Spring WebFlux, proporciona integración perfecta con el stack reactivo.

### 5. Índices en campos status y date

**Razón**: Optimiza consultas frecuentes de registros activos y búsquedas por fecha.

### 6. Timestamps automáticos

**Razón**: Auditoría automática de creación y modificación de registros sin lógica adicional en el código.

### 7. Swagger/OpenAPI para documentación

**Razón**: Proporciona documentación interactiva y actualizada automáticamente, facilita testing y adopción de la API.
