import { isApiEnabled } from '../api/client'
import { filesApi, inquiriesApi, usersApi } from '../api/modules'
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

function normalizeInquiryDetail(item, fallback) {
  const base = item || fallback || {}
  const status = base.statusLabel || base.status || fallback?.status || '접수'

  return {
    id: base.id || fallback?.id,
    category: base.categoryName || base.category || fallback?.category || '문의',
    title: base.title || fallback?.title || '',
    status,
    statusTone: base.statusTone || fallback?.statusTone || inquiryTone(status),
    date: formatDate(base.createdAt || base.date || fallback?.date),
    content: base.content || base.body || fallback?.content || '문의 상세 내용입니다.',
    answerContent: base.answerContent || base.answer || '',
    answeredByName: base.answeredByName || '',
    answeredAt: formatDate(base.answeredAt)
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

  const page = await inquiriesApi.my({ page: 0, size: 10 })
  return pageItems(page).map(normalizeInquiry)
}

export async function loadInquiryDetail(inquiryId) {
  const fallback = mockInquiries.find((item) => String(item.id) === String(inquiryId)) || mockInquiries[0]
  if (!isApiEnabled) return normalizeInquiryDetail(fallback, fallback)

  const inquiry = await inquiriesApi.detail(inquiryId)
  return normalizeInquiryDetail(inquiry, fallback)
}

export async function loadClassMembers() {
  if (!isApiEnabled) return classMembers

  const page = await usersApi.students({ page: 0, size: 12 })
  const items = pageItems(page).map(normalizeStudent)
  return items.length ? items : []
}


async function uploadInquiryFile(file) {
  if (!file) return null

  const formData = new FormData()
  formData.append('file', file)
  formData.append('targetType', 'INQUIRY')
  formData.append('fileRole', 'ATTACHMENT')

  const uploaded = await filesApi.upload(formData)
  return uploaded?.id || uploaded?.fileId || null
}

export async function createInquiry(form) {
  if (!isApiEnabled) return { id: 'mock-created', ...form }

  const fileId = await uploadInquiryFile(form.file)

  return inquiriesApi.create({
    category: form.category,
    title: form.title,
    content: form.content,
    fileIds: fileId ? [fileId] : []
  })
}
