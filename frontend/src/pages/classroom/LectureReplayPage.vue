<script setup>
import { ref, watch } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { allReplayItems as mockAllReplayItems, replayGroups } from '../../data/classroom'
import { loadReplayItems } from '../../services/classroomService'

const props = defineProps({
  variant: {
    type: String,
    default: 'my'
  }
})

const allReplayItems = ref(mockAllReplayItems)
const isLoading = ref(false)
const loadError = ref('')

watch(
  () => props.variant,
  async (variant) => {
    if (variant !== 'all') return

    isLoading.value = true
    loadError.value = ''
    try {
      allReplayItems.value = await loadReplayItems()
    } catch (error) {
      loadError.value = '강의 다시보기 목록을 불러오지 못해 데모 데이터를 표시합니다.'
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
      <article v-for="item in allReplayItems" :key="item.id" class="replay-item">
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
