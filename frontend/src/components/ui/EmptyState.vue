<template>
  <div class="empty-state">
    <p>{{ message }}</p>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    default: 'No content is available.'
  }
})
</script>
