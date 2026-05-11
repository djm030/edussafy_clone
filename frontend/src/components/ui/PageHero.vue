<template>
  <section class="page-hero">
    <div class="page-hero-overlay">
      <h1>{{ title }}</h1>
    </div>
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
