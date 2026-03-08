FROM maven:3.8-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
COPY discovery-service discovery-service/
RUN mvn clean package -DskipTests -pl discovery-service

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/discovery-service/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
