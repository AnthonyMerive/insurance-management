FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew :services:policy-service:build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/services/policy-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
