FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/eureka-server-1.0-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
