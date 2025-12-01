// Centralized Axios instance for API calls
import axios from 'axios'
import { authUtils } from '../../utils/auth'

// API base URL - uses environment variable with fallback to deployed backend
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://parche-lector.onrender.com'

// Create axios instance with default config
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    accept: '*/*',
  },
})

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = authUtils.getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle 401 Unauthorized - token expired or invalid
    if (error.response?.status === 401) {
      authUtils.clearToken()
      window.location.href = '/login'
    }

    // Extract error message from response
    const errorMessage =
      error.response?.data?.message || error.message || 'An error occurred'

    return Promise.reject(new Error(errorMessage))
  }
)
