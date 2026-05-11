<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
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
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { mycampusTabs, pledges } from '../../data/mycampus'

const columns = [
  { key: 'title', label: '서약서', className: 'title-cell' },
  { key: 'status', label: '상태', width: '140px' },
  { key: 'date', label: '서명일', width: '130px' }
]
</script>
