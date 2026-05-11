import axios from 'axios'

const ACCESS_TOKEN_KEY = 'edussafy.accessToken'

export const isApiEnabled = import.meta.env.VITE_USE_API === 'true'

export function getAccessToken() {
  return window.localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function setAccessToken(token) {
  window.localStorage.setItem(ACCESS_TOKEN_KEY, token)
}

export function clearAccessToken() {
  window.localStorage.removeItem(ACCESS_TOKEN_KEY)
}

export const healthClient = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export const apiClient = axios.create({
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
