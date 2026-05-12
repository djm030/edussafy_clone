import { isApiEnabled } from '../api/client'
import { bookmarksApi, classroomApi, learningApi, tasksApi } from '../api/modules'
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
  const answerData = item.answerData || item.myAnswerData || {}

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
    pass: item.passStatus || item.pass || null,
    submittedAt: formatDateTime(item.submittedAt || item.mySubmittedAt || item.updatedAt),
    answer: answerData.answer || item.answer || ''
  }
}

function normalizeQuestSubmission(item = {}) {
  const answerData = item.answerData || item.myAnswerData || {}
  return {
    result: item.resultStatus || item.myResultStatus || item.result || item.status || '제출완료',
    score: item.score ?? item.myScore ?? null,
    pass: item.passStatus || item.pass || null,
    submittedAt: formatDateTime(item.submittedAt || item.mySubmittedAt || item.updatedAt || new Date().toISOString()),
    answer: answerData.answer || item.answer || ''
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
    downloads: item.downloadCount ?? item.downloads ?? 0,
    textbook: item.isTextbook ?? type === 'FILE',
    label: item.label || category
  }
}

function requiredLearningFallback() {
  return learningResources.filter((item) => !item.textbook).slice(0, 3)
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

  const page = await tasksApi.my({ page: 0, size: 10 })
  return pageItems(page).map(normalizeQuest)
}

export async function loadQuestDetail(taskId) {
  const fallback = questItems.find((item) => String(item.id) === String(taskId)) || questItems[0]
  if (!isApiEnabled) return fallback

  const [detail, myResult] = await Promise.all([
    tasksApi.detail(taskId),
    tasksApi.myResult(taskId).catch(() => null)
  ])

  return normalizeQuest({ ...detail, ...(myResult || {}) })
}

export async function submitQuestAnswer(taskId, answer) {
  const payload = { answerData: { answer } }
  if (!isApiEnabled) {
    return normalizeQuestSubmission({ answerData: payload.answerData, submittedAt: new Date().toISOString() })
  }

  const submitted = await tasksApi.submit(taskId, payload)
  const result = await tasksApi.myResult(taskId).catch(() => submitted)
  return normalizeQuestSubmission(result || submitted)
}

export async function loadLearningResources({ required = false } = {}) {
  if (!isApiEnabled) return required ? requiredLearningFallback() : learningResources

  const page = required
    ? await learningApi.required({ page: 0, size: 10 })
    : await learningApi.openLearning({ page: 0, size: 10 })
  return pageItems(page).map(normalizeLearningResource)
}

export async function loadLearningResourceDetail(resourceId) {
  const fallback = learningResources.find((item) => String(item.id) === String(resourceId)) || learningResources[0]
  if (!isApiEnabled) return fallback

  const resource = await learningApi.content(resourceId)
  return normalizeLearningResource(resource)
}

export async function likeLearningResource(resourceId) {
  if (!isApiEnabled) return null

  return learningApi.like(resourceId)
}

export async function downloadLearningResource(resourceId) {
  if (!isApiEnabled) return null

  return learningApi.download(resourceId)
}

export async function completeLearningResource(resourceId) {
  if (!isApiEnabled) return null

  return learningApi.complete(resourceId)
}

export async function bookmarkLearningResource(resourceId) {
  if (!isApiEnabled) return null

  return bookmarksApi.add({ targetType: 'LEARNING_CONTENT', targetId: Number(resourceId) })
}

export async function loadReplayItems() {
  if (!isApiEnabled) return allReplayItems

  const page = await classroomApi.replays({ page: 0, size: 10 })
  return pageItems(page).map(normalizeReplay)
}

export async function loadMyReplayItems() {
  if (!isApiEnabled) return allReplayItems

  const page = await classroomApi.myReplays({ page: 0, size: 10 })
  return pageItems(page).map(normalizeReplay)
}

export async function loadCurriculumData() {
  if (!isApiEnabled) return { classroomPhases, curriculumWeeks, curriculumDays }

  const coursesPage = await classroomApi.myCourses()
  const course = pageItems(coursesPage)[0]
  const courseId = course?.id || course?.courseId
  if (!courseId) return { classroomPhases: [], curriculumWeeks: [], curriculumDays: [] }

  const weeksPage = await classroomApi.weeks(courseId)
  const weeks = pageItems(weeksPage).map(normalizeWeek)
  const activeWeek = pageItems(weeksPage).find((week) => week.active || week.isCurrent) || pageItems(weeksPage)[0]
  const weekId = activeWeek?.id || activeWeek?.weekId
  if (!weekId) {
    return {
      classroomPhases,
      curriculumWeeks: weeks,
      curriculumDays: []
    }
  }

  const sessionsPage = await classroomApi.sessionsInRange(courseId, {
    startDate: activeWeek?.startDate,
    endDate: activeWeek?.endDate,
    page: 0,
    size: 50
  }).catch(() => classroomApi.sessions(courseId, weekId))
  const days = normalizeCurriculumDays(pageItems(sessionsPage))

  return {
    classroomPhases,
    curriculumWeeks: weeks,
    curriculumDays: days
  }
}
