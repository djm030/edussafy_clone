<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Write</p>
        <h1>열린 게시판 글쓰기</h1>
        <p>게시글 제목, 분류, 내용을 입력하는 기본 작성 화면입니다.</p>
      </div>
      <FormTable :fields="fields" />
      <div class="form-actions">
        <RouterLink class="outline-button blue" to="/community/boards/open">목록</RouterLink>
        <button class="button-primary" type="button">등록</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { communityTabs } from '../../constants/navigation'

const fields = [
  { label: '분류', name: 'category', type: 'select', options: ['자유', '정보', '질문', '공지'] },
  { label: '제목', name: 'title', placeholder: '제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '내용을 입력하세요.', rows: 10 },
  { label: '첨부파일', name: 'file', type: 'file' }
]
</script>
