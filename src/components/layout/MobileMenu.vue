<script setup>
defineProps({
  navigation: {
    type: Array,
    required: true
  },
  serviceLinks: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <details class="mobile-menu">
    <summary>메뉴</summary>
    <div class="mobile-panel">
      <a v-for="item in navigation" :key="item.label" :href="item.href">
        {{ item.label }}
      </a>
      <a v-for="link in serviceLinks" :key="link.variant" :href="link.href">
        {{ link.label.replace("\n", " ") }}
      </a>
    </div>
  </details>
</template>
