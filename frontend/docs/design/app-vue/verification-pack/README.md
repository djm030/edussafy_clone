# Verification pack

This folder stores reproducible evidence for the Edu SSAFY frontend clone. Do not store secrets, authorization headers, cookies, passwords, tokens, or non-demo personal data here.

## Generate the Phase 0 contract

```bash
node frontend/docs/design/app-vue/scripts/build-contract-inventory.mjs
```

The command writes:

- `frontend/docs/design/app-vue/contract-inventory.json`
- `frontend/docs/design/app-vue/canonical-contract.md`
- `frontend/docs/design/app-vue/verification-pack/README.md`

## Evidence layout

- `api/<phase>/<route-slug>.json` ? API/runtime evidence per route.
- `screenshots/<phase>/<route-slug>.png` ? implementation screenshot captured at the agreed viewport.
- `visual/<phase>/<route-slug>.md` ? visual comparison notes against the screenshot reference.
- `no-screenshot-criteria.md` ? criteria-only evidence gate for routes without a primary reference screenshot.
- `mock-fallback-policy.md` ? final policy for demo fallbacks versus API-mode completion claims.

## API evidence schema

Each API evidence file must use this shape:

```json
{
  "routePath": "/dashboard",
  "routeKind": "page",
  "endpoint": "GET /api/v1/users/me/campus-summary",
  "method": "GET",
  "statusCode": 200,
  "success": true,
  "dataShapeSummary": "summary cards and user campus stats",
  "usedMockFallback": false,
  "redactedRequest": {},
  "redactedResponseSample": {},
  "capturedAt": "2026-05-12T00:00:00.000Z",
  "command": "document the exact command used",
  "blocker": null
}
```

If the API cannot be verified, set `success: false`, include `blocker`, and mark the related canonical row `BLOCKED` or `PARTIAL`.

## Stop rules

1. Do not begin visual/domain implementation unless `canonical-contract.md`, `contract-inventory.json`, and this README exist.
2. Route/screenshot/API/boardCode drift must be resolved or explicitly marked `BLOCKED`, `PARTIAL`, or `NO_SCREENSHOT`.
3. Rows with `mock_allowed: demo-only when VITE_USE_API is not true` cannot be claimed as API-complete unless API-mode evidence reports `usedMockFallback: false`.
4. NO_SCREENSHOT rows cannot make pixel-parity claims; they require route-smoke/API evidence plus the criteria in `no-screenshot-criteria.md`.
5. Dynamic routes require a representative sample URL in screenshot and API evidence.
6. Redirect routes require route-smoke evidence only.
7. Wildcard routes require 404 route-smoke evidence.
8. External links require href/target evidence only.
9. Delete or redact any evidence that contains secrets, credentials, tokens, cookies, or non-demo personal data.
