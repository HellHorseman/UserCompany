FROM openjdk:17
WORKDIR /app
COPY target/eureka-server.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
