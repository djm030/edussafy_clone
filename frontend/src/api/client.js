import axios from 'axios'

export const healthClient = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export const apiClient = axios.create({
  baseURL: '/api/v1',
  timeout: 10000
})

apiClient.interceptors.request.use((config) => {
  const token = window.localStorage.getItem('edussafy.accessToken')
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
