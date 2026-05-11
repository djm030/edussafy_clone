<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Survey</p>
        <h1>설문조사</h1>
        <p>교육 운영과 프로젝트 진행에 필요한 의견을 빠르게 확인합니다.</p>
      </div>

      <SearchFilterBar :options="['전체', '진행중', '완료', '결과공개']" placeholder="설문명을 입력하세요." />

      <section class="survey-grid" aria-label="Survey list">
        <article v-for="survey in surveys" :key="survey.id" class="survey-card">
          <span :class="['table-status', survey.statusTone]">{{ survey.status }}</span>
          <h2>{{ survey.title }}</h2>
          <dl>
            <div><dt>대상</dt><dd>{{ survey.target }}</dd></div>
            <div><dt>기간</dt><dd>{{ survey.period }}</dd></div>
          </dl>
          <RouterLink class="button-primary" :to="`/community/surveys/${survey.id}`">참여/결과 보기</RouterLink>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import { communityTabs } from '../../constants/navigation'
import { surveys } from '../../data/boards'
</script>
