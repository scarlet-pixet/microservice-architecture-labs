FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /workspace
COPY pom.xml .
COPY analytics-service/pom.xml analytics-service/pom.xml
COPY expense-service/pom.xml expense-service/pom.xml
COPY analytics-service/src analytics-service/src
RUN mvn -B -pl analytics-service -am -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S expense && adduser -S expense -G expense
COPY --from=build /workspace/analytics-service/target/analytics-service-6.0.0.jar app.jar
USER expense
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
