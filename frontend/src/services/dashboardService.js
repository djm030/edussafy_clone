import { isApiEnabled } from '../api/client'
import { dashboardApi } from '../api/modules'
import { dashboardData } from '../data/dashboard'
import { loadElearningData } from './mycampusService'
import { loadMentoringPosts } from './boardService'
import { loadUnreadNotificationCount } from './notificationsService'

const PREVIEW_LIMITS = {
  elearning: 2,
  notifications: 3,
  stories: 4
}

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
    createdAt: formatDate(item.createdAt || item.date)
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
  const hasCheckIn = Boolean(record?.checkInAt)
  const hasCheckOut = Boolean(record?.checkOutAt)

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
    nextAction: today?.nextAction || (hasCheckOut ? 'DONE' : hasCheckIn ? 'CHECK_OUT' : 'CHECK_IN'),
    message: today?.message || (hasCheckOut ? '오늘 출석 처리가 완료되었습니다.' : hasCheckIn ? '퇴실 처리를 진행해 주세요.' : '아직 출석 전입니다.'),
    serverTime: formatTime(today?.currentTime)
  }
}

function normalizePointSummary(pointSummary = {}, campusSummary = {}) {
  return {
    ...dashboardData.pointSummary,
    ...pointSummary,
    scholarshipPoint: pointSummary.scholarshipPoint ?? campusSummary.scholarshipPoint ?? dashboardData.pointSummary.scholarshipPoint,
    totalExp: pointSummary.totalExp ?? campusSummary.totalExp ?? dashboardData.pointSummary.totalExp,
    levelName: pointSummary.levelName ?? campusSummary.levelName ?? dashboardData.pointSummary.levelName,
    levelNo: pointSummary.levelNo ?? campusSummary.levelNo ?? dashboardData.pointSummary.levelNo,
    attendanceRate: pointSummary.attendanceRate ?? campusSummary.attendanceRate ?? dashboardData.pointSummary.attendanceRate,
    completedLearningCount: pointSummary.completedLearningCount ?? campusSummary.completedLearningCount ?? dashboardData.pointSummary.completedLearningCount,
    unreadNotificationCount: campusSummary.unreadNotificationCount ?? dashboardData.pointSummary.unreadNotificationCount
  }
}

function normalizeCurriculumItem(item) {
  const date = item.sessionDate || item.date || item.startAt
  const formattedDate = formatDate(date)
  const dayLabel = dayLabelFromDate(date).replace('요일', '')
  const startTime = formatTime(item.startAt)
  const endTime = formatTime(item.endAt)
  const timeRange = startTime !== '-' && endTime !== '-' ? `${startTime}~${endTime}` : item.timeRange || '시간 미정'

  return {
    id: item.id || item.sessionId,
    weekNo: item.weekId || item.weekNo || '-',
    title: item.title || item.sessionTitle || '강의',
    date: formattedDate,
    dateLabel: item.dateLabel || [formattedDate, dayLabel && `(${dayLabel})`].filter(Boolean).join(''),
    timeRange,
    type: item.sessionType || item.type,
    categoryName: item.categoryName || item.category || '과정 미정',
    subject: item.trackName || item.track || item.courseName || '과목 미정',
    location: item.instructorName || item.instructor || item.location || '강의 정보 미정',
    sessionId: item.id || item.sessionId,
    textbookId: item.textbookId || null
  }
}

function normalizeQuestItem(item) {
  const status = item.myResultStatus || item.resultStatus || item.status || 'SCHEDULED'
  const statusLabels = {
    COMPLETED: '완료',
    EVALUATED: '완료',
    GRADED: '완료',
    SUBMITTED: '제출',
    PASS: '통과',
    FAIL: '미통과'
  }

  return {
    id: item.id || item.taskId,
    title: item.title || 'Quest/평가',
    taskType: item.taskType || item.type || 'QUEST',
    status,
    statusLabel: statusLabels[status] || '예정',
    score: item.score ?? item.myScore ?? null,
    closeAt: formatDate(item.closeAt || item.endAt)
  }
}

