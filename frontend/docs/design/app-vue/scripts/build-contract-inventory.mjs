#!/usr/bin/env node
import { existsSync, mkdirSync, readdirSync, readFileSync, writeFileSync } from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const scriptDir = path.dirname(__filename)
const appVueDir = path.resolve(scriptDir, '..')
const repoRoot = path.resolve(appVueDir, '../../../..')
const frontendRoot = path.resolve(repoRoot, 'frontend')
const backendRoot = path.resolve(repoRoot, 'backend')

const paths = {
  routes: path.join(frontendRoot, 'src/constants/routes.js'),
  router: path.join(frontendRoot, 'src/router/index.js'),
  navigation: path.join(frontendRoot, 'src/constants/navigation.js'),
  boardCodes: path.join(frontendRoot, 'src/constants/boardCodes.js'),
  apiModules: path.join(frontendRoot, 'src/api/modules.js'),
  services: path.join(frontendRoot, 'src/services'),
  screenshotMap: path.join(appVueDir, '05-screenshot-reference-map.md'),
  frame: path.join(frontendRoot, 'docs/FRAME.md'),
  screenshots: path.join(frontendRoot, 'docs/edu_screnshot'),
  backendV1: path.join(backendRoot, 'src/main/resources/db/migration/V1__init_schema.sql'),
  outJson: path.join(appVueDir, 'contract-inventory.json'),
  outMd: path.join(appVueDir, 'canonical-contract.md'),
  verificationReadme: path.join(appVueDir, 'verification-pack/README.md')
}

function read(filePath) {
  return readFileSync(filePath, 'utf8')
}

function linesBefore(text, index) {
  return text.slice(0, index).split('\n').length
}

function parseRoutePaths(source) {
  const result = []
  const blockMatch = source.match(/export\s+const\s+routePaths\s*=\s*\{([\s\S]*?)\n\}/)
  if (!blockMatch) return result
  const blockStart = blockMatch.index + blockMatch[0].indexOf('{') + 1
  const entryRegex = /([A-Za-z0-9_]+)\s*:\s*'([^']+)'/g
  let match
  while ((match = entryRegex.exec(blockMatch[1]))) {
    result.push({ key: match[1], path: match[2], source: 'routes.js', line: linesBefore(source, blockStart + match.index) })
  }
  return result
}

function splitRouteObjects(routerSource) {
  const start = routerSource.indexOf('const routes = [')
  if (start < 0) return []
  const arrayStart = routerSource.indexOf('[', start)
  const arrayEnd = routerSource.indexOf('\n]', arrayStart)
  const body = routerSource.slice(arrayStart + 1, arrayEnd)
  const rows = []
  let depth = 0
  let inString = false
  let quote = ''
  let rowStart = -1

  for (let index = 0; index < body.length; index += 1) {
    const char = body[index]
    const previous = body[index - 1]
    if (inString) {
      if (char === quote && previous !== '\\') inString = false
      continue
    }
    if (char === '\'' || char === '"' || char === '`') {
      inString = true
      quote = char
      continue
    }
    if (char === '{') {
      if (depth === 0) rowStart = index
      depth += 1
    } else if (char === '}') {
      depth -= 1
      if (depth === 0 && rowStart >= 0) {
        const objectSource = body.slice(rowStart, index + 1)
        rows.push({ source: objectSource, line: linesBefore(routerSource, arrayStart + 1 + rowStart) })
        rowStart = -1
      }
    }
  }
  return rows
}

function parseRouter(routerSource, routePathByKey) {
  return splitRouteObjects(routerSource).map((row) => {
    const pathMatch = row.source.match(/path:\s*([^,}]+)/)
    const nameMatch = row.source.match(/name:\s*'([^']+)'/)
    const redirectMatch = row.source.match(/redirect:\s*([^,}]+)/)
    const componentMatch = row.source.match(/component:\s*([A-Za-z0-9_]+)/)
    const propsMatch = row.source.match(/props:\s*([^,}]+(?:\}))/)
    const pathExpr = pathMatch?.[1]?.trim() || ''
    const redirectExpr = redirectMatch?.[1]?.trim() || ''
    return {
      pathExpression: pathExpr,
      path: resolvePathExpression(pathExpr, routePathByKey),
      name: nameMatch?.[1] || '',
      redirect: resolvePathExpression(redirectExpr, routePathByKey),
      component: componentMatch?.[1] || '',
      props: propsMatch?.[1] || '',
      source: 'router/index.js',
      line: row.line
    }
  })
}

function resolvePathExpression(expression, routePathByKey) {
  if (!expression) return ''
  const literal = expression.match(/^'([^']+)'$/)
  if (literal) return literal[1]
  const routeRef = expression.match(/^routePaths\.([A-Za-z0-9_]+)$/)
  if (routeRef) return routePathByKey.get(routeRef[1]) || `UNRESOLVED:${expression}`
  return expression
}

