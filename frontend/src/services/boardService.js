import { isApiEnabled } from '../api/client'
import { boardsApi } from '../api/modules'
import { anonymousPosts, mentoringPosts, notices, openBoardPosts } from '../data/boards'

const boardCodes = {
  anonymous: ['anonymity', 'ANONYMOUS'],
  helpNotice: ['notice', 'NOTICE'],
  mentoringNotice: ['mento-notice', 'MENTO_NOTICE'],
  mentoringQna: ['mento-qna', 'MENTO_QNA'],
  mentoringReviews: ['mento-review', 'MENTO_REVIEW'],
  mentoringStories: ['mento-story', 'MENTO_STORY'],
  open: ['free', 'FREE']
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

function formatDate(value) {
  if (!value) return ''
  return String(value).split('T')[0].replaceAll('-', '.')
}

function normalizePost(item) {
  return {
    id: item.id,
    category: item.categoryName || item.category?.name || item.category || '일반',
    title: item.title,
    author: item.displayName || item.authorName || item.author || '익명',
    date: formatDate(item.createdAt || item.date),
    views: item.viewCount ?? item.views ?? 0
  }
}

async function fetchFirstAvailableBoard(codeCandidates) {
  for (const code of codeCandidates) {
    const page = await boardsApi.posts(code, { page: 0, size: 10 }).catch(() => null)
    const items = pageItems(page)
    if (items.length) return items
  }
  return []
}

async function loadBoardPosts(key) {
  const fallback = fallbackPosts[key] || fallbackPosts.open
  if (!isApiEnabled) return fallback

  const items = await fetchFirstAvailableBoard(boardCodes[key] || boardCodes.open)
  const posts = items.map(normalizePost)
  return posts.length ? posts : fallback
}

async function resolveCategoryId(boardCode, categoryName) {
  const categories = await boardsApi.categories(boardCode).catch(() => [])
  const items = pageItems(categories)
  const category = items.find((item) => item.name === categoryName || item.label === categoryName || item.categoryName === categoryName || item.code === categoryName)
  const fallbackCategory = items[0]
  return category?.id || category?.categoryId || fallbackCategory?.id || fallbackCategory?.categoryId || null
}

async function createBoardPost(key, form) {
  const boardCode = (boardCodes[key] || boardCodes.open)[0]
  if (!isApiEnabled) return { id: 'mock-created', ...form }

  const categoryId = await resolveCategoryId(boardCode, form.category || '일반')
  if (!categoryId) {
    throw new Error(`Missing board category for ${boardCode}`)
  }

  return boardsApi.createPost(boardCode, {
    categoryId,
    title: form.title,
    contentType: 'TEXT',
    contentText: form.content,
    contentHtml: null,
    contentJson: null,
    fileIds: []
  })
}

export function loadCommunityPosts(variant) {
  return loadBoardPosts(variant)
}

export function loadHelpNoticePosts() {
  return loadBoardPosts('helpNotice')
}

export function loadMentoringPosts(variant) {
  const key = `mentoring${variant.charAt(0).toUpperCase()}${variant.slice(1)}`
  return loadBoardPosts(key)
}

export function createCommunityPost(form) {
  return createBoardPost('open', form)
}

export function createMentoringReviewPost(form) {
  const content = form.meetup ? `[간담회] ${form.meetup}\n\n${form.content}` : form.content
  return createBoardPost('mentoringReviews', { ...form, category: '일반', content })
}
