<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page narrow-page">
      <p v-if="isLoading" class="dashboard-state">공지사항을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <article v-else class="article-detail">
        <p class="eyebrow-text">Notice Detail</p>
        <h1>{{ notice.title }}</h1>
        <dl class="article-meta">
          <div><dt>구분</dt><dd>{{ notice.category }}</dd></div>
          <div><dt>작성자</dt><dd>{{ notice.author }}</dd></div>
          <div><dt>등록일</dt><dd>{{ notice.date }}</dd></div>
          <div><dt>조회</dt><dd>{{ notice.views }}</dd></div>
        </dl>
        <div class="article-body">
          <p>{{ notice.body }}</p>
        </div>
        <RouterLink class="outline-button blue" to="/help/notice">목록</RouterLink>
      </article>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { helpTabs } from '../../constants/navigation'
import { notices } from '../../data/boards'
import { loadHelpNoticeDetail } from '../../services/boardService'

const route = useRoute()
const notice = ref({})
const isLoading = ref(false)
const loadError = ref('')

function fallbackNotice() {
  const fallback = notices.find((item) => String(item.id) === String(route.params.id)) || notices[0]
  return {
    ...fallback,
    body: '해당 공지는 교육 일정, 출결, 시스템 사용과 관련된 주요 안내를 담고 있습니다. 세부 내용은 캠퍼스 운영 기준과 EDU 시스템 알림을 함께 확인해 주세요.'
  }
}

async function loadNotice() {
  isLoading.value = true
  loadError.value = ''
  try {
    notice.value = await loadHelpNoticeDetail(route.params.id)
  } catch (error) {
    notice.value = fallbackNotice()
    loadError.value = '공지 상세를 불러오지 못해 임시 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

onMounted(loadNotice)
watch(() => route.params.id, loadNotice)
</script>
