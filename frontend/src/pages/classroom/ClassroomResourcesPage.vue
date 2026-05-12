<template>
  <PageHero title="학습자료" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">학습자료를 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <div class="resource-tabs"><button class="active" type="button">오프러닝</button></div>
    <div class="sub-tabs"><button class="active" type="button">전체</button><button type="button">커리큘럼 +</button></div>

    <div class="search-bar">
      <label><span>SEARCH</span><input v-model="searchQuery" type="search" placeholder="키워드 검색" /></label>
      <button type="button" @click="searchQuery = searchQuery.trim()">검색</button>
    </div>

    <div class="list-toolbar">
      <strong>총 <span>{{ filteredResources.length }}</span>건</strong>
      <div>
        <label class="check-label"><input v-model="textbookOnly" type="checkbox" /> 교재</label>
        <select v-model="selectedCategory" aria-label="Category">
          <option v-for="category in categoryOptions" :key="category">{{ category }}</option>
        </select>
        <select aria-label="Sort"><option>최신등록순</option></select>
      </div>
    </div>

    <div class="resource-results">
      <RouterLink v-for="item in filteredResources" :key="item.id" class="resource-result-item" :to="`/classroom/resources/${item.id}`">
        <div class="resource-image"><span>{{ item.label }}</span><small>BOOK</small></div>
        <div class="resource-body">
          <h2>{{ item.title }} <span v-if="item.textbook">교재</span></h2>
          <p>카테고리 <strong>{{ item.breadcrumb.join(' > ') }}</strong></p>
          <p>{{ item.description }}</p>
          <ul>
            <li>VIEW {{ item.views }}</li>
            <li>LIKE {{ item.likes }}</li>
            <li>BOOKMARK {{ item.bookmarks }}</li>
          </ul>
        </div>
      </RouterLink>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { learningResources as mockLearningResources } from '../../data/classroom'
import { loadLearningResources } from '../../services/classroomService'
import { matchesText } from '../../utils/listControls'

const route = useRoute()
const learningResources = ref(isApiEnabled ? [] : mockLearningResources)
const isLoading = ref(true)
const loadError = ref('')
const searchQuery = ref('')
const textbookOnly = ref(false)
const selectedCategory = ref('전체')

function applyRouteQuery() {
  searchQuery.value = typeof route.query.keyword === 'string' ? route.query.keyword : searchQuery.value
  textbookOnly.value = route.query.type === 'textbook' || textbookOnly.value
  selectedCategory.value = typeof route.query.category === 'string' ? route.query.category : selectedCategory.value
}

const categoryOptions = computed(() => ['전체', ...new Set(learningResources.value.map((item) => item.breadcrumb[0]).filter(Boolean))])
const filteredResources = computed(() => learningResources.value.filter((item) => {
  const matchesCategory = selectedCategory.value === '전체' || item.breadcrumb[0] === selectedCategory.value
  const matchesTextbook = !textbookOnly.value || item.textbook
  return matchesCategory && matchesText(item, ['title', 'description', 'label'], searchQuery.value)
}))

onMounted(async () => {
  applyRouteQuery()
  isLoading.value = true
  try {
    learningResources.value = await loadLearningResources()
  } catch (error) {
    if (isApiEnabled) {
      learningResources.value = []
      loadError.value = getApiErrorMessage(error, '학습자료를 불러오지 못했습니다.')
    } else {
      learningResources.value = mockLearningResources
      loadError.value = '학습자료를 불러오지 못해 데모 데이터를 표시합니다.'
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})

watch(() => route.query, applyRouteQuery)
</script>
