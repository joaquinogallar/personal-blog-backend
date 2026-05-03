# --- phase 1, build---
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# --- phase 2, run ---
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/personal-blog-0.0.1-SNAPSHOT.jar app.jar

# Seteamos variables de optimización para contenedores
ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]