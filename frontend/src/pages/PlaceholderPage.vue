<template>
  <section class="placeholder-page page-container">
    <p class="eyebrow-text">SSAFY EDU Clone</p>
    <h1>{{ title }}</h1>
    <p>This route is reserved in the canonical FRAME.md router skeleton and will be implemented in a later phase.</p>
  </section>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  }
})
</script>
