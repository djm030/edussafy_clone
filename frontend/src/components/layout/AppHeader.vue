<template>
  <header class="global-header">
    <div class="header-inner">
      <RouterLink class="brand" to="/dashboard" aria-label="SSAFY EDU home">
        <span class="brand-mark" aria-hidden="true">
          <span>SAMSUNG</span>
          <span>SW</span>
          <span>AI ACADEMY</span>
          <span>FOR YOUTH</span>
        </span>
        <span class="brand-copy">
          <strong>SSAFY EDU</strong>
          <small>SAMSUNG SW·AI ACADEMY FOR YOUTH</small>
        </span>
      </RouterLink>

      <nav class="primary-nav" aria-label="Primary navigation">
        <div
          v-for="item in primaryNavigation"
          :key="item.path"
          class="primary-nav-item"
        >
          <RouterLink :to="item.path" class="primary-nav-link">
            {{ item.label }}
          </RouterLink>
          <div v-if="item.children?.length" class="primary-nav-menu" role="menu">
            <RouterLink
              v-for="child in item.children"
              :key="child.path"
              :to="child.path"
              class="primary-nav-menu-link"
              role="menuitem"
            >
              {{ child.label }}
            </RouterLink>
          </div>
        </div>
      </nav>

      <div class="header-tools" aria-label="User tools">
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
        <RouterLink class="notification-link" to="/notifications" aria-label="Notifications">
          <span class="notification-bell" aria-hidden="true"></span>
          <span class="notification-dot">{{ notificationCount }}</span>
        </RouterLink>
        <span class="user-avatar" aria-hidden="true"></span>
        <div ref="profileMenuRef" class="profile-menu-wrap">
          <button
            type="button"
            class="profile-menu-trigger"
            :aria-expanded="isProfileMenuOpen"
            aria-haspopup="menu"
            @click="isProfileMenuOpen = !isProfileMenuOpen"
          >
            <span class="user-number">{{ headerUser.studentNo }}</span>
            <span class="profile-chip">{{ headerUser.name }}님</span>
            <span class="profile-caret" aria-hidden="true">⌄</span>
          </button>
          <div v-if="isProfileMenuOpen" class="profile-menu" role="menu">
            <RouterLink class="profile-menu-link" to="/mycampus/profile" role="menuitem" @click="isProfileMenuOpen = false">회원정보</RouterLink>
            <button class="profile-menu-link" type="button" role="menuitem" :disabled="isLoggingOut" @click="logout">
              로그아웃
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { clearAuthTokens, hasAccessToken, isApiEnabled } from '../../api/client'
import { authApi, usersApi } from '../../api/modules'
import { primaryNavigation, serviceLinks } from '../../constants/navigation'
import { dashboardData } from '../../data/dashboard'
import { loadUnreadNotificationCount } from '../../services/notificationsService'

const router = useRouter()
const notificationCount = ref(0)
const headerUser = ref({
  studentNo: dashboardData.user.studentNo,
  name: dashboardData.user.name
})
const isProfileMenuOpen = ref(false)
const isLoggingOut = ref(false)
const profileMenuRef = ref(null)

function normalizeHeaderUser(user = {}) {
  return {
    studentNo: user.studentNo || user.studentNumber || dashboardData.user.studentNo,
    name: user.name || user.displayName || dashboardData.user.name
  }
}

function closeProfileMenuOnOutsideClick(event) {
  if (!profileMenuRef.value || profileMenuRef.value.contains(event.target)) return
  isProfileMenuOpen.value = false
}

async function loadHeaderData() {
  if (!isApiEnabled) {
    notificationCount.value = dashboardData.notifications.filter((item) => !item.isRead).length
    return
  }
  if (!hasAccessToken()) return

  const [user, unreadCount] = await Promise.all([
    usersApi.me().catch(() => null),
    loadUnreadNotificationCount().catch(() => 0)
  ])
  if (user) headerUser.value = normalizeHeaderUser(user)
  notificationCount.value = unreadCount
}

async function logout() {
  if (isLoggingOut.value) return
  isLoggingOut.value = true
  try {
    if (isApiEnabled) {
      await authApi.logout().catch(() => null)
    }
  } finally {
    clearAuthTokens()
    isProfileMenuOpen.value = false
    isLoggingOut.value = false
    router.push('/login')
  }
}

onMounted(async () => {
  document.addEventListener('click', closeProfileMenuOnOutsideClick)
  await loadHeaderData()
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeProfileMenuOnOutsideClick)
})
</script>
