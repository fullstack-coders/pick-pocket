# ==========================================
# Stage 1: Build the Spring Boot Application
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy dependency configuration first to maximize Docker layer caching
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the actual source code
COPY src ./src

# Package the application (skipping tests for faster containerization)
RUN mvn clean package -DskipTests


# ==========================================
# Stage 2: Lightweight Production Runtime
# ==========================================
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Security: Create a dedicated non-root system user and group
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy only the compiled executable JAR artifact from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the standard Spring Boot port
EXPOSE 8081

# Run with container-aware JVM flags for resource management
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
