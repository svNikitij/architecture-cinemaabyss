FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8000
ENV PORT=8000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
