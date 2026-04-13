# Используем официальный образ с Gradle и JDK 17
FROM gradle:8.5-jdk17 AS build

WORKDIR /app
# Копируем файлы Gradle wrapper и конфигурации
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# Даем права на выполнение gradlew
RUN chmod +x gradlew

# Собираем JAR (без тестов)
RUN ./gradlew clean bootJar -x test

# Финальный образ — только JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Копируем JAR из этапа сборки (путь для Spring Boot с Gradle)
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]