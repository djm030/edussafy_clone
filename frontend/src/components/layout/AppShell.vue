<template>
  <div :class="['app-shell', { 'auth-shell': isAuthLayout }]">
    <AppHeader v-if="!isAuthLayout" />
    <main class="app-main">
      <RouterView />
    </main>
    <FloatingQuickAction v-if="!isAuthLayout" />
    <AppFooter v-if="!isAuthLayout" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppFooter from './AppFooter.vue'
import AppHeader from './AppHeader.vue'
import FloatingQuickAction from './FloatingQuickAction.vue'

const route = useRoute()
const isAuthLayout = computed(() => route.meta.layout === 'auth')
</script>
