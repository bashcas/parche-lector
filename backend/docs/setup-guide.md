# Development Setup Guide

This guide will help you set up the Parche Lector backend development environment.

---

## Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

- **Java** JDK 17 or higher
- **Maven** 3.8+ (or use included Maven wrapper)
- **PostgreSQL** 14 or higher
- **Git** Latest version

### Verify Installation

```bash
# Check Java version
java -version
# Should output: openjdk version "17.x.x" or higher

# Check Maven version (optional, wrapper included)
mvn --version
# Should output: Apache Maven 3.8.x or higher

# Check PostgreSQL
psql --version
# Should output: psql (PostgreSQL) 14.x or higher

# Check Git version
git --version
```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd parche-lector/backend
```

### 2. Database Setup

#### Create Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE parche_lector_dev;

# Exit psql
\q
```

#### Initialize Schema

```bash
# Execute schema creation script
psql -U postgres -d parche_lector_dev -f ESQUEMA_PARCHE_LECTOR_POSTGRES.sql
```

### 3. Configure Application Properties

Create `application-dev.properties` in `src/main/resources/`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/parche_lector_dev
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa loading.format_sql=true

# JWT Configuration
jwt.secret=your-secret-key-change-this-in-production
jwt.expiration=86400000

# Server Configuration
server.port=8080

# Logging
logging.level.com.parchelector=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### 4. Install Dependencies

```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or using system Maven
mvn clean install
```

### 5. Run the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using system Maven
mvn spring-boot:run

# Or run the JAR directly (after building)
java -jar target/backend-1.0.0.jar
```

The application will start on `http://localhost:8080`

---

## Project Structure Explained

```
backend/
├── src/
│   └── main/
│       ├── java/com/parchelector/
│       │   ├── config/              # Configuration classes
│       │   │   ├── SecurityConfig.java
│       │   │   └── OpenApiConfig.java
│       │   ├── controller/          # REST controllers
│       │   │   └── AuthController.java
│       │   ├── dto/                 # Data Transfer Objects
│       │   │   ├── ApiResponse.java
│       │   │   ├── request/
│       │   │   │   ├── LoginRequest.java
│       │   │   │   └── RegisterRequest.java
│       │   │   └── response/
│       │   │       └── AuthResponse.java
│       │   ├── entity/              # JPA entities
│       │   │   ├── User.java
│       │   │   ├── Book.java
│       │   │   └── ...
│       │   ├── exception/           # Custom exceptions
│       │   │   └── GlobalExceptionHandler.java
│       │   ├── repository/          # Data access layer
│       │   │   ├── UserRepository.java
│       │   │   └── ...
│       │   ├── security/            # Security components
│       │   │   ├── JwtTokenProvider.java
│       │   │   └── JwtAuthenticationFilter.java
│       │   ├── service/             # Business logic
│       │   │   ├── AuthService.java
│       │   │   └── impl/
│       │   │       └── AuthServiceImpl.java
│       │   └── BackendApplication.java  # Main class
│       └── resources/
│           ├── application.properties
│           ├── application-dev.properties
│           └── application-prod.properties
├── Dockerfile                       # Docker configuration
├── pom.xml                         # Maven dependencies
└── ESQUEMA_PARCHE_LECTOR_POSTGRES.sql  # Database schema
```

---

## Development Workflow

### Running in Development Mode

```bash
# Run with hot reload (Spring DevTools)
./mvnw spring-boot:run
```

Changes to code will automatically reload the application (for class files).

### Building the Project

```bash
# Clean and build
./mvnw clean package

# Skip tests
./mvnw clean package -DskipTests

# Build Docker image
docker build -t parche-lector-backend .
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthControllerTest

# Run with coverage
./mvnw test jacoco:report
```

---

## Configuration

### Environment Profiles

The application supports different profiles:

- **dev** - Development
- **prod** - Production  
- **test** - Testing

Activate a profile:

```bash
# Via command line
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Via environment variable
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run

# Via application.properties
spring.profiles.active=dev
```

### Environment Variables

For production, use environment variables:

```bash
# Database
export DB_URL=jdbc:postgresql://localhost:5432/parche_lector
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword

# JWT
export JWT_SECRET=your-production-secret-key
export JWT_EXPIRATION=86400000

# Server
export SERVER_PORT=8080
```

---

## Database Management

### Accessing Database

```bash
# Via psql
psql -U postgres -d parche_lector_dev

# Common commands
\dt                    # List tables
\d users              # Describe users table
\q                    # Quit
```

### Database Migrations

Currently using SQL scripts. For future migrations, consider:

```bash
# Flyway (Planned)
./mvnw flyway:migrate

# Liquibase (Alternative)
./mvnw liquibase:update
```

### Reset Database

```bash
# Drop and recreate database
psql -U postgres -c "DROP DATABASE parche_lector_dev;"
psql -U postgres -c "CREATE DATABASE parche_lector_dev;"
psql -U postgres -d parche_lector_dev -f ESQUEMA_PARCHE_LECTOR_POSTGRES.sql
```

---

## IDE Setup

### IntelliJ IDEA

1. **Import Project**
   - File → Open → Select `pom.xml`
   - Wait for Maven to download dependencies

2. **Configure JDK**
   - File → Project Structure → Project SDK → 17

