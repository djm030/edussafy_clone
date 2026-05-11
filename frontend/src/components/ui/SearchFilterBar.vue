<template>
  <form class="search-filter-bar" @submit.prevent="emit('submit')">
    <select :aria-label="selectLabel" :value="selectedFilter" @change="emit('update:filter', $event.target.value)">
      <option v-for="option in options" :key="option" :value="option">{{ option }}</option>
    </select>
    <label>
      <span class="sr-only">{{ placeholder }}</span>
      <input :placeholder="placeholder" :value="modelValue" type="search" @input="emit('update:modelValue', $event.target.value)" />
    </label>
    <button type="submit">검색</button>
  </form>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  filter: {
    type: String,
    default: ''
  },
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

const emit = defineEmits(['update:modelValue', 'update:filter', 'submit'])
const selectedFilter = computed(() => props.filter || props.options[0] || '')
</script>
