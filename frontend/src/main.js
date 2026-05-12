import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './styles/index.css'

function applyVisualAuthTokens() {
  const visualAuthEnabled = import.meta.env.DEV || import.meta.env.VITE_VISUAL_AUTH === 'true'
  if (!visualAuthEnabled || typeof window === 'undefined') return

  const url = new URL(window.location.href)
  const accessToken = url.searchParams.get('visualAccessToken')
  const refreshToken = url.searchParams.get('visualRefreshToken')
  if (!accessToken && !refreshToken) return

  if (accessToken) localStorage.setItem('edussafy.accessToken', accessToken)
  if (refreshToken) localStorage.setItem('edussafy.refreshToken', refreshToken)

  url.searchParams.delete('visualAccessToken')
  url.searchParams.delete('visualRefreshToken')
  window.history.replaceState(window.history.state, '', `${url.pathname}${url.search}${url.hash}`)
}

applyVisualAuthTokens()

createApp(App)
  .use(createPinia())
  .use(router)
  .mount('#app')
