# Component Documentation

## Overview

This document provides detailed API references and usage examples for all components in the Parche Lector frontend application.

## View Components

### WelcomeView

**File**: `src/views/WelcomeView.vue`  
**Route**: `/`  
**Access**: Public only (redirects to `/home` if authenticated)

#### Purpose
Landing page that introduces the application and provides navigation to login and registration.

#### Structure
```vue
<template>
  <div class="welcome-container">
    <img src="icon.svg" class="welcome-icon" />
    <h1 class="title">¡Bienvenido/a a El Parche Lector!</h1>
    <p class="description"><!-- Description --></p>
    <div class="buttons">
      <button @click="goToLogin">Iniciar sesión</button>
      <button @click="goToRegister">Crear cuenta</button>
    </div>
  </div>
</template>
```

#### Methods
- `goToLogin()`: Navigate to login page
- `goToRegister()`: Navigate to registration page

#### Screenshots
![Welcome View - Main Layout](./screenshots/welcome-view.png)


---

### LoginView

**File**: `src/login/views/LoginView.vue`  
**Route**: `/login`  
**Access**: Public only

#### Purpose
User authentication form with validation.

#### Props
None (uses composable)

#### Composable
Uses `useLogin()` from `src/login/composables/useLogin.ts`

#### State
```typescript
{
  usernameOrEmail: ref<string>(''),
  password: ref<string>(''),
  errors: ref<Record<string, string>>({}),
  isPending: ref<boolean>(false),
  loginError: ref<Error | null>(null)
}
```

#### Methods
- `onSubmit()`: Handle form submission and authentication

#### Form Fields
1. **Username or Email**
   - Type: `text`
   - Validation: Required
   - Placeholder: "tu@email.com o nombre de usuario"

2. **Password**
   - Type: `password`
   - Validation: Required, minimum 6 characters
   - Placeholder: "••••••••"

#### Screenshots
![Login View - Empty Form](./screenshots/login-view.png)

![Login View - Validation Errors](./screenshots/login-errors.png)

![Login View - Loading State](./screenshots/login-loading.png)

---

### RegisterView

**File**: `src/register/views/RegisterView.vue`  
**Route**: `/register`  
**Access**: Public only

#### Purpose
New user registration form with validation.

#### Composable
Uses `useRegister()` from `src/register/composables/useRegister.ts`

#### State
```typescript
{
  username: ref<string>(''),
  email: ref<string>(''),
  password: ref<string>(''),
  errors: ref<Record<string, string>>({}),
  isPending: ref<boolean>(false),
  registerError: ref<Error | null>(null)
}
```

#### Methods
- `onSubmit()`: Handle form submission and user registration

#### Form Fields
1. **Username**
   - Type: `text`
   - Validation: Required, alphanumeric with dots/underscores
   - Placeholder: "sebastian.castaneda"

2. **Email**
   - Type: `email`
   - Validation: Required, valid email format
   - Placeholder: "ana@email.com"

3. **Password**
   - Type: `password`
   - Validation: Required, minimum 6 characters
   - Placeholder: "Mínimo 6 caracteres"

#### Screenshots
![Register View - Empty Form](./screenshots/register-view.png)

![Register View - Validation Errors](./screenshots/register-errors.png)

![Register View - Success State](./screenshots/register-success.png)

---

### HomeView

**File**: `src/views/HomeView.vue`  
**Route**: `/home`  
**Access**: Protected (requires authentication)

#### Purpose
Main application dashboard displaying trending books and search functionality.

#### State
```typescript
{
  userName: ref<string>('Ana'),
  userAvatar: ref<string>('https://...'),
  showMenu: ref<boolean>(false),
  searchQuery: ref<string>(''),
  books: ref<Book[]>([]),
  isLoading: ref<boolean>(false),
  errorMessage: ref<string>(''),
  openBookMenuId: ref<string | null>(null)
}
```

#### Computed Properties
- `filteredBooks`: Books filtered by search query

#### Methods
- `goToProfile()`: Navigate to profile page
- `openMenu()`: Show user menu
- `closeMenu()`: Hide user menu with delay
- `toggleMenu()`: Toggle user menu visibility
- `logout()`: Clear session and redirect to welcome
- `loadBooks()`: Fetch books from Google Books API
- `handleSearch()`: Perform book search
- `toggleBookMenu(bookId)`: Toggle book action menu
- `addToList(book, status)`: Add book to reading list
- `statusLabel(status)`: Get localized status label

