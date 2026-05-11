<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <div class="board-page-title board-page-title-row">
        <div>
          <p class="eyebrow-text">Documents</p>
          <h1>서류제출</h1>
          <p>제출한 증빙 서류와 처리 상태를 확인합니다.</p>
        </div>
        <RouterLink class="button-primary" to="/mycampus/documents/write">서류 제출</RouterLink>
      </div>
      <BoardTable :columns="columns" :items="documents" />
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import { documents, mycampusTabs } from '../../data/mycampus'

const columns = [
  { key: 'category', label: '구분', width: '110px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'status', label: '상태', width: '120px' },
  { key: 'date', label: '제출일', width: '130px' }
]
</script>
