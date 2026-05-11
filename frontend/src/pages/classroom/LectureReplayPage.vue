<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { allReplayItems, replayGroups } from '../../data/classroom'

defineProps({
  variant: {
    type: String,
    default: 'my'
  }
})
</script>

<template>
  <PageHero :title="variant === 'all' ? '전체강의 다시보기' : '내강의 다시보기'" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
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
