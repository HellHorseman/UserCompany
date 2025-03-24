FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/user-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
