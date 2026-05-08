<script setup>
import PlusLink from "../ui/PlusLink.vue";
import StudyMaterialCard from "./StudyMaterialCard.vue";

defineProps({
  materials: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <article aria-labelledby="materials-title">
    <div class="bottom-heading-row">
      <div class="materials-title-group">
        <h2 id="materials-title" class="bottom-title">학습자료</h2>
        <div class="carousel-buttons" aria-label="학습자료 이동">
          <button class="arrow-button" type="button" aria-label="이전 학습자료">‹</button>
          <button class="arrow-button next" type="button" aria-label="다음 학습자료">›</button>
        </div>
      </div>
      <PlusLink label="학습자료 더 보기" />
    </div>

    <div class="materials-grid">
      <StudyMaterialCard v-for="material in materials" :key="material.id" :material="material" />
    </div>
  </article>
</template>