3. **Enable Lombok**
   - Install Lombok plugin
   - Enable annotation processing:
     - Settings → Build → Compiler → Annotation Processors
     - Check "Enable annotation processing"

4. **Run Configuration**
   - Run → Edit Configurations
   - Add new Spring Boot configuration
   - Main class: `com.parchelector.BackendApplication`

### Visual Studio Code

1. **Install Extensions**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. **Open Project**
   - File → Open Folder → Select `backend` directory

3. **Run Application**
   - Open `BackendApplication.java`
   - Click "Run" above the main method

### Eclipse

1. **Import Maven Project**
   - File → Import → Maven → Existing Maven Projects
   - Select `backend` directory

2. **Install Lombok**
   - Download lombok.jar
   - Run: `java -jar lombok.jar`
   - Select Eclipse installation

---

## API Testing

### Using Swagger UI

1. Start the application
2. Open browser to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
3. Test endpoints interactively

![Swagger UI Interface](./screenshots/swagger-ui.png)

### Using cURL

```bash
# Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@test.com","password":"test123"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"testuser","password":"test123"}'
```

### Using Postman

1. Import OpenAPI specification:
   - File → Import
   - URL: `http://localhost:8080/api-docs`

2. Create environment:
   - Add variable: `baseUrl` = `http://localhost:8080`
   - Add variable: `token` = `<paste-jwt-token-here>`

3. Test endpoints with auth:
   - Set Authorization: Bearer Token
   - Token: `{{token}}`

---

## Logging

### View Logs

```bash
# Real-time logs
./mvnw spring-boot:run

# Logs go to console by default
```

### Configure Log Levels

In `application-dev.properties`:

```properties
# Set log levels
logging.level.root=WARN
logging.level.com.parchelector=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Log to file
logging.file.name=logs/application.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

### Log Locations

```
logs/
└── application.log
```

---

## Troubleshooting

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port in application.properties
server.port=8081
```

### Database Connection Failed

```bash
# Check PostgreSQL is running
pg_isready

# Start PostgreSQL (Linux)
sudo systemctl start postgresql

# Start PostgreSQL (macOS with Homebrew)
brew services start postgresql

# Start PostgreSQL (Windows)
# Use Services app or pg_ctl
```

### Lombok Not Working

```bash
# Verify Lombok in classpath
./mvnw dependency:tree | grep lombok

# Reinstall Lombok plugin in IDE
# Enable annotation processing in IDE settings
```

### Build Failures

```bash
# Clear Maven cache
rm -rf ~/.m2/repository/*

# Clean and rebuild
./mvnw clean install -U

# Skip tests if failing
./mvnw clean install -DskipTests
```

### JWT Token Invalid

```bash
# Check JWT secret is configured
# Check token hasn't expired
# Ensure token format: Bearer <token>
```

---

## Docker Deployment

### Build Docker Image

```bash
# Build the JAR first
./mvnw clean package

# Build Docker image
docker build -t parche-lector-backend .
```

### Run with Docker

```bash
# Run container
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/parche_lector \ 
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=yourpassword \
  -e JWT_SECRET=your-secret \
  parche-lector-backend
```

### Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: parche_lector
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: yourpassword
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./ESQUEMA_PARCHE_LECTOR_POSTGRES.sql:/docker-entrypoint-initdb.d/schema.sql

  backend:
    build: .
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/parche_lector
      DB_USERNAME: postgres
      DB_PASSWORD: yourpassword
      JWT_SECRET: your-secret-key
    ports:
      - "8080:8080"
    depends_on:
      - postgres

volumes:
  postgres-data:
```

Run with Docker Compose:

```bash
docker-compose up
```

---

## Code Style

### Java Conventions

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and small
- Write JavaDoc for public methods

### Formatting

```xml
<!-- Maven plugin for formatting (add to pom.xml) -->
<plugin>
    <groupId>com.spotify.fmt</groupId>
    <artifactId>fmt-maven-plugin</artifactId>
    <version>2.19</version>
</plugin>
```

Run formatting:

```bash
./mvnw fmt:format
```

---

## Useful Commands

### Maven

```bash
# Clean build
./mvnw clean install

# Run tests
./mvnw test

# Run application
./mvnw spring-boot:run

# Package JAR
./mvnw package

# View dependency tree
./mvnw dependency:tree

# Update dependencies
./mvnw versions:display-dependency-updates
```

### Git

```bash
# Create feature branch
git checkout -b feature/my-feature

# Commit changes
git add .
git commit -m "Add: my feature"

# Push branch
git push origin feature/my-feature
```

---

## Performance Tuning

### JVM Options

```bash
# Increase heap size
java -Xms512m -Xmx2048m -jar target/backend-1.0.0.jar

# Enable GC logging
java -Xlog:gc* -jar target/backend-1.0.0.jar
```

### Database Connection Pool

In `application-dev.properties`:

```properties
# HikariCP settings
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

---

## Next Steps

After setup, you might want to:

1. **Read the Architecture Documentation** - Understand the system design
2. **Review API Endpoints** - Learn about available endpoints
3. **Study Database Schema** - Understand data relationships
4. **Check Security Documentation** - Learn about authentication

---

**Setup Guide Version**: 1.0  
**Last Updated**: 2025-11-21
