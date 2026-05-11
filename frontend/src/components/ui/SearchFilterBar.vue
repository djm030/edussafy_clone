<template>
  <form class="search-filter-bar" @submit.prevent>
    <select :aria-label="selectLabel">
      <option v-for="option in options" :key="option" :value="option">{{ option }}</option>
    </select>
    <label>
      <span class="sr-only">{{ placeholder }}</span>
      <input :placeholder="placeholder" type="search" />
    </label>
    <button type="submit">검색</button>
  </form>
</template>

<script setup>
defineProps({
  options: {
    type: Array,
    default: () => ['전체']
  },
  placeholder: {
    type: String,
    default: '검색어를 입력하세요.'
  },
  selectLabel: {
    type: String,
    default: 'Search category'
  }
})
</script>
