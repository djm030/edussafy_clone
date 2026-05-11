import { isApiEnabled } from '../api/client'
import { notificationsApi } from '../api/modules'
import { notifications as mockNotifications } from '../data/mycampus'

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function formatDate(value) {
  if (!value) return ''
  return String(value).split('T')[0].replaceAll('-', '.')
}

function normalizeNotification(item) {
  return {
    id: item.id,
    title: item.title,
    body: item.body || item.content || '',
    date: formatDate(item.createdAt || item.date),
    unread: item.unread ?? item.isRead === false
  }
}

export async function loadNotifications() {
  if (!isApiEnabled) return mockNotifications

  const page = await notificationsApi.my({ page: 0, size: 10 })
  return pageItems(page).map(normalizeNotification)
}

export async function loadUnreadNotificationCount() {
  if (!isApiEnabled) return mockNotifications.filter((item) => item.unread).length

  const data = await notificationsApi.unreadCount()
  return data?.count ?? data?.unreadCount ?? (Number(data) || 0)
}
