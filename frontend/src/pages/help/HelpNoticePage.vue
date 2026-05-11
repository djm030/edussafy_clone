<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <p v-if="isLoading" class="dashboard-state">공지사항을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Notice</p>
        <h1>공지사항</h1>
        <p>교육 운영과 시스템 공지를 확인합니다.</p>
      </div>
      <SearchFilterBar v-model="searchQuery" v-model:filter="searchFilter" :options="['전체', '공통', '학사', '시스템']" placeholder="공지 제목을 검색하세요." @submit="currentPage = 1" />
      <BoardTable :columns="columns" :items="pagedNotices" detail-base="/help/notice" />
      <PaginationBar v-model:active="currentPage" :pages="pages" />
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import BoardTable from '../../components/ui/BoardTable.vue'
import PaginationBar from '../../components/ui/PaginationBar.vue'
import { helpTabs } from '../../constants/navigation'
import { notices as mockNotices } from '../../data/boards'
import { loadHelpNoticePosts } from '../../services/boardService'
import { matchesText, pageNumbers, paginateItems } from '../../utils/listControls'

const notices = ref(mockNotices)
const isLoading = ref(false)
const loadError = ref('')
const searchQuery = ref('')
const searchFilter = ref('전체')
const currentPage = ref(1)
const pageSize = 10
const filteredNotices = computed(() => notices.value.filter((notice) => {
  const matchesCategory = searchFilter.value === '전체' || notice.category === searchFilter.value
  return matchesCategory && matchesText(notice, ['title', 'category', 'author'], searchQuery.value)
}))
const pages = computed(() => pageNumbers(filteredNotices.value, pageSize))
const pagedNotices = computed(() => paginateItems(filteredNotices.value, currentPage.value, pageSize))

const columns = [
  { key: 'category', label: '구분', width: '90px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'author', label: '작성자', width: '120px' },
  { key: 'date', label: '등록일', width: '130px' },
  { key: 'views', label: '조회', width: '80px' }
]

onMounted(async () => {
  isLoading.value = true
  try {
    notices.value = await loadHelpNoticePosts()
  } catch (error) {
    loadError.value = '공지사항을 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})

watch([searchQuery, searchFilter], () => {
  currentPage.value = 1
})
</script>
