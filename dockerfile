# # Build stage
FROM maven:3.8.2-jdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# # Package stage
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/Quiznew-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]