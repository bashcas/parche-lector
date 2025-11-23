# API Documentation - Parche Lector

## Swagger UI

Para explorar y probar los endpoints de la API, accede a la documentación interactiva de Swagger:

**URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## OpenAPI Docs (JSON)

Documentación en formato JSON disponible en:

**URL:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Base URL
```
http://localhost:8080
```

## Autenticación

La API usa autenticación JWT (Bearer Token). Para endpoints protegidos, incluye el token en el header:

```
Authorization: Bearer <tu-token-jwt>
```

### Cómo autenticarse en Swagger:

1. Haz login en `/auth/login` y copia el token de la respuesta
2. Haz clic en el botón **"Authorize"** 🔓 (arriba a la derecha)
3. Pega tu token JWT (sin el prefijo "Bearer")
4. Haz clic en **"Authorize"** y luego **"Close"**
5. Ahora puedes usar los endpoints protegidos

## Formato de Respuesta Estándar

Todas las respuestas de la API siguen el formato:

```json
{
  "status": "SUCCESS" | "ERROR",
  "message": "Mensaje descriptivo",
  "data": { ... }
}
```

---

## 📚 Endpoints Disponibles

### 🔐 Authentication (`/auth`)

#### POST /auth/register
Registrar un nuevo usuario.

**Request Body:**
```json
{
  "username": "ana_lector",
  "email": "ana@email.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "ana_lector"
  }
}
```

---

#### POST /auth/login
Iniciar sesión.

**Request Body:**
```json
{
  "usernameOrEmail": "ana_lector",
  "password": "password123"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "ana_lector"
  }
}
```

---

#### GET /auth/me
Obtener perfil del usuario autenticado. **Requiere autenticación.**

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Profile retrieved successfully",
  "data": {
    "userName": "ana_lector",
    "userAvatar": "https://api.dicebear.com/7.x/avataaars/svg?seed=Ana",
    "bio": "Apasionada lectora...",
    "followers": 128,
    "following": 42,
    "userBooks": [
      {
        "id": 1,
        "title": "Cien años de soledad",
        "author": "Gabriel García Márquez",
        "rating": 4.9,
        "cover": "https://...",
        "status": "leido"
      }
    ]
  }
}
```

---

#### PUT /auth/update
Actualizar perfil del usuario autenticado. **Requiere autenticación.**

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "username": "ana_lector_nueva",
  "bio": "Nueva biografía actualizada",
  "avatarUrl": "https://api.dicebear.com/7.x/avataaars/svg?seed=Ana2"
}
```

**Notas:**
- Todos los campos son opcionales
- Solo se actualizarán los campos enviados
- El username debe ser único

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Profile updated successfully",
  "data": {
    "userName": "ana_lector_nueva",
    "userAvatar": "https://api.dicebear.com/7.x/avataaars/svg?seed=Ana2",
    "bio": "Nueva biografía actualizada",
    "followers": 128,
    "following": 42,
    "userBooks": [...]
  }
}
```

---

#### GET /auth/activity
Obtener actividad del usuario autenticado (reviews, listas, estadísticas). **Requiere autenticación.**

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Activity retrieved successfully",
  "data": {
    "stats": {
      "totalReviews": 15,
      "totalReadLists": 3,
      "booksRead": 42,
      "booksReading": 5,
      "booksToRead": 18,
      "averageRating": 4.2
    },
    "recentReviews": [
      {
        "id": 1,
        "bookId": 1,
        "bookTitle": "Cien años de soledad",
        "bookCover": "https://...",
        "rating": 4.9,
        "title": "Una obra maestra",
        "body": "Increíble narrativa...",
        "createdAt": "2025-11-20 15:30:00",
        "likes": 23,
        "comments": 5
      }
    ],
    "readLists": [
      {
        "id": 1,
        "name": "Clásicos latinoamericanos",
        "description": "Mejores libros de literatura...",
        "visibility": "PUBLIC",
        "bookCount": 12,
        "createdAt": "2025-10-15 10:00:00",
        "likes": 45
      }
    ]
  }
}
```

---

#### POST /auth/forgot-password
Solicitar reseteo de contraseña (envía email con token). **No requiere autenticación.**

