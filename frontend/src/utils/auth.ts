const TOKEN_KEY = 'authToken'

export const authUtils = {
  getToken(): string | null {
    try {
      return localStorage.getItem(TOKEN_KEY)
    } catch (e) {
      return null
    }
  },

  setToken(token: string): void {
    try {
      localStorage.setItem(TOKEN_KEY, token)
    } catch (e) {
      console.error('Error saving token to localStorage:', e)
    }
  },

  removeToken(): void {
    try {
      localStorage.removeItem(TOKEN_KEY)
    } catch (e) {
      console.error('Error removing token from localStorage:', e)
    }
  },

  clearToken(): void {
    this.removeToken()
  },

  isAuthenticated(): boolean {
    return !!this.getToken()
  },
}

