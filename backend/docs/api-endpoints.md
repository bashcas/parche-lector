# API Endpoints Documentation

## Base URL

```
http://localhost:8080
```

## API Response Format

### Standard Response

All endpoints return responses in the following format:

```json
{
  "status": "SUCCESS" | "ERROR",
  "message": "Descriptive message",
  "data": {
    // Response payload
  }
}
```

### Success Response Example
```json
{
  "status": "SUCCESS",
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "username": "john_doe",
    "email": "john@example.com"
  }
}
```

### Error Response Example
```json
{
  "status": "ERROR",
  "message": "Invalid credentials",
  "data": null
}
```

---

## Authentication Endpoints

### Register New User

Create a new user account.

**Endpoint**: `POST /auth/register`

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Validations**:
- `username`: Required, 3-32 characters, alphanumeric with dots/underscores
- `email`: Required, valid email format
- `password`: Required, minimum 6 characters

**Success Response**: `201 Created`
```json
{
  "status": "SUCCESS",
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "username": "john_doe",
    "email": "john@example.com"
  }
}
```

**Error Responses**:

`400 Bad Request` - Validation error
```json
{
  "status": "ERROR",
  "message": "Username already exists",
  "data": null
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepass123"
  }'
```

---

### Login User

Authenticate a user and receive a JWT token.

**Endpoint**: `POST /auth/login`

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "usernameOrEmail": "string",
  "password": "string"
}
```

**Validations**:
- `usernameOrEmail`: Required (can be username or email)
- `password`: Required

**Success Response**: `200 OK`
```json
{
  "status": "SUCCESS",
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "username": "john_doe",
    "email": "john@example.com"
  }
}
```

**Error Responses**:

`401 Unauthorized` - Invalid credentials
```json
{
  "status": "ERROR",
  "message": "Invalid credentials",
  "data": null
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "john_doe",
    "password": "securepass123"
  }'
```

---

## Protected Endpoints

All protected endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

---

## Future Endpoints (Planned)

The following endpoints are planned but not yet implemented. See the database schema for the complete data model.

### User Endpoints

#### Get User Profile
```
GET /users/{userId}
```

#### Update User Profile
```
PUT /users/{userId}
```

#### Get User's Reading List
```
GET /users/{userId}/reading-status
```

---

### Book Endpoints

#### Get All Books
```
GET /books
Query Params: page, size, sort
```

#### Get Book by ID
```
GET /books/{bookId}
```

#### Search Books
```
GET /books/search
Query Params: query, page, size
```

#### Create Book
```
POST /books
Auth: Required (Admin)
```

#### Update Book
```
PUT /books/{bookId}
Auth: Required (Admin)
```

---

### Stats Endpoints

#### Get My Stats
```
GET /stats/me
Auth: Required
```

#### Get User Stats
```
GET /stats/users/{userId}
Auth: Required
```

---

### Reading Status Endpoints

#### Get User Reading Status
```
GET /reading-status
Auth: Required
```

#### Update Reading Status
```
POST /reading-status
Auth: Required

Request Body:
{
  "bookId": 123,
  "status": "READING",
  "progressPercent": 45
}
```

#### Delete Reading Status
```
DELETE /reading-status/{bookId}
Auth: Required
```

---

### Review Endpoints

#### Get Book Reviews
```
GET /books/{bookId}/reviews
Query Params: page, size, sort
```

#### Create Review
```
POST /reviews
Auth: Required

Request Body:
{
  "bookId": 123,
  "rating": 4.5,
  "title": "Great book!",
  "body": "I really enjoyed this book because..."
}
```

#### Update Review
```
PUT /reviews/{reviewId}
Auth: Required
```

#### Delete Review
```
DELETE /reviews/{reviewId}
Auth: Required
```

#### Like Review
```
POST /reviews/{reviewId}/likes
Auth: Required
```

#### Unlike Review
```
DELETE /reviews/{reviewId}/likes
Auth: Required
```

---

### List Endpoints

#### Get User Lists
```
GET /users/{userId}/lists
```

#### Create List
```
POST /lists
Auth: Required

Request Body:
{
  "name": "My Summer Reading",
  "description": "Books to read this summer",
  "visibility": "PUBLIC"
}
```

#### Add Book to List
```
POST /lists/{listId}/books
Auth: Required

Request Body:
{
  "bookId": 123,
  "note": "Recommended by friend"
}
```

#### Remove Book from List
```
DELETE /lists/{listId}/books/{bookId}
Auth: Required
```

---

### Social Endpoints

#### Follow User
```
POST /users/{userId}/follow
Auth: Required
```

#### Unfollow User
```
DELETE /users/{userId}/follow
Auth: Required
```

#### Get Followers
```
GET /users/{userId}/followers
```

#### Get Following
```
GET /users/{userId}/following
```

#### Get Activity Feed
```
GET /feed
Auth: Required
Query Params: page, size
```

---

## HTTP Status Codes

| Code | Status | Usage |
|------|--------|-------|
| 200 | OK | Successful GET, PUT, PATCH, DELETE |
| 201 | Created | Successful POST (resource created) |
| 204 | No Content | Successful DELETE (no content returned) |
| 400 | Bad Request | Invalid request data, validation errors |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Authenticated but not authorized |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Resource conflict (e.g., duplicate username) |
| 422 | Unprocessable Entity | Validation error with details |
| 500 | Internal Server Error | Server error |
| 503 | Service Unavailable | Service temporarily unavailable |

---

## Pagination

Endpoints that return lists support pagination:

**Query Parameters**:
```
page=0          # Page number (0-indexed)
size=20         # Items per page
sort=createdAt  # Sort field
direction=desc  # Sort direction (asc/desc)
```

**Paginated Response Format**:
```json
{
  "status": "SUCCESS",
  "message": "Books retrieved successfully",
  "data": {
    "content": [ /* items */ ],
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "last": false,
    "first": true
  }
}
```

---

## Filtering & Sorting

### Filter Examples

**Books by Genre**:
```
GET /books?genre=fiction
```

**Books by Year Range**:
```
GET /books?yearFrom=2020&yearTo=2023
```

**Reviews by Rating**:
```
GET /books/{bookId}/reviews?minRating=4.0
```

### Sort Examples

**Books by Title**:
```
GET /books?sort=title&direction=asc
```

**Reviews by Date**:
```
GET /books/{bookId}/reviews?sort=createdAt&direction=desc
```

**Users by Registration Date**:
```
GET /users?sort=createdAt&direction=desc
```

---

## Rate Limiting

API rate limiting is planned for future implementation:

**Planned Limits**:
- Unauthenticated: 100 requests/hour
- Authenticated: 1000 requests/hour
- Search endpoints: 50 requests/minute

**Rate Limit Headers** (planned):
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 995
X-RateLimit-Reset: 1640995200
```

