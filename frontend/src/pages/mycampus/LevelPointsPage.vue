<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <section class="level-dashboard">
        <article class="level-card primary">
          <p class="eyebrow-text">My Level</p>
          <h1>{{ pointSummary.level }}</h1>
          <p>{{ pointSummary.rank }}</p>
        </article>
        <article class="level-card">
          <span>총 포인트</span>
          <strong>{{ pointSummary.totalPoints }}</strong>
          <small>다음 레벨까지 {{ pointSummary.nextLevel - pointSummary.totalPoints }}P</small>
        </article>
        <article class="level-card">
          <span>장학포인트</span>
          <strong>{{ pointSummary.scholarshipPoints }}</strong>
          <small>최근 30일 기준</small>
        </article>
      </section>

      <section class="dashboard-card">
        <div class="dashboard-card-header">
          <h2>포인트 내역</h2>
          <RouterLink class="card-action" to="/mycampus/education-status">교육현황 보기</RouterLink>
        </div>
        <BoardTable :columns="columns" :items="pointHistory" />
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { mycampusTabs, pointHistory, pointSummary } from '../../data/mycampus'

const columns = [
  { key: 'title', label: '내용', className: 'title-cell' },
  { key: 'type', label: '구분', width: '140px' },
  { key: 'amount', label: '포인트', width: '120px' },
  { key: 'date', label: '일자', width: '130px' }
]
</script>
