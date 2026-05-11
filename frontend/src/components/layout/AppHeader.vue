<template>
  <header class="global-header">
    <div class="header-inner">
      <RouterLink class="brand" to="/dashboard" aria-label="SSAFY EDU home">
        <span class="brand-mark">SS</span>
        <span class="brand-copy">
          <strong>SSAFY EDU</strong>
          <small>SAMSUNG SW·AI ACADEMY FOR YOUTH</small>
        </span>
      </RouterLink>

      <nav class="primary-nav" aria-label="Primary navigation">
        <RouterLink
          v-for="item in primaryNavigation"
          :key="item.path"
          :to="item.path"
          class="primary-nav-link"
        >
          {{ item.label }}
        </RouterLink>
      </nav>

      <div class="header-tools" aria-label="User tools">
        <RouterLink class="notification-link" to="/notifications" aria-label="Notifications">
          <span class="notification-dot">1</span>
          알림
        </RouterLink>
        <span class="profile-chip">김싸피</span>
        <RouterLink class="logout-link" to="/login" @click="logout">로그아웃</RouterLink>
      </div>
    </div>

    <div class="service-strip">
      <a
        v-for="link in serviceLinks"
        :key="link.label"
        :href="link.url"
        :class="['service-link', `service-link-${link.tone}`]"
        target="_blank"
        rel="noreferrer"
      >
        {{ link.label }}
      </a>
    </div>
  </header>
</template>

<script setup>
import { clearAccessToken } from '../../api/client'
import { primaryNavigation, serviceLinks } from '../../constants/navigation'

function logout() {
  clearAccessToken()
}
</script>
