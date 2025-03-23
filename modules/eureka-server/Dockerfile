FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/eureka-server-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /app/config/application.yml
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.yml"]
