#!/usr/bin/env node
import { mkdirSync, readFileSync, writeFileSync } from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const scriptDir = path.dirname(__filename)
const appVueDir = path.resolve(scriptDir, '..')
const inventoryPath = path.join(appVueDir, 'contract-inventory.json')
const packDir = path.join(appVueDir, 'verification-pack')
const phase = 'phase-6'

function slug(value) {
  return String(value)
    .replace(/^https?:\/\//, '')
    .replace(/[^A-Za-z0-9]+/g, '-')
    .replace(/^-|-$/g, '')
    .toLowerCase() || 'root'
}

function now() {
  return new Date().toISOString()
}

function apiEvidence(row, capturedAt) {
  const hasRuntimeApi = false
  const routeOnly = ['redirect', 'wildcard', 'external', 'error'].includes(row.routeKind)
  return {
    routePath: row.routePath,
    routeKind: row.routeKind,
    endpoint: row.apiEndpoints?.join('; ') || 'N/A',
    method: row.apiEndpoints?.length ? 'MIXED' : 'N/A',
    statusCode: null,
    success: routeOnly || row.status === 'READY' || row.status === 'NO_SCREENSHOT',
    dataShapeSummary: row.expectedDataShape || 'N/A',
    usedMockFallback: null,
    redactedRequest: {},
    redactedResponseSample: {},
    capturedAt,
    command: 'node frontend/docs/design/app-vue/scripts/build-verification-pack.mjs',
    blocker: row.apiEndpoints?.length && !hasRuntimeApi
      ? 'PARTIAL: static contract evidence only. API runtime was not exercised, so this is not an API-complete claim.'
      : null
  }
}

function visualEvidence(row, capturedAt) {
  const status = row.screenshotPath ? 'REFERENCE_READY_RUNTIME_CAPTURE_PENDING' : 'NO_SCREENSHOT'
  return `# Visual evidence — ${row.routePath}\n\n- routeKind: ${row.routeKind}\n- canonical status: ${row.status}\n- capturedAt: ${capturedAt}\n- reference screenshot: ${row.screenshotPath || 'NO_SCREENSHOT'}\n- expected implementation screenshot path: ${row.evidenceScreenshotPath}\n- visual status: ${status}\n\nThis file is a Phase 6 evidence placeholder. It does not claim pixel parity. Add runtime screenshots and visual verdicts here when a browser capture tool is available.\n`
}

const inventory = JSON.parse(readFileSync(inventoryPath, 'utf8'))
const capturedAt = now()
const apiDir = path.join(packDir, 'api', phase)
const visualDir = path.join(packDir, 'visual', phase)
mkdirSync(apiDir, { recursive: true })
mkdirSync(visualDir, { recursive: true })

const routeRows = inventory.canonicalRows.filter((row) => !row.routePath.startsWith('http'))
const routeSmoke = {
  capturedAt,
  command: 'node frontend/docs/design/app-vue/scripts/build-verification-pack.mjs',
  sourceInventory: 'frontend/docs/design/app-vue/contract-inventory.json',
  apiCompletionClaim: false,
  visualParityClaim: false,
  summary: {
    routes: routeRows.length,
    ready: routeRows.filter((row) => row.status === 'READY').length,
    noScreenshot: routeRows.filter((row) => row.status === 'NO_SCREENSHOT').length,
    partial: routeRows.filter((row) => row.status === 'PARTIAL').length,
    blocked: routeRows.filter((row) => row.status === 'BLOCKED').length
  },
  rows: routeRows.map((row) => ({
    routePath: row.routePath,
    routeName: row.routeName,
    routeKind: row.routeKind,
    sourceOfTruth: row.sourceOfTruth,
    status: row.status,
    screenshotPath: row.screenshotPath || 'NO_SCREENSHOT',
    apiEndpoints: row.apiEndpoints,
    blocker: row.apiEndpoints?.length
      ? 'PARTIAL: API endpoint listed, but runtime API was not exercised in Phase 6.'
      : null
  }))
}

writeFileSync(path.join(apiDir, 'route-smoke-matrix.json'), `${JSON.stringify(routeSmoke, null, 2)}\n`)

for (const row of routeRows) {
  const routeSlug = slug(row.routePath === '/:pathMatch(.*)*' ? 'wildcard-404' : row.routePath)
  writeFileSync(path.join(apiDir, `${routeSlug}.json`), `${JSON.stringify(apiEvidence(row, capturedAt), null, 2)}\n`)
  writeFileSync(path.join(visualDir, `${routeSlug}.md`), visualEvidence(row, capturedAt))
}

const visualIndex = `# Phase 6 visual evidence index\n\nGenerated at ${capturedAt}.\n\nThis index records screenshot-reference coverage only. It does not claim pixel parity because no browser screenshot capture was run in this phase.\n\n| routePath | status | reference screenshot | visual evidence |\n| --- | --- | --- | --- |\n${routeRows.map((row) => {
  const routeSlug = slug(row.routePath === '/:pathMatch(.*)*' ? 'wildcard-404' : row.routePath)
  return `| ${row.routePath.replaceAll('|', '\\|')} | ${row.status} | ${row.screenshotPath || 'NO_SCREENSHOT'} | visual/${phase}/${routeSlug}.md |`
}).join('\n')}\n`
writeFileSync(path.join(visualDir, 'visual-index.md'), visualIndex)

console.log(JSON.stringify({ ok: true, phase, routes: routeRows.length, apiDir, visualDir }, null, 2))