function parseScreenshotMap(source) {
  const routeEntries = []
  const fullInventory = []
  let current = null
  let section = ''
  let pendingKind = ''

  for (const [index, line] of source.split('\n').entries()) {
    const heading = line.match(/^##\s+(.+)/)
    if (heading && heading[1] === 'Full screenshot inventory') {
      current = null
      pendingKind = ''
    } else if (heading && heading[1] !== 'Agent instruction' && heading[1] !== 'Screenshot path rule') {
      section = heading[1]
    }
    const routeHeading = line.match(/^###\s+`([^`]+)`/)
    if (routeHeading) {
      current = { routePath: routeHeading[1], section, primary: '', secondary: [], line: index + 1 }
      routeEntries.push(current)
      pendingKind = ''
      continue
    }
    if (/^Primary screenshot:/.test(line)) {
      pendingKind = 'primary'
      continue
    }
    if (/^Secondary screenshot:/.test(line)) {
      pendingKind = 'secondary'
      continue
    }
    const screenshot = line.match(/-\s+`([^`]+\.(?:png|jpg|jpeg))`/i)
    if (screenshot) {
      if (current && pendingKind === 'primary') current.primary = screenshot[1]
      else if (current && pendingKind === 'secondary') current.secondary.push(screenshot[1])
      else fullInventory.push(screenshot[1])
    }
  }

  return { routeEntries, fullInventory }
}

function parseFrameRoutes(source) {
  const routes = []
  const routeRegex = /`(\/[A-Za-z0-9_/:.*?{}=?&-]+)`/g
  let match
  while ((match = routeRegex.exec(source))) {
    const routePath = match[1]
    if (routePath.length > 1) routes.push({ routePath, line: linesBefore(source, match.index) })
  }
  return uniqueBy(routes, (item) => item.routePath)
}

function parseBackendBoards(source) {
  const rows = []
  const insert = source.match(/INSERT INTO boards \(name, code, board_type, description\) VALUES([\s\S]*?);/)
  if (!insert) return rows
  const entryRegex = /\('([^']+)',\s*'([^']+)',\s*'([^']+)',\s*'([^']+)'\)/g
  let match
  const offset = insert.index
  while ((match = entryRegex.exec(insert[1]))) {
    rows.push({ name: match[1], code: match[2], boardType: match[3], description: match[4], source: 'backend V1__init_schema.sql', line: linesBefore(source, offset + match.index) })
  }
  return rows
}

function parseFrontendBoardCodes(source) {
  const rows = []
  const entryRegex = /([A-Za-z0-9_]+)\s*:\s*'([^']+)'/g
  let match
  while ((match = entryRegex.exec(source))) {
    rows.push({ key: match[1], code: match[2], source: 'frontend/src/constants/boardCodes.js', line: linesBefore(source, match.index) })
  }
  return rows
}

function parseServiceBoardUsages(serviceDir, knownCodes) {
  const files = readdirSync(serviceDir).filter((name) => name.endsWith('.js')).sort()
  const usageCodes = new Set([...knownCodes, 'mento-story', 'MENTO_STORY', 'ANONYMOUS', 'NOTICE', 'MENTO_NOTICE', 'MENTO_QNA', 'MENTO_REVIEW', 'FREE'])
  const usages = []

  for (const fileName of files) {
    const filePath = path.join(serviceDir, fileName)
    const source = read(filePath)
    const stringRegex = /'([^']+)'|"([^"]+)"/g
    let match
    while ((match = stringRegex.exec(source))) {
      const value = match[1] || match[2]
      if (!usageCodes.has(value)) continue
      usages.push({ code: value, file: path.relative(repoRoot, filePath), line: linesBefore(source, match.index), context: source.split('\n')[linesBefore(source, match.index) - 1].trim() })
    }
    const directBoardApiRegex = /boardsApi\.(?:categories|posts|post|createPost|updatePost|deletePost|comments|createComment)\('([^']+)'/g
    while ((match = directBoardApiRegex.exec(source))) {
      const value = match[1]
      if (!usageCodes.has(value)) continue
      const line = linesBefore(source, match.index)
      if (usages.some((usage) => usage.code === value && usage.file === path.relative(repoRoot, filePath) && usage.line === line)) continue
      usages.push({ code: value, file: path.relative(repoRoot, filePath), line, context: source.split('\n')[line - 1].trim() })
    }
  }
  return usages.sort((a, b) => a.file.localeCompare(b.file) || a.line - b.line || a.code.localeCompare(b.code))
}

function parseExternalLinks(source) {
  const rows = []
  const blockMatch = source.match(/export\s+const\s+serviceLinks\s*=\s*\[([\s\S]*?)\n\]/)
  if (!blockMatch) return rows
  const blockStart = blockMatch.index + blockMatch[0].indexOf('[') + 1
  const regex = /\{\s*label:\s*'([^']+)'[\s\S]*?url:\s*'([^']+)'\s*\}/g
  let match
  while ((match = regex.exec(blockMatch[1]))) {
    rows.push({ label: match[1], url: match[2], source: 'navigation.js', line: linesBefore(source, blockStart + match.index) })
  }
  return rows
}

function listScreenshots(directory) {
  return readdirSync(directory).filter((name) => /\.(png|jpg|jpeg)$/i.test(name)).sort().map((name) => `../../edu_screnshot/${name}`)
}

function uniqueBy(items, keyFn) {
  const seen = new Set()
  const result = []
  for (const item of items) {
    const key = keyFn(item)
    if (seen.has(key)) continue
    seen.add(key)
    result.push(item)
  }
  return result
}

function escapeMarkdown(value) {
  return String(value ?? '').replaceAll('|', '\\|').replaceAll('\n', '<br>')
}

function slug(value) {
  return String(value).replace(/^https?:\/\//, '').replace(/[^A-Za-z0-9]+/g, '-').replace(/^-|-$/g, '').toLowerCase() || 'root'
}

function canonicalScreenshotRoute(routePath) {
  const aliases = new Map([
    ['/community/survey', '/community/surveys'],
    ['/community/open-board', '/community/boards/open'],
    ['/community/anonymous-board', '/community/boards/anonymous'],
    ['/mentoring/story', '/mentoring/stories'],
    ['/mentoring/meetup/apply', '/mentoring/meetups/apply'],
    ['/mentoring/meetup/info', '/mentoring/meetups/info'],
    ['/mentoring/meetup/reviews', '/mentoring/meetups/reviews'],
    ['/mycampus/learning/elearning', '/mycampus/elearning'],
    ['/mycampus/pledge', '/mycampus/pledges']
  ])
  return aliases.get(routePath) || routePath
}

function frameSection(routePath) {
  if (routePath === '/' || routePath === '/dashboard') return 'FRAME.md §5 홈 대시보드 프레임'
  if (routePath.startsWith('/classroom/curriculum')) return 'FRAME.md §6 강의실 — 주차별 커리큘럼 프레임'
  if (routePath.startsWith('/classroom/quests')) return 'FRAME.md §7 강의실 — Quest/평가 프레임'
  if (routePath.startsWith('/classroom/resources')) return 'FRAME.md §8 강의실 — 학습자료 프레임'
  if (routePath.startsWith('/classroom/')) return 'FRAME.md §9 강의실 — 강의 다시보기 / 필수학습 프레임'
  if (routePath.startsWith('/community/surveys')) return 'FRAME.md §10 커뮤니티 — 설문조사 프레임'
  if (routePath.startsWith('/community/boards')) return 'FRAME.md §11 커뮤니티 — 열린 게시판 / 익명 게시판 프레임'
  if (routePath.startsWith('/community/class-roster')) return 'FRAME.md §12 커뮤니티 — 우리반 보기 프레임'
  if (routePath.startsWith('/help/notice')) return 'FRAME.md §13 HELP DESK — 공지사항 프레임'
  if (routePath.startsWith('/help/')) return 'FRAME.md §14 HELP DESK — FAQ / 1:1 문의 / 학사규정 프레임'
  if (routePath.startsWith('/mentoring/')) return 'FRAME.md §15 멘토링 게시판 프레임'
  if (routePath.startsWith('/mycampus/')) return 'FRAME.md §16 마이캠퍼스 프레임'
  if (routePath.startsWith('/notifications') || routePath === '/403' || routePath === '/404' || routePath.includes('pathMatch')) return 'FRAME.md §17 알림 / 프로필 / 오류 프레임'
  if (routePath.startsWith('http')) return 'navigation.js serviceLinks'
  return 'FRAME.md §19 Route 제안'
}

function inferBoardCodes(routePath) {
  const rules = [
    [/^\/community\/boards\/open/, ['free']],
    [/^\/community\/boards\/anonymous/, ['anonymity']],
    [/^\/help\/notice/, ['notice']],
    [/^\/help\/faq/, ['faq']],
    [/^\/help\/rules/, ['rule']],
    [/^\/mycampus\/documents/, ['doc-req']],
    [/^\/mentoring\/stories/, ['mento-state']],
    [/^\/mentoring\/qna/, ['mento-qna']],
    [/^\/mentoring\/notice/, ['mento-notice']],
    [/^\/mentoring\/meetups\/info/, ['meeting-info']],
    [/^\/mentoring\/meetups\/reviews/, ['mento-review']]
  ]
  for (const [pattern, codes] of rules) {
    if (pattern.test(routePath)) return codes
  }
  return []
}

function inferApiEndpoints(routePath, routeKind) {
  if (['redirect', 'wildcard', 'external', 'error'].includes(routeKind)) return []
  if (routePath === '/login') return ['POST /api/v1/auth/login', 'GET /api/v1/auth/me']
  if (routePath === '/dashboard') return ['GET /api/v1/auth/me', 'GET /api/v1/users/me/campus-summary', 'GET /api/v1/points/my/summary', 'GET /api/v1/notifications/my']
  if (routePath.startsWith('/mycampus/level-points')) return ['GET /api/v1/points/my/summary', 'GET /api/v1/users/me/campus-summary', 'GET /api/v1/points/my/transactions']
  if (routePath.startsWith('/mycampus/attendance')) return ['GET /api/v1/attendance/my']
  if (routePath.startsWith('/mycampus/elearning')) return ['GET /api/v1/learning/progress/my']
  if (routePath.startsWith('/mycampus/bookmarks')) return ['GET /api/v1/bookmarks/my', 'GET /api/v1/learning/contents/my-selected']
  if (routePath.startsWith('/mycampus/documents/write')) return ['GET /api/v1/boards/doc-req/categories', 'POST /api/v1/files', 'POST /api/v1/boards/doc-req/posts']
  if (routePath.startsWith('/mycampus/documents')) return ['GET /api/v1/boards/doc-req/posts']
  if (routePath.startsWith('/mycampus/pledges')) return ['GET /api/v1/agreements/my', 'GET /api/v1/agreements']
  if (routePath.startsWith('/mycampus/education-status')) return ['GET /api/v1/points/my/summary', 'GET /api/v1/users/me/campus-summary']
  if (routePath.startsWith('/mycampus/profile')) return ['GET /api/v1/users/me', 'PATCH /api/v1/users/me']
  if (routePath.startsWith('/mycampus/password')) return ['PATCH /api/v1/auth/password']
  if (routePath.startsWith('/classroom/curriculum')) return ['GET /api/v1/courses/my', 'GET /api/v1/courses/{courseId}/weeks', 'GET /api/v1/courses/{courseId}/weeks/{weekId}/sessions']
  if (routePath.startsWith('/classroom/quests/:id')) return ['GET /api/v1/tasks/{taskId}', 'GET /api/v1/tasks/{taskId}/my-result']
  if (routePath.startsWith('/classroom/quests')) return ['GET /api/v1/tasks/my']
  if (routePath.startsWith('/classroom/resources/:id')) return ['GET /api/v1/learning/contents/{contentId}']
  if (routePath.startsWith('/classroom/resources')) return ['GET /api/v1/learning/categories', 'GET /api/v1/learning/contents']
  if (routePath.startsWith('/classroom/required-learning')) return ['GET /api/v1/learning/contents/required']
  if (routePath.startsWith('/classroom/my-replays') || routePath.startsWith('/classroom/all-replays')) return ['GET /api/v1/course-sessions/replays']
  if (routePath.startsWith('/community/surveys/:id')) return ['GET /api/v1/surveys/{surveyId}']
  if (routePath.startsWith('/community/surveys')) return ['GET /api/v1/survey-categories', 'GET /api/v1/surveys']
  if (routePath.startsWith('/community/class-roster')) return ['GET /api/v1/users/students']
  if (routePath.startsWith('/help/inquiries/write')) return ['POST /api/v1/inquiries']
  if (routePath.startsWith('/help/inquiries/:id')) return ['GET /api/v1/inquiries/{inquiryId}']
  if (routePath.startsWith('/help/inquiries')) return ['GET /api/v1/inquiries/my']
  if (routePath.startsWith('/notifications')) return ['GET /api/v1/notifications/my', 'GET /api/v1/notifications/my/unread-count']

  const boardCodes = inferBoardCodes(routePath)
  if (boardCodes.length) {
    const code = boardCodes[0]
    if (routePath.endsWith('/write')) return [`GET /api/v1/boards/${code}/categories`, `POST /api/v1/boards/${code}/posts`]
    if (routePath.includes('/:id')) return [`GET /api/v1/boards/${code}/posts/{postId}`]
    return [`GET /api/v1/boards/${code}/posts`]
  }
  return []
}

function expectedDataShape(routePath, endpoints) {
  if (!endpoints.length) return 'N/A'
  if (routePath.includes('/:id')) return 'detail object; route param id must map to API id'
  if (routePath.endsWith('/write') || routePath === '/login' || routePath.endsWith('/password')) return 'form submit response or auth/session object'
  if (routePath === '/dashboard') return 'summary cards, preview lists, notifications, user/campus summary'
  if (routePath.includes('/boards/') || routePath.includes('/notice') || routePath.includes('/qna') || routePath.includes('/stories') || routePath.includes('/reviews')) return 'paged board posts: content/items/data with id, title, category, author, date, views'
  return 'page-specific DTO or paged list normalized by frontend service'
}

function routeKind(row) {
  if (row.redirect) return 'redirect'
  if (row.path.startsWith('http')) return 'external'
  if (row.path.includes(':pathMatch')) return 'wildcard'
  if (row.path === '/403' || row.path === '/404') return 'error'
  if (row.path.includes(':')) return 'dynamic-page'
  return 'page'
}

function statusFor(row, screenshot, boardCodes, boardAudit) {
  const statuses = []
  if (row.routeKind === 'external' || row.routeKind === 'redirect') return 'READY'
  if (row.routeKind === 'wildcard') return 'PARTIAL'
  if (!screenshot?.primary) statuses.push('NO_SCREENSHOT')
  for (const code of boardCodes) {
    const audit = boardAudit.find((item) => item.code === code)
    if (!audit || audit.status !== 'OK') statuses.push('PARTIAL')
  }
  if (statuses.includes('NO_SCREENSHOT')) return 'NO_SCREENSHOT'
  if (statuses.includes('PARTIAL')) return 'PARTIAL'
  return 'READY'
}

function build() {
  const routeSource = read(paths.routes)
  const routerSource = read(paths.router)
  const navigationSource = read(paths.navigation)
  const screenshotMapSource = read(paths.screenshotMap)
  const frameSource = read(paths.frame)
  const backendSource = read(paths.backendV1)
  const frontendBoardSource = read(paths.boardCodes)

  const routePathEntries = parseRoutePaths(routeSource)
  const routePathByKey = new Map(routePathEntries.map((entry) => [entry.key, entry.path]))
  const routerRows = parseRouter(routerSource, routePathByKey)
  const screenshotMap = parseScreenshotMap(screenshotMapSource)
  const screenshotByCanonicalRoute = new Map()
  const aliasRows = []
  for (const entry of screenshotMap.routeEntries) {
    const canonical = canonicalScreenshotRoute(entry.routePath)
    const normalized = { ...entry, canonicalRoutePath: canonical }
    if (!screenshotByCanonicalRoute.has(canonical)) screenshotByCanonicalRoute.set(canonical, [])
    screenshotByCanonicalRoute.get(canonical).push(normalized)
    if (canonical !== entry.routePath) aliasRows.push({ legacyPath: entry.routePath, canonicalPath: canonical, source: '05-screenshot-reference-map.md', line: entry.line })
  }

  const actualScreenshots = listScreenshots(paths.screenshots)
  const frontendBoardCodes = parseFrontendBoardCodes(frontendBoardSource)
  const backendBoards = parseBackendBoards(backendSource)
  const frontendCodeSet = new Set(frontendBoardCodes.map((item) => item.code))
  const backendCodeSet = new Set(backendBoards.map((item) => item.code))
  const serviceBoardUsages = parseServiceBoardUsages(paths.services, new Set([...frontendCodeSet, ...backendCodeSet]))
  const serviceCodeSet = new Set(serviceBoardUsages.map((item) => item.code))
  const allBoardCodes = [...new Set([...frontendCodeSet, ...backendCodeSet, ...serviceCodeSet])].sort()
  const boardAudit = allBoardCodes.map((code) => {
    const inFrontend = frontendCodeSet.has(code)
    const inBackend = backendCodeSet.has(code)
    const usages = serviceBoardUsages.filter((usage) => usage.code === code)
    let status = 'OK'
    const notes = []
    if (!inFrontend) notes.push('missing from frontend constants')
    if (!inBackend) notes.push('missing from backend V1 seed')
    if (!inFrontend || !inBackend) status = 'PARTIAL'
    return { code, inFrontendConstants: inFrontend, inBackendSeed: inBackend, serviceUsages: usages, status, notes }
  })

  const externalRows = parseExternalLinks(navigationSource).map((link) => ({
    path: link.url,
    name: link.label,
    sourceOfTruth: 'external',
    routeKind: 'external',
    routeName: link.label,
    source: link.source,
    line: link.line,
    redirect: ''
  }))

  const routerPathSet = new Set(routerRows.map((row) => row.path))
  const routeOnlyRows = routePathEntries
    .filter((entry) => !routerPathSet.has(entry.path))
    .map((entry) => ({ path: entry.path, name: entry.key, sourceOfTruth: 'routes.js', routeKind: entry.path === '/404' ? 'error' : 'page', routeName: entry.key, source: entry.source, line: entry.line, redirect: '' }))

  const canonicalRows = [
    ...routerRows.map((row) => {
      const isRouteConstant = row.pathExpression.startsWith('routePaths.')
      return {
        path: row.path,
        name: row.name,
        sourceOfTruth: isRouteConstant ? 'routes+router' : 'router-only',
        routeKind: routeKind(row),
        routeName: row.name,
        source: row.source,
        line: row.line,
        redirect: row.redirect,
        component: row.component,
        props: row.props
      }
    }),
    ...routeOnlyRows,
    ...externalRows
  ]

  const frameRoutes = parseFrameRoutes(frameSource)
  const rows = canonicalRows.map((row) => {
    const screenshots = screenshotByCanonicalRoute.get(row.path) || []
    const primaryScreenshot = screenshots.find((item) => item.primary)?.primary || ''
    const secondaryScreenshots = screenshots.flatMap((item) => item.secondary || [])
    const boardCodes = inferBoardCodes(row.path)
    const endpoints = inferApiEndpoints(row.path, row.routeKind)
    const aliasOrLegacyPath = screenshots.filter((item) => item.routePath !== item.canonicalRoutePath).map((item) => item.routePath)
    const evidenceSlug = slug(row.path === '/:pathMatch(.*)*' ? 'wildcard-404' : row.path)
    const rowObject = {
      routePath: row.path,
      routeName: row.routeName || row.name || '',
      routeKind: row.routeKind,
      sourceOfTruth: row.sourceOfTruth,
      aliasOrLegacyPath,
      frameSection: frameSection(row.path),
      screenshotKey: primaryScreenshot ? path.basename(primaryScreenshot) : '',
      screenshotPath: primaryScreenshot,
      secondaryScreenshotPaths: secondaryScreenshots,
      apiEndpoints: endpoints,
      boardCodes,
      expectedDataShape: expectedDataShape(row.path, endpoints),
      mockAllowed: row.routeKind === 'redirect' || row.routeKind === 'wildcard' || row.routeKind === 'external' ? 'n/a' : 'yes until API verification phase; must be explicit in evidence',
      evidenceApiPath: endpoints.length ? `verification-pack/api/phase-0/${evidenceSlug}.json` : 'N/A',
      evidenceScreenshotPath: primaryScreenshot ? `verification-pack/screenshots/phase-0/${evidenceSlug}.png` : 'NO_SCREENSHOT',
      owner: 'frontend-phase-0-contract-lock',
      status: '',
      notes: []
    }
    rowObject.status = statusFor(rowObject, { primary: primaryScreenshot }, boardCodes, boardAudit)
    if (row.redirect) rowObject.notes.push(`redirects to ${row.redirect}`)
    if (row.routeKind === 'wildcard') rowObject.notes.push('router wildcard covers 404; /404 constant is route-only error target')
    if (!primaryScreenshot && !['redirect', 'external'].includes(row.routeKind)) rowObject.notes.push('NO_SCREENSHOT: no primary screenshot mapped in 05-screenshot-reference-map.md')
    if (boardCodes.some((code) => boardAudit.find((audit) => audit.code === code)?.status !== 'OK')) rowObject.notes.push('PARTIAL: board-code drift is listed in boardCodeAudit')
    return rowObject
  })

  const canonicalRouteSet = new Set(rows.map((row) => row.routePath))
  const unmappedScreenshotRoutes = screenshotMap.routeEntries
    .map((entry) => ({ ...entry, canonicalRoutePath: canonicalScreenshotRoute(entry.routePath) }))
    .filter((entry) => !canonicalRouteSet.has(entry.canonicalRoutePath))
    .map((entry) => ({ routePath: entry.routePath, canonicalRoutePath: entry.canonicalRoutePath, status: 'DRIFT_UNMAPPED', source: '05-screenshot-reference-map.md', line: entry.line }))

  const missingScreenshotFiles = [...new Set([...screenshotMap.routeEntries.flatMap((entry) => [entry.primary, ...entry.secondary]), ...screenshotMap.fullInventory].filter(Boolean))]
    .filter((filePath) => !actualScreenshots.includes(filePath))
    .map((filePath) => ({ screenshotPath: filePath, status: 'BLOCKED', notes: 'listed in 05-screenshot-reference-map.md but file does not exist' }))

  const extraScreenshotFiles = actualScreenshots
    .filter((filePath) => !screenshotMap.fullInventory.includes(filePath) && !screenshotMap.routeEntries.some((entry) => entry.primary === filePath || entry.secondary.includes(filePath)))
    .map((filePath) => ({ screenshotPath: filePath, status: 'PARTIAL', notes: 'file exists but is not listed in 05-screenshot-reference-map.md full inventory' }))

  const drift = {
    routeOnlyConstants: routeOnlyRows.map((row) => ({ routePath: row.path, status: row.path === '/404' ? 'PARTIAL' : 'BLOCKED', notes: row.path === '/404' ? 'routePaths.notFound is not registered directly; wildcard route renders the 404 page.' : 'route constant is not registered in router/index.js' })),
    routerOnlyRoutes: rows.filter((row) => row.sourceOfTruth === 'router-only').map((row) => ({ routePath: row.routePath, routeKind: row.routeKind, status: row.routeKind === 'redirect' || row.routeKind === 'external' ? 'READY' : (row.routeKind === 'dynamic-page' || row.routeKind === 'wildcard' ? 'PARTIAL' : 'BLOCKED'), notes: row.routeKind === 'redirect' ? 'intentional redirect route; route-smoke evidence required only' : (row.routeKind === 'dynamic-page' ? 'dynamic route intentionally router-only; representative sample URL required in verification' : (row.routeKind === 'wildcard' ? 'wildcard route intentionally router-only; 404 route-smoke evidence required' : 'router-only non-dynamic route needs owner review')) })),
    screenshotAliases: aliasRows.map((row) => ({ ...row, status: 'RESOLVED_ALIAS' })),
    unmappedScreenshotRoutes,
    missingScreenshotFiles,
    extraScreenshotFiles,
    boardCodePartial: boardAudit.filter((item) => item.status !== 'OK').map((item) => ({ code: item.code, status: item.status, notes: item.notes, serviceUsages: item.serviceUsages }))
  }

  const inventory = {
    generatedAt: new Date().toISOString(),
    generator: 'frontend/docs/design/app-vue/scripts/build-contract-inventory.mjs',
    constraints: {
      noVisualDomainImplementationBeforePhase0Artifacts: true,
      routeAuthority: ['frontend/src/constants/routes.js', 'frontend/src/router/index.js'],
      screenshotReferenceIsEvidenceMapNotRouteAuthority: true,
      frontendBoardCodeSourceOfTruthAfterPhase0: 'frontend/src/constants/boardCodes.js'
    },
    summary: {
      canonicalRows: rows.length,
      readyRows: rows.filter((row) => row.status === 'READY').length,
      partialRows: rows.filter((row) => row.status === 'PARTIAL').length,
      noScreenshotRows: rows.filter((row) => row.status === 'NO_SCREENSHOT').length,
      blockedRows: rows.filter((row) => row.status === 'BLOCKED').length,
      screenshotAliasesResolved: drift.screenshotAliases.length,
      unmappedScreenshotRoutes: drift.unmappedScreenshotRoutes.length,
      boardCodePartial: drift.boardCodePartial.length
    },
    routePathConstants: routePathEntries,
    routerRoutes: routerRows,
    frameRoutes,
    screenshotRoutes: screenshotMap.routeEntries,
    actualScreenshots,
    frontendBoardCodes,
    backendBoardCodes: backendBoards,
    boardCodeAudit: boardAudit,
    serviceBoardUsages,
    canonicalRows: rows,
    drift
  }

  mkdirSync(path.dirname(paths.outJson), { recursive: true })
  mkdirSync(path.dirname(paths.verificationReadme), { recursive: true })
  for (const dir of ['api/phase-0', 'screenshots/phase-0', 'visual/phase-0']) {
    mkdirSync(path.join(appVueDir, 'verification-pack', dir), { recursive: true })
  }

  writeFileSync(paths.outJson, `${JSON.stringify(inventory, null, 2)}\n`)
  writeFileSync(paths.outMd, renderCanonicalContract(inventory))
  writeFileSync(paths.verificationReadme, renderVerificationReadme())
  for (const filePath of [
    path.join(appVueDir, 'verification-pack/api/phase-0/.gitkeep'),
    path.join(appVueDir, 'verification-pack/screenshots/phase-0/.gitkeep'),
    path.join(appVueDir, 'verification-pack/visual/phase-0/.gitkeep')
  ]) {
    if (!existsSync(filePath)) writeFileSync(filePath, '')
  }

  return inventory
}

function renderCanonicalContract(inventory) {
  const rows = inventory.canonicalRows
  const tableRows = rows.map((row) => [
    row.routePath,
    row.routeName,
    row.routeKind,
    row.sourceOfTruth,
    row.aliasOrLegacyPath.join(', ') || '-',
    row.frameSection,
    row.screenshotKey || '-',
    row.screenshotPath || '-',
    row.apiEndpoints.join('<br>') || 'N/A',
    row.boardCodes.join(', ') || '-',
    row.expectedDataShape,
    row.mockAllowed,
    row.evidenceApiPath,
    row.evidenceScreenshotPath,
    row.owner,
    row.status,
    row.notes.join('<br>') || '-'
  ].map(escapeMarkdown).join(' | ')).map((row) => `| ${row} |`).join('\n')

  const boardRows = inventory.boardCodeAudit.map((item) => [
    item.code,
    item.inFrontendConstants ? 'yes' : 'no',
    item.inBackendSeed ? 'yes' : 'no',
    item.status,
    item.notes.join('; ') || '-',
    item.serviceUsages.map((usage) => `${usage.file}:${usage.line}`).join('<br>') || '-'
  ].map(escapeMarkdown).join(' | ')).map((row) => `| ${row} |`).join('\n')

  const driftRows = [
    ...inventory.drift.routeOnlyConstants.map((item) => ['route-only constant', item.routePath, item.status, item.notes]),
    ...inventory.drift.routerOnlyRoutes.map((item) => ['router-only route', item.routePath, item.status, item.notes]),
    ...inventory.drift.screenshotAliases.map((item) => ['screenshot alias', `${item.legacyPath} -> ${item.canonicalPath}`, item.status, `${item.source}:${item.line}`]),
    ...inventory.drift.unmappedScreenshotRoutes.map((item) => ['unmapped screenshot route', `${item.routePath} -> ${item.canonicalRoutePath}`, item.status, `${item.source}:${item.line}`]),
    ...inventory.drift.missingScreenshotFiles.map((item) => ['missing screenshot file', item.screenshotPath, item.status, item.notes]),
    ...inventory.drift.extraScreenshotFiles.map((item) => ['extra screenshot file', item.screenshotPath, item.status, item.notes]),
    ...inventory.drift.boardCodePartial.map((item) => ['board-code drift', item.code, item.status, item.notes.join('; ')])
  ].map((item) => `| ${item.map(escapeMarkdown).join(' | ')} |`).join('\n')

  return `# Canonical contract — Edu SSAFY frontend clone\n\nGenerated by \`scripts/build-contract-inventory.mjs\` at ${inventory.generatedAt}.\n\n## Phase 0 lock rules\n\n- \`frontend/src/constants/routes.js\` plus \`frontend/src/router/index.js\` are the route authority.\n- \`05-screenshot-reference-map.md\` is an evidence map, not route authority. Legacy screenshot paths are normalized as aliases below.\n- \`frontend/src/constants/boardCodes.js\` is the frontend board-code source of truth after this lock. Service-local board code strings must either match it and backend V1 seed data or remain explicitly PARTIAL/BLOCKED.\n- Visual/domain implementation must not start until this file, \`contract-inventory.json\`, and \`verification-pack/README.md\` exist and every drift item is resolved or explicitly marked BLOCKED/PARTIAL/NO_SCREENSHOT.\n\n## Summary\n\n- Canonical rows: ${inventory.summary.canonicalRows}\n- READY: ${inventory.summary.readyRows}\n- PARTIAL: ${inventory.summary.partialRows}\n- NO_SCREENSHOT: ${inventory.summary.noScreenshotRows}\n- BLOCKED: ${inventory.summary.blockedRows}\n- Resolved screenshot aliases: ${inventory.summary.screenshotAliasesResolved}\n- Unmapped screenshot routes: ${inventory.summary.unmappedScreenshotRoutes}\n- Board-code PARTIAL items: ${inventory.summary.boardCodePartial}\n\n## Canonical rows\n\n| routePath | routeName | routeKind | sourceOfTruth | aliasOrLegacyPath | FRAME section | screenshot key | screenshot path | API endpoint(s) | boardCode(s) | expected data shape | mock_allowed | evidenceApiPath | evidenceScreenshotPath | owner | status | notes |\n| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |\n${tableRows}\n\n## Board-code audit\n\n| code | frontend constants | backend V1 seed | status | notes | service usage locations |\n| --- | --- | --- | --- | --- | --- |\n${boardRows}\n\n## Drift ledger\n\n| type | item | status | notes |\n| --- | --- | --- | --- |\n${driftRows || '| none | none | READY | no drift detected |'}\n\n## Next-phase gate\n\nBefore any Phase 1+ visual/domain implementation, use this contract as the checklist. Rows with \`PARTIAL\`, \`NO_SCREENSHOT\`, or \`BLOCKED\` may proceed only within their marked limitation, and completion claims must include matching evidence files under \`verification-pack/\`.\n`
}

function renderVerificationReadme() {
  return `# Verification pack\n\nThis folder stores reproducible evidence for the Edu SSAFY frontend clone. Do not store secrets, authorization headers, cookies, passwords, tokens, or non-demo personal data here.\n\n## Generate the Phase 0 contract\n\n\`\`\`bash\nnode frontend/docs/design/app-vue/scripts/build-contract-inventory.mjs\n\`\`\`\n\nThe command writes:\n\n- \`frontend/docs/design/app-vue/contract-inventory.json\`\n- \`frontend/docs/design/app-vue/canonical-contract.md\`\n- \`frontend/docs/design/app-vue/verification-pack/README.md\`\n\n## Evidence layout\n\n- \`api/<phase>/<route-slug>.json\` — API/runtime evidence per route.\n- \`screenshots/<phase>/<route-slug>.png\` — implementation screenshot captured at the agreed viewport.\n- \`visual/<phase>/<route-slug>.md\` — visual comparison notes against the screenshot reference.\n\n## API evidence schema\n\nEach API evidence file must use this shape:\n\n\`\`\`json\n{\n  "routePath": "/dashboard",\n  "routeKind": "page",\n  "endpoint": "GET /api/v1/users/me/campus-summary",\n  "method": "GET",\n  "statusCode": 200,\n  "success": true,\n  "dataShapeSummary": "summary cards and user campus stats",\n  "usedMockFallback": false,\n  "redactedRequest": {},\n  "redactedResponseSample": {},\n  "capturedAt": "2026-05-12T00:00:00.000Z",\n  "command": "document the exact command used",\n  "blocker": null\n}\n\`\`\`\n\nIf the API cannot be verified, set \`success: false\`, include \`blocker\`, and mark the related canonical row \`BLOCKED\` or \`PARTIAL\`.\n\n## Stop rules\n\n1. Do not begin visual/domain implementation unless \`canonical-contract.md\`, \`contract-inventory.json\`, and this README exist.\n2. Route/screenshot/API/boardCode drift must be resolved or explicitly marked \`BLOCKED\`, \`PARTIAL\`, or \`NO_SCREENSHOT\`.\n3. Rows with \`mock_allowed: yes until API verification phase\` cannot be claimed as API-complete without an API evidence file.\n4. Dynamic routes require a representative sample URL in screenshot and API evidence.\n5. Redirect routes require route-smoke evidence only.\n6. Wildcard routes require 404 route-smoke evidence.\n7. External links require href/target evidence only.\n8. Delete or redact any evidence that contains secrets, credentials, tokens, cookies, or non-demo personal data.\n`
}

const inventory = build()
console.log(`Wrote ${path.relative(repoRoot, paths.outJson)}`)
console.log(`Wrote ${path.relative(repoRoot, paths.outMd)}`)
console.log(`Wrote ${path.relative(repoRoot, paths.verificationReadme)}`)
console.log(JSON.stringify(inventory.summary, null, 2))
