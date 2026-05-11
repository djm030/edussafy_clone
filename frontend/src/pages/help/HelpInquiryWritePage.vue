<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Inquiry Write</p>
        <h1>1:1 문의 작성</h1>
        <p>문의 유형과 내용을 남기면 담당자가 확인합니다.</p>
      </div>

      <p v-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <form @submit.prevent="submitInquiry">
        <FormTable v-model="form" :disabled="isSubmitting" :fields="fields" />
        <div class="form-actions">
          <RouterLink class="outline-button blue" to="/help/inquiries">목록</RouterLink>
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
import { helpTabs } from '../../constants/navigation'
import { createInquiry } from '../../services/userListService'

const fields = [
  { label: '문의유형', name: 'category', type: 'select', options: ['출결', '시스템', '학사', '기타'] },
  { label: '제목', name: 'title', placeholder: '문의 제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '문의 내용을 입력하세요.', rows: 10 },
  { label: '첨부파일', name: 'file', type: 'file' }
]

const form = ref({ category: fields[0].options[0], title: '', content: '', file: null })
const isSubmitting = ref(false)
const message = ref('')
const messageTone = ref('')

async function submitInquiry() {
  message.value = ''
  if (!form.value.title || !form.value.content) {
    message.value = '제목과 내용을 입력하세요.'
    messageTone.value = 'warning'
    return
  }

  isSubmitting.value = true
  try {
    await createInquiry(form.value)
    form.value = { category: fields[0].options[0], title: '', content: '', file: null }
    message.value = '문의가 등록되었습니다.'
    messageTone.value = ''
  } catch (error) {
    message.value = '문의를 등록하지 못했습니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}
</script>