<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Document Write</p>
        <h1>서류제출 작성</h1>
        <p>증빙 서류 유형과 내용을 입력합니다.</p>
      </div>
      <FormTable :fields="fields" />
      <div class="form-actions">
        <RouterLink class="outline-button blue" to="/mycampus/documents">목록</RouterLink>
        <button class="button-primary" type="button">제출</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { mycampusTabs } from '../../data/mycampus'

const fields = [
  { label: '서류유형', name: 'category', type: 'select', options: ['증빙', '서류', '기타'] },
  { label: '제목', name: 'title', placeholder: '제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '내용을 입력하세요.', rows: 8 },
  { label: '첨부파일', name: 'file', type: 'file' }
]
</script>
