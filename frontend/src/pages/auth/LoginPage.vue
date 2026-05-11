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
        <p>교육생 포털 화면 확인을 위한 demo login입니다.</p>
        <label>
          <span>이메일</span>
          <input v-model="email" type="email" autocomplete="username" />
        </label>
        <label>
          <span>비밀번호</span>
          <input v-model="password" type="password" autocomplete="current-password" />
        </label>
        <button class="button-primary" type="submit">로그인</button>
      </form>
    </section>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { isApiEnabled, setAccessToken } from '../../api/client'
import { authApi } from '../../api/modules'

const router = useRouter()
const email = ref('student@ssafy.com')
const password = ref('0000')

async function login() {
  if (isApiEnabled) {
    try {
      await authApi.login({ email: email.value, password: password.value })
      router.push('/dashboard')
      return
    } catch (error) {
      console.warn('Login API fallback:', error)
    }
  }

  setAccessToken(`demo-token:${email.value}:${password.value.length}`)
  router.push('/dashboard')
}
</script>
