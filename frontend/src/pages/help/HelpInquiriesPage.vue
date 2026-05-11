<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <div class="board-page-title board-page-title-row">
        <div>
          <p class="eyebrow-text">Inquiry</p>
          <h1>1:1 문의</h1>
          <p>개인 문의 현황과 답변 상태를 확인합니다.</p>
        </div>
        <RouterLink class="button-primary" to="/help/inquiries/write">문의하기</RouterLink>
      </div>
      <SearchFilterBar :options="['전체', '접수', '처리중', '답변완료']" placeholder="문의 제목을 검색하세요." />
      <BoardTable :columns="columns" :items="inquiries" detail-base="/help/inquiries" />
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
import { inquiries } from '../../data/boards'

const columns = [
  { key: 'category', label: '구분', width: '90px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'status', label: '처리상태', width: '120px' },
  { key: 'date', label: '등록일', width: '130px' }
]
</script>
