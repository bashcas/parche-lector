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
Obtener perfil del usuario autenticado.

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

### 3. Usar Endpoints Protegidos

```bash
# Obtener perfil (reemplaza <TOKEN> con tu token JWT)
curl -X GET http://localhost:8080/auth/me \
  -H "Authorization: Bearer <TOKEN>"

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
  - `GET /auth/me`
  - `GET /books/trending`
  - `GET /books/search`
  - `POST /books/reading-status`
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
