<script setup>
import PlusLink from "../ui/PlusLink.vue";
import LectureCard from "./LectureCard.vue";

defineProps({
  curriculum: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <article class="curriculum-section" aria-labelledby="curriculum-title">
    <div class="section-heading">
      <h2 id="curriculum-title">주차별 커리큘럼</h2>
      <PlusLink label="커리큘럼 더 보기" />
    </div>

    <div class="timeline">
      <div class="time-column" aria-hidden="true">
        <div class="date-marker">{{ curriculum.date }}</div>
        <div class="time-marker">{{ curriculum.time }}</div>
      </div>

      <div class="lecture-column">
        <LectureCard v-for="lecture in curriculum.lectures" :key="lecture.id" :lecture="lecture" />
      </div>
    </div>

    <div class="scroll-hint" aria-hidden="true" />
  </article>
</template>
