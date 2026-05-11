<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Academic Rules</p>
        <h1>학사규정</h1>
        <p>출결, 평가, 캠퍼스 생활과 관련된 주요 규정을 확인합니다.</p>
      </div>
      <section class="rule-list" aria-label="Academic rules">
        <details v-for="rule in ruleCategories" :key="rule.id" class="rule-item" open>
          <summary>{{ rule.title }}</summary>
          <p>{{ rule.body }}</p>
        </details>
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { helpTabs } from '../../constants/navigation'
import { ruleCategories } from '../../data/boards'
</script>
