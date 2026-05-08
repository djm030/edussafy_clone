<script setup>
import PointLevelSummary from "./PointLevelSummary.vue";
import NotificationList from "./NotificationList.vue";

defineProps({
  summary: {
    type: Object,
    required: true
  },
  notifications: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <article class="summary-card" aria-labelledby="summary-title">
    <h2 id="summary-title" class="sr-only">장학포인트 레벨 알림 요약</h2>
    <PointLevelSummary :summary="summary" />
    <NotificationList :notifications="notifications" />
  </article>
</template>
