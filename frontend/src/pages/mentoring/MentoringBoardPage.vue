<template>
  <div>
    <PageHero title="멘토링 게시판" />
    <SectionTabs :items="mentoringTabs" aria-label="Mentoring sections" />
    <main class="page-container board-page">
      <div class="board-page-title board-page-title-row">
        <div>
          <p class="eyebrow-text">Mentoring</p>
          <h1>{{ pageTitle }}</h1>
          <p>{{ description }}</p>
        </div>
        <RouterLink v-if="variant === 'reviews'" class="button-primary" to="/mentoring/meetups/reviews/write">후기 작성</RouterLink>
      </div>
      <SearchFilterBar :options="['전체', '제목', '작성자', '내용']" placeholder="검색어를 입력하세요." />
      <BoardTable :columns="columns" :items="items" :detail-base="detailBase" />
      <PaginationBar />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import PaginationBar from '../../components/ui/PaginationBar.vue'
import { mentoringTabs } from '../../constants/navigation'
import { mentoringPosts } from '../../data/boards'

const props = defineProps({
  variant: {
    type: String,
    default: 'stories'
  }
})

const meta = {
  stories: { title: '멘토 스토리', description: '선배와 현직자의 성장 경험을 확인합니다.', base: '/mentoring/stories' },
  qna: { title: '멘토링', description: '멘토에게 질문하고 답변을 확인합니다.', base: '/mentoring/qna' },
  notice: { title: '멘토링 공지사항', description: '멘토링 운영 공지를 확인합니다.', base: '/mentoring/notice' },
  reviews: { title: '간담회 후기', description: '참여자가 남긴 간담회 후기를 확인합니다.', base: '/mentoring/meetups/reviews' }
}

const columns = [
  { key: 'category', label: '구분', width: '90px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'author', label: '작성자', width: '120px' },
  { key: 'date', label: '등록일', width: '130px' },
  { key: 'views', label: '조회', width: '80px' }
]

const currentMeta = computed(() => meta[props.variant] || meta.stories)
const pageTitle = computed(() => currentMeta.value.title)
const description = computed(() => currentMeta.value.description)
const detailBase = computed(() => currentMeta.value.base)
const items = computed(() => mentoringPosts[props.variant] || mentoringPosts.stories)
</script>
