FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/eureka-server.jar app.jar
COPY src/main/resources/application.yml application.yml
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=application.yml"]
