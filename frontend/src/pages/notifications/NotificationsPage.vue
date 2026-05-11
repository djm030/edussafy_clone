<template>
  <div>
    <PageHero title="알림" />
    <main class="page-container notifications-page">
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
import PageHero from '../../components/ui/PageHero.vue'
import { notifications } from '../../data/mycampus'
</script>
