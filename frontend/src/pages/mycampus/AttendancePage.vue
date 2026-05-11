<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <p v-if="isLoading" class="dashboard-state">출석 정보를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <section class="attendance-summary">
        <article v-for="item in attendanceSummary" :key="item.label" class="attendance-card">
          <span :class="['table-status', item.tone]">{{ item.label }}</span>
          <strong>{{ item.value }}</strong>
        </article>
      </section>
      <section class="dashboard-card">
        <div class="dashboard-card-header"><h2>이번 주 출석현황</h2></div>
        <BoardTable :columns="columns" :items="attendanceDays" />
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { attendanceDays as mockAttendanceDays, attendanceSummary as mockAttendanceSummary, mycampusTabs } from '../../data/mycampus'
import { loadAttendanceData } from '../../services/mycampusService'

const attendanceSummary = ref(mockAttendanceSummary)
const attendanceDays = ref(mockAttendanceDays)
const isLoading = ref(false)
const loadError = ref('')

const columns = [
  { key: 'date', label: '일자', width: '150px' },
  { key: 'day', label: '요일', width: '80px' },
  { key: 'status', label: '상태', width: '110px' },
  { key: 'checkIn', label: '입실', width: '110px' },
  { key: 'checkOut', label: '퇴실', width: '110px' }
]

onMounted(async () => {
  isLoading.value = true
  try {
    const data = await loadAttendanceData()
    attendanceSummary.value = data.attendanceSummary
    attendanceDays.value = data.attendanceDays
  } catch (error) {
    loadError.value = '출석 데이터를 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
