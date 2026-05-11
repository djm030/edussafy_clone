import { isApiEnabled } from '../api/client'
import { classroomApi, learningApi, tasksApi } from '../api/modules'
import {
  allReplayItems,
  classroomPhases,
  curriculumDays,
  curriculumWeeks,
  learningResources,
  questItems
} from '../data/classroom'

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function formatDateTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 16).replaceAll('-', '.')
}

function formatDate(value) {
  if (!value) return ''
  return String(value).split('T')[0].replaceAll('-', '.')
}

function formatTime(value) {
  if (!value) return ''
  const text = String(value)
  return text.includes('T') ? text.split('T')[1].slice(0, 5) : text.slice(0, 5)
}

function dayLabel(value) {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return ''
  return new Intl.DateTimeFormat('ko-KR', { weekday: 'short' }).format(date).replace('요일', '')
}

function normalizeQuest(item) {
  const status = item.status === 'OPEN' || item.status === 'PENDING' ? '예정' : '완료'
  const result = item.resultStatus || item.myResultStatus || item.result || (status === '예정' ? '시험기간' : '제출완료')

  return {
    id: item.id || item.taskId,
    type: item.taskType || item.type || 'Quest',
    scope: item.scope || '[개인]',
    title: item.title,
    period: `${formatDateTime(item.openAt || item.startAt)} ~ ${formatDateTime(item.closeAt || item.endAt)}`,
    status,
    reward: item.rewardExp ?? item.reward ?? 0,
    result,
    score: item.score ?? item.myScore ?? null,
    pass: item.passStatus || item.pass || null
  }
}

function normalizeLearningResource(item) {
  const category = item.categoryName || item.category?.name || '학습자료'
  const type = item.contentType || item.type || '자료'

  return {
    id: item.id || item.contentId,
    title: item.title,
    breadcrumb: [category, type],
    description: item.description || item.summary || item.title,
    views: item.viewCount ?? item.views ?? 0,
    likes: item.likeCount ?? item.likes ?? 0,
    bookmarks: item.bookmarkCount ?? item.bookmarks ?? 0,
    textbook: item.isTextbook ?? type === 'FILE',
    label: item.label || category
  }
}

function normalizeReplay(item) {
  return {
    id: item.id || item.sessionId,
    title: item.title || item.sessionTitle || '강의 다시보기',
    meta: item.meta || [item.generation && `${item.generation}기`, item.region, item.classNo && `${item.classNo}반`].filter(Boolean).join(' '),
    period: `${formatDateTime(item.openAt || item.startAt)} ~ ${formatDateTime(item.closeAt || item.endAt)}`
  }
}

function normalizeWeek(item, index) {
  return {
    label: item.label || `${item.weekNo || item.week || index + 1}주차`,
    active: item.active ?? item.isCurrent ?? index === 0,
    period: item.period || [item.startDate, item.endDate].filter(Boolean).map(formatDate).join('~')
  }
}

function categoryTone(type) {
  if (type === 'PROJECT') return 'project'
  if (type === 'ALGORITHM') return 'algorithm'
  if (type === 'ETC') return 'etc'
  return 'coding'
}

function normalizeCurriculumSession(item) {
  const type = item.sessionType || item.categoryType || 'CODING'

  return {
    id: item.id || item.sessionId,
    category: item.categoryName || item.category || '코딩과정',
    categoryTone: item.categoryTone || categoryTone(type),
    track: item.trackName || item.track || item.courseName || '',
    title: item.title || item.sessionTitle || '강의',
    instructor: item.instructorName || item.instructor || item.location || '',
    hasReplay: item.hasReplay ?? Boolean(item.replayUrl),
    hasTextbook: item.hasTextbook ?? Boolean(item.textbookId)
  }
}

function normalizeCurriculumDays(items) {
  const grouped = items.reduce((acc, item) => {
    const date = item.sessionDate || item.date || item.startAt
    const key = formatDate(date)
    if (!key) return acc
    acc[key] ||= {
      date: `${key}(${dayLabel(date)})`,
      timeRange: `${formatTime(item.startAt)}~${formatTime(item.endAt)}`,
      items: []
    }
    acc[key].items.push(normalizeCurriculumSession(item))
    return acc
  }, {})

  return Object.values(grouped)
}

export async function loadQuestItems() {
  if (!isApiEnabled) return questItems

  const page = await tasksApi.my({ page: 0, size: 10 }).catch(() => null)
  const items = pageItems(page).map(normalizeQuest)
  return items.length ? items : questItems
}

export async function loadLearningResources({ required = false } = {}) {
  if (!isApiEnabled) return required ? [] : learningResources

  const page = required
    ? await learningApi.required({ page: 0, size: 10 }).catch(() => null)
    : await learningApi.openLearning({ page: 0, size: 10 }).catch(() => null)
  const items = pageItems(page).map(normalizeLearningResource)
  return items.length ? items : (required ? [] : learningResources)
}

export async function loadReplayItems() {
  if (!isApiEnabled) return allReplayItems

  const page = await classroomApi.replays({ page: 0, size: 10 }).catch(() => null)
  const items = pageItems(page).map(normalizeReplay)
  return items.length ? items : allReplayItems
}

export async function loadCurriculumData() {
  if (!isApiEnabled) return { classroomPhases, curriculumWeeks, curriculumDays }

  const coursesPage = await classroomApi.myCourses().catch(() => null)
  const course = pageItems(coursesPage)[0]
  const courseId = course?.id || course?.courseId
  if (!courseId) return { classroomPhases, curriculumWeeks, curriculumDays }

  const weeksPage = await classroomApi.weeks(courseId).catch(() => null)
  const weeks = pageItems(weeksPage).map(normalizeWeek)
  const activeWeek = pageItems(weeksPage).find((week) => week.active || week.isCurrent) || pageItems(weeksPage)[0]
  const weekId = activeWeek?.id || activeWeek?.weekId
  if (!weekId) {
    return {
      classroomPhases,
      curriculumWeeks: weeks.length ? weeks : curriculumWeeks,
      curriculumDays
    }
  }

  const sessionsPage = await classroomApi.sessions(courseId, weekId).catch(() => null)
  const days = normalizeCurriculumDays(pageItems(sessionsPage))

  return {
    classroomPhases,
    curriculumWeeks: weeks.length ? weeks : curriculumWeeks,
    curriculumDays: days.length ? days : curriculumDays
  }
}
