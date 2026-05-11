import { isApiEnabled } from '../api/client'
import { surveysApi } from '../api/modules'
import { surveys as mockSurveys } from '../data/boards'

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function formatDate(value) {
  if (!value) return ''
  return String(value).split('T')[0].replaceAll('-', '.')
}

function statusTone(status) {
  if (status === '진행중' || status === 'OPEN') return 'blue'
  if (status === '결과공개' || status === 'RESULT_OPEN') return 'green'
  return 'slate'
}

function normalizeSurvey(item) {
  const status = item.statusLabel || item.status || '진행중'

  return {
    id: item.id,
    title: item.title,
    period: `${formatDate(item.openAt || item.startDate)} ~ ${formatDate(item.closeAt || item.endDate)}`,
    status,
    statusTone: item.statusTone || statusTone(status),
    target: item.targetName || item.target || item.targetType || '교육생'
  }
}

export async function loadSurveys() {
  if (!isApiEnabled) return mockSurveys

  const page = await surveysApi.list({ page: 0, size: 12 }).catch(() => null)
  const items = pageItems(page).map(normalizeSurvey)
  return items.length ? items : mockSurveys
}
