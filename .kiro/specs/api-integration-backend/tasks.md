# Plan de Implementación

- [x] 1. Actualizar dependencias del proyecto y configuración base




  - Actualizar pom.xml con todas las dependencias necesarias (spring-boot-starter-data-r2dbc, r2dbc-postgresql, springdoc-openapi-starter-webflux-ui)
  - Crear application.yml con configuración de R2DBC, puerto 8080, y propiedades de APIs externas



  - _Requisitos: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 2.1, 2.2, 2.3, 3.4, 3.5_

- [ ] 2. Crear scripts SQL de base de datos

  - Crear directorio resources/database



  - Crear schema.sql con tabla ai_detections (id, text_content, is_ai_generated, confidence_score, analysis_date, status, created_at, updated_at)

  - Crear schema.sql con tabla nasa_apod (id, title, explanation, url, media_type, date, copyright, status, created_at, updated_at)
  - Agregar índices para optimización de consultas por status y date
  - _Requisitos: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6_

- [x] 3. Implementar capa de configuración



  - [ ] 3.1 Crear DatabaseConfig para configurar R2DBC ConnectionFactory con credenciales de Neon

    - Configurar URL, usuario, password y SSL mode







    - _Requisitos: 2.1, 2.2, 2.3, 2.4_

  - [ ] 3.2 Crear WebClientConfig con beans para RapidAPI y NASA API



    - Configurar WebClient para RapidAPI con headers x-rapidapi-key y x-rapidapi-host



    - Configurar WebClient para NASA API con URL base


    - _Requisitos: 3.1, 3.2, 3.3, 3.4, 3.5_

  - [ ] 3.3 Crear SwaggerConfig para documentación OpenAPI
    - Configurar título, versión y descripción del API
    - _Requisitos: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6_



- [ ] 4. Implementar entidades del modelo de datos

  - [ ] 4.1 Crear entidad AiDetection con anotaciones Lombok y R2DBC






    - Campos: id, textContent, isAiGenerated, confidenceScore, analysisDate, status, createdAt, updatedAt


    - Usar @Table, @Id, @Data, @Builder
    - _Requisitos: 4.1, 4.9_






  - [x] 4.2 Crear entidad NasaApod con anotaciones Lombok y R2DBC


    - Campos: id, title, explanation, url, mediaType, date, copyright, status, createdAt, updatedAt
    - Usar @Table, @Id, @Data, @Builder
    - _Requisitos: 5.1, 5.9_

- [x] 5. Implementar DTOs para requests y responses



  - [x] 5.1 Crear DTOs para AI Detection






    - AiDetectionRequestDTO con campo textContent
    - AiDetectionResponseDTO con todos los campos de la entidad
    - RapidApiResponse para mapear respuesta de la API externa
    - _Requisitos: 4.2_

  - [ ] 5.2 Crear DTOs para NASA APOD

    - NasaApodRequestDTO con campo date opcional
    - NasaApodResponseDTO con todos los campos de la entidad


    - NasaApiResponse para mapear respuesta de la API externa
    - _Requisitos: 5.2_

  - [ ] 5.3 Crear ErrorResponse DTO para manejo de errores
    - Campos: timestamp, status, error, message, path
    - _Requisitos: 8.1_




- [ ] 6. Implementar capa de repositorio

  - [ ] 6.1 Crear AiDetectionRepository extendiendo ReactiveCrudRepository

    - Métodos: findByStatus, findByIdAndStatus
    - _Requisitos: 4.3, 4.4, 4.5, 9.3_






  - [ ] 6.2 Crear NasaApodRepository extendiendo ReactiveCrudRepository
    - Métodos: findByStatus, findByIdAndStatus, findByDateAndStatus
    - _Requisitos: 5.3, 5.4, 5.5, 9.3_

