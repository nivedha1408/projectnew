# Build stage (uses Maven + supported JDK)
FROM maven:3.9.3-eclipse-temurin-20 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage (uses JDK 24)
FROM openjdk:24-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Quiznew-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]


