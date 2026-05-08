<script setup>
defineProps({
  navigation: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <nav class="primary-nav" aria-label="주요 메뉴">
    <a
      v-for="item in navigation"
      :key="item.label"
      :href="item.href"
      :aria-current="item.active ? 'page' : undefined"
    >
      {{ item.label }}
    </a>
  </nav>
</template>
