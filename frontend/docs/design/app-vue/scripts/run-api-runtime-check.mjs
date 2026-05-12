#!/usr/bin/env node
import { mkdirSync, readdirSync, readFileSync, unlinkSync, writeFileSync } from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const scriptDir = path.dirname(__filename)
const appVueDir = path.resolve(scriptDir, '..')
const inventoryPath = path.join(appVueDir, 'contract-inventory.json')
const outDir = path.join(appVueDir, 'verification-pack', 'api', 'runtime')
const apiBaseUrl = (process.env.API_BASE_URL || 'http://127.0.0.1:8088').replace(/\/$/, '')
const email = process.env.API_SMOKE_EMAIL || 'student@edussafy.local'
const password = process.env.API_SMOKE_PASSWORD || '0000'

const replacements = {
  courseId: '1',
  weekId: '1',
  taskId: '1',
  contentId: '1',
  postId: '1',
  surveyId: '1',
  inquiryId: '1'
}

function slug(value) {
  return String(value).replace(/^https?:\/\//, '').replace(/[^A-Za-z0-9]+/g, '-').replace(/^-|-$/g, '').toLowerCase() || 'root'
}

function artifactSlug(row) {
  return row.endpoint.includes('{') ? slug(row.endpoint) : slug(row.endpointPath)
}

function redactHeaders(headers) {
  return Object.fromEntries(Object.entries(headers).map(([key, value]) => [key, key.toLowerCase() === 'authorization' ? 'Bearer <redacted>' : value]))
}

function endpointToPath(endpoint, sampleReplacements = {}) {
  const boardPostMatch = endpoint.match(/^GET\s+(\/api\/v1\/boards\/([^/]+)\/posts\/)\{postId\}$/i)
  if (boardPostMatch) {
    const [, prefix, boardCode] = boardPostMatch
    const postId = sampleReplacements[`boardPost:${boardCode}`]
    if (postId) return `${prefix}${postId}`
  }
  return endpoint.replace(/^GET\s+|^POST\s+|^PATCH\s+/i, '').replace(/\{([^}]+)\}/g, (_, key) => replacements[key] || '1')
}

function methodOf(endpoint) {
  const match = endpoint.match(/^(GET|POST|PATCH|DELETE)\s+/i)
  return match ? match[1].toUpperCase() : 'GET'
}

async function request(pathname, options = {}) {
  const headers = { ...(options.headers || {}) }
  const response = await fetch(`${apiBaseUrl}${pathname}`, { ...options, headers })
  const text = await response.text()
  let body = null
  try {
    body = text ? JSON.parse(text) : null
  } catch {
    body = text.slice(0, 500)
  }
  return { statusCode: response.status, ok: response.ok, body }
}

function shapeOf(value) {
  if (Array.isArray(value)) return `array(${value.length})`
  if (!value || typeof value !== 'object') return typeof value
  return Object.keys(value).slice(0, 12).join(',')
}

function collectionItems(value) {
  if (Array.isArray(value)) return value
  return value?.content || value?.items || value?.data || []
}

