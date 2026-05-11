<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <p v-if="isLoading" class="dashboard-state">서약서 정보를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Pledge</p>
        <h1>교육생 서약서</h1>
        <p>교육 과정에 필요한 서약서 서명 상태를 확인합니다.</p>
      </div>
      <BoardTable :columns="columns" :items="pledges" />
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { mycampusTabs, pledges as mockPledges } from '../../data/mycampus'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { loadPledgesData } from '../../services/mycampusService'

const pledges = ref(isApiEnabled ? [] : mockPledges)
const isLoading = ref(false)
const loadError = ref('')

const columns = [
  { key: 'title', label: '서약서', className: 'title-cell' },
  { key: 'status', label: '상태', width: '140px' },
  { key: 'date', label: '서명일', width: '130px' }
]

onMounted(async () => {
  isLoading.value = true
  try {
    pledges.value = await loadPledgesData()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '서약서 데이터를 불러오지 못했습니다.')
    if (isApiEnabled) pledges.value = []
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
