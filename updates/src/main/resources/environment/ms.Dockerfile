FROM openjdk:17
EXPOSE 8080
COPY updates-service.jar updates-service.jar
ENTRYPOINT ["java", "-jar", "/updates-service.jar"]
