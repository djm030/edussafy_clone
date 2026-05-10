FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY backend/settings.gradle backend/build.gradle ./
COPY backend/src ./src
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=docker
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
