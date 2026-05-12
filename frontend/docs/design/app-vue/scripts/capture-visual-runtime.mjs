#!/usr/bin/env node
import { existsSync, mkdirSync, readFileSync, writeFileSync } from 'node:fs'
import { spawnSync } from 'node:child_process'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const scriptDir = path.dirname(__filename)
const appVueDir = path.resolve(scriptDir, '..')
const repoRoot = path.resolve(appVueDir, '../../../..')
const inventoryPath = path.join(appVueDir, 'contract-inventory.json')
const screenshotDir = path.join(appVueDir, 'verification-pack', 'screenshots', 'runtime')
const visualDir = path.join(appVueDir, 'verification-pack', 'visual', 'runtime')
const baseUrl = process.env.FRONTEND_BASE_URL || 'http://127.0.0.1:5173'
const apiBaseUrl = (process.env.VISUAL_API_BASE_URL || process.env.API_BASE_URL || 'http://127.0.0.1:8088').replace(/\/$/, '')
const visualAuthEnabled = process.env.VISUAL_AUTH !== 'false'
const visualAuthEmail = process.env.VISUAL_AUTH_EMAIL || process.env.API_SMOKE_EMAIL || 'student@edussafy.local'
const visualAuthPassword = process.env.VISUAL_AUTH_PASSWORD || process.env.API_SMOKE_PASSWORD || '0000'
const chromePath = process.env.CHROME_PATH || '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'
const limit = Number(process.env.VISUAL_CAPTURE_LIMIT || '0')

function slug(value) {
  return String(value).replace(/^https?:\/\//, '').replace(/[^A-Za-z0-9]+/g, '-').replace(/^-|-$/g, '').toLowerCase() || 'root'
}

function routeToUrl(routePath) {
  if (routePath === '/') return '/'
  return routePath.replace(/:id\b/g, '1')
}

function pngSize(filePath) {
  if (!existsSync(filePath)) return null
  const buffer = readFileSync(filePath)
  if (buffer.toString('ascii', 1, 4) !== 'PNG') return null
  return { width: buffer.readUInt32BE(16), height: buffer.readUInt32BE(20), bytes: buffer.length }
}

function resolveReference(referencePath) {
  if (!referencePath) return null
  return path.resolve(appVueDir, referencePath)
}

function apiUrl(pathname) {
  if (apiBaseUrl.endsWith('/api/v1') && pathname.startsWith('/api/v1')) {
    return `${apiBaseUrl}${pathname.slice('/api/v1'.length)}`
  }
  return `${apiBaseUrl}${pathname}`
}

async function loginForVisualCapture() {
  if (!visualAuthEnabled) {
    return { enabled: false, success: false, statusCode: null, accessToken: null, refreshToken: null, message: 'VISUAL_AUTH=false' }
  }

  try {
    const response = await fetch(apiUrl('/api/v1/auth/login'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: visualAuthEmail, password: visualAuthPassword })
    })
    const body = await response.json().catch(() => ({}))
    return {
      enabled: true,
      success: response.ok && Boolean(body?.data?.accessToken || body?.accessToken),
      statusCode: response.status,
      accessToken: body?.data?.accessToken || body?.accessToken || null,
      refreshToken: body?.data?.refreshToken || body?.refreshToken || null,
      message: body?.message || null
    }
  } catch (error) {
    return {
      enabled: true,
      success: false,
      statusCode: null,
      accessToken: null,
      refreshToken: null,
      message: error instanceof Error ? error.message : String(error)
    }
  }
}

function withVisualAuth(url, auth) {
  if (!auth?.accessToken && !auth?.refreshToken) return url
  const parsed = new URL(url)
  if (auth.accessToken) parsed.searchParams.set('visualAccessToken', auth.accessToken)
  if (auth.refreshToken) parsed.searchParams.set('visualRefreshToken', auth.refreshToken)
  return parsed.toString()
}

const inventory = JSON.parse(readFileSync(inventoryPath, 'utf8'))
const rows = inventory.canonicalRows.filter((row) => !row.routePath.startsWith('http') && row.screenshotPath)
const selectedRows = limit > 0 ? rows.slice(0, limit) : rows
mkdirSync(screenshotDir, { recursive: true })
mkdirSync(visualDir, { recursive: true })

