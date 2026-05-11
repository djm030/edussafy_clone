<template>
  <nav class="section-tabs" :aria-label="ariaLabel">
    <RouterLink
      v-for="item in items"
      :key="item.path"
      :to="item.path"
      :class="['section-tab', { active: route.path === item.path }]"
    >
      {{ item.label }}
    </RouterLink>
  </nav>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

defineProps({
  items: {
    type: Array,
    required: true
  },
  ariaLabel: {
    type: String,
    default: 'Section navigation'
  }
})
</script>
