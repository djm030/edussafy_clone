<script setup>
import OutlineButton from "../ui/OutlineButton.vue";

defineProps({
  lecture: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <article class="lecture-card" :class="lecture.variant">
    <div v-if="lecture.category" class="lecture-type">{{ lecture.category }}</div>
    <div class="lecture-name">{{ lecture.name ?? lecture.instructor }}</div>
    <div v-if="lecture.title" class="lecture-title">{{ lecture.title }}</div>
    <div v-if="lecture.meta" class="lecture-meta">{{ lecture.meta }}</div>
    <div class="lecture-actions">
      <OutlineButton variant="replay">강의 다시보기</OutlineButton>
      <OutlineButton>교재</OutlineButton>
    </div>
  </article>
</template>
