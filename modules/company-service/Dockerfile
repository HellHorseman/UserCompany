FROM openjdk:17
WORKDIR /app
COPY target/company-service.jar company-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "company-service.jar"]