- [ ] 7. Implementar clientes para APIs externas

  - [ ] 7.1 Crear RapidApiClient para detección de IA

    - Método detectAiContent que hace POST a /v1/ai-detection-rapid-api


    - Incluir headers requeridos y manejo de errores
    - Retornar Mono<RapidApiResponse>
    - _Requisitos: 3.1, 3.2, 3.3, 3.6, 4.8, 9.4_

  - [ ] 7.2 Crear NasaApiClient para APOD
    - Método getApod que hace GET a /planetary/apod con parámetros api_key y date



    - Manejo de errores y respuestas
    - Retornar Mono<NasaApiResponse>
    - _Requisitos: 3.4, 3.5, 3.6, 5.8, 9.4_

- [ ] 8. Implementar capa de servicio

  - [ ] 8.1 Crear AiDetectionService con lógica de negocio

    - Método create: llamar RapidAPI, crear entidad, guardar en BD
    - Método findAll: buscar todos con status='A'
    - Método findById: buscar por id y status='A'
    - Método update: actualizar registro y updatedAt
    - Método delete: eliminación lógica cambiando status a 'I'
    - Método restore: restaurar cambiando status a 'A'
    - Todos los métodos retornan Mono o Flux
    - _Requisitos: 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 9.1, 9.2, 9.5_

  - [ ] 8.2 Crear NasaApodService con lógica de negocio
    - Método create: llamar NASA API, crear entidad, guardar en BD
    - Método findAll: buscar todos con status='A'
    - Método findById: buscar por id y status='A'
    - Método update: actualizar registro y updatedAt
    - Método delete: eliminación lógica cambiando status a 'I'
    - Método restore: restaurar cambiando status a 'A'
    - Todos los métodos retornan Mono o Flux
    - _Requisitos: 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 9.1, 9.2, 9.5_

- [ ] 9. Implementar manejo global de excepciones

  - Crear GlobalExceptionHandler con @RestControllerAdvice
  - Manejar ResourceNotFoundException retornando 404
  - Manejar WebClientResponseException retornando 502
  - Manejar excepciones de validación retornando 400
  - Manejar excepciones generales retornando 500
  - Todas las respuestas de error usan ErrorResponse DTO
  - _Requisitos: 8.1, 8.2, 8.3, 8.4, 8.5_

- [ ] 10. Implementar capa de controladores

  - [ ] 10.1 Crear AiDetectionController con endpoints REST

    - POST /api/v1/ai-detections - crear nuevo análisis
    - GET /api/v1/ai-detections - listar todos activos
    - GET /api/v1/ai-detections/{id} - obtener por id
    - PUT /api/v1/ai-detections/{id} - actualizar
    - DELETE /api/v1/ai-detections/{id} - eliminación lógica
    - PATCH /api/v1/ai-detections/{id}/restore - restaurar
    - Agregar anotaciones de Swagger para documentación
    - _Requisitos: 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 7.3, 10.1, 10.2_

  - [ ] 10.2 Crear NasaApodController con endpoints REST
    - POST /api/v1/nasa-apod - crear nuevo registro APOD
    - GET /api/v1/nasa-apod - listar todos activos
    - GET /api/v1/nasa-apod/{id} - obtener por id
    - PUT /api/v1/nasa-apod/{id} - actualizar
    - DELETE /api/v1/nasa-apod/{id} - eliminación lógica
    - PATCH /api/v1/nasa-apod/{id}/restore - restaurar
    - Agregar anotaciones de Swagger para documentación
    - _Requisitos: 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 7.4, 10.1, 10.2_

- [ ] 11. Verificar integración completa y documentación Swagger
  - Verificar que la aplicación inicia correctamente en puerto 8080
  - Verificar conectividad con base de datos Neon
  - Verificar que Swagger UI está accesible en /swagger-ui.html
  - Verificar que todos los endpoints están documentados correctamente
  - _Requisitos: 1.6, 2.5, 7.1, 7.2, 7.3, 7.4, 7.5, 7.6_
