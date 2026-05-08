<script setup>
defineProps({
  notifications: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div class="notification-panel">
    <h2 class="notification-heading">
      알림 <strong>0</strong>
    </h2>
    <ul class="notice-list">
      <li v-for="notice in notifications" :key="`${notice.title}-${notice.date}`" class="notice-item">
        <a href="#">
          <span class="notice-badge">{{ notice.required ? "필독" : "일반" }}</span>
          <span class="notice-title" :title="notice.title">
            {{ notice.shortTitle ?? notice.title }}
          </span>
          <time class="notice-date" :datetime="notice.dateTime">⌂ {{ notice.date }}</time>
        </a>
      </li>
    </ul>
  </div>
</template>
