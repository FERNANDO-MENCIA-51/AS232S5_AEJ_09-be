# Documento de Requisitos

## Introducción

Sistema backend reactivo desarrollado con Spring WebFlux que integra dos APIs externas (RapidAPI AI Detection y NASA APOD) y proporciona operaciones CRUD completas con eliminación lógica sobre una base de datos PostgreSQL en Neon. El sistema expone endpoints REST documentados con Swagger y sigue una arquitectura MVC.

## Glosario

- **Sistema Backend**: El microservicio Spring WebFlux que se está desarrollando
- **API RapidAPI**: Servicio externo de detección de contenido generado por IA
- **API NASA APOD**: Servicio externo de NASA que proporciona la imagen astronómica del día
- **Base de Datos Neon**: Base de datos PostgreSQL 17 alojada en Neon
- **R2DBC**: Driver reactivo para conectividad con bases de datos relacionales
- **WebClient**: Cliente HTTP reactivo de Spring WebFlux
- **Eliminación Lógica**: Marcado de registros como eliminados sin borrarlos físicamente
- **Swagger UI**: Interfaz de documentación interactiva de la API

## Requisitos

### Requisito 1

**User Story:** Como desarrollador del sistema, quiero configurar el proyecto Spring WebFlux con todas las dependencias necesarias, para que el microservicio pueda ejecutarse en el puerto 8080 con soporte reactivo completo.

#### Acceptance Criteria

1. THE Sistema Backend SHALL incluir spring-boot-starter-webflux versión 3.5.7
2. THE Sistema Backend SHALL incluir spring-boot-starter-data-r2dbc para conectividad reactiva con PostgreSQL
3. THE Sistema Backend SHALL incluir r2dbc-postgresql como driver de base de datos
4. THE Sistema Backend SHALL incluir springdoc-openapi-starter-webflux-ui versión 2.0.2 para documentación Swagger
5. THE Sistema Backend SHALL incluir lombok para reducir código boilerplate
6. THE Sistema Backend SHALL ejecutarse en el puerto 8080

### Requisito 2

**User Story:** Como desarrollador del sistema, quiero configurar la conexión a la base de datos PostgreSQL en Neon usando R2DBC, para que el sistema pueda realizar operaciones reactivas sobre los datos.

#### Acceptance Criteria

1. THE Sistema Backend SHALL conectarse a la Base de Datos Neon usando la URL r2dbc:postgresql://ep-long-grass-adsq34n7-pooler.c-2.us-east-1.aws.neon.tech:5432/neondb
2. THE Sistema Backend SHALL autenticarse con el usuario neondb_owner
3. THE Sistema Backend SHALL usar SSL mode require para la conexión
4. WHEN la conexión a Base de Datos Neon falla, THEN THE Sistema Backend SHALL registrar el error y reintentar la conexión
5. THE Sistema Backend SHALL validar la conectividad al iniciar la aplicación

### Requisito 3

**User Story:** Como desarrollador del sistema, quiero configurar los clientes para las APIs externas de RapidAPI y NASA, para que el sistema pueda consumir estos servicios de forma reactiva.

#### Acceptance Criteria

1. THE Sistema Backend SHALL configurar un WebClient para API RapidAPI con la URL base https://ai-detection4.p.rapidapi.com
2. THE Sistema Backend SHALL incluir el header x-rapidapi-key con valor 3f797b008emsh2e4ad644606c6b8p18678cjsn053f92651618 en todas las peticiones a API RapidAPI
3. THE Sistema Backend SHALL incluir el header x-rapidapi-host con valor ai-detection4.p.rapidapi.com en todas las peticiones a API RapidAPI
4. THE Sistema Backend SHALL configurar un WebClient para API NASA APOD con la URL base https://api.nasa.gov
5. THE Sistema Backend SHALL incluir el parámetro api_key con valor JR8yjMmoORyUHGaKOzLOGeWpNblx335ZXjFMXwRd en todas las peticiones a API NASA APOD
6. WHEN una petición a las APIs externas falla, THEN THE Sistema Backend SHALL manejar el error y retornar un mensaje descriptivo

### Requisito 4

**User Story:** Como usuario de la API, quiero almacenar y gestionar los resultados de detección de IA, para poder consultar el historial de análisis realizados.

#### Acceptance Criteria

1. THE Sistema Backend SHALL crear una tabla ai_detections con campos: id, text_content, is_ai_generated, confidence_score, analysis_date, status, created_at, updated_at
2. THE Sistema Backend SHALL exponer un endpoint POST /api/v1/ai-detections para crear nuevos análisis
3. THE Sistema Backend SHALL exponer un endpoint GET /api/v1/ai-detections para listar todos los análisis activos
4. THE Sistema Backend SHALL exponer un endpoint GET /api/v1/ai-detections/{id} para obtener un análisis específico
5. THE Sistema Backend SHALL exponer un endpoint PUT /api/v1/ai-detections/{id} para actualizar un análisis
6. THE Sistema Backend SHALL exponer un endpoint DELETE /api/v1/ai-detections/{id} para eliminación lógica
7. THE Sistema Backend SHALL exponer un endpoint PATCH /api/v1/ai-detections/{id}/restore para restaurar registros eliminados
8. WHEN se crea un análisis, THEN THE Sistema Backend SHALL invocar API RapidAPI para obtener el resultado de detección
9. THE Sistema Backend SHALL almacenar el campo status con valor 'A' para registros activos y 'I' para registros eliminados

