<script setup>
import { onMounted, ref } from 'vue'
import { getHealth } from '../api/client'

const health = ref(null)
const error = ref('')

onMounted(async () => {
  try {
    health.value = await getHealth()
  } catch (err) {
    error.value = 'Backend health check is not available yet.'
  }
})
</script>

<template>
  <main class="home-page">
    <section class="ready-card">
      <p class="eyebrow">SSAFY EDU Clone</p>
      <h1>Frontend shell ready</h1>
      <p class="description">
        Vue 프론트엔드 기본 구조가 준비되었습니다. 이후 DESIGN.md, FRAME.md, 스크린샷 기준으로 페이지를 구현합니다.
      </p>
      <div class="status-box">
        <strong>API health</strong>
        <pre v-if="health">{{ health }}</pre>
        <span v-else>{{ error || 'Checking...' }}</span>
      </div>
    </section>
  </main>
</template>
