<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import StatusBadge from '../../components/ui/StatusBadge.vue'
import { classroomTabs } from '../../constants/navigation'
import { questItems } from '../../data/classroom'

const route = useRoute()
const quest = computed(() => questItems.find((item) => String(item.id) === route.params.id) || questItems[0])
</script>

<template>
  <PageHero title="Quest/평가" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <article class="detail-panel">
      <div class="detail-header">
        <div>
          <p class="eyebrow-text">{{ quest.type }}</p>
          <h1>{{ quest.title }}</h1>
          <p>{{ quest.period }}</p>
        </div>
        <StatusBadge :tone="quest.status === '예정' ? 'blue' : 'slate'">{{ quest.status }}</StatusBadge>
      </div>

      <dl class="detail-grid">
        <div>
          <dt>구분</dt>
          <dd>{{ quest.scope }}</dd>
        </div>
        <div>
          <dt>획득 가능 경험치</dt>
          <dd>{{ quest.reward }} P</dd>
        </div>
        <div>
          <dt>결과</dt>
          <dd>{{ quest.result }}</dd>
        </div>
        <div>
          <dt>점수</dt>
          <dd>{{ quest.score === null ? '-' : `${quest.score}점` }}</dd>
        </div>
      </dl>

      <div class="submission-box">
        <h2>제출 현황</h2>
        <p>이 화면은 백엔드 연동 전 mock 상태입니다. 실제 제출/결과 데이터는 API 연동 단계에서 연결합니다.</p>
        <RouterLink class="button-primary" to="/classroom/quests">목록</RouterLink>
      </div>
    </article>
  </section>
</template>
