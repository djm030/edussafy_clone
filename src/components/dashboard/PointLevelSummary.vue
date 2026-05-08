<script setup>
defineProps({
  summary: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <div>
    <div class="metric-row">
      <span class="metric-label">장학포인트</span>
      <strong class="metric-value">{{ summary.scholarshipPoint }}</strong>
    </div>
    <div class="metric-row">
      <span class="metric-label">레벨&amp;경험치</span>
      <strong class="metric-value">{{ summary.exp }}</strong>
    </div>

    <div
      class="level-graphic"
      :aria-label="`${summary.currentLevel.replace('\n', ' ')}에서 ${summary.goalLevel}까지 진행 중`"
    >
      <div class="avatar-pin">
        <span class="face-pin" aria-hidden="true">☻</span>
        <span class="pin-text">
          <template v-for="line in summary.currentLevel.split('\n')" :key="line">
            {{ line }}<br />
          </template>
        </span>
      </div>
      <div
        class="progress-line"
        role="progressbar"
        aria-label="경험치 진행률"
        aria-valuemin="0"
        aria-valuemax="100"
        :aria-valuenow="summary.progress"
        :style="{ '--progress': `${summary.progress}%` }"
      />
      <span class="level-node start">{{ summary.startLevel }}</span>
      <span class="level-node goal">{{ summary.goalLevel }}</span>
    </div>
  </div>
</template>
