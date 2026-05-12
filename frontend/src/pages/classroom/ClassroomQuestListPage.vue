<template>
  <PageHero title="Quest/평가" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">Quest/평가 목록을 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <div v-if="!isLoading && visibleQuestItems.length" class="quest-evaluation-list">
      <RouterLink v-for="item in visibleQuestItems" :key="item.id" class="quest-evaluation-card" :to="`/classroom/quests/${item.id}`">
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
    <p v-else-if="!isLoading" class="dashboard-state">표시할 Quest/평가 항목이 없습니다.</p>

    <PaginationBar v-if="!isLoading" v-model:active="currentPage" :pages="pages" />
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import PaginationBar from '../../components/ui/PaginationBar.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { questItems as mockQuestItems } from '../../data/classroom'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { loadQuestItems } from '../../services/classroomService'
import { pageNumbers, paginateItems } from '../../utils/listControls'

const questItems = ref(isApiEnabled ? [] : mockQuestItems)
const isLoading = ref(true)
const loadError = ref('')
const currentPage = ref(1)
const pageSize = 5
const pages = computed(() => pageNumbers(questItems.value.length, pageSize))
const visibleQuestItems = computed(() => paginateItems(questItems.value, currentPage.value, pageSize))

watch(questItems, () => {
  currentPage.value = 1
})

onMounted(async () => {
  isLoading.value = true
  try {
    questItems.value = await loadQuestItems()
  } catch (error) {
    if (isApiEnabled) {
      questItems.value = []
      loadError.value = getApiErrorMessage(error, 'Quest/평가 목록을 불러오지 못했습니다.')
    } else {
      questItems.value = mockQuestItems
      loadError.value = 'Quest/평가 목록을 불러오지 못해 데모 데이터를 표시합니다.'
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
