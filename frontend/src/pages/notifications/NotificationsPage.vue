<template>
  <div>
    <PageHero title="알림" />
    <main class="page-container notifications-page">
      <p v-if="isLoading" class="dashboard-state">알림을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Notifications</p>
        <h1>알림</h1>
        <p>학습, 공지, 멘토링 관련 알림을 확인합니다.</p>
      </div>
      <section class="notification-list" aria-label="Notification list">
        <article v-for="item in notifications" :key="item.id" :class="['notification-card', { unread: item.unread }]">
          <span class="list-dot" :class="{ unread: item.unread }"></span>
          <div>
            <h2>{{ item.title }}</h2>
            <p>{{ item.body }}</p>
          </div>
          <time>{{ item.date }}</time>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import { notifications as mockNotifications } from '../../data/mycampus'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { loadNotifications } from '../../services/notificationsService'

const notifications = ref(isApiEnabled ? [] : mockNotifications)
const isLoading = ref(false)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    notifications.value = await loadNotifications()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '알림 데이터를 불러오지 못했습니다.')
    if (isApiEnabled) notifications.value = []
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
