<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page">
      <div class="board-page-title board-page-title-row">
        <div>
          <p class="eyebrow-text">Board</p>
          <h1>{{ pageTitle }}</h1>
          <p>{{ description }}</p>
        </div>
        <RouterLink v-if="variant === 'open'" class="button-primary" to="/community/boards/open/write">글쓰기</RouterLink>
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
import { communityTabs } from '../../constants/navigation'
import { anonymousPosts, openBoardPosts } from '../../data/boards'

const props = defineProps({
  variant: {
    type: String,
    default: 'open'
  }
})

const columns = [
  { key: 'category', label: '구분', width: '90px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'author', label: '작성자', width: '120px' },
  { key: 'date', label: '등록일', width: '130px' },
  { key: 'views', label: '조회', width: '80px' }
]

const items = computed(() => props.variant === 'anonymous' ? anonymousPosts : openBoardPosts)
const pageTitle = computed(() => props.variant === 'anonymous' ? '익명 게시판' : '열린 게시판')
const description = computed(() => props.variant === 'anonymous' ? '교육생 의견을 부담 없이 공유하는 익명 공간입니다.' : '정보 공유와 질문을 위한 공개 게시판입니다.')
const detailBase = computed(() => props.variant === 'anonymous' ? '/community/boards/anonymous' : '/community/boards/open')
</script>
