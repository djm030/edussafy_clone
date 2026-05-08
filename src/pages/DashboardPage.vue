<script setup>
import AttendanceCard from "../components/dashboard/AttendanceCard.vue";
import SummaryPanel from "../components/dashboard/SummaryPanel.vue";
import CurriculumSection from "../components/dashboard/CurriculumSection.vue";
import QuestEvaluationPanel from "../components/dashboard/QuestEvaluationPanel.vue";
import StudyMaterialsSection from "../components/dashboard/StudyMaterialsSection.vue";
import ELearningCard from "../components/dashboard/ELearningCard.vue";

defineProps({
  dashboardData: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <main id="main" class="page">
    <section class="top-row" aria-label="출석과 주요 알림">
      <AttendanceCard :attendance="dashboardData.attendance" />
      <SummaryPanel :summary="dashboardData.summary" :notifications="dashboardData.notifications" />
    </section>

    <section class="learning-panel" aria-label="커리큘럼과 Quest 평가">
      <CurriculumSection :curriculum="dashboardData.curriculum" />
      <QuestEvaluationPanel :items="dashboardData.questEvaluations" />
    </section>

    <section class="bottom-row" aria-label="학습자료와 이러닝">
      <StudyMaterialsSection :materials="dashboardData.materials" />
      <ELearningCard />
    </section>
  </main>
</template>
