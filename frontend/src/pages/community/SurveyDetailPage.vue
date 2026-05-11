<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page narrow-page">
      <p v-if="isLoading" class="dashboard-state">설문 정보를 확인하고 있습니다.</p>
      <p v-else-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Survey Detail</p>
        <h1>{{ survey.title }}</h1>
        <p>{{ survey.description }}</p>
      </div>

      <form class="info-panel" @submit.prevent="submitSurvey">
        <div v-if="survey.questions.length" class="form-table">
          <label v-for="question in survey.questions" :key="question.id" class="form-row">
            <span>{{ question.questionNo }}. {{ question.questionText }}</span>
            <textarea
              v-if="isTextQuestion(question)"
              v-model="answers[question.id]"
              :required="question.isRequired"
              rows="5"
              placeholder="답변을 입력하세요."
            ></textarea>
            <select v-else v-model="answers[question.id]" :required="question.isRequired">
              <option value="">선택하세요.</option>
              <option v-for="option in question.options" :key="optionValue(option)" :value="optionValue(option)">{{ optionLabel(option) }}</option>
            </select>
          </label>
        </div>
        <p v-else>별도 문항이 없는 신청/설문입니다. 제출 버튼을 누르면 참여가 접수됩니다.</p>

        <div class="form-actions">
          <RouterLink class="outline-button blue" to="/community/surveys">목록</RouterLink>
          <button class="button-primary" :disabled="isSubmitting" type="submit">제출</button>
        </div>
      </form>
    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { getAccessToken, isApiEnabled } from '../../api/client'
import { communityTabs } from '../../constants/navigation'
import { loadSurveyDetail, submitSurveyAnswers } from '../../services/surveyService'

const route = useRoute()
const survey = ref({ title: '설문조사', description: '', questions: [] })
const answers = reactive({})
const isLoading = ref(false)
const isSubmitting = ref(false)
const message = ref('')
const messageTone = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    survey.value = await loadSurveyDetail(route.params.id)
    survey.value.questions.forEach((question) => {
      answers[question.id] = ''
    })
  } catch (error) {
    message.value = '설문 정보를 불러오지 못했습니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})

function isTextQuestion(question) {
  return String(question.questionType).includes('TEXT') || String(question.questionType).includes('SUBJECTIVE')
}

function optionLabel(option) {
  return typeof option === 'object' ? option.label || option.text || option.value : option
}

function optionValue(option) {
  return typeof option === 'object' ? option.value || option.label || option.text : option
}

async function submitSurvey() {
  isSubmitting.value = true
  message.value = ''
  if (isApiEnabled && !getAccessToken()) {
    message.value = '로그인이 필요합니다. /login에서 계정으로 먼저 로그인하세요.'
    messageTone.value = 'warning'
    isSubmitting.value = false
    return
  }
  try {
    await submitSurveyAnswers(route.params.id, { ...answers })
    message.value = '설문 응답이 제출되었습니다.'
    messageTone.value = ''
  } catch (error) {
    message.value = '설문 응답을 제출하지 못했습니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}
</script>