#### Components Structure
1. **Header**
   - App icon
   - User avatar with dropdown menu
   
2. **Main Content**
   - Welcome section with personalized greeting
   - Search bar
   - Books grid with trending books
   - Loading state
   - Error state

3. **Bottom Navigation** (mobile)
   - Home link
   - Explore link
   - Profile link

#### Book Card Features
- Cover image
- Title and author
- Rating display (stars)
- Action menu for adding to lists
- Status badge

#### Screenshots
![Home View - Default State](./screenshots/home-view.png)

![Home View - Search Active](./screenshots/home-search.png)

![Home View - User Menu Open](./screenshots/home-menu.png)

![Home View - Book Action Menu](./screenshots/home-book-menu.png)

![Home View - Loading State](./screenshots/home-loading.png)

![Home View - Error State](./screenshots/home-error.png)

![Home View - Mobile Bottom Nav](./screenshots/home-mobile-nav.png)

---

### ProfileView

**File**: `src/views/ProfileView.vue`  
**Route**: `/profile`  
**Access**: Protected (requires authentication)

#### Purpose
User profile page with reading lists organized by status.

#### State
```typescript
{
  userName: ref<string>('Ana'),
  userAvatar: ref<string>('https://...'),
  userBio: ref<string>('Amante de...'),
  userBooks: ref<Book[]>([]),
  showEdit: ref<boolean>(false),
  editName: ref<string>(''),
  editBio: ref<string>(''),
  editAvatar: ref<string>('')
}
```

#### Computed Properties
- `readingBooks`: Books with status "leyendo"
- `readBooks`: Books with status "leido"
- `toReadBooks`: Books with status "por_leer"

#### Methods
- `openEdit()`: Open edit profile modal
- `closeEdit()`: Close edit modal
- `saveEdit()`: Save profile changes
- `logout()`: Clear session and redirect
- `statusLabel(status)`: Get localized status label

#### Components Structure
1. **Profile Header**
   - Large avatar
   - User name and bio
   - Reading statistics
   - Edit and logout buttons

2. **Edit Modal**
   - Name input
   - Bio textarea
   - Avatar URL input
   - Save/Cancel buttons

3. **Reading Lists Section**
   - Three columns:
     - Leyendo (Currently Reading)
     - Leídos (Finished)
     - Por leer (To Read)
   - Book count badges
   - Book cards with covers and ratings

#### Screenshots
![Profile View - Main Layout](./screenshots/profile-view.png)

![Profile View - Edit Modal Open](./screenshots/profile-edit.png)

![Profile View - Reading Lists Detail](./screenshots/profile-lists.png)

![Profile View - Empty List State](./screenshots/profile-empty.png)

![Profile View - Mobile Layout](./screenshots/profile-mobile.png)

---
 
 ### BookDetailView
 
 **File**: `src/features/books/views/BookDetailView.vue`
 **Route**: `/books/:id`
 **Access**: Protected
 
 #### Purpose
 Displays detailed information about a specific book, including reviews and reading status.
 
 #### Components Structure
 - Book cover and metadata
 - Reading status selector
 - Rating summary
 - User reviews list
 - "Write a review" form
 
 ---
 
 ### FavoritesView
 
 **File**: `src/features/books/views/FavoritesView.vue`
 **Route**: `/favorites`
 **Access**: Protected
 
 #### Purpose
 Shows a grid of books marked as favorites by the user.
 
 ---
 
 ### ListsView
 
 **File**: `src/features/lists/views/ListsView.vue`
 **Route**: `/lists`
 **Access**: Protected
 
 #### Purpose
 Displays all user-created reading lists and allows creating new ones.
 
 ---
 
 ### ListDetailView
 
 **File**: `src/features/lists/views/ListDetailView.vue`
 **Route**: `/lists/:id`
 **Access**: Protected
 
 #### Purpose
 Shows the contents of a specific reading list, allowing users to add/remove books and edit list details.
 
 ---
 
 ### FeedView
 
 **File**: `src/features/social/views/FeedView.vue`
 **Route**: `/feed`
 **Access**: Protected
 
 #### Purpose
 Displays a social feed of activities from followed users (reviews, status updates, etc.).

