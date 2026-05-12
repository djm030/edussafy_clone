<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <p v-if="isLoading" class="dashboard-state">출석 정보를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <template v-if="!isLoading && !loadError">
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

      <section class="dashboard-card attendance-calendar-card">
        <div class="dashboard-card-header"><h2>월간 출결 달력</h2></div>
        <div class="attendance-calendar-weekdays" aria-hidden="true">
          <span v-for="day in calendarWeekdays" :key="day">{{ day }}</span>
        </div>
        <div class="attendance-calendar-grid">
          <div
            v-for="cell in calendarCells"
            :key="cell.key"
            :class="['attendance-calendar-cell', { 'is-muted': !cell.inMonth, 'has-attendance': cell.status !== '-' }]"
          >
            <span class="calendar-day-number">{{ cell.dayNumber }}</span>
            <strong v-if="cell.status !== '-'" :class="['table-status', cell.statusTone]">{{ cell.status }}</strong>
            <small v-if="cell.checkIn !== '-'">입실 {{ cell.checkIn }}</small>
            <small v-if="cell.checkOut !== '-'">퇴실 {{ cell.checkOut }}</small>
          </div>
        </div>
      </section>

      <section class="dashboard-card">
        <div class="dashboard-card-header"><h2>이번 주 출석현황</h2></div>
        <BoardTable :columns="columns" :items="attendanceDays" />
      </section>
      </template>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { attendanceDays as mockAttendanceDays, attendanceSummary as mockAttendanceSummary, mycampusTabs } from '../../data/mycampus'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { checkInAttendanceData, checkOutAttendanceData, loadAttendanceData } from '../../services/mycampusService'

const emptyAttendanceSummary = mockAttendanceSummary.map((item) => ({ ...item, value: 0 }))
const attendanceSummary = ref(isApiEnabled ? emptyAttendanceSummary : mockAttendanceSummary)
const attendanceDays = ref(isApiEnabled ? [] : mockAttendanceDays)
const attendanceCalendarDays = ref(isApiEnabled ? [] : mockAttendanceDays)
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
const isLoading = ref(true)
const isSubmitting = ref(false)
const loadError = ref('')

const columns = [
  { key: 'date', label: '일자', width: '150px' },
  { key: 'day', label: '요일', width: '80px' },
  { key: 'status', label: '상태', width: '110px' },
  { key: 'checkIn', label: '입실', width: '110px' },
  { key: 'checkOut', label: '퇴실', width: '110px' }
]
const calendarWeekdays = ['일', '월', '화', '수', '목', '금', '토']
const calendarCells = computed(() => buildCalendarCells(attendanceCalendarDays.value))

onMounted(async () => {
  await refreshAttendance()
})

async function refreshAttendance() {
  isLoading.value = true
  try {
    const data = await loadAttendanceData()
    attendanceSummary.value = data.attendanceSummary
    attendanceDays.value = data.attendanceDays
    attendanceCalendarDays.value = data.attendanceCalendarDays || data.attendanceDays
    todayAttendance.value = data.todayAttendance
    loadError.value = ''
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '출석 데이터를 불러오지 못했습니다.')
    if (isApiEnabled) {
      attendanceSummary.value = emptyAttendanceSummary
      attendanceDays.value = []
      attendanceCalendarDays.value = []
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

function parseAttendanceDate(value) {
  const normalized = String(value || '').replaceAll('.', '-')
  const date = new Date(`${normalized}T00:00:00`)
  return Number.isNaN(date.getTime()) ? null : date
}

function buildCalendarCells(days) {
  const parsedDays = days
    .map((item) => ({ ...item, parsedDate: parseAttendanceDate(item.date) }))
    .filter((item) => item.parsedDate)
  const baseDate = parsedDays[0]?.parsedDate || new Date()
  const year = baseDate.getFullYear()
  const month = baseDate.getMonth()
  const firstDate = new Date(year, month, 1)
  const lastDate = new Date(year, month + 1, 0)
  const dayMap = new Map(parsedDays.map((item) => [item.parsedDate.getDate(), item]))
  const cells = []

  for (let i = 0; i < firstDate.getDay(); i += 1) {
    cells.push({ key: `blank-${i}`, dayNumber: '', inMonth: false, status: '-', checkIn: '-', checkOut: '-', statusTone: 'slate' })
  }

  for (let day = 1; day <= lastDate.getDate(); day += 1) {
    const item = dayMap.get(day)
    cells.push({
      key: `${year}-${month + 1}-${day}`,
      dayNumber: day,
      inMonth: true,
      status: item?.status || '-',
      statusTone: item?.statusTone || 'slate',
      checkIn: item?.checkIn || '-',
      checkOut: item?.checkOut || '-'
    })
  }

  while (cells.length % 7 !== 0) {
    cells.push({ key: `tail-${cells.length}`, dayNumber: '', inMonth: false, status: '-', checkIn: '-', checkOut: '-', statusTone: 'slate' })
  }

  return cells
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
