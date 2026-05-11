<template>
  <PageHero title="Quest/평가" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">Quest/평가 목록을 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <div class="quest-evaluation-list">
      <RouterLink v-for="item in questItems" :key="item.id" class="quest-evaluation-card" :to="`/classroom/quests/${item.id}`">
        <strong class="quest-type">{{ item.type }}</strong>
        <span :class="['status-circle', item.status === '예정' ? 'pending' : 'done']">{{ item.status }}</span>
        <div class="quest-main">
          <h2><span>{{ item.scope }}</span> {{ item.title }}</h2>
          <p>{{ item.period }}</p>
        </div>
        <div class="reward-info">
          <small>획득 가능 경험치</small>
          <strong>{{ item.reward }} P</strong>
        </div>
        <div class="result-info">
          <span v-if="item.pass" :class="['result-badge', item.pass.toLowerCase()]">{{ item.pass }}</span>
          <strong v-if="item.score !== null">{{ item.score }}점</strong>
          <p>{{ item.result }}</p>
        </div>
      </RouterLink>
    </div>

    <div class="pagination-row" aria-label="Pagination">
      <button type="button" disabled>‹‹</button>
      <button type="button" disabled>‹</button>
      <button class="active" type="button">1</button>
      <button type="button">2</button>
      <button type="button">3</button>
      <button type="button">›</button>
      <button type="button">››</button>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { questItems as mockQuestItems } from '../../data/classroom'
import { loadQuestItems } from '../../services/classroomService'

const questItems = ref(mockQuestItems)
const isLoading = ref(false)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    questItems.value = await loadQuestItems()
  } catch (error) {
    loadError.value = 'Quest/평가 목록을 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
