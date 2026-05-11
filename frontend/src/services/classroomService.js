import { isApiEnabled } from '../api/client'
import { classroomApi, learningApi, tasksApi } from '../api/modules'
import { allReplayItems, learningResources, questItems } from '../data/classroom'

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function formatDateTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 16).replaceAll('-', '.')
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
