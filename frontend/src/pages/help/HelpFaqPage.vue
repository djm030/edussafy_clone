<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">FAQ</p>
        <h1>FAQ</h1>
        <p>자주 묻는 질문을 유형별로 확인합니다.</p>
      </div>
      <SearchFilterBar v-model="searchQuery" v-model:filter="searchFilter" :options="filterOptions" placeholder="FAQ를 검색하세요." />
      <p v-if="isLoading" class="dashboard-state">FAQ를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>
      <section class="faq-list" aria-label="FAQ list">
        <details v-for="faq in filteredFaqs" :key="faq.id" class="faq-item" :open="faq.id === firstFaqId">
          <summary><span>{{ faq.category }}</span>{{ faq.question }}</summary>
          <p>{{ faq.answer }}</p>
        </details>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import { helpTabs } from '../../constants/navigation'
import { faqs as mockFaqs } from '../../data/boards'
import { loadHelpFaqs } from '../../services/boardService'
import { matchesText } from '../../utils/listControls'

const faqs = ref(mockFaqs)
const isLoading = ref(false)
const loadError = ref('')
const searchQuery = ref('')
const searchFilter = ref('전체')
const filterOptions = computed(() => ['전체', ...new Set(faqs.value.map((faq) => faq.category).filter(Boolean))])
const firstFaqId = computed(() => faqs.value[0]?.id)
const filteredFaqs = computed(() => faqs.value.filter((faq) => {
  const matchesCategory = searchFilter.value === '전체' || faq.category === searchFilter.value
  return matchesCategory && matchesText(faq, ['question', 'answer', 'category'], searchQuery.value)
}))
</script>


onMounted(async () => {
  isLoading.value = true
  try {
    faqs.value = await loadHelpFaqs()
  } catch (error) {
    loadError.value = 'FAQ를 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
