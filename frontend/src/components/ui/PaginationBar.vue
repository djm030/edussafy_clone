<template>
  <nav class="pagination-bar" aria-label="Pagination">
    <button type="button">‹</button>
    <button v-for="page in pages" :key="page" type="button" :class="{ active: page === active }">
      {{ page }}
    </button>
    <button type="button">›</button>
  </nav>
</template>

<script setup>
defineProps({
  pages: {
    type: Array,
    default: () => [1, 2, 3, 4, 5]
  },
  active: {
    type: Number,
    default: 1
  }
})
</script>
