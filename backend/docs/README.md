# Parche Lector - Backend Documentation

## Project Overview

**Parche Lector Backend** is a RESTful API built with Spring Boot that powers the Parche Lector book community platform. It provides authentication, book management, social features, and user interactions through a comprehensive and secure API.

## Technology Stack

### Core Technologies
- **Spring Boot 3.2.0** - Enterprise-grade Java framework
- **Java 17** - LTS version with modern language features
- **PostgreSQL** - Robust relational database
- **Maven** - Dependency management and build tool

### Key Dependencies
- **Spring Data JPA** - Database ORM and repository pattern
- **Spring Security** - Authentication and authorization
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Lombok** - Reduce boilerplate code
- **Springdoc OpenAPI** - API documentation (Swagger)
- **Thymeleaf** - Template engine (optional)
- **Jakarta Validation** - Request validation

## Project Structure

```
backend/
├── src/
│   └── main/
│       ├── java/com/parchelector/
│       │   ├── config/            # Configuration classes
│       │   ├── controller/        # REST controllers
│       │   ├── dto/              # Data Transfer Objects
│       │   │   ├── request/      # Request DTOs
│       │   │   └── response/     # Response DTOs
│       │   ├── entity/           # JPA entities
│       │   ├── exception/        # Custom exceptions & handlers
│       │   ├── repository/       # Data access layer
│       │   ├── security/         # Security configuration
│       │   └── service/          # Business logic
│       │       └── impl/         # Service implementations
│       └── resources/
│           ├── application.properties
│           └── templates/        # Thymeleaf templates
├── Dockerfile                    # Container configuration
├── pom.xml                      # Maven dependencies
└── ESQUEMA_PARCHE_LECTOR_POSTGRES.sql  # Database schema
```

## Application Features

### 1. Authentication & Authorization
- User registration with email and username
- Secure login with JWT token generation
- Password hashing with BCrypt
- Token-based session management
- Protected endpoints with role-based access

### 2. User Management
- User profiles with avatars and bios
- Account activation/deactivation
- Password reset tokens
- Email verification

### 3. Book Management
- Book catalog with metadata
- Author associations (many-to-many)
- Genre categorization
- ISBN tracking (ISBN-10 and ISBN-13)
- Book cover images

### 4. Reading Status Tracking
- Three status types: Want to Read, Reading, Read
- Progress percentage tracking
- Start and finish dates
- Reading events log

### 5. Social Features
- User reviews and ratings
- Review likes and comments
- User follows
- Custom library lists
- List sharing and collaboration
- Author follows
- Activity feed

### 6. Content Moderation
- Report system for inappropriate content
- Admin moderation tools
- Soft deletes for reviews and comments

## Database Schema

The application uses a comprehensive PostgreSQL schema with 19+ tables:

### Core Tables
- **users** - User accounts
- **books** - Book catalog
- **authors** - Author information
- **genres** - Genre classifications

### Relationship Tables
- **book_authors** - Book-Author associations
- **book_genres** - Book-Genre associations
- **reading_status** - User reading progress
- **reviews** - Book reviews
- **library_lists** - Custom user lists
- **follows** - User social connections

See [database-schema.md](./database-schema.md) for complete schema documentation.

## API Endpoints

### Base URL
```
http://localhost:8080
```

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register new user | No |
| POST | `/auth/login` | User login | No |

### Standard Response Format

All API responses follow this format:

```json
{
  "status": "SUCCESS",
  "message": "Descriptive message",
  "data": {
    // Response data
  }
}
```

## Interactive API Documentation

### Swagger UI
Explore and test all endpoints interactively:

**URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

![Swagger UI Screenshot](./screenshots/swagger-ui.png)

### OpenAPI Specification
JSON format API documentation:

**URL**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Security Features

### Password Security
- BCrypt hashing algorithm
- Salted password storage
- Minimum password requirements enforced

### JWT Authentication
- Stateless authentication
- Token-based session management
- Configurable token expiration
- Secure token signing

### API Security
- CORS configuration
- Request validation
- SQL injection protection via JPA
- XSS protection

## Configuration

### Application P roperties
The application uses profile-based configuration:

- **Dev Profile** (`application-dev.properties`)
- **Production Profile** (`application-prod.properties`)

Active profile set in `application.properties`:
```properties
spring.profiles.active=dev
```

### Environment Variables
Key configuration variables:

```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/parche_lector
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

## Available Commands

```bash
# Development (with Maven wrapper)
./mvnw spring-boot:run

# Build
./mvnw clean package

# Run tests
./mvnw test

# Build Docker image
docker build -t parche-lector-backend .
```

## Architecture Patterns

### Layered Architecture
The application follows a clean layered architecture:

1. **Controller Layer** - HTTP request handling
2. **Service Layer** - Business logic
3. **Repository Layer** - Data access
4. **Entity Layer** - Domain models

### Design Patterns
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - API data transfer
- **Dependency Injection** - Loose coupling
- **Builder Pattern** - Object construction (via Lombok)

## Error Handling

### Global Exception Handler
Centralized error handling with custom exception classes:

- `@ControllerAdvice` for global exception handling
- Consistent error response format
- Appropriate HTTP status codes
- Detailed error messages

### HTTP Status Codes

| Code | Description | Usage |
|------|-------------|-------|
| 200 | OK | Successful GET/PUT/PATCH |
| 201 | Created | Successful POST |
| 400 | Bad Request | Validation errors |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 500 | Internal Server Error | Server-side errors |

## Development Guidelines

### Code Style
- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Write meaningful JavaDoc comments
- Keep methods focused and single-responsibility

### API Design
- RESTful principles
- Consistent naming patterns
- Proper HTTP method usage
- Version endpoints if needed

### Database
- Use JPA entities for database mapping
- Create indexes for frequently queried fields
- Use database constraints for data integrity
- Implement soft deletes where appropriate

## Related Documentation

- [Architecture Documentation](./architecture.md) - Detailed technical architecture
- [API Endpoints](./api-endpoints.md) - Complete API reference
- [Database Schema](./database-schema.md) - Database design
- [Setup Guide](./setup-guide.md) - Development environment setup
- [Security Documentation](./security.md) - Security implementation details

---

**Version**: 1.0.0  
**Last Updated**: 2025-11-21  
**Java Version**: 17  
**Spring Boot Version**: 3.2.0
