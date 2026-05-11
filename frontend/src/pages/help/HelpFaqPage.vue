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
      <SearchFilterBar v-model="searchQuery" v-model:filter="searchFilter" :options="['전체', '출결', '과제', '계정']" placeholder="FAQ를 검색하세요." />
      <section class="faq-list" aria-label="FAQ list">
        <details v-for="faq in filteredFaqs" :key="faq.id" class="faq-item" :open="faq.id === 1">
          <summary><span>{{ faq.category }}</span>{{ faq.question }}</summary>
          <p>{{ faq.answer }}</p>
        </details>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import { helpTabs } from '../../constants/navigation'
import { faqs } from '../../data/boards'
import { matchesText } from '../../utils/listControls'

const searchQuery = ref('')
const searchFilter = ref('전체')
const filteredFaqs = computed(() => faqs.filter((faq) => {
  const matchesCategory = searchFilter.value === '전체' || faq.category === searchFilter.value
  return matchesCategory && matchesText(faq, ['question', 'answer', 'category'], searchQuery.value)
}))
</script>
