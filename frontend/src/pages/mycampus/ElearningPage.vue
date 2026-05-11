<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />

    <main class="page-container mycampus-page">
      <div class="board-page-title">
        <p class="eyebrow-text">E-Learning</p>
        <h1>학습중 이러닝</h1>
        <p>진행 중인 이러닝과 학습 상태를 확인합니다.</p>
      </div>

      <p v-if="isLoading" class="dashboard-state">이러닝 학습 목록을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="list-toolbar">
        <strong>총 <span>{{ eLearningItems.length }}</span>건</strong>
        <select aria-label="Sort"><option>최근 학습순</option></select>
      </div>

      <div v-if="eLearningItems.length" class="resource-results">
        <RouterLink v-for="item in eLearningItems" :key="item.id" class="resource-result-item" :to="`/classroom/resources/${item.id}`">
          <div class="resource-image"><span>{{ item.label }}</span><small>{{ item.status }}</small></div>
          <div class="resource-body">
            <h2>{{ item.title }} <span>{{ item.progress }}</span></h2>
            <p>카테고리 <strong>{{ item.breadcrumb.join(' > ') }}</strong></p>
            <p>{{ item.description }}</p>
            <ul>
              <li>DATE {{ item.date }}</li>
              <li>VIEW {{ item.views }}</li>
              <li>BOOKMARK {{ item.bookmarks }}</li>
            </ul>
          </div>
        </RouterLink>
      </div>
      <EmptyState v-else message="학습중인 이러닝이 없습니다." />
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import EmptyState from '../../components/ui/EmptyState.vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { eLearningItems as mockElearningItems, mycampusTabs } from '../../data/mycampus'
import { loadElearningData } from '../../services/mycampusService'

const eLearningItems = ref(mockElearningItems)
const isLoading = ref(false)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    eLearningItems.value = await loadElearningData()
  } catch (error) {
    loadError.value = '이러닝 학습 목록을 불러오지 못해 예시 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>