**Request Body:**
```json
{
  "email": "ana@email.com"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "If the email exists, a password reset link has been sent.",
  "data": null
}
```

**Notas:**
- ✅ **Endpoint público** - No requiere autenticación
- Envía un email con un enlace que contiene el token
- El token expira en 1 hora
- Por seguridad, siempre responde con éxito (no revela si el email existe)
- El enlace enviado es: `http://localhost:5173/reset-password?token=ABC123...`

---

#### POST /auth/reset-password
Confirmar reseteo de contraseña con token (desde el enlace del email). **No requiere autenticación.**

**Request Body:**
```json
{
  "token": "Xy8kL2pQ9mN4vB7cT1fG6hR3aZ5eW0uY",
  "newPassword": "nuevaPassword123"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Password has been reset successfully. You can now login with your new password.",
  "data": null
}
```

**Notas:**
- ✅ **Endpoint público** - No requiere autenticación
- El token debe ser el que se recibió por email
- El token expira en 1 hora desde que fue generado
- El token solo se puede usar una vez

**Errores posibles:**
- Token inválido o expirado
- Token ya utilizado
- Contraseña no cumple requisitos mínimos (mínimo 6 caracteres)

---

### 📖 Books (`/books`)

#### GET /books/trending
Obtener libros en tendencia de la comunidad.

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `limit` (opcional): Número de libros a retornar (default: 20)

**Example:** `GET /books/trending?limit=10`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Books retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Cien años de soledad",
      "author": "Gabriel García Márquez",
      "rating": 4.8,
      "cover": "https://images.unsplash.com/photo-1544947950-fa07a98d237f",
      "status": "leido"
    },
    {
      "id": 2,
      "title": "Rayuela",
      "author": "Julio Cortázar",
      "rating": 4.3,
      "cover": "https://images.unsplash.com/photo-1512820790803",
      "status": "leyendo"
    }
  ]
}
```

---

#### GET /books/search
Buscar libros por título o autor.

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `query` (requerido): Término de búsqueda
- `limit` (opcional): Número de resultados (default: 20)

**Example:** `GET /books/search?query=garcia&limit=10`

**Response:** (mismo formato que `/books/trending`)

---

#### POST /books/reading-status
Actualizar el estado de lectura de un libro.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "bookId": 1,
  "status": "READING"
}
```

**Valores válidos para `status`:**
- `"READING"` - Leyendo actualmente
- `"READ"` - Ya leído
- `"WANT_TO_READ"` - Por leer

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Reading status updated successfully",
  "data": null
}
```

---

### 📋 Lists (`/lists`)

#### POST /lists
Crear una nueva lista de lectura personalizada.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "Clásicos latinoamericanos",
  "description": "Mejores libros de literatura latinoamericana",
  "visibility": "PUBLIC"
}
```

**Valores válidos para `visibility`:**
- `"PUBLIC"` - Visible para todos (default)
- `"PRIVATE"` - Solo visible para el dueño
- `"FOLLOWERS_ONLY"` - Solo visible para seguidores

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "List created successfully",
  "data": {
    "id": 1,
    "name": "Clásicos latinoamericanos",
    "description": "Mejores libros de literatura latinoamericana",
    "visibility": "PUBLIC",
    "userId": 1,
    "username": "ana_lector",
    "createdAt": "2025-11-22 20:00:00",
    "updatedAt": "2025-11-22 20:00:00",
    "bookCount": 0,
    "likeCount": 0,
    "books": []
  }
}
```

---

#### GET /lists/{id}
Obtener detalles de una lista específica con todos sus libros.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "List retrieved successfully",
  "data": {
    "id": 1,
    "name": "Clásicos latinoamericanos",
    "description": "Mejores libros de literatura latinoamericana",
    "visibility": "PUBLIC",
    "userId": 1,
    "username": "ana_lector",
    "createdAt": "2025-11-22 20:00:00",
    "updatedAt": "2025-11-22 20:00:00",
    "bookCount": 2,
    "likeCount": 5,
    "books": [
      {
        "bookId": 1,
        "title": "Cien años de soledad",
        "coverUrl": "https://...",
        "authors": ["Gabriel García Márquez"],
        "position": 1,
        "note": "Mi favorito absoluto",
        "addedAt": "2025-11-22 20:05:00"
      },
      {
        "bookId": 2,
        "title": "Rayuela",
        "coverUrl": "https://...",
        "authors": ["Julio Cortázar"],
        "position": 2,
        "note": null,
        "addedAt": "2025-11-22 20:10:00"
      }
    ]
  }
}
```