---

## Error Codes

### Custom Error Codes (Planned)

| Code | Message | Description |
|------|---------|-------------|
| AUTH_001 | Invalid credentials | Login failed |
| AUTH_002 | Token expired | JWT token expired |
| AUTH_003 | Username already exists | Registration failed |
| AUTH_004 | Email already exists | Registration failed |
| USER_001 | User not found | User doesn't exist |
| BOOK_001 | Book not found | Book doesn't exist |
| REVIEW_001 | Review already exists | One review per user per book |
| PERMISSION_001 | Insufficient permissions | Not authorized |

---

## Swagger/OpenAPI Documentation

### Interactive Documentation

Access the interactive API documentation at:

**URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**Features**:
- Browse all endpoints
- View request/response schemas
- Test endpoints directly
- See example requests/responses
- Download OpenAPI specification

![Swagger UI - Overview](./screenshots/swagger-overview.png)

![Swagger UI - Endpoint Detail](./screenshots/swagger-endpoint.png)

![Swagger UI - Try It Out](./screenshots/swagger-try.png)

### OpenAPI JSON

Download the OpenAPI specification:

**URL**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## Request Examples

### JavaScript (Fetch API)

#### Register
```javascript
const register = async () => {
  const response = await fetch('http://localhost:8080/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username: 'john_doe',
      email: 'john@example.com',
      password: 'securepass123'
    })
  });
  
  const data = await response.json();
  console.log(data);
};
```

#### Login
```javascript
const login = async () => {
  const response = await fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      usernameOrEmail: 'john_doe',
      password: 'securepass123'
    })
  });
  
  const data = await response.json();
  const token = data.data.token;
  
  // Store token for future requests
  localStorage.setItem('token', token);
};
```

#### Protected Request
```javascript
const getUserProfile = async (userId) => {
  const token = localStorage.getItem('token');
  
  const response = await fetch(`http://localhost:8080/users/${userId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const data = await response.json();
  console.log(data);
};
```

---

### Python (Requests)

#### Register
```python
import requests

url = 'http://localhost:8080/auth/register'
payload = {
    'username': 'john_doe',
    'email': 'john@example.com',
    'password': 'securepass123'
}

response = requests.post(url, json=payload)
data = response.json()
print(data)
```

#### Login and Protected Request
```python
import requests

# Login
url = 'http://localhost:8080/auth/login'
payload = {
    'usernameOrEmail': 'john_doe',
    'password': 'securepass123'
}

response = requests.post(url, json=payload)
data = response.json()
token = data['data']['token']

# Protected request
headers = {'Authorization': f'Bearer {token}'}
response = requests.get('http://localhost:8080/users/1', headers=headers)
print(response.json())
```

---

### cURL Examples

#### Register with Pretty Output
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepass123"
  }' | json_pp
```

#### Login and Extract Token
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "john_doe",
    "password": "securepass123"
  }' | jq -r '.data.token')

echo "Token: $TOKEN"
```

#### Use Token for Protected Request
```bash
curl -X GET http://localhost:8080/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## CORS Configuration

The API supports Cross-Origin Resource Sharing (CORS) for frontend applications.

**Allowed Origins** (Development):
```
http://localhost:5173
http://localhost:3000
```

**Allowed Methods**:
```
GET, POST, PUT, DELETE, PATCH, OPTIONS
```

**Allowed Headers**:
```
Content-Type, Authorization
```

**Exposed Headers**:
```
Authorization
```

---

## API Versioning

### Current Version
The API is currently at version 1.0 without explicit versioning in URLs.

### Future Versioning Strategy

If breaking changes are introduced, versioning may be added:

**Option 1: URL Versioning**
```
http://localhost:8080/v1/auth/register
http://localhost:8080/v2/auth/register
```

**Option 2: Header Versioning**
```
Accept: application/vnd.parchelector.v1+json
```

---

## Best Practices

### Authentication
1. Store JWT token securely (HttpOnly cookies or secure storage)
2. Include token in Authorization header for all protected requests
3. Handle token expiration gracefully
4. Never share tokens

### Error Handling
1. Always check `status` field in response
2. Display user-friendly error messages
3. Log errors for debugging
4. Implement retry logic for transient errors

### Performance
1. Use pagination for list endpoints
2. Cache responses when appropriate
3. Minimize payload size
4. Use compression (gzip)

---

**API Documentation Version**: 1.0.0  
**Last Updated**: 2025-11-21
