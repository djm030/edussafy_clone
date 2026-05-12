<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Academic Rules</p>
        <h1>학사규정</h1>
        <p>출결, 평가, 캠퍼스 생활과 관련된 주요 규정을 확인합니다.</p>
      </div>
      <p v-if="isLoading" class="dashboard-state">학사규정을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>
      <section class="rule-list" aria-label="Academic rules">
        <details v-for="rule in ruleCategories" :key="rule.id" class="rule-item" open>
          <summary>{{ rule.title }}</summary>
          <p>{{ rule.body }}</p>
        </details>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { helpTabs } from '../../constants/navigation'
import { ruleCategories as mockRuleCategories } from '../../data/boards'
import { loadHelpRules } from '../../services/boardService'

const ruleCategories = ref(isApiEnabled ? [] : mockRuleCategories)
const isLoading = ref(true)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    ruleCategories.value = await loadHelpRules()
  } catch (error) {
    if (isApiEnabled) {
      ruleCategories.value = []
      loadError.value = getApiErrorMessage(error, '학사규정을 불러오지 못했습니다.')
    } else {
      ruleCategories.value = mockRuleCategories
      loadError.value = '학사규정을 불러오지 못해 데모 데이터를 표시합니다.'
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
