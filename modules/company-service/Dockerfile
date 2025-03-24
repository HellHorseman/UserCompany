FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/company-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
