<template>
  <main class="login-page">
    <section class="login-panel" aria-label="Login form">
      <div class="login-brand">
        <span class="brand-mark">SS</span>
        <div>
          <strong>SSAFY EDU</strong>
          <p>SAMSUNG SW·AI ACADEMY FOR YOUTH</p>
        </div>
      </div>

      <form class="login-form" @submit.prevent="login">
        <h1>로그인</h1>
        <p>{{ helperText }}</p>
        <label>
          <span>이메일</span>
          <input v-model="email" type="email" autocomplete="username" />
        </label>
        <label>
          <span>비밀번호</span>
          <input v-model="password" type="password" autocomplete="current-password" />
        </label>
        <p v-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>
        <button class="button-primary" :disabled="isSubmitting" type="submit">{{ isSubmitting ? '로그인 중' : '로그인' }}</button>
      </form>
    </section>
  </main>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getApiErrorMessage, isApiEnabled, setAuthTokens } from '../../api/client'
import { authApi } from '../../api/modules'

const router = useRouter()
const route = useRoute()
const email = ref(isApiEnabled ? '' : 'student@ssafy.com')
const password = ref(isApiEnabled ? '' : '0000')
const isSubmitting = ref(false)
const message = ref(route.query.reason === 'session-expired'
  ? '세션이 만료되었습니다. 다시 로그인해 주세요.'
  : route.query.reason === 'session-required'
    ? '로그인이 필요한 페이지입니다.'
    : '')
const messageTone = ref('')
const helperText = computed(() => isApiEnabled
  ? '백엔드 계정으로 로그인합니다.'
  : '로컬 미리보기에서는 데모 토큰으로 입장합니다.'
)

async function login() {
  message.value = ''
  messageTone.value = ''

  if (!email.value || !password.value) {
    message.value = '이메일과 비밀번호를 입력하세요.'
    messageTone.value = 'warning'
    return
  }

  isSubmitting.value = true
  try {
    if (isApiEnabled) {
      await authApi.login({ email: email.value, password: password.value })
      router.push(typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard')
      return
    }

    setAuthTokens({ accessToken: `demo-token:${email.value}:${password.value.length}` })
    router.push('/dashboard')
  } catch (error) {
    message.value = getApiErrorMessage(error, '로그인에 실패했습니다.')
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}
</script>
