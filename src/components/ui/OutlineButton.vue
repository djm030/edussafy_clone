<script setup>
defineProps({
  href: {
    type: String,
    default: "#"
  },
  variant: {
    type: String,
    default: "default"
  }
});
</script>

<template>
  <a class="outline-button" :class="variant" :href="href">
    <slot />
  </a>
</template>
