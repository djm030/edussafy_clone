<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page narrow-page">
      <p v-if="isLoading" class="dashboard-state">문의 내용을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <article v-else class="article-detail">
        <p class="eyebrow-text">Inquiry Detail</p>
        <h1>{{ inquiry.title }}</h1>
        <dl class="article-meta">
          <div><dt>구분</dt><dd>{{ inquiry.category }}</dd></div>
          <div><dt>처리상태</dt><dd><span :class="['table-status', inquiry.statusTone]">{{ inquiry.status }}</span></dd></div>
          <div><dt>등록일</dt><dd>{{ inquiry.date }}</dd></div>
        </dl>
        <div class="article-body">
          <p>{{ inquiry.content }}</p>
        </div>
        <section v-if="inquiry.answerContent" class="answer-panel">
          <p class="eyebrow-text">Answer</p>
          <h2>답변</h2>
          <dl class="article-meta">
            <div v-if="inquiry.answeredByName"><dt>답변자</dt><dd>{{ inquiry.answeredByName }}</dd></div>
            <div v-if="inquiry.answeredAt"><dt>답변일</dt><dd>{{ inquiry.answeredAt }}</dd></div>
          </dl>
          <div class="article-body"><p>{{ inquiry.answerContent }}</p></div>
        </section>
        <RouterLink class="outline-button blue" to="/help/inquiries">목록</RouterLink>
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
import { loadInquiryDetail } from '../../services/userListService'

const route = useRoute()
const inquiry = ref({})
const isLoading = ref(true)
const loadError = ref('')

async function loadInquiry() {
  isLoading.value = true
  loadError.value = ''
  try {
    inquiry.value = await loadInquiryDetail(route.params.id)
  } catch (error) {
    loadError.value = '문의 상세를 불러오지 못했습니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

onMounted(loadInquiry)
watch(() => route.params.id, loadInquiry)
</script>
