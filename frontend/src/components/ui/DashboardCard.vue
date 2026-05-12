<template>
  <article class="dashboard-card">
    <header v-if="title || actionLabel" class="dashboard-card-header">
      <h2 v-if="title">{{ title }}</h2>
      <RouterLink v-if="actionTo && actionLabel" :to="actionTo" class="card-action">{{ actionLabel }}</RouterLink>
      <span v-else-if="actionLabel" class="card-action">{{ actionLabel }}</span>
    </header>
    <slot />
  </article>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: ''
  },
  actionLabel: {
    type: String,
    default: ''
  },
  actionTo: {
    type: String,
    default: ''
  }
})
</script>
