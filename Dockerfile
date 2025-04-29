# stage 1. Gradle Build
FROM gradle:jdk17 AS gradle_runner
WORKDIR /app/runner
COPY . /app/runner
RUN gradle wrapper && ./gradlew -P vaadin.productionMode=true bootJar

# stage 2. Java App Runner
FROM openjdk:17 AS java_runner
WORKDIR /app/runner
COPY --from=gradle_runner /app/runner/build/libs/fraud-protection-backend-0.0.1-SNAPSHOT.jar /app/runner/

ENV TZ="Asia/Tashkent"

# Run the application
ENTRYPOINT ["java", "-jar", "fraud-protection-backend-0.0.1-SNAPSHOT.jar"]