async function login() {
  const result = await request('/api/v1/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  })
  const token = result.body?.data?.accessToken || result.body?.accessToken
  const refreshToken = result.body?.data?.refreshToken || result.body?.refreshToken
  return { ...result, token, refreshToken }
}

async function getJson(pathname, token) {
  const headers = token ? { Authorization: `Bearer ${token}` } : {}
  const result = await request(pathname, { headers })
  const data = result.body?.data ?? result.body
  return {
    endpointPath: pathname,
    method: 'GET',
    statusCode: result.statusCode,
    success: result.ok && (result.body?.success !== false),
    dataShapeSummary: shapeOf(data),
    redactedRequest: { headers: redactHeaders(headers) },
    redactedResponseSample: shapeOf(data),
    errorCode: result.body?.errorCode || null,
    message: result.body?.message || null
  }
}

async function findCategoryId(boardCode, token) {
  const raw = await request(`/api/v1/boards/${boardCode}/categories`, {
    headers: { Authorization: `Bearer ${token}` }
  })
  const data = raw.body?.data ?? raw.body
  const items = collectionItems(data)
  return items[0]?.id || items[0]?.categoryId || null
}

async function findBoardPostId(boardCode, token) {
  const raw = await request(`/api/v1/boards/${boardCode}/posts`, {
    headers: { Authorization: `Bearer ${token}` }
  })
  const data = raw.body?.data ?? raw.body
  const items = collectionItems(data)
  return items[0]?.id || items[0]?.postId || null
}

async function createBoardPost(boardCode, token) {
  const categoryId = await findCategoryId(boardCode, token)
  if (!categoryId) return null
  const result = await request(`/api/v1/boards/${boardCode}/posts`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
    body: JSON.stringify({
      categoryId,
      title: `Runtime smoke ${boardCode} post`,
      contentType: 'TEXT',
      contentText: 'Runtime smoke evidence.',
      contentHtml: null,
      contentJson: null,
      fileIds: []
    })
  })
  return result.body?.data?.id || result.body?.id || null
}

async function resolveBoardPostSamples(endpoints, token) {
  const boardCodes = new Set()
  for (const endpoint of endpoints) {
    const match = endpoint.match(/^GET\s+\/api\/v1\/boards\/([^/]+)\/posts\/\{postId\}$/i)
    if (match) boardCodes.add(match[1])
  }

  const replacementsByBoard = {}
  const sampleRows = []
  for (const boardCode of boardCodes) {
    let postId = await findBoardPostId(boardCode, token)
    let source = 'existing-list'
    if (!postId) {
      postId = await createBoardPost(boardCode, token)
      source = postId ? 'created-runtime-smoke-post' : 'unresolved'
    }
    if (postId) replacementsByBoard[`boardPost:${boardCode}`] = String(postId)
    sampleRows.push({ boardCode, postId: postId ? String(postId) : null, source })
  }
  return { replacementsByBoard, sampleRows }
}

