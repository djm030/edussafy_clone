<script setup>
defineProps({
  status: {
    type: String,
    required: true
  }
});
</script>

<template>
  <span class="status-circle" :class="status === '예정' ? 'scheduled' : 'done'">
    {{ status }}
  </span>
</template>
