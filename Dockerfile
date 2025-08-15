# Step 1: Base image with Java 21
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper & pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw

# Download dependencies (cache layer)
RUN ./mvnw dependency:go-offline

# Copy source code & build
COPY src src
RUN ./mvnw clean package -DskipTests

# Step 2: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Use Render's PORT env variable dynamically
ENV SPRING_PROFILES_ACTIVE=prod

# Expose a generic port (Render uses PORT env)
EXPOSE 8080

# Run JAR binding to Render's PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
