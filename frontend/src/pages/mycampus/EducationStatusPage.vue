<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Education Status</p>
        <h1>교육현황</h1>
        <p>출석, 평가, 과제, 활동 현황을 요약합니다.</p>
      </div>
      <section class="education-grid">
        <article v-for="item in educationStatus" :key="item.id" class="education-card">
          <span>{{ item.title }}</span>
          <strong>{{ item.value }}</strong>
          <p>{{ item.description }}</p>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { educationStatus, mycampusTabs } from '../../data/mycampus'
</script>
