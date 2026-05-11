<template>
  <span :class="['status-badge', `status-badge-${tone}`]"><slot /></span>
</template>

<script setup>
defineProps({
  tone: {
    type: String,
    default: 'blue'
  }
})
</script>
