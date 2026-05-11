#!/usr/bin/env node
import { mkdirSync, readFileSync, writeFileSync } from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const scriptDir = path.dirname(__filename)
const appVueDir = path.resolve(scriptDir, '..')
const inventoryPath = path.join(appVueDir, 'contract-inventory.json')
const outDir = path.join(appVueDir, 'verification-pack', 'api', 'runtime')
const baseUrl = process.env.FRONTEND_BASE_URL || 'http://127.0.0.1:5173'

const sampleIds = {
  '/classroom/quests/:id': '1',
  '/classroom/resources/:id': '1',
  '/community/surveys/:id': '1',
  '/community/boards/open/:id': '1',
  '/community/boards/anonymous/:id': '1',
  '/help/notice/:id': '1',
  '/help/inquiries/:id': '1',
  '/mentoring/stories/:id': '1',
  '/mentoring/qna/:id': '1',
  '/mentoring/notice/:id': '1',
  '/mentoring/meetups/reviews/:id': '1'
}

function routeToUrl(routePath) {
  if (routePath === '/:pathMatch(.*)*') return '/not-a-real-route-for-smoke'
  return routePath.replace(/:id\b/g, (match, offset, text) => sampleIds[text] || '1')
}

function slug(value) {
  return String(value).replace(/^https?:\/\//, '').replace(/[^A-Za-z0-9]+/g, '-').replace(/^-|-$/g, '').toLowerCase() || 'root'
}

async function fetchRoute(row) {
  const samplePath = routeToUrl(row.routePath)
  const startedAt = new Date().toISOString()
  try {
    const response = await fetch(`${baseUrl}${samplePath}`, { redirect: 'manual' })
    const body = await response.text()
    return {
      routePath: row.routePath,
      samplePath,
      routeKind: row.routeKind,
      canonicalStatus: row.status,
      statusCode: response.status,
      ok: response.status >= 200 && response.status < 400,
      contentType: response.headers.get('content-type'),
      containsAppRoot: body.includes('id="app"') || body.includes('/src/main.js') || body.includes('assets/'),
      bodyBytes: body.length,
      capturedAt: startedAt
    }
  } catch (error) {
    return {
      routePath: row.routePath,
      samplePath,
      routeKind: row.routeKind,
      canonicalStatus: row.status,
      statusCode: null,
      ok: false,
      error: error.message,
      capturedAt: startedAt
    }
  }
}

const inventory = JSON.parse(readFileSync(inventoryPath, 'utf8'))
const rows = inventory.canonicalRows.filter((row) => !row.routePath.startsWith('http'))
mkdirSync(outDir, { recursive: true })

const results = []
for (const row of rows) {
  const result = await fetchRoute(row)
  results.push(result)
  writeFileSync(path.join(outDir, `${slug(row.routePath === '/:pathMatch(.*)*' ? 'wildcard-404' : row.routePath)}.route-smoke.json`), `${JSON.stringify(result, null, 2)}\n`)
}

const summary = {
  capturedAt: new Date().toISOString(),
  command: 'FRONTEND_BASE_URL=<url> node frontend/docs/design/app-vue/scripts/run-route-smoke.mjs',
  baseUrl,
  routeSmokeComplete: results.every((item) => item.ok && item.containsAppRoot),
  totalRoutes: results.length,
  passedRoutes: results.filter((item) => item.ok && item.containsAppRoot).length,
  failedRoutes: results.filter((item) => !(item.ok && item.containsAppRoot)).length,
  dynamicSamples: sampleIds,
  results
}
writeFileSync(path.join(outDir, 'route-smoke-runtime-summary.json'), `${JSON.stringify(summary, null, 2)}\n`)
console.log(JSON.stringify(summary, null, 2))
