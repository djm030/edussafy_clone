import { isApiEnabled } from '../api/client'
import { boardsApi, filesApi } from '../api/modules'
import { anonymousPosts, faqs, mentoringPosts, notices, openBoardPosts, ruleCategories } from '../data/boards'
import { boardCodes as canonicalBoardCodes } from '../constants/boardCodes'

const boardCodeByKey = {
  anonymous: canonicalBoardCodes.anonymity,
  helpNotice: canonicalBoardCodes.notice,
  mentoringNotice: canonicalBoardCodes.mentoringNotice,
  mentoringQna: canonicalBoardCodes.mentoringQna,
  mentoringReviews: canonicalBoardCodes.meetupReview,
  mentoringStories: canonicalBoardCodes.mentoringStory,
  open: canonicalBoardCodes.free
}

const fallbackPosts = {
  anonymous: anonymousPosts,
  helpNotice: notices,
  mentoringNotice: mentoringPosts.notice,
  mentoringQna: mentoringPosts.qna,
  mentoringReviews: mentoringPosts.reviews,
  mentoringStories: mentoringPosts.stories,
  open: openBoardPosts
}

function pageItems(page) {
  if (Array.isArray(page)) return page
  return page?.content || page?.items || page?.data || []
}

function normalizePostId(value) {
  if (value === null || value === undefined || value === '') return ''
  return String(value)
}

function matchPostId(item, postId) {
  const target = normalizePostId(postId)
  return normalizePostId(item.id) === target || normalizePostId(item.postId) === target
}

function formatDate(value) {
  if (!value) return ''
  return String(value).split('T')[0].replaceAll('-', '.')
}

function formatDateTime(value) {
  if (!value) return ''
  return String(value).replace('T', ' ').slice(0, 16).replaceAll('-', '.')
}

function normalizePost(item) {
  const postId = normalizePostId(item.postId ?? item.id)
  return {
    id: postId,
    postId,
    category: item.categoryName || item.category?.name || item.category || '일반',
    title: item.title,
    author: item.displayName || item.authorName || item.author || '익명',
    date: formatDate(item.createdAt || item.date),
    views: item.viewCount ?? item.views ?? 0,
    likes: item.likeCount ?? item.likes ?? 0
  }
}

function plainText(value) {
  return value ? String(value).replace(/<[^>]*>/g, '').trim() : ''
}


function normalizeFaq(item) {
  return {
    id: item.id,
    category: item.categoryName || item.category?.name || item.category || 'FAQ',
    question: item.title || item.question || 'FAQ',
    answer: item.contentText || item.content || item.body || plainText(item.contentHtml) || item.answer || ''
  }
}

function normalizeRule(item) {
  return {
    id: item.id,
    title: item.title || item.categoryName || item.category?.name || '학사규정',
    body: item.contentText || item.content || item.body || plainText(item.contentHtml) || item.description || ''
  }
}

function fallbackBody(post) {
  if (!post) return ''
  return `${post.title}에 대한 상세 내용입니다. SSAFY EDU 화면 흐름과 동일하게 제목, 작성자, 등록일, 본문 영역을 확인할 수 있습니다.`
}

function normalizePostDetail(item, fallback) {
  const base = item || fallback || {}
  return {
    ...normalizePost(base),
    category: base.categoryName || base.category?.name || base.category || fallback?.category || '일반',
    title: base.title || fallback?.title || '',
    author: base.displayName || base.authorName || base.author || fallback?.author || '익명',
    date: formatDate(base.createdAt || base.date || fallback?.date),
    views: base.viewCount ?? base.views ?? fallback?.views ?? 0,
    likes: base.likeCount ?? base.likes ?? fallback?.likes ?? 0,
    body: base.contentText || base.content || base.body || plainText(base.contentHtml) || fallback?.body || fallbackBody(fallback || base),
    files: base.files || []
  }
}

function normalizeComment(item) {
  return {
    id: item.id,
    parentId: item.parentId,
    body: item.content || '',
    author: item.displayName || item.author || '익명',
    isMine: Boolean(item.isMine),
    date: formatDateTime(item.createdAt || item.date)
  }
}

function boardKey(group, variant) {
  if (group === 'mentoring') {
    if (variant === 'qna') return 'mentoringQna'
    if (variant === 'notice') return 'mentoringNotice'
    if (variant === 'reviews') return 'mentoringReviews'
    return 'mentoringStories'
  }
  return variant || 'open'
}

async function fetchBoardItems(boardCode) {
  const page = await boardsApi.posts(boardCode, { page: 0, size: 10 })
  return pageItems(page)
}

async function loadBoardPosts(key) {
  const fallback = fallbackPosts[key] || fallbackPosts.open
  if (!isApiEnabled) return fallback

  const items = await fetchBoardItems(boardCodeByKey[key] || boardCodeByKey.open)
  return items.map(normalizePost)
}