### Requisito 5

**User Story:** Como usuario de la API, quiero almacenar y gestionar las imágenes astronómicas de NASA APOD, para poder consultar el catálogo de imágenes del día.

#### Acceptance Criteria

1. THE Sistema Backend SHALL crear una tabla nasa_apod con campos: id, title, explanation, url, media_type, date, copyright, status, created_at, updated_at
2. THE Sistema Backend SHALL exponer un endpoint POST /api/v1/nasa-apod para crear nuevos registros APOD
3. THE Sistema Backend SHALL exponer un endpoint GET /api/v1/nasa-apod para listar todos los registros activos
4. THE Sistema Backend SHALL exponer un endpoint GET /api/v1/nasa-apod/{id} para obtener un registro específico
5. THE Sistema Backend SHALL exponer un endpoint PUT /api/v1/nasa-apod/{id} para actualizar un registro
6. THE Sistema Backend SHALL exponer un endpoint DELETE /api/v1/nasa-apod/{id} para eliminación lógica
7. THE Sistema Backend SHALL exponer un endpoint PATCH /api/v1/nasa-apod/{id}/restore para restaurar registros eliminados
8. WHEN se crea un registro APOD, THEN THE Sistema Backend SHALL invocar API NASA APOD para obtener los datos de la imagen del día
9. THE Sistema Backend SHALL almacenar el campo status con valor 'A' para registros activos y 'I' para registros eliminados

### Requisito 6

**User Story:** Como desarrollador del sistema, quiero scripts SQL para crear las tablas necesarias, para poder inicializar la Base de Datos Neon correctamente.

#### Acceptance Criteria

1. THE Sistema Backend SHALL incluir un script SQL en resources/database/schema.sql para crear la tabla ai_detections
2. THE Sistema Backend SHALL incluir un script SQL en resources/database/schema.sql para crear la tabla nasa_apod
3. THE script SQL SHALL definir id como clave primaria autoincremental tipo SERIAL
4. THE script SQL SHALL definir created_at con valor por defecto CURRENT_TIMESTAMP
5. THE script SQL SHALL definir updated_at con valor por defecto CURRENT_TIMESTAMP
6. THE script SQL SHALL definir status con valor por defecto 'A'

### Requisito 7

**User Story:** Como usuario de la API, quiero acceder a la documentación Swagger UI, para poder explorar y probar los endpoints disponibles de forma interactiva.

#### Acceptance Criteria

1. THE Sistema Backend SHALL exponer Swagger UI en la ruta /swagger-ui.html
2. THE Sistema Backend SHALL exponer la especificación OpenAPI en /v3/api-docs
3. THE Swagger UI SHALL documentar todos los endpoints de ai-detections
4. THE Swagger UI SHALL documentar todos los endpoints de nasa-apod
5. THE Swagger UI SHALL incluir descripciones de los parámetros y respuestas
6. THE Swagger UI SHALL permitir ejecutar peticiones de prueba directamente desde la interfaz

### Requisito 8

**User Story:** Como desarrollador del sistema, quiero implementar manejo de errores global, para que todas las excepciones sean capturadas y retornadas con formato consistente.

#### Acceptance Criteria

1. WHEN ocurre una excepción no controlada, THEN THE Sistema Backend SHALL retornar un objeto JSON con campos: timestamp, status, error, message, path
2. WHEN un recurso no es encontrado, THEN THE Sistema Backend SHALL retornar código HTTP 404
3. WHEN los datos de entrada son inválidos, THEN THE Sistema Backend SHALL retornar código HTTP 400
4. WHEN ocurre un error en las APIs externas, THEN THE Sistema Backend SHALL retornar código HTTP 502
5. WHEN ocurre un error interno, THEN THE Sistema Backend SHALL retornar código HTTP 500

### Requisito 9

**User Story:** Como desarrollador del sistema, quiero que todas las operaciones sean reactivas usando Project Reactor, para maximizar el rendimiento y escalabilidad del microservicio.

#### Acceptance Criteria

1. THE Sistema Backend SHALL retornar Mono para operaciones que devuelven un único elemento
2. THE Sistema Backend SHALL retornar Flux para operaciones que devuelven múltiples elementos
3. THE Sistema Backend SHALL usar ReactiveCrudRepository para operaciones de base de datos
4. THE Sistema Backend SHALL usar WebClient para llamadas HTTP no bloqueantes
5. THE Sistema Backend SHALL evitar operaciones bloqueantes en el flujo reactivo

### Requisito 10

**User Story:** Como desarrollador del sistema, quiero seguir la arquitectura MVC con separación de responsabilidades, para mantener el código organizado y mantenible.

#### Acceptance Criteria

1. THE Sistema Backend SHALL organizar los controladores en el paquete controller
2. THE Sistema Backend SHALL organizar los servicios en el paquete service
3. THE Sistema Backend SHALL organizar los repositorios en el paquete repository
4. THE Sistema Backend SHALL organizar los DTOs en el paquete dto
5. THE Sistema Backend SHALL organizar las entidades en el paquete model o entity
6. THE Sistema Backend SHALL organizar las configuraciones en el paquete config
7. THE Sistema Backend SHALL usar anotaciones de Lombok para reducir código boilerplate