const auth = await loginForVisualCapture()
const results = []
for (const row of selectedRows) {
  const routeSlug = slug(row.routePath)
  const outputPath = path.join(screenshotDir, `${routeSlug}.png`)
  const url = withVisualAuth(`${baseUrl}${routeToUrl(row.routePath)}`, auth)
  const referencePath = resolveReference(row.screenshotPath)
  const referenceSize = referencePath ? pngSize(referencePath) : null
  const viewport = {
    width: referenceSize?.width || 1326,
    height: referenceSize?.height || 1600
  }
  const args = [
    '--headless=new',
    '--disable-gpu',
    '--hide-scrollbars',
    `--window-size=${viewport.width},${viewport.height}`,
    `--screenshot=${outputPath}`,
    url
  ]
  const run = spawnSync(chromePath, args, { encoding: 'utf8', timeout: 60000 })
  const result = {
    routePath: row.routePath,
    url: url.replace(/visualAccessToken=[^&]+/g, 'visualAccessToken=<redacted>').replace(/visualRefreshToken=[^&]+/g, 'visualRefreshToken=<redacted>'),
    referenceScreenshot: row.screenshotPath,
    referenceExists: referencePath ? existsSync(referencePath) : false,
    referenceSize,
    captureViewport: viewport,
    capturedScreenshot: path.relative(repoRoot, outputPath),
    captured: run.status === 0 && existsSync(outputPath),
    capturedSize: pngSize(outputPath),
    chromeStatus: run.status,
    stderr: run.stderr?.slice(0, 1000) || '',
    authenticatedCapture: auth.success,
    visualParityClaim: false,
    verdict: 'CAPTURED_DIMENSION_CHECK_ONLY'
  }
  results.push(result)
  writeFileSync(path.join(visualDir, `${routeSlug}.json`), `${JSON.stringify(result, null, 2)}\n`)
}

const summary = {
  capturedAt: new Date().toISOString(),
  command: 'FRONTEND_BASE_URL=<url> API_BASE_URL=<url> VISUAL_AUTH=true node frontend/docs/design/app-vue/scripts/capture-visual-runtime.mjs',
  baseUrl,
  apiBaseUrl,
  chromePath,
  auth: {
    enabled: auth.enabled,
    email: visualAuthEmail,
    authenticated: auth.success,
    statusCode: auth.statusCode,
    accessToken: auth.accessToken ? '<redacted>' : null,
    refreshToken: auth.refreshToken ? '<redacted>' : null,
    message: auth.message
  },
  visualCompleteClaim: false,
  reason: 'Authenticated runtime screenshots were captured where possible and reference dimensions checked, but no pixel-diff dependency was added; parity remains evidence-backed PARTIAL unless manually reviewed.',
  totalReferenceRoutes: rows.length,
  attemptedRoutes: selectedRows.length,
  capturedRoutes: results.filter((row) => row.captured).length,
  failedRoutes: results.filter((row) => !row.captured).length,
  results
}
writeFileSync(path.join(visualDir, 'visual-runtime-summary.json'), `${JSON.stringify(summary, null, 2)}\n`)
writeFileSync(path.join(visualDir, 'visual-runtime-summary.md'), `# Visual runtime summary\n\n- capturedAt: ${summary.capturedAt}\n- baseUrl: ${baseUrl}\n- apiBaseUrl: ${apiBaseUrl}\n- authenticatedCapture: ${summary.auth.authenticated}\n- attempted routes: ${summary.attemptedRoutes}\n- captured routes: ${summary.capturedRoutes}\n- failed routes: ${summary.failedRoutes}\n- visualCompleteClaim: false\n- reason: ${summary.reason}\n\n| route | reference | captured | authenticated | captured screenshot | verdict |\n| --- | --- | --- | --- | --- | --- |\n${results.map((row) => `| ${row.routePath} | ${row.referenceScreenshot} | ${row.captured ? 'yes' : 'no'} | ${row.authenticatedCapture ? 'yes' : 'no'} | ${row.capturedScreenshot} | ${row.verdict} |`).join('\n')}\n`)
console.log(JSON.stringify(summary, null, 2))
