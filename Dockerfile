FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar agribridge-0.0.1-SNAPSHOT.jar
EXPOSE 7081
ENTRYPOINT ["java", "-jar", "agribridge-0.0.1-SNAPSHOT.jar"]