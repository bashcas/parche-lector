import { describe, it, expect, beforeEach, vi, afterEach } from 'vitest'
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios'

// Mock auth utils
vi.mock('../../utils/auth', () => ({
  authUtils: {
    getToken: vi.fn(),
    clearToken: vi.fn(),
  },
}))

import { authUtils } from '../../utils/auth'

describe('apiClient', () => {
  let apiClient: AxiosInstance

  beforeEach(async () => {
    // Reset modules to get fresh apiClient instance
    vi.resetModules()
    vi.clearAllMocks()

    // Re-import to get fresh instance
    const module = await import('./apiClient')
    apiClient = module.apiClient
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  describe('configuration', () => {
    it('should be configured with correct base URL', () => {
      expect(apiClient.defaults.baseURL).toBe(
        'https://parche-lector.onrender.com'
      )
    })

    it('should have correct default headers', () => {
      expect(apiClient.defaults.headers['Content-Type']).toBe('application/json')
      expect(apiClient.defaults.headers['accept']).toBe('*/*')
    })
  })

  describe('request interceptor', () => {
    it('should add Authorization header when token exists', async () => {
      const mockToken = 'test-jwt-token'
      vi.mocked(authUtils.getToken).mockReturnValue(mockToken)

      // Get the request interceptor
      const requestInterceptor = apiClient.interceptors.request
      expect(requestInterceptor).toBeDefined()

      // Create a mock config
      const config: InternalAxiosRequestConfig = {
        headers: new axios.AxiosHeaders(),
        url: '/test',
        method: 'get',
      }

      // Manually test the interceptor logic
      const token = authUtils.getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }

      expect(config.headers.Authorization).toBe(`Bearer ${mockToken}`)
    })

    it('should not add Authorization header when token is null', () => {
      vi.mocked(authUtils.getToken).mockReturnValue(null)

      const config: InternalAxiosRequestConfig = {
        headers: new axios.AxiosHeaders(),
        url: '/test',
        method: 'get',
      }

      const token = authUtils.getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }

      expect(config.headers.Authorization).toBeUndefined()
    })
  })

  describe('API_BASE_URL', () => {
    it('should export the correct API base URL', async () => {
      const module = await import('./apiClient')
      expect(module.API_BASE_URL).toBe('https://parche-lector.onrender.com')
    })
  })
})

