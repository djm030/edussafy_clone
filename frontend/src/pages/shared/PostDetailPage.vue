<template>
  <div>
    <PageHero :title="heroTitle" />
    <SectionTabs :items="tabs" aria-label="Section tabs" />
    <main class="page-container board-page narrow-page">
      <p v-if="isLoading" class="dashboard-state">게시글을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <article v-else class="article-detail">
        <p class="eyebrow-text">Detail</p>
        <h1>{{ post.title }}</h1>
        <dl class="article-meta">
          <div><dt>구분</dt><dd>{{ post.category }}</dd></div>
          <div><dt>작성자</dt><dd>{{ post.author }}</dd></div>
          <div><dt>등록일</dt><dd>{{ post.date }}</dd></div>
          <div><dt>조회</dt><dd>{{ post.views }}</dd></div>
          <div><dt>좋아요</dt><dd>{{ post.likes || 0 }}</dd></div>
        </dl>
        <div class="article-body">
          <p>{{ post.body }}</p>
        </div>
        <div class="form-actions">
          <button class="button-primary" :disabled="isSubmitting" type="button" @click="handleLike">좋아요</button>
          <RouterLink class="outline-button blue" :to="backPath">목록</RouterLink>
        </div>

        <section class="comment-panel">
          <h2>댓글</h2>
          <form class="comment-form" @submit.prevent="handleCommentSubmit">
            <textarea v-model="commentBody" placeholder="댓글을 입력하세요" rows="3"></textarea>
            <button class="button-primary" :disabled="isSubmitting || !commentBody.trim()" type="submit">등록</button>
          </form>
          <ul class="comment-list">
            <li v-for="comment in comments" :key="comment.id">
              <div>
                <strong>{{ comment.author }}</strong>
                <small>{{ comment.date }}</small>
                <p>{{ comment.body }}</p>
              </div>
              <button v-if="comment.isMine" class="outline-button green" :disabled="isSubmitting" type="button" @click="handleCommentDelete(comment)">삭제</button>
            </li>
          </ul>
        </section>
      </article>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { communityTabs, mentoringTabs } from '../../constants/navigation'
import { anonymousPosts, openBoardPosts, mentoringPosts } from '../../data/boards'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import {
  createBoardComment,
  deleteBoardComment,
  likeBoardPost,
  loadBoardComments,
  loadCommunityPostDetail,
  loadMentoringPostDetail
} from '../../services/boardService'

const props = defineProps({
  source: {
    type: String,
    required: true
  }
})

const route = useRoute()
const post = ref({})
const comments = ref([])
const commentBody = ref('')
const isLoading = ref(true)
const isSubmitting = ref(false)
const loadError = ref('')

const sourceMap = {
  open: { hero: '커뮤니티', tabs: communityTabs, items: openBoardPosts, back: '/community/boards/open', group: 'community', variant: 'open' },
  anonymous: { hero: '커뮤니티', tabs: communityTabs, items: anonymousPosts, back: '/community/boards/anonymous', group: 'community', variant: 'anonymous' },
  stories: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.stories, back: '/mentoring/stories', group: 'mentoring', variant: 'stories' },
  qna: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.qna, back: '/mentoring/qna', group: 'mentoring', variant: 'qna' },
  notice: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.notice, back: '/mentoring/notice', group: 'mentoring', variant: 'notice' },
  reviews: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.reviews, back: '/mentoring/meetups/reviews', group: 'mentoring', variant: 'reviews' }
}

const current = computed(() => sourceMap[props.source] || sourceMap.open)
const heroTitle = computed(() => current.value.hero)
const tabs = computed(() => current.value.tabs)
const backPath = computed(() => current.value.back)

function fallbackPost() {
  const targetPostId = String(route.params.id)
  return current.value.items.find((item) => String(item.id) === targetPostId || String(item.postId) === targetPostId) || current.value.items[0]
}

async function loadPost() {
  isLoading.value = true
  loadError.value = ''
  try {
    post.value = current.value.group === 'mentoring'
      ? await loadMentoringPostDetail(current.value.variant, route.params.id)
      : await loadCommunityPostDetail(current.value.variant, route.params.id)
    comments.value = await loadBoardComments(current.value.group, current.value.variant, route.params.id)
  } catch (error) {
    if (isApiEnabled) {
      post.value = {}
      loadError.value = 'Post detail could not be loaded from the API.'
      console.warn(error)
      return
    }
    const fallback = fallbackPost()
    post.value = {
      ...fallback,
      body: `${fallback.title}에 대한 상세 내용입니다. 잠시 후 다시 시도해 주세요.`
    }
    comments.value = []
    loadError.value = '게시글 상세를 불러오지 못해 임시 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

async function runPostAction(action) {
  isSubmitting.value = true
  try {
    await action()
    await loadPost()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '게시글 액션을 처리하지 못했습니다.')
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}

function handleLike() {
  runPostAction(async () => {
    const result = await likeBoardPost(current.value.group, current.value.variant, route.params.id)
    if (result?.likeCount !== undefined) post.value.likes = result.likeCount
  })
}

function handleCommentSubmit() {
  const content = commentBody.value.trim()
  if (!content) return
  runPostAction(async () => {
    await createBoardComment(current.value.group, current.value.variant, route.params.id, content)
    commentBody.value = ''
  })
}

function handleCommentDelete(comment) {
  runPostAction(() => deleteBoardComment(comment.id))
}

onMounted(loadPost)
watch(() => [props.source, route.params.id], loadPost)
</script>
