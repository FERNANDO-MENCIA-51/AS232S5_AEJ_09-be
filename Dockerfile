# Usar una imagen base multi-stage para optimizar el tamaño
# Primera etapa: Construcción
# Backend APIs AI - Spring Boot WebFlux
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instalar Maven
RUN apk add --no-cache maven

# Configurar directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn/ .mvn/

# Descargar dependencias (esto se cachea si no cambia el pom.xml)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Segunda etapa: Ejecución
FROM eclipse-temurin:21-jre-alpine

# Crear usuario no privilegiado para mayor seguridad
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup

# Configurar directorio de trabajo
WORKDIR /app

# Copiar el JAR construido desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Cambiar ownership del directorio a nuestro usuario
RUN chown -R appuser:appgroup /app

# Cambiar al usuario no privilegiado
USER appuser

# Exponer el puerto de la aplicación
EXPOSE 8080

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8080

# Healthcheck para verificar que la aplicación está funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:$SERVER_PORT/v1/api/ai-detection/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${SERVER_PORT}"]