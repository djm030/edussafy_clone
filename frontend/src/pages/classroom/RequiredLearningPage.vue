<template>
  <PageHero title="필수학습" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">필수학습 목록을 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <div class="sub-tabs required-tabs">
      <button class="active" type="button">전체</button>
      <button type="button">IT</button>
      <button type="button">인사·총무·HRD</button>
      <button type="button">외국어</button>
    </div>

    <div class="search-bar">
      <label><span>SEARCH</span><input type="search" placeholder="키워드 검색" /></label>
      <button type="button">검색</button>
    </div>

    <div class="list-toolbar">
      <strong>총 <span>{{ requiredItems.length }}</span>건</strong>
      <select aria-label="Sort"><option>최신등록순</option></select>
    </div>

    <div v-if="requiredItems.length" class="resource-results">
      <RouterLink v-for="item in requiredItems" :key="item.id" class="resource-result-item" :to="`/classroom/resources/${item.id}`">
        <div class="resource-image"><span>{{ item.label }}</span><small>REQ</small></div>
        <div class="resource-body">
          <h2>{{ item.title }}</h2>
          <p>카테고리 <strong>{{ item.breadcrumb.join(' > ') }}</strong></p>
          <p>{{ item.description }}</p>
        </div>
      </RouterLink>
    </div>
    <EmptyState v-else message="이러닝과정을 준비중입니다." />
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import EmptyState from '../../components/ui/EmptyState.vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { loadLearningResources } from '../../services/classroomService'

const requiredItems = ref([])
const isLoading = ref(false)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    requiredItems.value = await loadLearningResources({ required: true })
  } catch (error) {
    loadError.value = '필수학습 목록을 불러오지 못했습니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