function normalizeLearningItem(item) {
  return {
    id: item.id || item.contentId,
    title: item.title || '학습자료',
    contentType: item.contentType || item.type || '자료',
    duration: item.durationSeconds ? `${Math.ceil(item.durationSeconds / 60)}분` : item.duration || '자료',
    required: Boolean(item.isRequired ?? item.required),
    description: item.description || item.summary || item.title || '',
    viewCount: item.viewCount ?? item.views ?? 0,
    likeCount: item.likeCount ?? item.likes ?? 0,
    bookmarkCount: item.bookmarkCount ?? item.bookmarks ?? 0,
    coverImage: item.coverImage || '',
    coverVariant: item.coverVariant || String(item.contentType || item.type || '').toLowerCase(),
    coverLabel: item.coverLabel || item.categoryName || item.contentType || '자료'
  }
}

function normalizeBoardPost(item) {
  const id = item.postId ?? item.id
  return {
    id,
    postId: id,
    categoryName: item.categoryName || item.category?.name || item.category || '일반',
    title: item.title || '게시글',
    displayName: item.displayName || item.authorName || item.author || '익명',
    viewCount: item.viewCount ?? item.views ?? 0,
    commentCount: item.commentCount ?? 0,
    createdAt: formatDate(item.createdAt || item.date)
  }
}

async function loadDashboardSupplements() {
  const [elearning, stories] = await Promise.all([
    loadElearningData().catch(() => []),
    loadMentoringPosts('stories').catch(() => [])
  ])

  return {
    elearningPreview: elearning.slice(0, PREVIEW_LIMITS.elearning),
    storyPosts: stories.slice(0, PREVIEW_LIMITS.stories).map(normalizeBoardPost)
  }
}

export async function loadDashboardData() {
  if (!isApiEnabled) {
    const unreadNotificationCount = dashboardData.notifications.filter((item) => !item.isRead).length
    return {
      ...dashboardData,
      pointSummary: {
        ...dashboardData.pointSummary,
        unreadNotificationCount
      },
      elearningPreview: []
    }
  }

  const [apiDashboard, supplements, unreadCount] = await Promise.all([
    dashboardApi.my(),
    loadDashboardSupplements(),
    loadUnreadNotificationCount().catch(() => null)
  ])

  const campusSummary = apiDashboard?.campusSummary || {}
  const pointSummary = apiDashboard?.pointSummary || {}
  const summaryUser = apiDashboard?.user || campusSummary?.user || dashboardData.user
  const notifications = pageItems(apiDashboard?.notifications).slice(0, PREVIEW_LIMITS.notifications).map(normalizeNotification)
  const unreadNotificationCount = unreadCount ?? campusSummary.unreadNotificationCount ?? notifications.filter((item) => !item.isRead).length
  const storyPosts = pageItems(apiDashboard?.storyPosts).map(normalizeBoardPost)

  return {
    ...dashboardData,
    user: {
      ...dashboardData.user,
      ...summaryUser
    },
    pointSummary: {
      ...normalizePointSummary(pointSummary, campusSummary),
      unreadNotificationCount
    },
    attendanceSummary: normalizeAttendanceSummary(apiDashboard?.todayAttendance),
    notifications,
    curriculumPreview: pageItems(apiDashboard?.curriculumPreview).map(normalizeCurriculumItem),
    questPreview: pageItems(apiDashboard?.questPreview).map(normalizeQuestItem),
    learningPreview: pageItems(apiDashboard?.learningPreview).map(normalizeLearningItem),
    elearningPreview: supplements.elearningPreview,
    freeBoardPosts: pageItems(apiDashboard?.freeBoardPosts).map(normalizeBoardPost),
    storyPosts: storyPosts.length ? storyPosts : supplements.storyPosts,
    notices: pageItems(apiDashboard?.notices).map(normalizeBoardPost)
  }
}
