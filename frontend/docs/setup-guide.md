# Development Setup Guide

This guide will help you set up the Parche Lector frontend development environment.

---

## Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

- **Node.js**: v18.0.0 or higher
- **npm**: v9.0.0 or higher (comes with Node.js)
- **Git**: Latest version

### Verify Installation

```bash
# Check Node.js version
node --version
# Should output: v18.x.x or higher

# Check npm version
npm --version
# Should output: 9.x.x or higher

# Check Git version
git --version
```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd parche-lector/frontend
```

### 2. Install Dependencies

```bash
npm install
```

This will install all dependencies listed in `package.json`:
- Vue 3
- TypeScript
- Vite
- Vue Router
- TanStack Query
- Axios
- VeeValidate
- Yup
- And development dependencies

### 3. Environment Configuration

Create a `.env` file in the `frontend` directory:

```bash
# Optional: Google Books API Key
VITE_GOOGLE_BOOKS_API_KEY=your_api_key_here
```

> **Note**: The Google Books API key is optional. The app will work without it but may have lower rate limits.

---

## Running the Application

### Development Server

Start the development server with hot module replacement:

```bash
npm run dev
```

The application will be available at:
```
http://localhost:5173
```

### Build for Production

Create an optimized production build:

```bash
npm run build
```

Output will be in the `dist` directory.

### Preview Production Build

Preview the production build locally:

```bash
npm run preview
```

### Type Checking

Run TypeScript type checking without building:

```bash
npm run type-check
```

---

## Project Structure Explained

```
frontend/
├── public/                 # Static assets
│   └── icon.svg           # App icon
├── src/
│   ├── assets/            # Images, fonts, etc.
│   ├── login/             # Login feature module
│   │   ├── composables/   # Login-specific composables
│   │   └── views/         # Login views
│   ├── register/          # Registration feature module
│   │   ├── composables/   # Register-specific composables
│   │   └── views/         # Register views
│   ├── router/            # Vue Router configuration
│   │   └── index.ts       # Routes and navigation guards
│   ├── services/          # API service layer
│   │   └── googleBooksService.ts
│   ├── utils/             # Utility functions
│   │   └── auth.ts        # Authentication helpers
│   ├── views/             # Main application views
│   │   ├── WelcomeView.vue
│   │   ├── HomeView.vue
│   │   └── ProfileView.vue
│   ├── App.vue            # Root component
│   ├── main.ts            # Application entry point
│   ├── style.css          # Global styles
│   └── env.d.ts           # TypeScript environment types
├── index.html             # HTML entry point
├── package.json           # Dependencies and scripts
├── tsconfig.json          # TypeScript configuration
├── tsconfig.node.json     # TypeScript config for Node
├── vite.config.ts         # Vite build configuration
└── .gitignore            # Git ignore patterns
```

---

## Development Workflow

### 1. Creating New Components

```bash
# Create a new view component
touch src/views/MyNewView.vue

# Create a new composable
touch src/composables/useMyFeature.ts
```

### 2. Adding Routes

Edit `src/router/index.ts`:

```typescript
import MyNewView from '../views/MyNewView.vue'

const routes = [
  // ... existing routes
  {
    path: '/my-route',
    name: 'my-route',
    component: MyNewView,
    meta: { requiresAuth: true },
  },
]
```

### 3. Adding Dependencies

```bash
# Install a new dependency
npm install package-name

# Install a dev dependency
npm install -D package-name
```

---

## Code Style Guidelines

### Vue Components

Use Composition API with `<script setup>`:

```vue
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

// Component logic here
</script>

<template>
  <!-- Template here -->
</template>

<style scoped>
/* Scoped styles here */
</style>
```

### TypeScript

Always use TypeScript types:

```typescript
// Define interfaces
interface User {
  id: string
  username: string
}

// Type function parameters and returns
function getUser(id: string): Promise<User> {
  // ...
}
```

### Naming Conventions

- **Components**: PascalCase (e.g., `MyComponent.vue`)
- **Composables**: camelCase with "use" prefix (e.g., `useAuth.ts`)
- **Files**: kebab-case or PascalCase
- **CSS Classes**: kebab-case (e.g., `user-profile`)

---

## Common Development Tasks

### Adding a New View

1. Create the view component in `src/views/`
2. Add the route in `src/router/index.ts`
3. Add navigation links in relevant components
4. Test the route and navigation

### Creating a Composable

```typescript
// src/composables/useCounter.ts
import { ref } from 'vue'

