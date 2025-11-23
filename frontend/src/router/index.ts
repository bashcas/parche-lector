// src/router/index.ts
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { authUtils } from '../utils/auth'

// Importa tus vistas/páginas
import WelcomeView from '../views/WelcomeView.vue'
import LoginView from '../features/auth/views/LoginView.vue'
import RegisterView from '../features/auth/views/RegisterView.vue'
import HomeView from '../views/HomeView.vue'
import ProfileView from '../features/profile/views/ProfileView.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'welcome',
    component: WelcomeView,
    meta: { publicOnly: true },
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { publicOnly: true },
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: { publicOnly: true },
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true },
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { requiresAuth: true },
  },
  // ruta comodín para 404 (opcional)
  { path: '/:pathMatch(.*)*', name: 'not-found', component: WelcomeView },
]

const router = createRouter({
  history: createWebHistory(), // <-- URLs bonitas (sin #)
  routes,
})

// Route guard to protect authenticated routes and redirect authenticated users from public pages
router.beforeEach((to, _from, next) => {
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)
  const publicOnly = to.matched.some((record) => record.meta.publicOnly)
  const isAuthenticated = authUtils.isAuthenticated()

  // If route requires auth and user is not authenticated, redirect to welcome
  if (requiresAuth && !isAuthenticated) {
    next({ name: 'welcome' })
    return
  }

  // If route is public-only and user is authenticated, redirect to home
  if (publicOnly && isAuthenticated) {
    next({ name: 'home' })
    return
  }

  // Otherwise, allow navigation
  next()
})

export default router

