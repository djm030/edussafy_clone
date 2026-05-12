<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { learningResources } from '../../data/classroom'
import {
  bookmarkLearningResource,
  completeLearningResource,
  downloadLearningResource,
  likeLearningResource,
  loadLearningResourceDetail
} from '../../services/classroomService'

const route = useRoute()
const resource = ref(isApiEnabled ? null : learningResources[0])
const isLoading = ref(true)
const isSubmitting = ref(false)
const loadError = ref('')
const message = ref('')
const breadcrumb = computed(() => resource.value?.breadcrumb || [])

async function loadResource() {
  isLoading.value = true
  loadError.value = ''
  try {
    resource.value = await loadLearningResourceDetail(route.params.id)
    message.value = ''
  } catch (error) {
    if (isApiEnabled) {
      resource.value = null
      loadError.value = getApiErrorMessage(error, '학습자료 상세를 불러오지 못했습니다.')
    } else {
      resource.value = learningResources.find((item) => String(item.id) === String(route.params.id)) || learningResources[0]
      loadError.value = '학습자료 상세를 불러오지 못해 데모 데이터를 표시합니다.'
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

async function runResourceAction(action, successMessage) {
  isSubmitting.value = true
  loadError.value = ''
  try {
    const result = await action(route.params.id)
    if (result?.likeCount !== undefined) resource.value.likes = result.likeCount
    if (result?.downloadCount !== undefined) resource.value.downloads = result.downloadCount
    if (successMessage === '찜했습니다.') resource.value.bookmarks = (resource.value.bookmarks || 0) + 1
    message.value = successMessage
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '학습자료 액션을 처리하지 못했습니다.')
    console.warn(error)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(loadResource)
watch(() => route.params.id, loadResource)
</script>

<template>
  <PageHero title="학습자료" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">학습자료 상세를 확인하고 있습니다.</p>
    <p v-if="loadError" class="dashboard-state warning">{{ loadError }}</p>
    <p v-else-if="message" class="dashboard-state">{{ message }}</p>

    <p v-if="!isLoading && !resource" class="dashboard-state">표시할 학습자료가 없습니다.</p>

    <article v-if="!isLoading && resource" class="detail-panel">
      <div class="detail-header">
        <div>
          <p class="eyebrow-text">{{ breadcrumb.join(' > ') }}</p>
          <h1>{{ resource.title }}</h1>
          <p>{{ resource.description }}</p>
        </div>
      </div>
      <div class="resource-detail-preview">{{ resource.label }}</div>
      <dl class="article-meta">
        <div><dt>조회</dt><dd>{{ resource.views }}</dd></div>
        <div><dt>좋아요</dt><dd>{{ resource.likes }}</dd></div>
        <div><dt>찜</dt><dd>{{ resource.bookmarks }}</dd></div>
        <div><dt>다운로드</dt><dd>{{ resource.downloads }}</dd></div>
      </dl>
      <div class="form-actions">
        <button class="button-primary" :disabled="isSubmitting" type="button" @click="runResourceAction(likeLearningResource, '좋아요를 반영했습니다.')">좋아요</button>
        <button class="button-primary" :disabled="isSubmitting" type="button" @click="runResourceAction(bookmarkLearningResource, '찜했습니다.')">찜하기</button>
        <button class="button-primary" :disabled="isSubmitting" type="button" @click="runResourceAction(downloadLearningResource, '다운로드 기록을 반영했습니다.')">다운로드</button>
        <button class="button-primary" :disabled="isSubmitting" type="button" @click="runResourceAction(completeLearningResource, '학습 완료 처리되었습니다.')">완료 처리</button>
        <RouterLink class="button-primary" to="/classroom/resources">목록</RouterLink>
      </div>
    </article>
  </section>
</template>
