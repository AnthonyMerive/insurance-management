FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew :services:customer-service:build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/services/customer-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
