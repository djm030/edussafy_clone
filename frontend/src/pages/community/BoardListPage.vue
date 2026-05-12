<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page">
      <p v-if="isLoading" class="dashboard-state">게시글을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title board-page-title-row">
        <div>
          <p class="eyebrow-text">Board</p>
          <h1>{{ pageTitle }}</h1>
          <p>{{ description }}</p>
        </div>
        <RouterLink v-if="variant === 'open'" class="button-primary" to="/community/boards/open/write">글쓰기</RouterLink>
      </div>

      <SearchFilterBar v-model="searchQuery" v-model:filter="searchFilter" :options="['전체', '제목', '작성자', '내용']" placeholder="검색어를 입력하세요." @submit="currentPage = 1" />
      <BoardTable :columns="columns" :items="pagedItems" :detail-base="detailBase" />
      <PaginationBar v-model:active="currentPage" :pages="pages" />
    </main>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import PaginationBar from '../../components/ui/PaginationBar.vue'
import { communityTabs } from '../../constants/navigation'
import { anonymousPosts, openBoardPosts } from '../../data/boards'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { loadCommunityPosts } from '../../services/boardService'
import { filterByOption, pageNumbers, paginateItems } from '../../utils/listControls'

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

const items = ref(isApiEnabled ? [] : normalizeIds(props.variant === 'anonymous' ? anonymousPosts : openBoardPosts))
const isLoading = ref(true)
const loadError = ref('')
const searchQuery = ref('')
const searchFilter = ref('전체')
const currentPage = ref(1)
const pageSize = 10
const pageTitle = computed(() => props.variant === 'anonymous' ? '익명 게시판' : '열린 게시판')
const description = computed(() => props.variant === 'anonymous' ? '교육생 의견을 부담 없이 공유하는 익명 공간입니다.' : '정보 공유와 질문을 위한 공개 게시판입니다.')
const detailBase = computed(() => props.variant === 'anonymous' ? '/community/boards/anonymous' : '/community/boards/open')
function normalizeIds(rows) {
  return rows.map((item) => ({
    ...item,
    id: String(item.postId ?? item.id ?? ''),
    postId: String(item.postId ?? item.id ?? '')
  }))
}
const filteredItems = computed(() => filterByOption(items.value, searchFilter.value, searchQuery.value, {
  제목: ['title'],
  작성자: ['author'],
  내용: ['title', 'category']
}, ['title', 'author', 'category']))
const pages = computed(() => pageNumbers(filteredItems.value, pageSize))
const pagedItems = computed(() => paginateItems(filteredItems.value, currentPage.value, pageSize))

watch([searchQuery, searchFilter], () => {
  currentPage.value = 1
})

watch(
  () => props.variant,
  async (variant) => {
    isLoading.value = true
    loadError.value = ''
    try {
      items.value = normalizeIds(await loadCommunityPosts(variant))
      currentPage.value = 1
    } catch (error) {
      loadError.value = getApiErrorMessage(error, '게시글을 불러오지 못했습니다.')
      items.value = isApiEnabled ? [] : normalizeIds(variant === 'anonymous' ? anonymousPosts : openBoardPosts)
      console.warn(error)
    } finally {
      isLoading.value = false
    }
  },
  { immediate: true }
)
</script>
