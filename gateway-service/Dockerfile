# Dockerfile for Gateway Service
FROM maven:3.8-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY gateway-service gateway-service/
RUN mvn clean package -DskipTests -pl gateway-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/gateway-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
