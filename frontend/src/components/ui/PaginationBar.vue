<template>
  <nav class="pagination-bar" aria-label="Pagination">
    <button type="button" :disabled="active <= 1" @click="emit('update:active', active - 1)">‹</button>
    <button v-for="page in pages" :key="page" type="button" :class="{ active: page === active }" @click="emit('update:active', page)">
      {{ page }}
    </button>
    <button type="button" :disabled="active >= pages.length" @click="emit('update:active', active + 1)">›</button>
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

const emit = defineEmits(['update:active'])
</script>
