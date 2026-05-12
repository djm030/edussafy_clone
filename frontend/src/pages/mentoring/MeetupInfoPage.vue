<template>
  <div>
    <PageHero title="멘토링 게시판" />
    <SectionTabs :items="mentoringTabs" aria-label="Mentoring sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Meetup Info</p>
        <h1>간담회 정보</h1>
        <p>신청한 간담회 일정과 참여 정보를 확인합니다.</p>
      </div>

      <p v-if="isLoading" class="dashboard-state">간담회 신청 내역을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <section v-if="!isLoading && participations.length" class="meetup-grid" aria-label="My meetup applications">
        <article v-for="meetup in participations" :key="meetup.id" class="meetup-card">
          <span :class="['table-status', meetup.statusTone]">{{ meetup.status }}</span>
          <h2>{{ meetup.title }}</h2>
          <dl>
            <div><dt>멘토</dt><dd>{{ meetup.mentor }}</dd></div>
            <div><dt>일정</dt><dd>{{ meetup.date }}</dd></div>
            <div v-if="meetup.location"><dt>접속/장소</dt><dd>{{ meetup.location }}</dd></div>
            <div><dt>상태</dt><dd>{{ meetup.capacity }}</dd></div>
          </dl>
          <div v-if="meetup.linkedPostTitle || meetup.linkedPostContent" class="dashboard-state">
            <strong>{{ meetup.linkedPostTitle || '간담회 안내' }}</strong>
            <p>{{ meetup.linkedPostContent }}</p>
          </div>
        </article>
      </section>
      <EmptyState v-else-if="!isLoading" message="신청한 간담회 정보가 없습니다." />

      <section class="info-panel">
        <h2>안내</h2>
        <p>간담회 신청이 완료되면 이 영역에서 접속 링크, 준비사항, 진행 시간을 확인할 수 있습니다.</p>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import EmptyState from '../../components/ui/EmptyState.vue'
import { mentoringTabs } from '../../constants/navigation'
import { loadMyMeetupParticipations } from '../../services/surveyService'

const participations = ref([])
const isLoading = ref(true)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    participations.value = await loadMyMeetupParticipations()
  } catch (error) {
    loadError.value = '간담회 신청 내역을 불러오지 못했습니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
