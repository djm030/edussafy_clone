<script setup>
defineProps({
  href: {
    type: String,
    default: "#"
  },
  label: {
    type: String,
    required: true
  }
});
</script>

<template>
  <a class="plus-link" :href="href" :aria-label="label">+</a>
</template>