---

#### PUT /lists/{id}
Actualizar una lista existente (solo el dueño puede hacerlo).

**Headers:** `Authorization: Bearer <token>`

**Request Body:** (todos los campos son opcionales)
```json
{
  "name": "Clásicos imprescindibles",
  "description": "Nueva descripción actualizada",
  "visibility": "FOLLOWERS_ONLY"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "List updated successfully",
  "data": {
    "id": 1,
    "name": "Clásicos imprescindibles",
    "description": "Nueva descripción actualizada",
    "visibility": "FOLLOWERS_ONLY",
    ...
  }
}
```

---

#### DELETE /lists/{id}
Eliminar una lista (solo el dueño puede hacerlo).

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "List deleted successfully",
  "data": null
}
```

---

#### POST /lists/{id}/books
Añadir un libro a una lista.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "bookId": 1,
  "position": 1,
  "note": "Mi favorito absoluto"
}
```

**Notas:**
- `position` es opcional (default: 1)
- `note` es opcional (máximo 255 caracteres)
- No se puede añadir el mismo libro dos veces a la misma lista

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Book added to list successfully",
  "data": null
}
```

---

#### DELETE /lists/{id}/books/{bookId}
Remover un libro de una lista.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Book removed from list successfully",
  "data": null
}
```

---

### ⭐ Favorites (`/books/favorites`)

#### GET /books/favorites
Obtener todos los libros favoritos del usuario.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Favorites retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Cien años de soledad",
      "author": "Gabriel García Márquez",
      "rating": 0.0,
      "cover": "https://...",
      "status": null
    },
    {
      "id": 5,
      "title": "El amor en los tiempos del cólera",
      "author": "Gabriel García Márquez",
      "rating": 0.0,
      "cover": "https://...",
      "status": null
    }
  ]
}
```

---

#### POST /books/favorites
Añadir un libro a favoritos.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "bookId": 1
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Book added to favorites successfully",
  "data": null
}
```

**Errores posibles:**
- `Book is already in favorites` - El libro ya está en favoritos
- `Book not found` - El libro no existe

---

#### DELETE /books/favorites/{bookId}
Remover un libro de favoritos.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Book removed from favorites successfully",
  "data": null
}
```

**Errores posibles:**
- `Book is not in favorites` - El libro no está en favoritos

---

### ⭐ Reviews (`/reviews`)

#### POST /reviews
Crear una nueva reseña para un libro.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "bookId": 1,
  "rating": 4.5,
  "title": "Una obra maestra",
  "body": "Increíble narrativa que te atrapa desde el inicio..."
}
```

**Notas:**
- `rating` es requerido y debe estar entre 1.0 y 5.0
- `title` es opcional (máximo 140 caracteres)
- `body` es opcional (máximo 5000 caracteres)
- Solo se puede crear una reseña por usuario por libro

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Review created successfully",
  "data": {
    "id": 1,
    "bookId": 1,
    "bookTitle": "Cien años de soledad",
    "bookCover": "https://...",
    "userId": 1,
    "username": "ana_lector",
    "userAvatar": "https://...",
    "rating": 4.5,
    "title": "Una obra maestra",
    "body": "Increíble narrativa...",
    "createdAt": "2025-11-22 22:00:00",
    "updatedAt": "2025-11-22 22:00:00",
    "likes": 0,
    "comments": 0
  }
}
```

**Errores posibles:**
- `You have already reviewed this book` - Ya existe una reseña del usuario para este libro
- `Book not found` - El libro no existe

---

#### PUT /reviews/{id}
Actualizar una reseña existente (solo el autor puede hacerlo).

**Headers:** `Authorization: Bearer <token>`

**Request Body:** (todos los campos son opcionales)
```json
{
  "rating": 5.0,
  "title": "Actualización: aún mejor en segunda lectura",
  "body": "Después de releerlo, aprecio aún más..."
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Review updated successfully",
  "data": {
    "id": 1,
    "bookId": 1,
    ...
    "rating": 5.0,
    "title": "Actualización: aún mejor en segunda lectura",
    "updatedAt": "2025-11-23 10:00:00"
  }
}
```

---

#### DELETE /reviews/{id}
Eliminar una reseña (soft delete, solo el autor puede hacerlo).

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Review deleted successfully",
  "data": null
}
```

