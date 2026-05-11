<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Inquiry Write</p>
        <h1>1:1 문의 작성</h1>
        <p>문의 유형과 내용을 남기면 담당자가 확인합니다.</p>
      </div>
      <FormTable :fields="fields" />
      <div class="form-actions">
        <RouterLink class="outline-button blue" to="/help/inquiries">목록</RouterLink>
        <button class="button-primary" type="button">등록</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { helpTabs } from '../../constants/navigation'

const fields = [
  { label: '문의유형', name: 'category', type: 'select', options: ['출결', '시스템', '학사', '기타'] },
  { label: '제목', name: 'title', placeholder: '문의 제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '문의 내용을 입력하세요.', rows: 10 },
  { label: '첨부파일', name: 'file', type: 'file' }
]
</script>