export function useCounter(initialValue = 0) {
  const count = ref(initialValue)
  
  const increment = () => count.value++
  const decrement = () => count.value--
  
  return {
    count,
    increment,
    decrement
  }
}
```

### Adding an API Service

```typescript
// src/services/myService.ts
import axios from 'axios'

const API_BASE = 'https://api.example.com'

export async function fetchData() {
  const response = await axios.get(`${API_BASE}/data`)
  return response.data
}
```

---

## Debugging

### Vue DevTools

Install the Vue DevTools browser extension:
- [Chrome](https://chrome.google.com/webstore/detail/vuejs-devtools)
- [Firefox](https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/)

### Console Debugging

```typescript
// Add debug logs
console.log('Debug:', someVariable)

// Use debugger statement
debugger
```

### Vite Debug Mode

Add debug output in `vite.config.ts`:

```typescript
export default defineConfig({
  // ...
  logLevel: 'info', // or 'warn', 'error', 'silent'
})
```

---

## Troubleshooting

### Port Already in Use

If port 5173 is in use:

```bash
# Kill the process using the port (Linux/Mac)
lsof -ti:5173 | xargs kill

# Or specify a different port
npm run dev -- --port 3000
```

### Module Not Found Errors

```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

### TypeScript Errors

```bash
# Run type checking to see all errors
npm run type-check

# Clear TypeScript cache
rm -rf node_modules/.vite
```

### Build Errors

```bash
# Clear Vite cache and rebuild
rm -rf dist .vite
npm run build
```

---

## Testing

### Manual Testing Checklist

- [ ] All routes are accessible
- [ ] Authentication works (login/register)
- [ ] Protected routes redirect when not authenticated
- [ ] Book search returns results
- [ ] Books can be added to reading lists
- [ ] Profile can be edited
- [ ] Logout works correctly
- [ ] Responsive design works on mobile

### Browser Testing

Test in multiple browsers:
- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

---

## Performance Optimization

### Development

- Use Vue DevTools Performance tab
- Monitor network requests
- Check for unnecessary re-renders

### Production

```bash
# Analyze bundle size
npm run build -- --mode analyze

# Check build output
du -sh dist/*
```

---

## Environment Variables

### Available Variables

All environment variables must be prefixed with `VITE_`:

```bash
# .env file
VITE_API_BASE_URL=http://localhost:3000
VITE_GOOGLE_BOOKS_API_KEY=your_key_here
```

### Using in Code

```typescript
const apiKey = import.meta.env.VITE_GOOGLE_BOOKS_API_KEY
```

### Environment Files

- `.env` - Loaded in all environments
- `.env.local` - Loaded in all environments, ignored by git
- `.env.development` - Development only
- `.env.production` - Production only

---

## Git Workflow

### Branch Strategy

```bash
# Create feature branch
git checkout -b feature/my-feature

# Make changes and commit
git add .
git commit -m "Add my feature"

# Push to remote
git push origin feature/my-feature
```

### Commit Message Format

```
feat: Add book search functionality
fix: Fix login validation error
docs: Update setup guide
style: Format code with prettier
refactor: Simplify auth logic
```

---

## Useful Resources

### Official Documentation

- [Vue 3 Docs](https://vuejs.org/)
- [Vite Docs](https://vitejs.dev/)
- [TypeScript Docs](https://www.typescriptlang.org/)
- [Vue Router Docs](https://router.vuejs.org/)

### Community Resources

- [Vue Forum](https://forum.vuejs.org/)
- [Vue Discord](https://discord.com/invite/vue)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/vue.js)

---

## Next Steps

After setup, you might want to:

1. **Read the Architecture Documentation** - Understand the app structure
2. **Review Component Documentation** - Learn about available components
3. **Check API Integration** - Understand how APIs are integrated
4. **Read User Guide** - See the app from user perspective

---

**Setup Guide Version**: 1.0  
**Last Updated**: 2025-11-21
