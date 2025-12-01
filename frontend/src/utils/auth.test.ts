import { describe, it, expect, beforeEach, vi } from 'vitest'
import { authUtils } from './auth'

describe('authUtils', () => {
  // Mock localStorage
  const localStorageMock = {
    getItem: vi.fn(),
    setItem: vi.fn(),
    removeItem: vi.fn(),
    clear: vi.fn(),
    length: 0,
    key: vi.fn(),
  }

  beforeEach(() => {
    // Reset mocks before each test
    vi.clearAllMocks()
    Object.defineProperty(window, 'localStorage', {
      value: localStorageMock,
      writable: true,
    })
  })

  describe('getToken', () => {
    it('should return token from localStorage when it exists', () => {
      const mockToken = 'test-jwt-token-123'
      localStorageMock.getItem.mockReturnValue(mockToken)

      const result = authUtils.getToken()

      expect(localStorageMock.getItem).toHaveBeenCalledWith('authToken')
      expect(result).toBe(mockToken)
    })

    it('should return null when token does not exist', () => {
      localStorageMock.getItem.mockReturnValue(null)

      const result = authUtils.getToken()

      expect(result).toBeNull()
    })

    it('should return null when localStorage throws an error', () => {
      localStorageMock.getItem.mockImplementation(() => {
        throw new Error('Storage error')
      })

      const result = authUtils.getToken()

      expect(result).toBeNull()
    })
  })

  describe('setToken', () => {
    it('should save token to localStorage', () => {
      const token = 'new-token-456'

      authUtils.setToken(token)

      expect(localStorageMock.setItem).toHaveBeenCalledWith('authToken', token)
    })

    it('should handle localStorage errors gracefully', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
      localStorageMock.setItem.mockImplementation(() => {
        throw new Error('Storage error')
      })

      // Should not throw
      expect(() => authUtils.setToken('token')).not.toThrow()
      expect(consoleSpy).toHaveBeenCalled()

      consoleSpy.mockRestore()
    })
  })

  describe('removeToken', () => {
    it('should remove token from localStorage', () => {
      authUtils.removeToken()

      expect(localStorageMock.removeItem).toHaveBeenCalledWith('authToken')
    })

    it('should handle localStorage errors gracefully', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
      localStorageMock.removeItem.mockImplementation(() => {
        throw new Error('Storage error')
      })

      // Should not throw
      expect(() => authUtils.removeToken()).not.toThrow()
      expect(consoleSpy).toHaveBeenCalled()

      consoleSpy.mockRestore()
    })
  })

  describe('clearToken', () => {
    it('should call removeToken', () => {
      authUtils.clearToken()

      expect(localStorageMock.removeItem).toHaveBeenCalledWith('authToken')
    })
  })

  describe('isAuthenticated', () => {
    it('should return true when token exists', () => {
      localStorageMock.getItem.mockReturnValue('valid-token')

      const result = authUtils.isAuthenticated()

      expect(result).toBe(true)
    })

    it('should return false when token is null', () => {
      localStorageMock.getItem.mockReturnValue(null)

      const result = authUtils.isAuthenticated()

      expect(result).toBe(false)
    })

    it('should return false when token is empty string', () => {
      localStorageMock.getItem.mockReturnValue('')

      const result = authUtils.isAuthenticated()

      expect(result).toBe(false)
    })
  })
})

