<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Notice</p>
        <h1>공지사항</h1>
        <p>교육 운영과 시스템 공지를 확인합니다.</p>
      </div>
      <SearchFilterBar :options="['전체', '공통', '학사', '시스템']" placeholder="공지 제목을 검색하세요." />
      <BoardTable :columns="columns" :items="notices" detail-base="/help/notice" />
      <PaginationBar />
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import PaginationBar from '../../components/ui/PaginationBar.vue'
import { helpTabs } from '../../constants/navigation'
import { notices } from '../../data/boards'

const columns = [
  { key: 'category', label: '구분', width: '90px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'author', label: '작성자', width: '120px' },
  { key: 'date', label: '등록일', width: '130px' },
  { key: 'views', label: '조회', width: '80px' }
]
</script>
