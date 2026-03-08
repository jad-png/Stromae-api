# Dockerfile for Config Service
FROM maven:3.8-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY config-service config-service/
RUN mvn clean package -DskipTests -pl config-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/config-service/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
