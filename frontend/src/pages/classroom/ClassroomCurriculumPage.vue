<template>
  <PageHero title="주차별 커리큘럼" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <div class="phase-stepper" aria-label="Semester phases">
      <div v-for="phase in classroomPhases" :key="phase.label" :class="['phase-step', { active: phase.active }]">
        <span class="phase-icon">{{ phase.active ? '▦' : '▣' }}</span>
        <strong>{{ phase.status }}</strong>
        <small>{{ phase.label }}</small>
      </div>
    </div>

    <div class="curriculum-toolbar">
      <div class="view-toggle" aria-label="Curriculum view mode">
        <button class="active" type="button">주간</button>
        <button type="button">월간</button>
      </div>
      <ul class="category-legend" aria-label="Curriculum categories">
        <li><span class="legend-dot algorithm"></span>알고리즘</li>
        <li><span class="legend-dot coding"></span>코딩과정</li>
        <li><span class="legend-dot project"></span>프로젝트</li>
        <li><span class="legend-dot etc"></span>기타</li>
      </ul>
    </div>

    <div class="week-selector">
      <button type="button" aria-label="Previous week">‹</button>
      <span v-for="week in curriculumWeeks" :key="week.label" :class="['week-item', { active: week.active }]">
        <small v-if="week.period">{{ week.period }}</small>
        {{ week.label }}
      </span>
      <button type="button" aria-label="Next week">›</button>
    </div>

    <div class="curriculum-timeline">
      <article v-for="day in curriculumDays" :key="day.date" class="curriculum-day">
        <aside class="day-meta">
          <strong>{{ day.date }}</strong>
          <span>{{ day.timeRange }}</span>
        </aside>
        <div class="day-line" aria-hidden="true"></div>
        <div class="day-content">
          <p v-if="day.message" class="day-message">{{ day.message }}</p>
          <div v-for="item in day.items" :key="item.id" class="curriculum-card">
            <div>
              <span :class="['category-label', item.categoryTone]">{{ item.category }}</span>
              <p>{{ item.track }}</p>
              <h2>{{ item.title }}</h2>
            </div>
            <div class="curriculum-actions">
              <small>{{ item.instructor }}</small>
              <RouterLink v-if="item.hasReplay" class="outline-button green" :to="`/classroom/my-replays?sessionId=${item.id}`">강의 다시보기</RouterLink>
              <RouterLink v-if="item.hasTextbook" class="outline-button blue" :to="`/classroom/resources?sessionId=${item.id}&type=textbook`">교재</RouterLink>
            </div>
          </div>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { classroomPhases, curriculumWeeks, curriculumDays } from '../../data/classroom'
</script>