## Reusable Composables

### useLogin

**File**: `src/login/composables/useLogin.ts`

#### Purpose
Manages login form state, validation, and submission.

#### Returns
```typescript
{
  usernameOrEmail: Ref<string>
  password: Ref<string>
  errors: Ref<Record<string, string>>
  onSubmit: (e?: Event) => Promise<void>
  isPending: Ref<boolean>
  loginError: Ref<Error | null>
}
```

#### Usage
```vue
<script setup>
import { useLogin } from '../composables/useLogin'

const { 
  usernameOrEmail, 
  password, 
  errors, 
  onSubmit, 
  isPending 
} = useLogin()
</script>
```

---

### useRegister

**File**: `src/register/composables/useRegister.ts`

#### Purpose
Manages registration form state, validation, and submission.

#### Returns
```typescript
{
  username: Ref<string>
  email: Ref<string>
  password: Ref<string>
  errors: Ref<Record<string, string>>
  onSubmit: (e?: Event) => Promise<void>
  isPending: Ref<boolean>
  registerError: Ref<Error | null>
}
```

---

## Service Modules

### Google Books Service

**File**: `src/services/googleBooksService.ts`

#### Exports

1. **Type Definitions**
```typescript
interface Book {
  id: string
  title: string
  author: string
  cover: string
  rating: number
  status?: 'leyendo' | 'leido' | 'por_leer'
}
```

2. **Functions**

```typescript
// Fetch random trending books
getRandomBooks(count?: number): Promise<Book[]>

// Search books by query
searchBooks(query: string): Promise<Book[]>
```

#### Usage Example
```typescript
import { getRandomBooks, searchBooks } from '@/services/googleBooksService'

// Load trending books
const books = await getRandomBooks(12)

// Search for specific books
const results = await searchBooks('Harry Potter')
```

---

## Utility Modules

### Auth Utils

**File**: `src/utils/auth`

#### Functions

```typescript
// Check if user is authenticated
isAuthenticated(): boolean

// Get authentication token
getToken(): string | null

// Store authentication token
setToken(token: string): void

// Remove authentication token
clearToken(): void

// Logout user and redirect
logout(router: Router): void
```

#### Usage Example
```typescript
import { authUtils } from '@/utils/auth'

// Check authentication
if (authUtils.isAuthenticated()) {
  // User is logged in
}

// Store token after login
authUtils.setToken(responseToken)

// Logout
authUtils.logout(router)
```

---

## Styling Conventions

### CSS Variables
```css
:root {
  --bg: #f5f0e9;
  --accent: #f9c846;
  --primary: #2e5266;
  --muted: #6b9080;
}
```

### Common CSS Classes

#### Buttons
- `.btn-primary`: Primary action button (yellow accent)
- `.btn-secondary`: Secondary action button (outlined)
- `.btn.outline`: Outlined button style
- `.btn.primary`: Filled primary button

#### Layout
- `.auth-container`: Centered authentication layout
- `.auth-card`: Card container for auth forms
- `.welcome-container`: Full-page welcome layout

#### Form Elements
- `.input-group`: Form field wrapper
- `.error-message`: Validation error display
- `.server-error`: Server-side error display

#### Interactive States
- `:hover`: Elevation and color change
- `:focus`: Border accent color
- `:disabled`: Reduced opacity

---

## Component Best Practices

### 1. Type Safety
Always define TypeScript interfaces for component data:
```typescript
interface Book {
  id: string
  title: string
  // ...
}
```

### 2. Scoped Styles
Use scoped styles to prevent CSS leakage:
```vue
<style scoped>
.component-specific {
  /* ... */
}
</style>
```

### 3. Error Handling
Implement proper error states in all data-fetching components:
```typescript
try {
  const data = await fetchData()
} catch (error) {
  errorMessage.value = 'Failed to load data'
}
```

### 4. Loading States
Always show loading indicators during async operations:
```vue
<div v-if="isLoading">Loading...</div>
<div v-else><!-- Content --></div>
```

---

**Component Documentation Version**: 1.0  
**Last Updated**: 2025-11-21
