<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import StatusBadge from '../../components/ui/StatusBadge.vue'
import { getAccessToken, getApiErrorMessage, isApiEnabled } from '../../api/client'
import { classroomTabs } from '../../constants/navigation'
import { questItems } from '../../data/classroom'
import { loadQuestDetail, submitQuestAnswer } from '../../services/classroomService'

const route = useRoute()
const quest = ref(isApiEnabled ? null : questItems[0])
const answer = ref('')
const isLoading = ref(true)
const isSubmitting = ref(false)
const loadError = ref('')
const submitMessage = ref('')

async function loadQuest() {
  isLoading.value = true
  loadError.value = ''
  submitMessage.value = ''
  try {
    quest.value = await loadQuestDetail(route.params.id)
    answer.value = quest.value.answer || ''
  } catch (error) {
    if (isApiEnabled) {
      quest.value = null
      answer.value = ''
      loadError.value = getApiErrorMessage(error, 'Quest/평가 상세를 불러오지 못했습니다.')
    } else {
      quest.value = questItems.find((item) => String(item.id) === String(route.params.id)) || questItems[0]
      answer.value = quest.value.answer || ''
      loadError.value = 'Quest/평가 상세를 불러오지 못해 데모 데이터를 표시합니다.'
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

async function submitAnswer() {
  if (!answer.value.trim()) {
    submitMessage.value = '제출할 답안을 입력하세요.'
    return
  }
  if (isApiEnabled && !getAccessToken()) {
    submitMessage.value = '로그인이 필요합니다. /login에서 계정으로 먼저 로그인하세요.'
    return
  }

  isSubmitting.value = true
  submitMessage.value = ''
  try {
    const result = await submitQuestAnswer(route.params.id, answer.value.trim())
    quest.value = { ...quest.value, ...result }
    submitMessage.value = `제출이 완료되었습니다.${result.submittedAt ? ` (${result.submittedAt})` : ''}`
  } catch (error) {
    submitMessage.value = getApiErrorMessage(error, '제출에 실패했습니다. 로그인 상태와 네트워크를 확인하세요.')
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadQuest)
watch(() => route.params.id, loadQuest)
</script>

<template>
  <PageHero title="Quest/평가" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">Quest/평가 상세를 확인하고 있습니다.</p>
    <p v-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <article v-if="!isLoading && quest" class="detail-panel">
      <div class="detail-header">
        <div>
          <p class="eyebrow-text">{{ quest.type }}</p>
          <h1>{{ quest.title }}</h1>
          <p>{{ quest.period }}</p>
        </div>
        <StatusBadge :tone="quest.status === '예정' ? 'blue' : 'slate'">{{ quest.status }}</StatusBadge>
      </div>

      <dl class="detail-grid">
        <div>
          <dt>구분</dt>
          <dd>{{ quest.scope }}</dd>
        </div>
        <div>
          <dt>획득 가능 경험치</dt>
          <dd>{{ quest.reward }} P</dd>
        </div>
        <div>
          <dt>결과</dt>
          <dd>{{ quest.result }}</dd>
        </div>
        <div>
          <dt>점수</dt>
          <dd>{{ quest.score === null ? '-' : `${quest.score}점` }}</dd>
        </div>
      </dl>

      <div class="submission-box">
        <h2>제출 현황</h2>
        <p>
          {{ quest.submittedAt ? `${quest.submittedAt} 제출` : '아직 제출 내역이 없습니다.' }}
          <span v-if="quest.pass"> · {{ quest.pass }}</span>
        </p>
        <form class="submission-form" @submit.prevent="submitAnswer">
          <label>
            <span>답안/메모</span>
            <textarea v-model="answer" rows="5" placeholder="제출할 답안을 입력하세요."></textarea>
          </label>
          <p v-if="submitMessage" class="form-message">{{ submitMessage }}</p>
          <div class="form-actions">
            <button class="button-primary" :disabled="isSubmitting" type="submit">{{ isSubmitting ? '제출 중' : '제출' }}</button>
            <RouterLink class="button-primary" to="/classroom/quests">목록</RouterLink>
          </div>
        </form>
      </div>
    </article>
  </section>
</template>
