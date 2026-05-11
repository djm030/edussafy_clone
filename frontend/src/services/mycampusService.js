import { isApiEnabled } from '../api/client'
import { agreementsApi, attendanceApi, authApi, boardsApi, bookmarksApi, filesApi, learningApi, pointsApi, usersApi } from '../api/modules'
import {
  attendanceDays,
  attendanceSummary,
  bookmarkedLearningItems,
  documents,
  educationStatus,
  eLearningItems,
  pledges,
  pointHistory,
  pointSummary,
  profileInfo
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

function normalizeProfile(user) {
  const campus = user.campus || [
    user.generation && `${user.generation}기`,
    user.region,
    user.classNo && `${user.classNo}반`
  ].filter(Boolean).join(' ')

  return {
    ...profileInfo,
    name: user.name || profileInfo.name,
    email: user.email || profileInfo.email,
    phoneNumber: user.phoneNumber || user.phone || profileInfo.phoneNumber,
    campus: campus || profileInfo.campus
  }
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

function unwrapLearningItem(item) {
  return item.learningContent || item.content || item.target || item.targetContent || item.learning || item
}

function normalizeLearningItem(item) {
  const content = unwrapLearningItem(item)
  const category = content.categoryName || content.category?.name || item.categoryName || '이러닝'
  const type = content.contentType || content.type || item.contentType || '학습자료'
  const progressRate = item.progressRate ?? item.progressPercent ?? item.progress ?? content.progressRate
  const completed = item.completedAt || item.status === 'COMPLETED' || item.progressStatus === 'COMPLETED'
  const statusLabel = completed ? '수강완료' : (progressRate ? `${progressRate}% 진행` : item.statusLabel || '학습중')

  return {
    id: content.id || content.contentId || item.contentId || item.id,
    title: content.title || item.title || '이러닝 콘텐츠',
    breadcrumb: [category, type],
    description: content.description || content.summary || item.description || item.summary || content.title || '학습 콘텐츠입니다.',
    progress: statusLabel,
    status: completed ? '완료' : '학습중',
    date: formatDate(item.updatedAt || item.lastStudiedAt || item.createdAt || content.createdAt),
    views: content.viewCount ?? content.views ?? 0,
    likes: content.likeCount ?? content.likes ?? 0,
    bookmarks: content.bookmarkCount ?? content.bookmarks ?? 0,
    textbook: content.isTextbook ?? type === 'FILE',
    label: content.label || 'E-LEARNING'
  }
}

function normalizeBookmarkedLearningItem(item) {
  const content = unwrapLearningItem(item)
  const category = content.categoryName || content.category?.name || item.categoryName || '찜한 목록'
  const type = content.contentType || content.type || item.targetType || '학습자료'

  return {
    id: content.id || content.contentId || item.targetId || item.contentId || item.id,
    title: content.title || item.title || '찜한 학습자료',
    breadcrumb: [category, type],
    description: content.description || content.summary || item.description || item.summary || content.title || '찜한 학습 콘텐츠입니다.',
    progress: '찜한 콘텐츠',
    status: '찜함',
    date: formatDate(item.bookmarkedAt || item.createdAt || content.createdAt),
    views: content.viewCount ?? content.views ?? 0,
    likes: content.likeCount ?? content.likes ?? 0,
    bookmarks: content.bookmarkCount ?? content.bookmarks ?? 1,
    textbook: content.isTextbook ?? type === 'FILE',
    label: content.label || 'BOOKMARK'
  }
}

function normalizeDocumentSubmission(item) {
  return {
    id: item.id,
    category: item.categoryName || item.category || '서류',
    title: item.title || '서류 제출',
    status: item.statusLabel || item.status || '제출완료',
    statusTone: item.statusTone || 'blue',
    date: formatDate(item.createdAt || item.date)
  }
}

async function resolveDocumentCategoryId(categoryName) {
  const categories = await boardsApi.categories('doc-req').catch(() => [])
  const items = pageItems(categories)
  const category = items.find((item) => item.name === categoryName || item.label === categoryName || item.categoryName === categoryName || item.code === categoryName)
  const fallback = items[0]
  return category?.id || category?.categoryId || fallback?.id || fallback?.categoryId || null
}

async function uploadDocumentFile(file) {
  if (!file) return null

  const formData = new FormData()
  formData.append('file', file)
  formData.append('targetType', 'BOARD_POST')
  formData.append('fileRole', 'ATTACHMENT')

  const uploaded = await filesApi.upload(formData)
  return uploaded?.id || uploaded?.fileId || null
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

export async function loadProfileData() {
  if (!isApiEnabled) return profileInfo

  const user = await usersApi.me().catch(() => null)
  return user ? normalizeProfile(user) : profileInfo
}

export async function saveProfileData(profile) {
  if (!isApiEnabled) return { ...profileInfo, ...profile }

  const saved = await usersApi.updateMe({
    phoneNumber: profile.phoneNumber
  })

  return saved ? normalizeProfile(saved) : { ...profileInfo, ...profile }
}

export async function changePasswordData(passwords) {
  if (!isApiEnabled) return true

  return authApi.changePassword({
    currentPassword: passwords.currentPassword,
    newPassword: passwords.newPassword
  })
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

export async function loadElearningData() {
  if (!isApiEnabled) return eLearningItems

  const page = await learningApi.myProgress({ page: 0, size: 10 }).catch(() => null)
  const items = pageItems(page).map(normalizeLearningItem)
  return items.length ? items : eLearningItems
}

export async function loadBookmarkData() {
  if (!isApiEnabled) return bookmarkedLearningItems

  const bookmarkPage = await bookmarksApi.my({ targetType: 'LEARNING_CONTENT', page: 0, size: 10 }).catch(() => null)
  const bookmarkItems = pageItems(bookmarkPage).map(normalizeBookmarkedLearningItem)
  if (bookmarkItems.length) return bookmarkItems

  const selectedPage = await learningApi.mySelected({ page: 0, size: 10 }).catch(() => null)
  const selectedItems = pageItems(selectedPage).map(normalizeBookmarkedLearningItem)
  return selectedItems.length ? selectedItems : bookmarkedLearningItems
}

export async function loadDocumentsData() {
  if (!isApiEnabled) return documents

  const page = await boardsApi.posts('doc-req', { page: 0, size: 10 }).catch(() => null)
  const items = pageItems(page).map(normalizeDocumentSubmission)
  return items.length ? items : documents
}

export async function submitDocumentData(form) {
  if (!isApiEnabled) {
    return {
      id: 'mock-document',
      category: form.category,
      title: form.title,
      status: '제출완료',
      statusTone: 'blue',
      date: formatDate(new Date().toISOString())
    }
  }

  const categoryId = await resolveDocumentCategoryId(form.category)
  if (!categoryId) {
    throw new Error('Missing document board category')
  }

  const fileId = await uploadDocumentFile(form.file)

  return boardsApi.createPost('doc-req', {
    categoryId,
    title: form.title,
    contentType: 'TEXT',
    contentText: form.content,
    contentHtml: null,
    contentJson: null,
    fileIds: fileId ? [fileId] : []
  })
}
