# Dockerfile for User Service
FROM maven:3.8-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY user-service user-service/
RUN mvn clean package -DskipTests -pl user-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/user-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
