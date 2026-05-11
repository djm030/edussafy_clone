<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page">
      <p v-if="isLoading" class="dashboard-state">설문 목록을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Survey</p>
        <h1>설문조사</h1>
        <p>교육 운영과 프로젝트 진행에 필요한 의견을 빠르게 확인합니다.</p>
      </div>

      <SearchFilterBar v-model="searchQuery" v-model:filter="searchFilter" :options="['전체', '진행중', '완료', '결과공개']" placeholder="설문명을 입력하세요." />

      <section class="survey-grid" aria-label="Survey list">
        <article v-for="survey in filteredSurveys" :key="survey.id" class="survey-card">
          <span :class="['table-status', survey.statusTone]">{{ survey.status }}</span>
          <h2>{{ survey.title }}</h2>
          <dl>
            <div><dt>대상</dt><dd>{{ survey.target }}</dd></div>
            <div><dt>기간</dt><dd>{{ survey.period }}</dd></div>
          </dl>
          <RouterLink class="button-primary" :to="`/community/surveys/${survey.id}`">참여/결과 보기</RouterLink>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import { communityTabs } from '../../constants/navigation'
import { surveys as mockSurveys } from '../../data/boards'
import { loadSurveys } from '../../services/surveyService'
import { matchesText } from '../../utils/listControls'

const surveys = ref(mockSurveys)
const isLoading = ref(false)
const loadError = ref('')
const searchQuery = ref('')
const searchFilter = ref('전체')
const filteredSurveys = computed(() => surveys.value.filter((survey) => {
  const matchesStatus = searchFilter.value === '전체' || survey.status === searchFilter.value
  return matchesStatus && matchesText(survey, ['title', 'target', 'status'], searchQuery.value)
}))

onMounted(async () => {
  isLoading.value = true
  try {
    surveys.value = await loadSurveys()
  } catch (error) {
    loadError.value = '설문 목록을 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
