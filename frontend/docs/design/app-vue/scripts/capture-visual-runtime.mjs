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

const inventory = JSON.parse(readFileSync(inventoryPath, 'utf8'))
const rows = inventory.canonicalRows.filter((row) => !row.routePath.startsWith('http') && row.screenshotPath)
const selectedRows = limit > 0 ? rows.slice(0, limit) : rows
mkdirSync(screenshotDir, { recursive: true })
mkdirSync(visualDir, { recursive: true })

const results = []
for (const row of selectedRows) {
  const routeSlug = slug(row.routePath)
  const outputPath = path.join(screenshotDir, `${routeSlug}.png`)
  const url = `${baseUrl}${routeToUrl(row.routePath)}`
  const args = [
    '--headless=new',
    '--disable-gpu',
    '--hide-scrollbars',
    '--window-size=1440,1600',
    `--screenshot=${outputPath}`,
    url
  ]
  const run = spawnSync(chromePath, args, { encoding: 'utf8', timeout: 60000 })
  const referencePath = resolveReference(row.screenshotPath)
  const result = {
    routePath: row.routePath,
    url,
    referenceScreenshot: row.screenshotPath,
    referenceExists: referencePath ? existsSync(referencePath) : false,
    referenceSize: referencePath ? pngSize(referencePath) : null,
    capturedScreenshot: path.relative(repoRoot, outputPath),
    captured: run.status === 0 && existsSync(outputPath),
    capturedSize: pngSize(outputPath),
    chromeStatus: run.status,
    stderr: run.stderr?.slice(0, 1000) || '',
    visualParityClaim: false,
    verdict: 'CAPTURED_DIMENSION_CHECK_ONLY'
  }
  results.push(result)
  writeFileSync(path.join(visualDir, `${routeSlug}.json`), `${JSON.stringify(result, null, 2)}\n`)
}

const summary = {
  capturedAt: new Date().toISOString(),
  command: 'FRONTEND_BASE_URL=<url> node frontend/docs/design/app-vue/scripts/capture-visual-runtime.mjs',
  baseUrl,
  chromePath,
  visualCompleteClaim: false,
  reason: 'Runtime screenshots were captured where possible and reference dimensions checked, but no pixel-diff dependency was added; parity remains evidence-backed PARTIAL unless manually reviewed.',
  totalReferenceRoutes: rows.length,
  attemptedRoutes: selectedRows.length,
  capturedRoutes: results.filter((row) => row.captured).length,
  failedRoutes: results.filter((row) => !row.captured).length,
  results
}
writeFileSync(path.join(visualDir, 'visual-runtime-summary.json'), `${JSON.stringify(summary, null, 2)}\n`)
writeFileSync(path.join(visualDir, 'visual-runtime-summary.md'), `# Visual runtime summary\n\n- capturedAt: ${summary.capturedAt}\n- baseUrl: ${baseUrl}\n- attempted routes: ${summary.attemptedRoutes}\n- captured routes: ${summary.capturedRoutes}\n- failed routes: ${summary.failedRoutes}\n- visualCompleteClaim: false\n- reason: ${summary.reason}\n\n| route | reference | captured | captured screenshot | verdict |\n| --- | --- | --- | --- | --- |\n${results.map((row) => `| ${row.routePath} | ${row.referenceScreenshot} | ${row.captured ? 'yes' : 'no'} | ${row.capturedScreenshot} | ${row.verdict} |`).join('\n')}\n`)
console.log(JSON.stringify(summary, null, 2))
