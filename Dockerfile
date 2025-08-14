# Step 1: Base image with Java 17
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper & pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# mvnw ko executable bana do
RUN chmod +x mvnw
# Download dependencies (cache layer)
RUN ./mvnw dependency:go-offline

# Copy the full source code
COPY src src

# Build the JAR file
RUN ./mvnw clean package -DskipTests

# Step 2: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variables (will be overridden by Render dashboard or .env)
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

# Expose the port
EXPOSE ${PORT}

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
