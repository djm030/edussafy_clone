<template>
  <div>
    <PageHero title="멘토링 게시판" />
    <SectionTabs :items="mentoringTabs" aria-label="Mentoring sections" />
    <main class="page-container board-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Review Write</p>
        <h1>간담회 후기 작성</h1>
        <p>간담회 참여 후 느낀 점과 배운 내용을 공유합니다.</p>
      </div>
      <FormTable :fields="fields" />
      <div class="form-actions">
        <RouterLink class="outline-button blue" to="/mentoring/meetups/reviews">목록</RouterLink>
        <button class="button-primary" type="button">등록</button>
      </div>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { mentoringTabs } from '../../constants/navigation'

const fields = [
  { label: '간담회', name: 'meetup', type: 'select', options: ['백엔드 개발자 커리어 간담회', '금융권 IT 직무 이해하기', '프로젝트 코드리뷰 실전'] },
  { label: '제목', name: 'title', placeholder: '후기 제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '후기 내용을 입력하세요.', rows: 10 },
  { label: '첨부파일', name: 'file', type: 'file' }
]
</script>
