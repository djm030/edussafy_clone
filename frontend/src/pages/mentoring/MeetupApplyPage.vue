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
      <section class="meetup-grid" aria-label="Meetup apply list">
        <article v-for="meetup in meetups" :key="meetup.id" class="meetup-card">
          <span :class="['table-status', meetup.statusTone]">{{ meetup.status }}</span>
          <h2>{{ meetup.title }}</h2>
          <dl>
            <div><dt>멘토</dt><dd>{{ meetup.mentor }}</dd></div>
            <div><dt>일정</dt><dd>{{ meetup.date }}</dd></div>
            <div><dt>정원</dt><dd>{{ meetup.capacity }}</dd></div>
          </dl>
          <RouterLink class="button-primary" :to="`/mentoring/meetups/apply/${meetup.id}`">신청하기</RouterLink>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { mentoringTabs } from '../../constants/navigation'
import { meetups } from '../../data/boards'
</script>