async function fetchBoardDetail(key, postId) {
  const fallback = fallbackPosts[key] || fallbackPosts.open
  const fallbackPost = fallback.find((item) => matchPostId(item, postId)) || fallback[0]
  if (!isApiEnabled) return normalizePostDetail(fallbackPost, fallbackPost)

  const boardCode = boardCodeByKey[key] || boardCodeByKey.open
  const post = await boardsApi.post(boardCode, postId)
  if (!post) {
    throw new Error('게시글을 찾을 수 없습니다.')
  }

  return normalizePostDetail(post, fallbackPost)
}

async function resolveCategoryId(boardCode, categoryName) {
  const categories = await boardsApi.categories(boardCode).catch(() => [])
  const items = pageItems(categories)
  const category = items.find((item) => item.name === categoryName || item.label === categoryName || item.categoryName === categoryName || item.code === categoryName)
  const fallbackCategory = items[0]
  return category?.id || category?.categoryId || fallbackCategory?.id || fallbackCategory?.categoryId || null
}


async function uploadBoardFile(file) {
  if (!file) return null

  const formData = new FormData()
  formData.append('file', file)
  formData.append('targetType', 'BOARD_POST')
  formData.append('fileRole', 'ATTACHMENT')

  const uploaded = await filesApi.upload(formData)
  return uploaded?.id || uploaded?.fileId || null
}

async function createBoardPost(key, form) {
  const boardCode = boardCodeByKey[key] || boardCodeByKey.open
  if (!isApiEnabled) return { id: 'mock-created', postId: 'mock-created', ...form }

  const categoryId = await resolveCategoryId(boardCode, form.category || '일반')
  if (!categoryId) {
    throw new Error(`Missing board category for ${boardCode}`)
  }

  const fileId = await uploadBoardFile(form.file)

  return boardsApi.createPost(boardCode, {
    categoryId,
    title: form.title,
    contentType: 'TEXT',
    contentText: form.content,
    contentHtml: null,
    contentJson: null,
    fileIds: fileId ? [fileId] : []
  })
}

export function loadCommunityPosts(variant) {
  return loadBoardPosts(variant)
}

export function loadCommunityPostDetail(variant, postId) {
  return fetchBoardDetail(variant, postId)
}

export async function likeBoardPost(group, variant, postId) {
  if (!isApiEnabled) return null

  const key = boardKey(group, variant)
  return boardsApi.likePost(boardCodeByKey[key] || boardCodeByKey.open, postId)
}

export async function loadBoardComments(group, variant, postId) {
  if (!isApiEnabled) return []

  const key = boardKey(group, variant)
  const comments = await boardsApi.comments(boardCodeByKey[key] || boardCodeByKey.open, postId)
  return pageItems(comments).map(normalizeComment)
}

export async function createBoardComment(group, variant, postId, content) {
  if (!isApiEnabled) return { id: 'mock-comment' }

  const key = boardKey(group, variant)
  return boardsApi.createComment(boardCodeByKey[key] || boardCodeByKey.open, postId, { content })
}

export async function deleteBoardComment(commentId) {
  if (!isApiEnabled) return true

  await boardsApi.deleteComment(commentId)
  return true
}

export function loadHelpNoticePosts() {
  return loadBoardPosts('helpNotice')
}

export function loadHelpNoticeDetail(postId) {
  return fetchBoardDetail('helpNotice', postId)
}

export function loadMentoringPosts(variant) {
  const key = `mentoring${variant.charAt(0).toUpperCase()}${variant.slice(1)}`
  return loadBoardPosts(key)
}

export function loadMentoringPostDetail(variant, postId) {
  const key = `mentoring${variant.charAt(0).toUpperCase()}${variant.slice(1)}`
  return fetchBoardDetail(key, postId)
}

export function createCommunityPost(form) {
  return createBoardPost('open', form)
}

export function createMentoringReviewPost(form) {
  const content = form.meetup ? `[간담회] ${form.meetup}\n\n${form.content}` : form.content
  return createBoardPost('mentoringReviews', { ...form, category: '일반', content })
}

export async function loadHelpFaqs() {
  if (!isApiEnabled) return faqs

  const page = await boardsApi.posts(canonicalBoardCodes.faq, { page: 0, size: 20 })
  return pageItems(page).map(normalizeFaq).filter((item) => item.question || item.answer)
}

export async function loadHelpRules() {
  if (!isApiEnabled) return ruleCategories

  const page = await boardsApi.posts(canonicalBoardCodes.rule, { page: 0, size: 20 })
  return pageItems(page).map(normalizeRule).filter((item) => item.title || item.body)
}
