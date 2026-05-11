<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Write</p>
        <h1>열린 게시판 글쓰기</h1>
        <p>게시글 제목, 분류, 내용을 입력하는 기본 작성 화면입니다.</p>
      </div>

      <p v-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <form @submit.prevent="submitPost">
        <FormTable v-model="form" :disabled="isSubmitting" :fields="fields" />
        <div class="form-actions">
          <RouterLink class="outline-button blue" to="/community/boards/open">목록</RouterLink>
          <button class="button-primary" :disabled="isSubmitting" type="submit">등록</button>
        </div>
      </form>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { communityTabs } from '../../constants/navigation'
import { createCommunityPost } from '../../services/boardService'
import { getAccessToken, getApiErrorMessage, isApiEnabled } from '../../api/client'

const router = useRouter()

const fields = [
  { label: '분류', name: 'category', type: 'select', options: ['일반'] },
  { label: '제목', name: 'title', placeholder: '제목을 입력하세요.' },
  { label: '내용', name: 'content', type: 'textarea', placeholder: '내용을 입력하세요.', rows: 10 },
  { label: '첨부파일', name: 'file', type: 'file' }
]

const form = ref({ category: fields[0].options[0], title: '', content: '', file: null })
const isSubmitting = ref(false)
const message = ref('')
const messageTone = ref('')

async function submitPost() {
  message.value = ''
  if (!form.value.title || !form.value.content) {
    message.value = '제목과 내용을 입력하세요.'
    messageTone.value = 'warning'
    return
  }

  if (isApiEnabled && !getAccessToken()) {
    message.value = '로그인 후 게시글을 등록할 수 있습니다.'
    messageTone.value = 'warning'
    return
  }

  isSubmitting.value = true
  try {
    const created = await createCommunityPost(form.value)
    form.value = { category: fields[0].options[0], title: '', content: '', file: null }
    const createdId = created?.id || created?.postId
    if (createdId) {
      router.push(`/community/boards/open/${createdId}`)
    } else {
      router.push('/community/boards/open')
    }
  } catch (error) {
    message.value = getApiErrorMessage(error, '게시글을 등록하지 못했습니다.')
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}
</script>
