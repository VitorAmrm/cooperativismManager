FROM openjdk:21-jdk-slim
LABEL authors="Luis Vitor"

WORKDIR /app

COPY target/cooperativism.manager-1.0.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]