# ========================
# Stage 1: Build the App
# ========================
# We use a Maven image with JDK 17 (multi-arch support)
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# 1. Copy only the pom.xml first
COPY pom.xml .

# 2. Download dependencies (This layer will be cached if pom.xml doesn't change)
# This speeds up future builds dramatically
RUN mvn dependency:go-offline

# 3. Copy the source code
COPY src ./src

# 4. Build the jar, skipping tests to speed up deployment
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# ========================
# Stage 2: Run the App
# ========================
# We use a smaller JRE image for the final container (multi-arch support)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar from the previous stage
# We rename it to 'app.jar' so we don't have to care about version numbers
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for local dev, 10000 for Render
EXPOSE 8080 10000

# Run the application
# -Xmx350m: Limits Java Heap to 350MB for Render Free Tier
# For docker-compose, memory is typically more available
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]