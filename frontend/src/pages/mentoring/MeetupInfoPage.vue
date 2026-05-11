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
      <EmptyState message="신청한 간담회 정보가 없습니다." />
      <section class="info-panel">
        <h2>안내</h2>
        <p>간담회 신청이 완료되면 이 영역에서 접속 링크, 준비사항, 진행 시간을 확인할 수 있습니다.</p>
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import EmptyState from '../../components/ui/EmptyState.vue'
import { mentoringTabs } from '../../constants/navigation'
</script>
