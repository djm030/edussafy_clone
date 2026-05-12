import { isApiEnabled } from '../api/client'
import { dashboardApi } from '../api/modules'
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

  const apiDashboard = await dashboardApi.my()

  const campusSummary = apiDashboard?.campusSummary || {}
  const pointSummary = apiDashboard?.pointSummary || {}
  const summaryUser = apiDashboard?.user || campusSummary?.user || dashboardData.user
  const notifications = pageItems(apiDashboard?.notifications).slice(0, 3).map(normalizeNotification)
  const storyPosts = pageItems(apiDashboard?.storyPosts)

  return {
    ...dashboardData,
    user: {
      ...dashboardData.user,
      ...summaryUser
    },
    pointSummary: {
      ...dashboardData.pointSummary,
      ...pointSummary,
      unreadNotificationCount: campusSummary.unreadNotificationCount ?? dashboardData.pointSummary.unreadNotificationCount
    },
    attendanceSummary: normalizeAttendanceSummary(apiDashboard?.todayAttendance),
    notifications,
    curriculumPreview: pageItems(apiDashboard?.curriculumPreview).map((item) => ({
      id: item.id,
      weekNo: item.weekId || '-',
      title: item.title,
      date: formatDate(item.sessionDate || item.startAt),
      type: item.sessionType
    })),
    questPreview: pageItems(apiDashboard?.questPreview).map((item) => ({
      id: item.id,
      title: item.title,
      taskType: item.taskType,
      status: item.myResultStatus || 'SCHEDULED',
      score: item.score,
      closeAt: formatDate(item.closeAt)
    })),
    learningPreview: pageItems(apiDashboard?.learningPreview).map((item) => ({
      id: item.id,
      title: item.title,
      contentType: item.contentType,
      duration: item.durationSeconds ? `${Math.ceil(item.durationSeconds / 60)}분` : '자료',
      required: Boolean(item.isRequired)
    })),
    freeBoardPosts: pageItems(apiDashboard?.freeBoardPosts),
    storyPosts: storyPosts.length ? storyPosts : dashboardData.storyPosts,
    notices: pageItems(apiDashboard?.notices)
  }
}