---

#### GET /reviews/book/{bookId}
Obtener todas las reseñas de un libro con rating agregado.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Reviews retrieved successfully",
  "data": {
    "bookId": 1,
    "bookTitle": "Cien años de soledad",
    "averageRating": 4.7,
    "totalReviews": 156,
    "reviews": [
      {
        "id": 1,
        "bookId": 1,
        "bookTitle": "Cien años de soledad",
        "bookCover": "https://...",
        "userId": 1,
        "username": "ana_lector",
        "userAvatar": "https://...",
        "rating": 5.0,
        "title": "Una obra maestra",
        "body": "Increíble narrativa...",
        "createdAt": "2025-11-22 22:00:00",
        "updatedAt": "2025-11-22 22:00:00",
        "likes": 23,
        "comments": 5
      },
      {
        "id": 2,
        "userId": 2,
        "username": "carlos_reader",
        "rating": 4.5,
        ...
      }
    ]
  }
}
```

---

#### GET /reviews/book/{bookId}/my-review
Obtener la reseña del usuario actual para un libro específico.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Review retrieved successfully",
  "data": {
    "id": 1,
    "bookId": 1,
    "bookTitle": "Cien años de soledad",
    "userId": 1,
    "username": "ana_lector",
    "rating": 4.5,
    "title": "Una obra maestra",
    "body": "Increíble narrativa...",
    "createdAt": "2025-11-22 22:00:00",
    "updatedAt": "2025-11-22 22:00:00",
    "likes": 0,
    "comments": 0
  }
}
```

**Errores posibles:**
- `Review not found` - El usuario no ha reseñado este libro

---

### 👥 Social (`/social`)

#### POST /social/follow/user
Seguir a otro usuario.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "userId": 2
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "User followed successfully",
  "data": {
    "followerId": 1,
    "followerUsername": "ana_lector",
    "followedId": 2,
    "followedUsername": "carlos_reader",
    "createdAt": "2025-11-22 23:00:00"
  }
}
```

**Errores posibles:**
- `You cannot follow yourself` - No puedes seguirte a ti mismo
- `You are already following this user` - Ya sigues a este usuario
- `User to follow not found` - El usuario no existe

---

#### DELETE /social/follow/user/{userId}
Dejar de seguir a un usuario.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "User unfollowed successfully",
  "data": null
}
```

**Errores posibles:**
- `You are not following this user` - No sigues a este usuario

---

#### POST /social/follow/author
Seguir a un autor.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "authorId": 5
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Author followed successfully",
  "data": null
}
```

**Errores posibles:**
- `You are already following this author` - Ya sigues a este autor
- `Author not found` - El autor no existe

---

#### DELETE /social/follow/author/{authorId}
Dejar de seguir a un autor.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Author unfollowed successfully",
  "data": null
}
```

**Errores posibles:**
- `You are not following this author` - No sigues a este autor

---

#### GET /social/users/{userId}/stats
Obtener estadísticas de seguidores de un usuario.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "User follow statistics retrieved successfully",
  "data": {
    "userId": 2,
    "username": "carlos_reader",
    "followersCount": 45,
    "followingCount": 23,
    "isFollowing": true
  }
}
```

**Notas:**
- `isFollowing` indica si el usuario actual sigue a este usuario
- `isFollowing` será `null` si consultas tus propias estadísticas

---

#### GET /social/follow/user/{userId}/status
Verificar si sigues a un usuario específico.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Follow status retrieved successfully",
  "data": true
}
```

**Valores posibles:** `true` (siguiendo) o `false` (no siguiendo)

---

