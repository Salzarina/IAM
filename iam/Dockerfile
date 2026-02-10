# Используем JDK 25
FROM eclipse-temurin:25-jdk-jammy

# Рабочая директория в контейнере
WORKDIR /app

# Копируем скомпилированный jar
COPY target/iam-0.0.1-SNAPSHOT.jar app.jar

# Экспонируем порт (тот, что у Spring Boot)
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java","-jar","app.jar"]