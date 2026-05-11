<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { learningResources } from '../../data/classroom'

const route = useRoute()
const resource = computed(() => learningResources.find((item) => String(item.id) === route.params.id) || learningResources[0])
</script>

<template>
  <PageHero title="학습자료" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <article class="detail-panel">
      <div class="detail-header">
        <div>
          <p class="eyebrow-text">{{ resource.breadcrumb.join(' > ') }}</p>
          <h1>{{ resource.title }}</h1>
          <p>{{ resource.description }}</p>
        </div>
      </div>
      <div class="resource-detail-preview">{{ resource.label }}</div>
      <RouterLink class="button-primary" to="/classroom/resources">목록</RouterLink>
    </article>
  </section>
</template>
