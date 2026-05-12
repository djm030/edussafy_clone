<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <p v-if="isLoading" class="dashboard-state">출석 정보를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <section class="attendance-action-panel">
        <div>
          <p class="eyebrow-text">오늘 출석</p>
          <h2>{{ todayAttendance.date }} · {{ todayAttendance.dayLabel }}</h2>
          <p>{{ todayAttendance.message }}</p>
          <dl class="attendance-action-times">
            <div>
              <dt>입실</dt>
              <dd>{{ todayAttendance.checkInAt }}</dd>
            </div>
            <div>
              <dt>퇴실</dt>
              <dd>{{ todayAttendance.checkOutAt }}</dd>
            </div>
          </dl>
        </div>
        <div class="attendance-action-buttons">
          <span :class="['table-status', todayAttendance.statusTone]">{{ todayAttendance.statusLabel }}</span>
          <button class="button-primary" :disabled="isSubmitting || !todayAttendance.canCheckIn" type="button" @click="handleCheckIn">
            출석하기
          </button>
          <button class="button-primary secondary" :disabled="isSubmitting || !todayAttendance.canCheckOut" type="button" @click="handleCheckOut">
            퇴실하기
          </button>
        </div>
      </section>

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
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { checkInAttendanceData, checkOutAttendanceData, loadAttendanceData } from '../../services/mycampusService'

const emptyAttendanceSummary = mockAttendanceSummary.map((item) => ({ ...item, value: 0 }))
const attendanceSummary = ref(isApiEnabled ? emptyAttendanceSummary : mockAttendanceSummary)
const attendanceDays = ref(isApiEnabled ? [] : mockAttendanceDays)
const todayAttendance = ref({
  date: '-',
  dayLabel: '',
  checkInAt: '-',
  checkOutAt: '-',
  statusLabel: '대기',
  statusTone: 'slate',
  canCheckIn: false,
  canCheckOut: false,
  message: '오늘 출석 상태를 확인하고 있습니다.'
})
const isLoading = ref(false)
const isSubmitting = ref(false)
const loadError = ref('')

const columns = [
  { key: 'date', label: '일자', width: '150px' },
  { key: 'day', label: '요일', width: '80px' },
  { key: 'status', label: '상태', width: '110px' },
  { key: 'checkIn', label: '입실', width: '110px' },
  { key: 'checkOut', label: '퇴실', width: '110px' }
]

onMounted(async () => {
  await refreshAttendance()
})

async function refreshAttendance() {
  isLoading.value = true
  try {
    const data = await loadAttendanceData()
    attendanceSummary.value = data.attendanceSummary
    attendanceDays.value = data.attendanceDays
    todayAttendance.value = data.todayAttendance
    loadError.value = ''
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '출석 데이터를 불러오지 못했습니다.')
    if (isApiEnabled) {
      attendanceSummary.value = emptyAttendanceSummary
      attendanceDays.value = []
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

async function runAttendanceAction(action) {
  isSubmitting.value = true
  try {
    todayAttendance.value = await action()
    await refreshAttendance()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '출석 처리에 실패했습니다.')
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}

function handleCheckIn() {
  runAttendanceAction(checkInAttendanceData)
}

function handleCheckOut() {
  runAttendanceAction(checkOutAttendanceData)
}
</script>