#### GET /social/follow/author/{authorId}/status
Verificar si sigues a un autor específico.

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Follow status retrieved successfully",
  "data": true
}
```

**Valores posibles:** `true` (siguiendo) o `false` (no siguiendo)

---

## 🔧 Códigos de Estado HTTP

- `200 OK` - Solicitud exitosa
- `201 Created` - Recurso creado exitosamente
- `400 Bad Request` - Datos de entrada inválidos
- `401 Unauthorized` - Token inválido o faltante
- `404 Not Found` - Recurso no encontrado
- `500 Internal Server Error` - Error del servidor

---

## 🚀 Cómo usar la API

### 1. Registro e Inicio de Sesión

```bash
# Registrar nuevo usuario
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"ana_lector","email":"ana@email.com","password":"password123"}'

# Iniciar sesión
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"ana_lector","password":"password123"}'
```

### 2. Reseteo de Contraseña (Sin Autenticación)

```bash
# Solicitar reseteo (se envía email) - Endpoint PÚBLICO
curl -X POST http://localhost:8080/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@email.com"}'

# Confirmar reseteo con token (desde el email) - Endpoint PÚBLICO
curl -X POST http://localhost:8080/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{"token":"TOKEN_DEL_EMAIL","newPassword":"nuevaPassword123"}'
```

### 3. Gestión de Perfil (Con Autenticación)

```bash
# Obtener perfil (reemplaza <TOKEN> con tu token JWT)
curl -X GET http://localhost:8080/auth/me \
  -H "Authorization: Bearer <TOKEN>"

# Actualizar perfil
curl -X PUT http://localhost:8080/auth/update \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"username":"nuevo_nombre","bio":"Nueva bio","avatarUrl":"https://..."}'

# Obtener actividad (reviews, listas, stats)
curl -X GET http://localhost:8080/auth/activity \
  -H "Authorization: Bearer <TOKEN>"
```

### 4. Gestión de Libros (Con Autenticación)

```bash
# Obtener libros en tendencia
curl -X GET http://localhost:8080/books/trending?limit=10 \
  -H "Authorization: Bearer <TOKEN>"

# Buscar libros
curl -X GET "http://localhost:8080/books/search?query=garcia" \
  -H "Authorization: Bearer <TOKEN>"

# Actualizar estado de lectura
curl -X POST http://localhost:8080/books/reading-status \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"status":"READING"}'
```

### 5. Gestión de Listas de Lectura (Con Autenticación)

```bash
# Crear una nueva lista
curl -X POST http://localhost:8080/lists \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Mis favoritos","description":"Los mejores libros","visibility":"PUBLIC"}'

# Obtener detalles de una lista
curl -X GET http://localhost:8080/lists/1 \
  -H "Authorization: Bearer <TOKEN>"

# Actualizar una lista
curl -X PUT http://localhost:8080/lists/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Nuevo nombre","visibility":"PRIVATE"}'

# Eliminar una lista
curl -X DELETE http://localhost:8080/lists/1 \
  -H "Authorization: Bearer <TOKEN>"

# Añadir un libro a la lista
curl -X POST http://localhost:8080/lists/1/books \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"position":1,"note":"Mi favorito"}'

# Remover un libro de la lista
curl -X DELETE http://localhost:8080/lists/1/books/1 \
  -H "Authorization: Bearer <TOKEN>"
```

### 6. Gestión de Favoritos (Con Autenticación)

```bash
# Obtener todos los favoritos
curl -X GET http://localhost:8080/books/favorites \
  -H "Authorization: Bearer <TOKEN>"

# Añadir un libro a favoritos
curl -X POST http://localhost:8080/books/favorites \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"bookId":1}'

# Remover un libro de favoritos
curl -X DELETE http://localhost:8080/books/favorites/1 \
  -H "Authorization: Bearer <TOKEN>"
```

### 7. Gestión de Reseñas (Con Autenticación)

```bash
# Crear una reseña
curl -X POST http://localhost:8080/reviews \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"rating":4.5,"title":"Excelente","body":"Me encantó este libro..."}'

# Actualizar una reseña
curl -X PUT http://localhost:8080/reviews/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"rating":5.0,"title":"Perfecto"}'

# Eliminar una reseña
curl -X DELETE http://localhost:8080/reviews/1 \
  -H "Authorization: Bearer <TOKEN>"

# Ver todas las reseñas de un libro
curl -X GET http://localhost:8080/reviews/book/1 \
  -H "Authorization: Bearer <TOKEN>"

