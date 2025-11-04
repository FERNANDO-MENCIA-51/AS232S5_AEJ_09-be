# 🚀 APIs AI Demos - Spring Boot WebFlux

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen?style=for-the-badge&logo=spring-boot)
![Java](https://img.shields.io/badge/Java-24-orange?style=for-the-badge&logo=openjdk)
![WebFlux](https://img.shields.io/badge/WebFlux-Reactive-blue?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=for-the-badge&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apache-maven)

¡Bienvenido a mi proyecto de demostración de APIs de Inteligencia Artificial! Este es un proyecto educativo donde exploro diferentes servicios de IA utilizando tecnologías modernas y reactivas.

## 📋 Descripción del Proyecto Ai

Este proyecto integra múltiples APIs de servicios cognitivos y espaciales para demostrar cómo consumir y procesar datos de manera reactiva. Actualmente incluye:

### 🤖 **RapidAPI - AI Detection Service**
![RapidAPI](https://img.shields.io/badge/RapidAPI-AI%20Detection-purple?style=flat-square&logo=rapidapi)

Servicio inteligente que detecta si un texto fue generado por inteligencia artificial. Utiliza algoritmos avanzados de machine learning para analizar patrones de escritura y determinar la probabilidad de que el contenido sea generado por IA.

<div align="center">
  <img src="https://cdn-icons-png.flaticon.com/512/8637/8637099.png" alt="AI Detection" width="150"/>
</div>

### 🌌 **NASA APOD (Astronomy Picture of the Day)**
![NASA](https://img.shields.io/badge/NASA-APOD%20API-red?style=flat-square&logo=nasa)

Integración con la API oficial de la NASA que proporciona acceso a la imagen astronómica del día, incluyendo fotografías espectaculares del espacio, explicaciones científicas y datos técnicos de cada imagen.

<div align="center">
  <img src="https://www.nasa.gov/wp-content/themes/nasa/assets/images/nasa-logo.svg" alt="NASA Logo" width="200"/>
</div>

## 🛠️ Tecnologías Utilizadas

### ☕ **Entorno de Desarrollo**

<div align="center">
  <img src="https://logos-world.net/wp-content/uploads/2022/07/Java-Logo.png" alt="Java" width="100"/>
  <img src="https://spring.io/img/spring-2.svg" alt="Spring Boot" width="100"/>
  <img src="https://maven.apache.org/images/maven-logo-black-on-white.png" alt="Maven" width="100"/>
</div>

- ![Java](https://img.shields.io/badge/Java-JDK%2024-orange?logo=openjdk) **Java:** JDK 24
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen?logo=spring-boot) **Framework:** Spring Boot 3.5.5
- ![WebFlux](https://img.shields.io/badge/WebFlux-Reactive-blue?logo=spring) **Arquitectura:** Programación Reactiva con WebFlux
- ![Maven](https://img.shields.io/badge/Maven-Build%20Tool-red?logo=apache-maven) **Build Tool:** Apache Maven
- ![IDE](https://img.shields.io/badge/IDE-IntelliJ%20IDEA%20%7C%20VS%20Code-blue?logo=intellij-idea) **IDE Recomendado:** IntelliJ IDEA | Visual Studio Code

### 🗄️ **Base de Datos**

<div align="center">
  <img src="https://www.postgresql.org/media/img/about/press/elephant.png" alt="PostgreSQL" width="100"/>
  <img src="https://neon.tech/brand/neon-logo-dark-color.svg" alt="Neon" width="100"/>
</div>

- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql) **Motor:** PostgreSQL 17
- ![Neon](https://img.shields.io/badge/Neon-Database-green?logo=neon) **Proveedor Cloud:** Neon Database
- ![R2DBC](https://img.shields.io/badge/R2DBC-Reactive%20Driver-purple?logo=spring) **Driver:** R2DBC PostgreSQL (Reactive)
- ![Pool](https://img.shields.io/badge/Connection%20Pool-5--10%20connections-yellow) **Pool de Conexiones:** Configurado con 5-10 conexiones

### 🔧 **Frameworks y Librerías**

<div align="center">
  <img src="https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/legend.svg" alt="Project Reactor" width="150"/>
  <img src="https://projectlombok.org/img/lombok-logo.png" alt="Lombok" width="100"/>
</div>

- ![WebFlux](https://img.shields.io/badge/Spring%20WebFlux-Reactive%20Programming-blue?logo=spring) **Spring WebFlux:** Para programación reactiva
- ![R2DBC](https://img.shields.io/badge/Spring%20Data%20R2DBC-Reactive%20Database-green?logo=spring) **Spring Data R2DBC:** Acceso reactivo a base de datos
- ![Reactor](https://img.shields.io/badge/Project%20Reactor-Reactive%20Streams-purple?logo=spring) **Project Reactor:** Manejo de streams reactivos
- ![Lombok](https://img.shields.io/badge/Lombok-Code%20Generation-red?logo=lombok) **Lombok:** Reducción de código boilerplate
- ![OpenAPI](https://img.shields.io/badge/SpringDoc%20OpenAPI-API%20Documentation-orange?logo=swagger) **SpringDoc OpenAPI:** Documentación automática de APIs

## 📦 Dependencias Maven

### 🌐 **Dependencias Core - Spring WebFlux + PostgreSQL**

![WebFlux](https://img.shields.io/badge/Spring%20WebFlux-Core-blue?logo=spring)
![R2DBC](https://img.shields.io/badge/Spring%20Data%20R2DBC-Database-green?logo=spring)
![PostgreSQL](https://img.shields.io/badge/R2DBC%20PostgreSQL-Driver-blue?logo=postgresql)

```xml
<!-- Spring WebFlux - Programación Reactiva -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- Spring Data R2DBC - Acceso Reactivo a BD -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>

<!-- R2DBC PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>r2dbc-postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 📚 **Dependencias de Documentación**

![OpenAPI](https://img.shields.io/badge/SpringDoc%20OpenAPI-2.0.2-orange?logo=swagger)
![Swagger](https://img.shields.io/badge/Swagger%20UI-Documentation-green?logo=swagger)

```xml
<!-- SpringDoc OpenAPI - Swagger UI para WebFlux -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

### 🔨 **Dependencias de Desarrollo**

![Lombok](https://img.shields.io/badge/Lombok-Code%20Generation-red?logo=lombok)
![Development](https://img.shields.io/badge/Development-Tools-yellow?logo=tools)

```xml
<!-- Lombok - Reducción de Código Boilerplate -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### 🧪 **Dependencias de Testing**

![Spring Test](https://img.shields.io/badge/Spring%20Boot%20Test-Testing-brightgreen?logo=spring)
![Reactor Test](https://img.shields.io/badge/Reactor%20Test-Reactive%20Testing-purple?logo=spring)

```xml
<!-- Spring Boot Test Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Reactor Test - Testing para Programación Reactiva -->
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <scope>test</scope>
</dependency>
```

## 🚀 Cómo Ejecutar el Proyecto

### 📋 **Prerrequisitos**
- Java JDK 24 instalado
- Maven configurado
- Acceso a internet para las APIs externas

### ▶️ **Pasos para Ejecutar**

1. **Clonar el repositorio**
   ```bash
   git clone [tu-repositorio]
   cd apis-ai-demos
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la documentación**
   - ![Swagger](https://img.shields.io/badge/Swagger%20UI-localhost:8080/webjars/swagger--ui.html-green?logo=swagger) Swagger UI: http://localhost:8080/webjars/swagger-ui/index.html#/
   - ![API Docs](https://img.shields.io/badge/API%20Docs-localhost:8080/v3/api--docs-blue?logo=swagger) API Docs: http://localhost:8080/v3/api-docs

<div align="center">
  <img src="https://swagger.io/swagger/media/assets/images/swagger_logo.svg" alt="Swagger UI" width="200"/>
</div>

## 🔗 Endpoints Disponibles

### 🤖 **AI Detection Service**
![POST](https://img.shields.io/badge/POST-/api/ai--detection-blue?style=flat-square)
- `POST /api/ai-detection` - Detectar si un texto es generado por IA

### 🌌 **NASA APOD Service**
![GET](https://img.shields.io/badge/GET-/api/nasa/apod/today-green?style=flat-square)
![GET](https://img.shields.io/badge/GET-/api/nasa/apod/date/{date}-green?style=flat-square)
![GET](https://img.shields.io/badge/GET-/api/nasa/health-yellow?style=flat-square)

- `GET /api/nasa/apod/today` - Obtener la imagen astronómica de hoy
- `GET /api/nasa/apod/date/{date}` - Obtener imagen de una fecha específica
- `GET /api/nasa/health` - Verificar estado del servicio


## 🔧 Configuración

El proyecto utiliza un archivo `application.yml` para la configuración de:
- Conexión a base de datos Neon PostgreSQL
- Claves de API para RapidAPI y NASA
- Configuración de pools de conexión
- Logging y debugging
- Documentación OpenAPI

## 🎯 Características Técnicas

### ⚡ **Programación Reactiva**
- Uso de `Mono` y `Flux` para operaciones no bloqueantes
- WebClient para llamadas HTTP reactivas
- R2DBC para acceso reactivo a base de datos

### 🛡️ **Manejo de Errores**
- Gestión centralizada de excepciones
- Logging detallado para debugging
- Respuestas HTTP apropiadas

### 📊 **Persistencia de Datos**
- Almacenamiento de consultas realizadas
- Esquemas SQL optimizados con índices
- Pool de conexiones configurado para rendimiento

## 🎓 Aprendizajes del Proyecto

Este proyecto me ha permitido explorar y aprender:
- Programación reactiva con Spring WebFlux
- Integración con APIs externas de terceros
- Manejo de bases de datos reactivas con R2DBC
- Configuración de servicios cloud (Neon Database)
- Documentación automática de APIs con OpenAPI
- Mejores prácticas en arquitectura de microservicios

---
