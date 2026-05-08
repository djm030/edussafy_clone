<script setup>
import PlusLink from "../ui/PlusLink.vue";
import IconLogout from "../ui/IconLogout.vue";

defineProps({
  attendance: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <article class="attendance-card" aria-labelledby="attendance-title">
    <div class="section-heading">
      <h1 id="attendance-title">출석체크 &amp; 현황</h1>
      <PlusLink label="출석 현황 더 보기" />
    </div>

    <div class="attendance-rule" />

    <div class="attendance-body">
      <div :aria-label="`오늘 날짜 ${attendance.date} ${attendance.weekday}`">
        <div class="date-number">{{ attendance.date }}</div>
        <div class="date-weekday">{{ attendance.weekday }}</div>
      </div>

      <div class="attendance-actions">
        <div class="time-box" :aria-label="`출석 시간 ${attendance.time} ${attendance.status}`">
          <span>
            <strong>{{ attendance.time }}</strong>
            <span>{{ attendance.status }}</span>
          </span>
        </div>

        <button class="checkout-box" type="button">
          <span>
            <IconLogout />
            <span>퇴실하기</span>
          </span>
        </button>
      </div>
    </div>
  </article>
</template>
