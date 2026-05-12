<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />

    <main class="page-container mycampus-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Bookmarks</p>
        <h1>찜한 목록</h1>
        <p>찜한 학습 콘텐츠를 모아 확인합니다.</p>
      </div>

      <p v-if="isLoading" class="dashboard-state">찜한 학습자료를 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="list-toolbar">
        <strong>총 <span>{{ bookmarkedItems.length }}</span>건</strong>
        <select aria-label="Sort"><option>최근 찜한순</option></select>
      </div>

      <div v-if="!isLoading && bookmarkedItems.length" class="resource-results">
        <RouterLink v-for="item in bookmarkedItems" :key="item.id" class="resource-result-item" :to="`/classroom/resources/${item.id}`">
          <div class="resource-image"><span>{{ item.label }}</span><small>{{ item.status }}</small></div>
          <div class="resource-body">
            <h2>{{ item.title }} <span v-if="item.textbook">교재</span></h2>
            <p>카테고리 <strong>{{ item.breadcrumb.join(' > ') }}</strong></p>
            <p>{{ item.description }}</p>
            <ul>
              <li>DATE {{ item.date }}</li>
              <li>VIEW {{ item.views }}</li>
              <li>BOOKMARK {{ item.bookmarks }}</li>
            </ul>
          </div>
        </RouterLink>
      </div>
      <EmptyState v-else-if="!isLoading" message="찜한 학습자료가 없습니다." />
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import EmptyState from '../../components/ui/EmptyState.vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { bookmarkedLearningItems as mockBookmarkedItems, mycampusTabs } from '../../data/mycampus'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { loadBookmarkData } from '../../services/mycampusService'

const bookmarkedItems = ref(isApiEnabled ? [] : mockBookmarkedItems)
const isLoading = ref(true)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    bookmarkedItems.value = await loadBookmarkData()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '찜한 학습자료를 불러오지 못했습니다.')
    if (isApiEnabled) bookmarkedItems.value = []
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>