<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <p v-if="isLoading" class="dashboard-state">교육현황 정보를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Education Status</p>
        <h1>교육현황</h1>
        <p>출석, 평가, 과제, 활동 현황을 요약합니다.</p>
      </div>
      <section class="education-grid">
        <article v-for="item in educationStatus" :key="item.id" class="education-card">
          <span>{{ item.title }}</span>
          <strong>{{ item.value }}</strong>
          <p>{{ item.description }}</p>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { educationStatus as mockEducationStatus, mycampusTabs } from '../../data/mycampus'
import { loadEducationStatusData } from '../../services/mycampusService'

const educationStatus = ref(mockEducationStatus)
const isLoading = ref(false)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    educationStatus.value = await loadEducationStatusData()
  } catch (error) {
    loadError.value = '교육현황 데이터를 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
