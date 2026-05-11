<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <EmptyState :message="message" />
      <section class="empty-guide">
        <h1>{{ title }}</h1>
        <p>{{ description }}</p>
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import EmptyState from '../../components/ui/EmptyState.vue'
import { mycampusTabs } from '../../data/mycampus'

defineProps({
  title: {
    type: String,
    required: true
  },
  message: {
    type: String,
    required: true
  },
  description: {
    type: String,
    required: true
  }
})
</script>
