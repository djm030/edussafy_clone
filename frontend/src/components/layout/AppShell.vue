<template>
  <div class="app-shell">
    <AppHeader />
    <main class="app-main">
      <RouterView />
    </main>
    <FloatingQuickAction />
    <AppFooter />
  </div>
</template>

<script setup>
import AppFooter from './AppFooter.vue'
import AppHeader from './AppHeader.vue'
import FloatingQuickAction from './FloatingQuickAction.vue'
</script>
