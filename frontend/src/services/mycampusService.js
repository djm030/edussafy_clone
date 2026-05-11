import { isApiEnabled } from '../api/client'
import { agreementsApi, attendanceApi, pointsApi, usersApi } from '../api/modules'
import {
  attendanceDays,
  attendanceSummary,
  educationStatus,
  pledges,
  pointHistory,
  pointSummary
} from '../data/mycampus'

const attendanceLabels = {
  ABSENT: '결석',
  EARLY_LEAVE: '조퇴',
  HOLIDAY: '공휴일',
  LATE: '지각',
  NORMAL: '출석',
  OUT: '외출',
  PRESENT: '출석'
}

const pointTypeLabels = {
  ACTIVITY: '활동포인트',
  ATTENDANCE: '기본포인트',
  BASE: '기본포인트',
  SCHOLARSHIP: '장학포인트',
  TASK: '활동포인트'
}

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function formatDate(value) {
  if (!value) return '-'
  return String(value).split('T')[0].replaceAll('-', '.')
}

function formatTime(value) {
  if (!value) return '-'
  const text = String(value)
  return text.includes('T') ? text.split('T')[1].slice(0, 5) : text.slice(0, 5)
}

function dayFromDate(value) {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return ''
  return new Intl.DateTimeFormat('ko-KR', { weekday: 'short' }).format(date).replace('요일', '')
}

function normalizePointSummary(summary, campusSummary) {
  const stat = summary || campusSummary?.stat || {}
  const totalPoints = stat.totalExp ?? stat.totalPoints ?? pointSummary.totalPoints
  const nextLevel = stat.nextLevelExp ?? Math.max(pointSummary.nextLevel, totalPoints + 160)

  return {
    ...pointSummary,
    level: stat.levelName || (stat.levelNo ? `Lv. ${stat.levelNo}` : pointSummary.level),
    totalPoints,
    scholarshipPoints: stat.scholarshipPoint ?? stat.scholarshipPoints ?? pointSummary.scholarshipPoints,
    nextLevel,
    rank: campusSummary?.user
      ? `${campusSummary.user.region || pointSummary.rank.split(' ')[0]} ${campusSummary.user.classNo || 6}반`
      : pointSummary.rank
  }
}

function normalizePointTransaction(item) {
  const amount = item.amount ?? item.point ?? item.exp ?? 0
  const type = item.transactionType || item.type || 'ACTIVITY'

  return {
    id: item.id || `${item.createdAt || item.date}-${amount}`,
    title: item.title || item.reason || item.description || '포인트 적립',
    type: pointTypeLabels[type] || item.typeName || type,
    amount: Number(amount) > 0 ? `+${amount}` : `${amount}`,
    date: formatDate(item.createdAt || item.transactionDate || item.date)
  }
}

function normalizeAttendanceDay(item) {
  const status = item.status || item.attendanceStatus || 'NORMAL'
  const date = item.attendanceDate || item.date || item.calendarDate

  return {
    id: item.id || item.attendanceRecordId || date,
    date: formatDate(date),
    day: item.dayLabel || item.day || dayFromDate(date),
    status: attendanceLabels[status] || item.statusLabel || status,
    checkIn: formatTime(item.checkInAt || item.checkIn),
    checkOut: formatTime(item.checkOutAt || item.checkOut)
  }
}

function summarizeAttendance(days) {
  if (!days.length) return attendanceSummary

  const counts = days.reduce(
    (acc, item) => {
      acc[item.status] = (acc[item.status] || 0) + 1
      return acc
    },
    {}
  )

  return [
    { label: '출석', value: counts['출석'] || 0, tone: 'blue' },
    { label: '지각', value: counts['지각'] || 0, tone: 'slate' },
    { label: '외출', value: counts['외출'] || counts['조퇴'] || 0, tone: 'green' },
    { label: '결석', value: counts['결석'] || 0, tone: 'slate' }
  ]
}

function normalizeAgreement(item) {
  const agreement = item.agreement || item
  const agreedAt = item.agreedAt || item.signedAt || item.createdAt

  return {
    id: item.id || item.agreementId || agreement.id,
    title: agreement.title || item.title || '교육생 서약서',
    status: agreedAt ? '서명완료' : '대기',
    date: formatDate(agreedAt)
  }
}

function buildEducationStatus(summary, campusSummary) {
  const stat = campusSummary?.stat || summary || {}

  return [
    {
      ...educationStatus[0],
      value: stat.attendanceRate ? `${stat.attendanceRate}%` : educationStatus[0].value
    },
    {
      ...educationStatus[1],
      value: stat.completedLearningCount ? `${stat.completedLearningCount}개` : educationStatus[1].value,
      description: '완료한 필수/선택 학습 수'
    },
    {
      ...educationStatus[2],
      value: stat.totalExp ? `${stat.totalExp}P` : educationStatus[2].value,
      description: '누적 경험치 기준'
    },
    {
      ...educationStatus[3],
      value: stat.levelName || educationStatus[3].value,
      description: '현재 학습 레벨'
    }
  ]
}

export async function loadLevelPointsData() {
  if (!isApiEnabled) return { pointSummary, pointHistory }

  const [summary, campusSummary, transactionsPage] = await Promise.all([
    pointsApi.summary().catch(() => null),
    usersApi.campusSummary().catch(() => null),
    pointsApi.transactions({ page: 0, size: 5 }).catch(() => null)
  ])
  const transactions = pageItems(transactionsPage).map(normalizePointTransaction)

  return {
    pointSummary: normalizePointSummary(summary, campusSummary),
    pointHistory: transactions.length ? transactions : pointHistory
  }
}

export async function loadAttendanceData() {
  if (!isApiEnabled) return { attendanceSummary, attendanceDays }

  const page = await attendanceApi.my({ page: 0, size: 7 }).catch(() => null)
  const days = pageItems(page).map(normalizeAttendanceDay)

  return {
    attendanceSummary: summarizeAttendance(days),
    attendanceDays: days.length ? days : attendanceDays
  }
}

export async function loadPledgesData() {
  if (!isApiEnabled) return pledges

  const myAgreements = await agreementsApi.my().catch(() => null)
  const items = pageItems(myAgreements).map(normalizeAgreement)

  if (items.length) return items

  const requiredAgreements = await agreementsApi.list({ requiredOnly: true }).catch(() => null)
  const requiredItems = pageItems(requiredAgreements).map(normalizeAgreement)
  return requiredItems.length ? requiredItems : pledges
}

export async function loadEducationStatusData() {
  if (!isApiEnabled) return educationStatus

  const [summary, campusSummary] = await Promise.all([
    pointsApi.summary().catch(() => null),
    usersApi.campusSummary().catch(() => null)
  ])

  return buildEducationStatus(summary, campusSummary)
}
