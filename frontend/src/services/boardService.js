import { isApiEnabled } from '../api/client'
import { boardsApi } from '../api/modules'
import { anonymousPosts, openBoardPosts } from '../data/boards'

const boardCodes = {
  anonymous: ['anonymity', 'ANONYMOUS'],
  open: ['free', 'FREE']
}

const fallbackPosts = {
  anonymous: anonymousPosts,
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

export async function loadCommunityPosts(variant) {
  const fallback = fallbackPosts[variant] || fallbackPosts.open
  if (!isApiEnabled) return fallback

  const items = await fetchFirstAvailableBoard(boardCodes[variant] || boardCodes.open)
  const posts = items.map(normalizePost)
  return posts.length ? posts : fallback
}
