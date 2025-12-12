# Backend Screenshots Guide

This directory contains screenshots and visual documentation for the Parche Lector backend API and development tools.

---

## Screenshot Categories

### 1. API Documentation (Swagger)

#### Swagger UI - Overview
**File**: `swagger-overview.png`

**Description**: Main Swagger UI interface showing all endpoint categories

**How to Capture**:
1. Start the backend application
2. Navigate to `http://localhost:8080/swagger-ui.html`
3. Capture full page view
4. Ensure all endpoint groups are visible (Authentication, Users, Books, etc.)

---

#### Swagger UI - Endpoint Detail
**File**: `swagger-endpoint.png`

**Description**: Detailed view of a specific endpoint (e.g., `/auth/register`)

**How to Capture**:
1. Expand the Authentication section
2. Click on POST `/auth/register`
3. Show request schema, response codes, and example responses
4. Capture the expanded endpoint view

---

#### Swagger UI - Try It Out
**File**: `swagger-try.png`

**Description**: Screenshot showing the "Try it out" feature with a sample request

**How to Capture**:
1. Click "Try it out" on `/auth/register`
2. Fill in sample request body
3. Execute the request
4. Capture both the request and response panels

---

### 2. Database Tools

#### pgAdmin - Database Overview
**File**: `pgadmin-overview.png`

**Description**: pgAdmin interface showing the database structure

**How to Capture**:
1. Open pgAdmin
2. Connect to `parche_lector_dev` database
3. Expand the Schemas → Tables tree
4. Show all 24 tables in the left sidebar
5. Capture the full interface

---

#### pgAdmin - Table Structure
**File**: `pgadmin-users-table.png`

**Description**: Detailed view of the `users` table structure

**How to Capture**:
1. Right-click on `users` table
2. Select Properties
3. Show Columns tab with all fields, types, and constraints
4. Capture the properties window

---

#### pgAdmin - Query Tool
**File**: `pgadmin-query.png`

**Description**: Query tool with a sample SELECT query

**How to Capture**:
1. Open Query Tool
2. Write a sample query (e.g., `SELECT * FROM users LIMIT 10;`)
3. Execute the query
4. Show both the query and results
5. Capture the full query tool window

---

### 3. API Testing Tools

#### Postman - Collection
**File**: `postman-collection.png`

**Description**: Postman collection with all backend endpoints

**How to Capture**:
1. Import OpenAPI spec into Postman
2. Show organized collection structure
3. Display folders for different endpoint categories
4. Capture the collection sidebar

---

#### Postman - Request Example
**File**: `postman-register.png`

**Description**: Successful POST request to `/auth/register`

**How to Capture**:
1. Create POST request to `http://localhost:8080/auth/register`
2. Add JSON body with sample user data
3. Send request
4. Show the 201 Created response with auth token
5. Capture full Postman window with request, headers, and response

---

#### Postman - Authentication
**File**: `postman-auth.png`

**Description**: Protected endpoint request with JWT token

**How to Capture**:
1. Create GET request to a protected endpoint (e.g., `/users/1`)
2. Set Authorization type to "Bearer Token"
3. Add valid JWT token
4. Send request and show successful response
5. Capture the Authorization tab and response

---

### 4. Development Environment

#### IDE - Project Structure
**File**: `ide-project-structure.png`

**Description**: IDE showing the backend project structure

**How to Capture**:
1. Open backend project in IntelliJ IDEA or VS Code
2. Expand all main package folders
3. Show layered architecture (controller, service, repository, etc.)
4. Capture the file explorer/project view

---

#### IDE - Application Running
**File**: `ide-running.png`

**Description**: Console output when application successfully starts

**How to Capture**:
1. Run the Spring Boot application
2. Wait for startup to complete
3. Show console with "Started BackendApplication" message
4. Capture console output with startup logs

---

#### IDE - Debugging
**File**: `ide-debugging.png`

**Description**: Debugger paused at a breakpoint in AuthController

**How to Capture**:
1. Set breakpoint in `AuthController.register()` method
2. Make API request to trigger breakpoint
3. Show variables, call stack, and code
4. Capture the debug perspective

---

### 5. Docker

#### Docker - Container Running
**File**: `docker-running.png`

**Description**: Docker Desktop showing backend container running

**How to Capture**:
1. Build and run Docker container
2. Open Docker Desktop
3. Show container status, port mappings, and logs
4. Capture the Containers view

---

#### Docker Compose - Services
**File**: `docker-compose.png`

**Description**: Docker Compose with both postgres and backend services

**How to Capture**:
1. Run `docker-compose up`
2. Show logs for both services
3. Verify both containers are healthy
4. Capture terminal output

---

### 6. Database Schema Diagrams

#### ER Diagram - Full Schema
**File**: `er-diagram-full.png`

**Description**: Complete entity relationship diagram

**How to Create**:
1. Use dbdiagram.io, draw.io, or similar tool
2. Import schema from SQL file
3. Auto-generate ER diagram
4. Export as PNG