async function runFormChecks(token, refreshToken) {
  const headers = { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' }
  const rows = []
  async function add(name, pathname, body, method = 'POST') {
    const result = await request(pathname, { method, headers, body: JSON.stringify(body) })
    rows.push({
      name,
      endpointPath: pathname,
      method,
      statusCode: result.statusCode,
      success: result.ok && result.body?.success !== false,
      errorCode: result.body?.errorCode || null,
      message: result.body?.message || null,
      dataShapeSummary: shapeOf(result.body?.data ?? result.body),
      redactedRequest: { headers: redactHeaders(headers), bodyShape: shapeOf(body) }
    })
  }

  if (refreshToken) {
    await add('auth refresh', '/api/v1/auth/refresh', { refreshToken })
  }

  const categoryCache = new Map()
  async function categoryId(boardCode) {
    if (categoryCache.has(boardCode)) return categoryCache.get(boardCode)
    const result = await getJson(`/api/v1/boards/${boardCode}/categories`, token)
    const raw = await request(`/api/v1/boards/${boardCode}/categories`, { headers: { Authorization: `Bearer ${token}` } })
    const data = raw.body?.data ?? raw.body
    const items = Array.isArray(data) ? data : data?.content || data?.items || data?.data || []
    const id = items[0]?.id || items[0]?.categoryId || null
    categoryCache.set(boardCode, id)
    rows.push({ name: `${boardCode} category lookup`, ...result, categoryIdFound: Boolean(id) })
    return id
  }

  const freeCategoryId = await categoryId('free')
  if (freeCategoryId) await add('community board write', '/api/v1/boards/free/posts', { categoryId: freeCategoryId, title: 'Runtime smoke community post', contentType: 'TEXT', contentText: 'Runtime smoke evidence.', contentHtml: null, contentJson: null, fileIds: [] })

  const docCategoryId = await categoryId('doc-req')
  if (docCategoryId) await add('document submit without file', '/api/v1/boards/doc-req/posts', { categoryId: docCategoryId, title: 'Runtime smoke document', contentType: 'TEXT', contentText: 'Runtime smoke evidence.', contentHtml: null, contentJson: null, fileIds: [] })

  await add('inquiry write', '/api/v1/inquiries', { category: '시스템', title: 'Runtime smoke inquiry', content: 'Runtime smoke evidence.' })

  const reviewCategoryId = await categoryId('mento-review')
  if (reviewCategoryId) await add('mentoring review write', '/api/v1/boards/mento-review/posts', { categoryId: reviewCategoryId, title: 'Runtime smoke review', contentType: 'TEXT', contentText: 'Runtime smoke evidence.', contentHtml: null, contentJson: null, fileIds: [] })

  await add('profile save', '/api/v1/users/me', { phoneNumber: '010-0000-0001' }, 'PATCH')
  await add('password save same password', '/api/v1/auth/password', { currentPassword: password, newPassword: password }, 'PATCH')
  await add('survey submit sample', '/api/v1/surveys/1/submit', { answers: { smoke: 'ok' } })
  await add('quest submit sample', '/api/v1/tasks/1/submit', { answerData: { answer: 'Runtime smoke answer' } })

  return rows
}

const inventory = JSON.parse(readFileSync(inventoryPath, 'utf8'))
const capturedAt = new Date().toISOString()
mkdirSync(outDir, { recursive: true })
for (const fileName of readdirSync(outDir)) {
  if (fileName.endsWith('.api.json')) unlinkSync(path.join(outDir, fileName))
}

const loginResult = await login()
const endpointRows = []
let boardPostSamples = []
if (loginResult.token) {
  const endpoints = new Map()
  const endpointLabels = new Map()
  for (const row of inventory.canonicalRows) {
    for (const endpoint of row.apiEndpoints || []) {
      if (!endpoint || endpoint === 'N/A') continue
      if (methodOf(endpoint) !== 'GET') continue
      endpointLabels.set(endpoint, endpoint)
    }
  }
  const resolvedSamples = await resolveBoardPostSamples(endpointLabels.keys(), loginResult.token)
  boardPostSamples = resolvedSamples.sampleRows
  for (const endpoint of endpointLabels.keys()) {
    endpoints.set(endpointToPath(endpoint, resolvedSamples.replacementsByBoard), endpoint)
  }
  for (const [pathname, endpoint] of endpoints) {
    const result = await getJson(pathname, loginResult.token)
    endpointRows.push({ endpoint, ...result })
  }
}

const formRows = loginResult.token ? await runFormChecks(loginResult.token, loginResult.refreshToken) : []
const summary = {
  capturedAt,
  command: 'API_BASE_URL=<url> node frontend/docs/design/app-vue/scripts/run-api-runtime-check.mjs',
  apiBaseUrl,
  usedMockFallback: false,
  apiRuntimeExercised: Boolean(loginResult.token),
  apiCompleteClaim: false,
  login: {
    email,
    statusCode: loginResult.statusCode,
    success: Boolean(loginResult.token),
    token: loginResult.token ? '<redacted>' : null,
    errorCode: loginResult.body?.errorCode || null,
    message: loginResult.body?.message || null
  },
  summary: {
    getEndpoints: endpointRows.length,
    getEndpointSuccesses: endpointRows.filter((row) => row.success).length,
    formChecks: formRows.length,
    formCheckSuccesses: formRows.filter((row) => row.success).length
  },
  blocker: loginResult.token ? null : 'BLOCKED: real API login failed; API completion cannot be claimed.',
  boardPostSamples,
  endpoints: endpointRows,
  formInteractions: formRows
}

writeFileSync(path.join(outDir, 'api-runtime-summary.json'), `${JSON.stringify(summary, null, 2)}\n`)
for (const row of endpointRows) {
  writeFileSync(path.join(outDir, `${artifactSlug(row)}.api.json`), `${JSON.stringify(row, null, 2)}\n`)
}
writeFileSync(path.join(outDir, 'form-interactions-runtime.json'), `${JSON.stringify({ capturedAt, rows: formRows }, null, 2)}\n`)
console.log(JSON.stringify(summary, null, 2))
