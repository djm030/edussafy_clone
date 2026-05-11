<template>
  <div>
    <PageHero title="멘토링 게시판" />
    <SectionTabs :items="mentoringTabs" aria-label="Mentoring sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Meetup Apply</p>
        <h1>간담회 신청</h1>
        <p>진행 예정인 멘토 간담회를 확인하고 신청합니다.</p>
      </div>

      <p v-if="isLoading" class="dashboard-state">간담회 신청 목록을 확인하고 있습니다.</p>
      <p v-else-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <section class="meetup-grid" aria-label="Meetup apply list">
        <article v-for="meetup in meetups" :key="meetup.id" class="meetup-card">
          <span :class="['table-status', meetup.statusTone]">{{ meetup.status }}</span>
          <h2>{{ meetup.title }}</h2>
          <dl>
            <div><dt>멘토</dt><dd>{{ meetup.mentor }}</dd></div>
            <div><dt>일정</dt><dd>{{ meetup.date }}</dd></div>
            <div><dt>정원</dt><dd>{{ meetup.capacity }}</dd></div>
          </dl>
          <button class="button-primary" :disabled="isSubmitting === meetup.id" type="button" @click="applyMeetup(meetup)">신청하기</button>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { mentoringTabs } from '../../constants/navigation'
import { meetups as mockMeetups } from '../../data/boards'
import { loadMeetupApplications, submitMeetupApplication } from '../../services/surveyService'

const meetups = ref(isApiEnabled ? [] : mockMeetups)
const isLoading = ref(false)
const isSubmitting = ref(null)
const message = ref('')
const messageTone = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    meetups.value = await loadMeetupApplications()
  } catch (error) {
    if (isApiEnabled) {
      meetups.value = []
      message.value = getApiErrorMessage(error, '간담회 신청 목록을 불러오지 못했습니다.')
    } else {
      meetups.value = mockMeetups
      message.value = '간담회 신청 목록을 불러오지 못해 예시 데이터를 표시합니다.'
    }
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})

async function applyMeetup(meetup) {
  message.value = ''
  isSubmitting.value = meetup.id
  try {
    await submitMeetupApplication(meetup.id)
    message.value = `${meetup.title} 신청이 접수되었습니다.`
    messageTone.value = ''
  } catch (error) {
    message.value = getApiErrorMessage(error, '간담회 신청을 접수하지 못했습니다.')
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSubmitting.value = null
  }
}
</script>
