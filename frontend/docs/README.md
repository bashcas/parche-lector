# Parche Lector - Frontend Documentation

## Project Overview

**Parche Lector** is a modern web application for book lovers to discover, track, and share their reading experiences. The frontend is built with Vue 3, TypeScript, and Vite, providing a fast, responsive, and type-safe user experience.

## Technology Stack

### Core Technologies
- **Vue 3** - Progressive JavaScript framework with Composition API
- **TypeScript** - Type-safe development
- **Vite** - Next-generation frontend build tool
- **Vue Router 4** - Official routing library

### Key Dependencies
- **@tanstack/vue-query** - Powerful data synchronization and caching
- **axios** - HTTP client for API requests
- **vee-validate** - Form validation library
- **yup** - Schema validation

## Project Structure

```
frontend/
├── public/               # Static assets
│   └── icon.svg         # Application icon
├── src/
│   ├── views/           # Main application views
│   │   ├── WelcomeView.vue
│   │   ├── HomeView.vue
│   │   └── ProfileView.vue
│   ├── login/           # Login feature module
│   │   ├── views/
│   │   └── composables/
│   ├── register/        # Registration feature module
│   │   ├── views/
│   │   └── composables/
│   ├── router/          # Vue Router configuration
│   ├── services/        # API services
│   ├── utils/           # Utility functions
│   ├── assets/          # Images, fonts, etc.
│   ├── App.vue          # Root component
│   ├── main.ts          # Application entry point
│   └── style.css        # Global styles
├── index.html           # HTML entry point
├── package.json         # Dependencies and scripts
├── tsconfig.json        # TypeScript configuration
└── vite.config.ts       # Vite configuration
```

## Application Features

### 1. Authentication System
- User registration with email validation
- Secure login with username or email
- Protected routes with authentication guards
- Session management with localStorage

### 2. Book Discovery
- Integration with Google Books API
- Search functionality for finding books
- Trending books display
- Book details with ratings and authors

### 3. Reading Lists
- Three reading status categories:
  - **Leyendo** (Currently Reading)
  - **Leídos** (Finished)
  - **Por leer** (To Read)
- Visual organization with book cards
- Easy status management

### 4. User Profile
- Editable user information
- Avatar customization
- Personal book collection management
- Reading statistics

## Available Scripts

```bash
# Development server
npm run dev

# Type checking
npm run type-check

# Production build
npm run build

# Preview production build
npm run preview
```

## Screenshots

### Welcome View
![Welcome View - Landing page with app introduction and call-to-action buttons](./screenshots/welcome-view.png)

### Login View
![Login View - User authentication form](./screenshots/login-view.png)

### Register View
![Register View - New user registration form](./screenshots/register-view.png)

### Home View
![Home View - Main dashboard with trending books and search functionality](./screenshots/home-view.png)

### Profile View
![Profile View - User profile with reading lists organized by status](./screenshots/profile-view.png)

## Design System

### Color Palette
- **Background**: `#F5F0E9` (Warm beige)
- **Primary**: `#2E5266` (Deep blue)
- **Accent**: `#F9C846` (Mustard yellow)
- **Muted**: `#6B9080` (Olive green)

### Typography
- **Font Family**: Inter, system-ui, sans-serif
- **Headings**: Bold, 700 weight
- **Body**: Regular, 400 weight

### UI Components
- Rounded corners (12px border-radius)
- Soft shadows for depth
- Smooth transitions (0.25s ease)
- Hover states for interactive elements

## Routing Structure

| Route | Component | Access | Description |
|-------|-----------|--------|-------------|
| `/` | WelcomeView | Public only | Landing page |
| `/login` | LoginView | Public only | User login |
| `/register` | RegisterView | Public only | New user registration |
| `/home` | HomeView | Protected | Main dashboard |
| `/profile` | ProfileView | Protected | User profile |

## Authentication Flow

1. User visits the application
2. Unauthenticated users see the Welcome page
3. Users can register or login
4. Upon successful authentication, token is stored in localStorage
5. Protected routes become accessible
6. Route guards redirect based on authentication status

## API Integration

### Google Books Service
The application integrates with the Google Books API to provide:
- Random book recommendations
- Search functionality
- Book metadata (title, author, cover, ratings)

**Service Location**: `src/services/googleBooksService.ts`

## Development Guidelines

### Component Organization
- Use Composition API with `<script setup>`
- Keep components focused and single-responsibility
- Use TypeScript for type safety
- Implement proper error handling

### State Management
- Local component state with `ref()` and `reactive()`
- Composables for shared logic
- TanStack Query for server state

### Styling
- Scoped styles in Single File Components
- CSS variables for theming
- Mobile-first responsive design

## Browser Support

- Modern browsers (Chrome, Firefox, Safari, Edge)
- ES2015+ support required
- Responsive design for mobile and desktop

## Related Documentation

- [Architecture Documentation](./architecture.md) - Detailed technical architecture
- [Component Documentation](./components.md) - Component API reference
- [User Guide](./user-guide.md) - End-user documentation

---

**Version**: 0.0.0  
**Last Updated**: 2025-11-21