# Ver mi reseña de un libro
curl -X GET http://localhost:8080/reviews/book/1/my-review \
  -H "Authorization: Bearer <TOKEN>"
```

### 8. Funciones Sociales (Con Autenticación)

```bash
# Seguir a un usuario
curl -X POST http://localhost:8080/social/follow/user \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"userId":2}'

# Dejar de seguir a un usuario
curl -X DELETE http://localhost:8080/social/follow/user/2 \
  -H "Authorization: Bearer <TOKEN>"

# Seguir a un autor
curl -X POST http://localhost:8080/social/follow/author \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"authorId":5}'

# Dejar de seguir a un autor
curl -X DELETE http://localhost:8080/social/follow/author/5 \
  -H "Authorization: Bearer <TOKEN>"

# Ver estadísticas de seguidores de un usuario
curl -X GET http://localhost:8080/social/users/2/stats \
  -H "Authorization: Bearer <TOKEN>"

# Verificar si sigues a un usuario
curl -X GET http://localhost:8080/social/follow/user/2/status \
  -H "Authorization: Bearer <TOKEN>"

# Verificar si sigues a un autor
curl -X GET http://localhost:8080/social/follow/author/5/status \
  -H "Authorization: Bearer <TOKEN>"
```

---

## 📝 Notas Importantes

- **La contraseña por defecto en datos dummy es:** `password123` (encriptada con BCrypt)
- **Estados de lectura en frontend:** `"leyendo"`, `"leido"`, `"por_leer"`
- **Estados de lectura en backend:** `"READING"`, `"READ"`, `"WANT_TO_READ"`
- **Endpoints públicos (sin autenticación):**
  - `POST /auth/register`
  - `POST /auth/login`
  - `POST /auth/forgot-password`
  - `POST /auth/reset-password`
- **Endpoints protegidos (requieren JWT):**
  - `GET /auth/me` - Obtener perfil
  - `PUT /auth/update` - Actualizar perfil
  - `GET /auth/activity` - Obtener actividad (reviews, listas, stats)
  - `GET /books/trending` - Libros en tendencia
  - `GET /books/search` - Buscar libros
  - `POST /books/reading-status` - Actualizar estado de lectura
  - `POST /lists` - Crear lista de lectura
  - `GET /lists/{id}` - Ver detalles de lista
  - `PUT /lists/{id}` - Actualizar lista
  - `DELETE /lists/{id}` - Eliminar lista
  - `POST /lists/{id}/books` - Añadir libro a lista
  - `DELETE /lists/{id}/books/{bookId}` - Remover libro de lista
  - `GET /books/favorites` - Ver libros favoritos
  - `POST /books/favorites` - Añadir libro a favoritos
  - `DELETE /books/favorites/{bookId}` - Remover libro de favoritos
  - `POST /reviews` - Crear reseña
  - `PUT /reviews/{id}` - Actualizar reseña
  - `DELETE /reviews/{id}` - Eliminar reseña
  - `GET /reviews/book/{bookId}` - Ver reseñas de un libro
  - `GET /reviews/book/{bookId}/my-review` - Ver mi reseña de un libro
  - `POST /social/follow/user` - Seguir usuario
  - `DELETE /social/follow/user/{userId}` - Dejar de seguir usuario
  - `POST /social/follow/author` - Seguir autor
  - `DELETE /social/follow/author/{authorId}` - Dejar de seguir autor
  - `GET /social/users/{userId}/stats` - Ver estadísticas de seguidores
  - `GET /social/follow/user/{userId}/status` - Verificar si sigues a un usuario
  - `GET /social/follow/author/{authorId}/status` - Verificar si sigues a un autor
- La documentación se genera automáticamente desde el código
- Todos los endpoints están documentados en Swagger UI
- El manejo de errores está centralizado y devuelve códigos HTTP apropiados

---

## 🧪 Testing con Swagger UI

1. Asegúrate de que la aplicación esté corriendo en el puerto 8080
2. Abre tu navegador: `http://localhost:8080/swagger-ui.html`
3. Haz login en `/auth/login` y copia el token
4. Haz clic en **"Authorize"** y pega el token
5. Prueba cualquier endpoint directamente desde el navegador
