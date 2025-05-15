# Stage 1: Build and extract layers (optional)
FROM amazoncorretto:21-alpine as builder

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Optional: Extract layers if using layered JARs (Spring Boot with layertools)
# Uncomment if you generate a layered JAR
# RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Lightweight runtime image
FROM amazoncorretto:21-alpine

# Install curl (for health check) and add a non-root user
RUN apk add --no-cache curl && adduser -D -g '' springboot

USER springboot

# Health check (Spring Boot Actuator assumed)
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/actuator/health || exit 1

# Use the full JAR (simplest form)
COPY --from=builder app.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
