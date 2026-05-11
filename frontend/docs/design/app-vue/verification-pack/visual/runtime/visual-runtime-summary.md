# Visual runtime capture summary

Generated at: 2026-05-11T16:14:54.657Z

## Result

- visual-complete: false
- pixel-parity-complete: false
- captured routes: 0/0
- viewport: 1440x1000
- runtime base URL: http://127.0.0.1:5173
- capture tool: existing Google Chrome headless CLI
- comparison method: reference/runtime screenshot existence and dimension metadata only; no pixel parity claim because no existing pixel-diff tooling is present and no dependencies were added.

## Commands

```bash
cd frontend && npm ci
cd frontend && npm run dev -- --host 127.0.0.1
node /tmp/visual-runtime-capture.mjs
cd frontend && npm run build
```

## Evidence files

- `frontend/docs/design/app-vue/verification-pack/visual/runtime/visual-runtime-evidence.json`
- `frontend/docs/design/app-vue/verification-pack/screenshots/runtime/*.png`

## Route capture matrix

| route | status | runtime screenshot | reference | runtime size | reference size | size match |
| --- | --- | --- | --- | --- | --- | --- |

## Notes / blockers

- Browser capture was blocked for at least one route. See `visual-runtime-evidence.json` for Chrome exit status/stderr.
- Dynamic routes and canonical NO_SCREENSHOT rows are intentionally outside this worker task; this lane covers static READY rows with reference screenshots only.
