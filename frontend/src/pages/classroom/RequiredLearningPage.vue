<template>
  <PageHero title="필수학습" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <div class="sub-tabs required-tabs">
      <button class="active" type="button">전체</button>
      <button type="button">IT</button>
      <button type="button">인사·총무·HRD</button>
      <button type="button">외국어</button>
    </div>

    <div class="search-bar">
      <label><span>SEARCH</span><input type="search" placeholder="키워드 검색" /></label>
      <button type="button">검색</button>
    </div>

    <div class="list-toolbar">
      <strong>총 <span>0</span>건</strong>
      <select aria-label="Sort"><option>최신등록순</option></select>
    </div>

    <EmptyState message="이러닝과정을 준비중입니다." />
  </section>
</template>

<script setup>
import EmptyState from '../../components/ui/EmptyState.vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
</script>
