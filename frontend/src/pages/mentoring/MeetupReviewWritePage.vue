<template>
  <div>
    <PageHero title="멘토링 게시판" />
    <SectionTabs :items="mentoringTabs" aria-label="Mentoring sections" />
    <main class="page-container board-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Review Write</p>
        <h1>간담회 후기 작성</h1>
        <p>간담회 참여 후 느낀 점과 배운 내용을 공유합니다.</p>
      </div>

      <p v-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <form @submit.prevent="submitReview">
        <FormTable v-model="form" :disabled="isSubmitting" :fields="fields" />
        <div class="form-actions">
          <RouterLink class="outline-button blue" to="/mentoring/meetups/reviews">목록</RouterLink>
          <button class="button-primary" :disabled="isSubmitting" type="submit">등록</button>
        </div>
      </form>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { getAccessToken, isApiEnabled } from '../../api/client'
import { mentoringTabs } from '../../constants/navigation'
import { createMentoringReviewPost } from '../../services/boardService'

const fields = [
  { label: '간담회', name: 'meetup', type: 'select', options: ['백엔드 개발자 커리어 간담회', '금융권 IT 직무 이해하기', '프로젝트 코드리뷰 실전'] },
  { label: '제목', name: 'title', placeholder: '후기 제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '후기 내용을 입력하세요.', rows: 10 },
  { label: '첨부파일', name: 'file', type: 'file' }
]

const form = ref({ meetup: fields[0].options[0], title: '', content: '', file: null })
const isSubmitting = ref(false)
const message = ref('')
const messageTone = ref('')

async function submitReview() {
  message.value = ''
  if (!form.value.title || !form.value.content) {
    message.value = '제목과 내용을 입력하세요.'
    messageTone.value = 'warning'
    return
  }
  if (isApiEnabled && !getAccessToken()) {
    message.value = '로그인이 필요합니다. /login에서 계정으로 먼저 로그인하세요.'
    messageTone.value = 'warning'
    return
  }

  isSubmitting.value = true
  try {
    await createMentoringReviewPost({
      ...form.value
    })
    form.value = { meetup: fields[0].options[0], title: '', content: '', file: null }
    message.value = '간담회 후기가 등록되었습니다.'
    messageTone.value = ''
  } catch (error) {
    message.value = '간담회 후기를 등록하지 못했습니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}
</script>
