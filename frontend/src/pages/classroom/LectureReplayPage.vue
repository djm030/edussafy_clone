<script setup>
import { computed, ref, watch } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { useRoute } from 'vue-router'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { classroomTabs } from '../../constants/navigation'
import { allReplayItems as mockAllReplayItems, replayGroups } from '../../data/classroom'
import { loadReplayItems } from '../../services/classroomService'

const props = defineProps({
  variant: {
    type: String,
    default: 'my'
  }
})

const route = useRoute()
const allReplayItems = ref(isApiEnabled ? [] : mockAllReplayItems)
const isLoading = ref(false)
const loadError = ref('')
const selectedSessionId = computed(() => typeof route.query.sessionId === 'string' ? route.query.sessionId : '')
const visibleReplayItems = computed(() => {
  if (!selectedSessionId.value) return allReplayItems.value
  const exact = allReplayItems.value.filter((item) => String(item.id) === selectedSessionId.value)
  return exact.length ? exact : allReplayItems.value
})

watch(
  () => props.variant,
  async (variant) => {
    if (variant !== 'all') return

    isLoading.value = true
    loadError.value = ''
    try {
      allReplayItems.value = await loadReplayItems()
    } catch (error) {
      if (isApiEnabled) {
        allReplayItems.value = []
        loadError.value = getApiErrorMessage(error, '강의 다시보기 목록을 불러오지 못했습니다.')
      } else {
        allReplayItems.value = mockAllReplayItems
        loadError.value = '강의 다시보기 목록을 불러오지 못해 데모 데이터를 표시합니다.'
      }
      console.warn(error)
    } finally {
      isLoading.value = false
    }
  },
  { immediate: true }
)
</script>

<template>
  <PageHero :title="variant === 'all' ? '전체강의 다시보기' : '내강의 다시보기'" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">강의 다시보기 목록을 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <div v-if="variant === 'my'" class="replay-banner">Java 전공 다시보기</div>

    <p v-if="selectedSessionId" class="dashboard-state">선택한 세션 {{ selectedSessionId }} 기준으로 다시보기를 확인합니다.</p>

    <div class="replay-toolbar">
      <button type="button">최신강의 보러가기</button>
      <button type="button">⌄ 모두 펼치기</button>
    </div>

    <div v-if="variant === 'my'" class="replay-accordion">
      <button v-for="title in replayGroups" :key="title" type="button">
        <span>{{ title }}</span>
        <strong>⌄</strong>
      </button>
    </div>

    <div v-else class="replay-list">
      <article v-for="item in visibleReplayItems" :key="item.id" class="replay-item">
        <div class="replay-thumb">PLAY</div>
        <div>
          <h2>{{ item.title }}</h2>
          <p>{{ item.meta }}</p>
          <small>{{ item.period }}</small>
        </div>
        <button class="outline-button blue" type="button">보기</button>
      </article>
    </div>

    <p v-if="variant === 'my'" class="replay-note">※ 트랙별 라이브 강의 진행시 순차적으로 다시보기가 업로드될 예정입니다.</p>
  </section>
</template>
