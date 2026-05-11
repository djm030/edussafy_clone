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


function normalizeSurveyQuestion(question, index) {
  return {
    id: question.id || question.questionId || index + 1,
    questionNo: question.questionNo || index + 1,
    questionText: question.questionText || question.title || question.label || `질문 ${index + 1}`,
    questionType: question.questionType || question.type || 'TEXT',
    options: Array.isArray(question.options) ? question.options : [],
    isRequired: question.isRequired ?? question.required ?? false
  }
}

function normalizeSurveyDetail(item) {
  const summary = normalizeSurvey(item)
  const questions = (item.questions || item.surveyQuestions || []).map(normalizeSurveyQuestion)

  return {
    ...summary,
    description: item.description || '설문 내용을 확인한 뒤 응답을 제출하세요.',
    formType: item.formType,
    questions
  }
}

function mockSurveyDetail(surveyId) {
  const survey = mockSurveys.find((item) => String(item.id) === String(surveyId)) || mockSurveys[0]

  return {
    ...survey,
    description: '교육 운영 개선을 위한 의견을 남겨 주세요.',
    formType: 'SURVEY',
    questions: [
      { id: 'satisfaction', questionNo: 1, questionText: '이번 교육 운영에 대한 의견을 입력하세요.', questionType: 'TEXT', options: [], isRequired: true }
    ]
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

export async function loadSurveyDetail(surveyId) {
  if (!isApiEnabled) return mockSurveyDetail(surveyId)

  const survey = await surveysApi.detail(surveyId).catch(() => null)
  return survey ? normalizeSurveyDetail(survey) : mockSurveyDetail(surveyId)
}

export async function submitSurveyAnswers(surveyId, answers) {
  if (!isApiEnabled) return { id: surveyId, participantStatus: 'SUBMITTED' }

  return surveysApi.submit(surveyId, { answers })
}
