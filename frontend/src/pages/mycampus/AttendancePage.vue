<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
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
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { attendanceDays, attendanceSummary, mycampusTabs } from '../../data/mycampus'

const columns = [
  { key: 'date', label: '일자', width: '150px' },
  { key: 'day', label: '요일', width: '80px' },
  { key: 'status', label: '상태', width: '110px' },
  { key: 'checkIn', label: '입실', width: '110px' },
  { key: 'checkOut', label: '퇴실', width: '110px' }
]
</script>