---

#### ER Diagram - Core Tables
**File**: `er-diagram-core.png`

**Description**: Simplified diagram showing only core tables (users, books, reviews)

**How to Create**:
1. Use diagram tool
2. Show only main tables and their relationships
3. Highlight primary and foreign keys
4. Export as high-resolution PNG

---

### 7. Security & Authentication

#### JWT Token Structure
**File**: `jwt-token-structure.png`

**Description**: Visual representation of JWT token parts

**How to Create**:
1. Visit jwt.io
2. Paste a sample token from the application
3. Show decoded header, payload, and signature
4. Capture the debugger view

---

#### Password Hashing Flow
**File**: `bcrypt-flow.png`

**Description**: Diagram showing BCrypt hashing process

**How to Create**:
1. Create flowchart diagram
2. Show: Raw Password → Salt Generation → BCrypt → Hashed Password
3. Include verification flow
4. Export as PNG

---

## Screenshot Specifications

### Technical Requirements

- **Format**: PNG (preferred) or JPEG
- **Resolution**: Minimum 1920x1080 for desktop screenshots
- **DPI**: 72 DPI minimum, 144 DPI preferred for Retina displays
- **File Size**: Optimize to < 500KB per image
- **Naming**: Use kebab-case (e.g., `swagger-endpoint-detail.png`)

### Content Guidelines

- **Clarity**: Ensure text is readable at normal zoom
- **Privacy**: Remove any sensitive data (real emails, passwords, production URLs)
- **Context**: Include enough surrounding UI for context
- **Highlighting**: Use arrows or highlights to emphasize key areas
- **Dark Mode**: Prefer dark theme for code/IDE screenshots
- **Light Mode**: Use light theme for documentation/Swagger

### Cropping & Framing

- Remove unnecessary whitespace
- Keep relevant UI elements (menus, tabs)
- Center main content
- Maintain aspect ratio

---

## Screenshot Checklist

### API Documentation
- [ ] `swagger-overview.png` - Swagger UI main page
- [ ] `swagger-endpoint.png` - Endpoint detail view
- [ ] `swagger-try.png` - Try it out functionality

### Database
- [ ] `pgadmin-overview.png` - Database structure
- [ ] `pgadmin-users-table.png` - Users table details
- [ ] `pgadmin-query.png` - Sample query execution
- [ ] `er-diagram-full.png` - Complete ER diagram
- [ ] `er-diagram-core.png` - Core tables diagram

### API Testing
- [ ] `postman-collection.png` - Organized collection
- [ ] `postman-register.png` - Registration request
- [ ] `postman-auth.png` - Authenticated request

### Development
- [ ] `ide-project-structure.png` - Project file tree
- [ ] `ide-running.png` - Application startup logs
- [ ] `ide-debugging.png` - Debugger in action

### Docker
- [ ] `docker-running.png` - Container running
- [ ] `docker-compose.png` - Multi-service setup

### Security
- [ ] `jwt-token-structure.png` - JWT visualization
- [ ] `bcrypt-flow.png` - Hashing process diagram

---

## Tools & Resources

### Screenshot Tools

**Windows**:
- Snipping Tool (built-in)
- Snagit
- ShareX
- Greenshot

**macOS**:
- Screenshot app (Cmd + Shift + 5)
- CleanShot X
- Snagit

**Cross-platform**:
- Flameshot
- Browser DevTools screenshot

### Diagram Tools

- **draw.io** - Free, powerful diagramming tool
- **dbdiagram.io** - Database ER diagrams from SQL
- **Mermaid Live Editor** - For Mermaid diagram visualization
- **Lucidchart** - Professional diagramming
- **PlantUML** - Text-based diagrams

### Image Optimization

- **TinyPNG** - Compress PNG files
- **ImageOptim** - macOS image optimization
- **GIMP** - Edit and optimize images
- **Squoosh** - Web-based image compressor

---

## Embedding Screenshots

### Markdown Syntax

```markdown
![Screenshot Description](./screenshots/filename.png)
```

### With Caption

```markdown
**Swagger UI Interface**

![Swagger UI showing all available endpoints](./screenshots/swagger-overview.png)

*Figure 1: Complete API documentation interface*
```

### Inline Link

```markdown
See the [Swagger UI screenshot](./screenshots/swagger-overview.png) for details.
```

---

## Maintenance

### When to Update Screenshots

- After UI changes in Swagger
- When new endpoints are added
- After database schema modifications
- When updating to new tool versions
- If branding/styling changes

### Version Control

- Commit screenshots with documentation updates
- Use descriptive commit messages
- Keep old versions in git history
- Tag releases with screenshot versions

---

## Notes

- Screenshots should complement, not replace, written documentation
- Always provide alt text for accessibility
- Consider creating animated GIFs for workflows
- Update screenshots when they become outdated
- Compress images to reduce repository size

---

**Last Updated**: 2025-11-21  
**Total Placeholders**: 19
