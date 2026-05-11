import { isApiEnabled } from '../api/client'
import { authApi, notificationsApi, pointsApi, usersApi } from '../api/modules'
import { dashboardData } from '../data/dashboard'

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function normalizeNotification(item) {
  return {
    id: item.id,
    title: item.title,
    content: item.content || item.body || '',
    notificationType: item.notificationType || item.type || 'SYSTEM',
    isRead: item.isRead ?? item.read ?? false,
    createdAt: item.createdAt || item.date || ''
  }
}

export async function loadDashboardData() {
  if (!isApiEnabled) return dashboardData

  const [me, campusSummary, pointSummary, notificationsPage] = await Promise.all([
    authApi.me(),
    usersApi.campusSummary(),
    pointsApi.summary(),
    notificationsApi.my({ page: 0, size: 3 })
  ])

  const summaryUser = campusSummary?.user || me || dashboardData.user
  const stat = campusSummary?.stat || pointSummary || dashboardData.pointSummary
  const notifications = pageItems(notificationsPage).slice(0, 3).map(normalizeNotification)

  return {
    ...dashboardData,
    user: {
      ...dashboardData.user,
      ...summaryUser
    },
    pointSummary: {
      ...dashboardData.pointSummary,
      ...stat,
      unreadNotificationCount: stat.unreadNotificationCount ?? dashboardData.pointSummary.unreadNotificationCount
    },
    notifications
  }
}
