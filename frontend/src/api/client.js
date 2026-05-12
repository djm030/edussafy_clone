import axios from 'axios'

const ACCESS_TOKEN_KEY = 'edussafy.accessToken'
const REFRESH_TOKEN_KEY = 'edussafy.refreshToken'

export const isApiEnabled = import.meta.env.VITE_USE_API === 'true'

export function getAccessToken() {
  return window.localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function getRefreshToken() {
  return window.localStorage.getItem(REFRESH_TOKEN_KEY)
}

export function hasAccessToken() {
  return Boolean(getAccessToken())
}

export function setAccessToken(token) {
  window.localStorage.setItem(ACCESS_TOKEN_KEY, token)
}

export function setRefreshToken(token) {
  window.localStorage.setItem(REFRESH_TOKEN_KEY, token)
}

export function setAuthTokens({ accessToken, refreshToken }) {
  if (accessToken) setAccessToken(accessToken)
  if (refreshToken) setRefreshToken(refreshToken)
}

export function clearAccessToken() {
  window.localStorage.removeItem(ACCESS_TOKEN_KEY)
}

export function clearAuthTokens() {
  window.localStorage.removeItem(ACCESS_TOKEN_KEY)
  window.localStorage.removeItem(REFRESH_TOKEN_KEY)
}

function redirectToLogin() {
  if (window.location.pathname === '/login') return
  const redirect = encodeURIComponent(`${window.location.pathname}${window.location.search}`)
  window.location.assign(`/login?reason=session-expired&redirect=${redirect}`)
}

export const healthClient = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export const apiClient = axios.create({
  baseURL: '/api/v1',
  timeout: 10000
})

const refreshClient = axios.create({
  baseURL: '/api/v1',
  timeout: 10000
})

apiClient.interceptors.request.use((config) => {
  const token = getAccessToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error?.config
    const authUrl = originalRequest?.url || ''
    const canRefresh = !authUrl.includes('/auth/login') && !authUrl.includes('/auth/refresh')
    if (error?.response?.status === 401 && originalRequest && !originalRequest._retry && canRefresh) {
      const refreshToken = getRefreshToken()
      if (refreshToken) {
        originalRequest._retry = true
        try {
          const response = await refreshClient.post('/auth/refresh', { refreshToken })
          const data = unwrapApiResponse(response)
          setAuthTokens(data)
          originalRequest.headers = originalRequest.headers || {}
          originalRequest.headers.Authorization = `Bearer ${data.accessToken}`
          return apiClient(originalRequest)
        } catch (refreshError) {
          clearAuthTokens()
          redirectToLogin()
          return Promise.reject(refreshError)
        }
      }
      clearAuthTokens()
      redirectToLogin()
    }
    return Promise.reject(error)
  }
)

export function getApiErrorStatus(error) {
  return error?.response?.status || null
}

export function getApiErrorCode(error) {
  return error?.response?.data?.errorCode || error?.response?.data?.code || null
}

export function getApiErrorMessage(error, fallback = '요청 처리 중 오류가 발생했습니다.') {
  if (getApiErrorStatus(error) === 401) {
    return '세션이 만료되었습니다. 다시 로그인해 주세요.'
  }
  return error?.response?.data?.message || getApiErrorCode(error) || error?.message || fallback
}

export function isApiAuthError(error) {
  const status = getApiErrorStatus(error)
  return status === 401 || status === 403
}

export function unwrapApiResponse(response) {
  if (response.data && Object.prototype.hasOwnProperty.call(response.data, 'success')) {
    if (!response.data.success) {
      throw new Error(response.data.message || response.data.errorCode || 'Request failed')
    }
    return response.data.data
  }
  return response.data
}

export async function getHealth() {
  const response = await healthClient.get('/health')
  return response.data
}
