<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <p v-if="isLoading" class="dashboard-state">문의 목록을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title board-page-title-row">
        <div>
          <p class="eyebrow-text">Inquiry</p>
          <h1>1:1 문의</h1>
          <p>개인 문의 현황과 답변 상태를 확인합니다.</p>
        </div>
        <RouterLink class="button-primary" to="/help/inquiries/write">문의하기</RouterLink>
      </div>
      <SearchFilterBar v-model="searchQuery" v-model:filter="searchFilter" :options="['전체', '접수', '처리중', '답변완료']" placeholder="문의 제목을 검색하세요." @submit="currentPage = 1" />
      <BoardTable :columns="columns" :items="pagedInquiries" detail-base="/help/inquiries" />
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
import { inquiries as mockInquiries } from '../../data/boards'
import { loadInquiries } from '../../services/userListService'
import { matchesText, pageNumbers, paginateItems } from '../../utils/listControls'

const inquiries = ref(mockInquiries)
const isLoading = ref(false)
const loadError = ref('')
const searchQuery = ref('')
const searchFilter = ref('전체')
const currentPage = ref(1)
const pageSize = 10
const filteredInquiries = computed(() => inquiries.value.filter((inquiry) => {
  const matchesStatus = searchFilter.value === '전체' || inquiry.status === searchFilter.value
  return matchesStatus && matchesText(inquiry, ['title', 'category', 'status'], searchQuery.value)
}))
const pages = computed(() => pageNumbers(filteredInquiries.value, pageSize))
const pagedInquiries = computed(() => paginateItems(filteredInquiries.value, currentPage.value, pageSize))

const columns = [
  { key: 'category', label: '구분', width: '90px' },
  { key: 'title', label: '제목', className: 'title-cell' },
  { key: 'status', label: '처리상태', width: '120px' },
  { key: 'date', label: '등록일', width: '130px' }
]

onMounted(async () => {
  isLoading.value = true
  try {
    inquiries.value = await loadInquiries()
  } catch (error) {
    loadError.value = '문의 목록을 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})

watch([searchQuery, searchFilter], () => {
  currentPage.value = 1
})
</script>
