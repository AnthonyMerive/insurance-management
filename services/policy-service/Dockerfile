FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace
COPY . .
RUN chmod +x ./gradlew && ./gradlew :services:policy-service:build -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /workspace/services/policy-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


