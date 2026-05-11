<template>
  <section class="error-page page-container">
    <h1>{{ code }}</h1>
    <p>{{ message }}</p>
    <RouterLink class="button-primary" to="/dashboard">Go to dashboard</RouterLink>
  </section>
</template>

<script setup>
defineProps({
  code: {
    type: String,
    required: true
  },
  message: {
    type: String,
    required: true
  }
})
</script>
