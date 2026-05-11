import { isApiEnabled } from '../api/client'
import { surveysApi } from '../api/modules'
import { meetups as mockMeetups, surveys as mockSurveys } from '../data/boards'

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

function normalizeMeetup(item) {
  const status = item.statusLabel || item.participantStatus || item.status || '신청가능'

  return {
    id: item.id || item.surveyId,
    title: item.title || item.surveyTitle || '간담회 신청',
    mentor: item.mentorName || item.hostName || item.ownerName || '멘토',
    date: [formatDate(item.openAt || item.startAt || item.createdAt), formatDate(item.closeAt || item.endAt)].filter(Boolean).join(' ~ '),
    capacity: item.capacity ? `${item.capacity}명` : item.capacityLabel || '정원 확인중',
    status,
    statusTone: item.statusTone || statusTone(status)
  }
}

function normalizeMeetupParticipation(item) {
  const survey = item.survey || item
  const formType = item.formType || survey.formType
  const status = item.participantStatus || item.status || '신청완료'

  return {
    id: item.id || item.participantId || survey.id,
    surveyId: survey.id || item.surveyId,
    formType,
    title: survey.title || item.title || '간담회 신청',
    mentor: survey.mentorName || item.mentorName || '멘토',
    date: [formatDate(survey.openAt || item.submittedAt || item.createdAt), formatDate(survey.closeAt)].filter(Boolean).join(' ~ '),
    capacity: survey.capacity ? `${survey.capacity}명` : item.capacityLabel || '신청 내역',
    status,
    statusTone: item.statusTone || statusTone(status)
  }
}

export async function loadSurveys() {
  if (!isApiEnabled) return mockSurveys

  const page = await surveysApi.list({ page: 0, size: 12 }).catch(() => null)
  const items = pageItems(page).map(normalizeSurvey)
  return items.length ? items : mockSurveys
}

export async function loadMeetupApplications() {
  if (!isApiEnabled) return mockMeetups

  const page = await surveysApi.list({ formType: 'MEETUP_APPLICATION', page: 0, size: 12 }).catch(() => null)
  const items = pageItems(page).map(normalizeMeetup)
  return items.length ? items : mockMeetups
}

export async function loadMyMeetupParticipations() {
  if (!isApiEnabled) return []

  const page = await surveysApi.myParticipations({ formType: 'MEETUP_APPLICATION', page: 0, size: 12 }).catch(() => null)
  return pageItems(page).map(normalizeMeetupParticipation)
}

export async function submitMeetupApplication(meetupId) {
  if (!isApiEnabled) return { id: meetupId, status: '신청완료' }

  return surveysApi.submit(meetupId, { answers: {} })
}
