# Instrucciones para subir la imagen a Docker Hub

## 1. Hacer login a Docker Hub
Primero necesitas hacer login con tu cuenta de Docker Hub:
```bash
docker login
```
Te pedirá tu username (luismencia) y tu password de Docker Hub.

## 2. Subir la imagen a Docker Hub
Una vez que hayas hecho login, puedes subir ambas versiones de la imagen:

```bash
# Subir la versión latest
docker push luismencia/apis-ai-demos:latest

# Subir la versión específica
docker push luismencia/apis-ai-demos:v1.0.0
```

## 3. Verificar en Docker Hub
Después del push, puedes verificar en tu Docker Hub (https://hub.docker.com/u/luismencia) que la imagen se subió correctamente.

## 4. Para que otros usen tu imagen
Una vez subida, otros pueden descargar y usar tu imagen con:
```bash
docker pull luismencia/apis-ai-demos:latest
docker run -p 8080:8080 luismencia/apis-ai-demos:latest
```

## 5. Usando docker-compose con la imagen de Docker Hub
También puedes actualizar tu docker-compose.yml para usar la imagen de Docker Hub en lugar de construirla localmente:

```yaml
services:
  app:
    image: luismencia/apis-ai-demos:latest  # En lugar de build: .
    # resto de la configuración...
```

## Información de la imagen creada:
- **Nombre de la imagen**: luismencia/apis-ai-demos
- **Versiones disponibles**: latest, v1.0.0
- **Tamaño**: 274MB
- **Base**: Eclipse Temurin Java 21 Alpine
- **Puerto expuesto**: 8080
- **Aplicación**: Spring Boot con APIs de NASA y AI Detection