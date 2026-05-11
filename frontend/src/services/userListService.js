import { isApiEnabled } from '../api/client'
import { inquiriesApi, usersApi } from '../api/modules'
import { classMembers, inquiries as mockInquiries } from '../data/boards'

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function formatDate(value) {
  if (!value) return ''
  return String(value).split('T')[0].replaceAll('-', '.')
}

function inquiryTone(status) {
  if (status === '답변완료' || status === 'ANSWERED') return 'green'
  if (status === '접수' || status === 'OPEN') return 'blue'
  return 'slate'
}

function normalizeInquiry(item) {
  const status = item.statusLabel || item.status || '접수'

  return {
    id: item.id,
    category: item.categoryName || item.category || '문의',
    title: item.title,
    status,
    statusTone: item.statusTone || inquiryTone(status),
    date: formatDate(item.createdAt || item.date)
  }
}

function normalizeStudent(item) {
  return {
    id: item.id || item.userId,
    name: item.name || item.displayName || '교육생',
    role: item.roleLabel || item.role || '교육생',
    track: item.trackName || item.track || item.majorTrack || 'Java 전공',
    campus: item.region || item.campus || '서울',
    className: item.className || (item.classNo ? `${item.classNo}반` : '6반')
  }
}

export async function loadInquiries() {
  if (!isApiEnabled) return mockInquiries

  const page = await inquiriesApi.my({ page: 0, size: 10 }).catch(() => null)
  const items = pageItems(page).map(normalizeInquiry)
  return items.length ? items : mockInquiries
}

export async function loadClassMembers() {
  if (!isApiEnabled) return classMembers

  const page = await usersApi.students({ page: 0, size: 12 }).catch(() => null)
  const items = pageItems(page).map(normalizeStudent)
  return items.length ? items : classMembers
}

export async function createInquiry(form) {
  if (!isApiEnabled) return { id: 'mock-created', ...form }

  return inquiriesApi.create({
    category: form.category,
    title: form.title,
    content: form.content,
    fileIds: []
  })
}
