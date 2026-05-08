<script setup>
import IconBell from "../ui/IconBell.vue";

defineProps({
  profile: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <div class="user-tools" aria-label="사용자 도구">
    <button class="bell-button" type="button" :aria-label="`읽지 않은 알림 ${profile.unreadNotificationCount}개`">
      <IconBell />
      <span class="bell-badge" aria-hidden="true">
        {{ profile.unreadNotificationCount }}
      </span>
    </button>

    <button class="profile" type="button" :aria-label="`${profile.name} 프로필 메뉴 열기`">
      <span class="profile-photo" aria-hidden="true" />
      <span>
        <span class="profile-id">{{ profile.id }}</span>
        <span class="profile-name">{{ profile.name }}</span>
      </span>
      <span class="profile-chevron" aria-hidden="true">⌄</span>
    </button>
  </div>
</template>
