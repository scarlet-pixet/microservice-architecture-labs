FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /workspace
COPY pom.xml .
COPY src src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S expense && adduser -S expense -G expense
COPY --from=build /workspace/target/expense-tracker-docker-3.0.0.jar app.jar
USER expense
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
