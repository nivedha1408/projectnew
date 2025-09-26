


FROM openjdk:24-jdk-alpine
ARG JAR_FILE=target/myapp.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
