import { isApiEnabled } from '../api/client'
import { attendanceApi, authApi, notificationsApi, pointsApi, usersApi } from '../api/modules'
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

const attendanceLabels = {
  ABSENT: '결석',
  EARLY_LEAVE: '조퇴',
  LATE: '지각',
  NORMAL: '정상',
  OUTING: '외출',
  PENDING: '대기'
}

const attendanceTones = {
  EARLY_LEAVE: 'green',
  LATE: 'slate',
  NORMAL: 'green',
  PENDING: 'slate'
}

function formatDate(value) {
  if (!value) return '-'
  return String(value).split('T')[0]
}

function formatTime(value) {
  if (!value) return '-'
  const text = String(value)
  return text.includes('T') ? text.split('T')[1].slice(0, 5) : text.slice(0, 5)
}

function dayLabelFromDate(value) {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return ''
  return new Intl.DateTimeFormat('ko-KR', { weekday: 'long' }).format(date)
}

function normalizeAttendanceSummary(today) {
  const record = today?.record || null
  const date = record?.attendanceDate || today?.currentDate || dashboardData.attendanceSummary.date
  const status = record?.status || 'PENDING'

  return {
    date: formatDate(date),
    dayLabel: dayLabelFromDate(date),
    checkInAt: formatTime(record?.checkInAt),
    checkOutAt: formatTime(record?.checkOutAt),
    status,
    statusLabel: attendanceLabels[status] || status,
    statusTone: attendanceTones[status] || 'slate',
    canCheckIn: Boolean(today?.canCheckIn),
    canCheckOut: Boolean(today?.canCheckOut),
    message: today?.message || (record?.checkInAt ? '퇴실 처리를 진행해 주세요.' : '아직 출석 전입니다.')
  }
}

export async function loadDashboardData() {
  if (!isApiEnabled) return dashboardData

  const [me, campusSummary, pointSummary, notificationsPage, todayAttendance] = await Promise.all([
    authApi.me(),
    usersApi.campusSummary(),
    pointsApi.summary(),
    notificationsApi.my({ page: 0, size: 3 }),
    attendanceApi.today()
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
    attendanceSummary: normalizeAttendanceSummary(todayAttendance),
    notifications
  }
}
