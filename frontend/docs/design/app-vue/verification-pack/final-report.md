# Final verification report ? Edu SSAFY frontend clone

Generated at: 2026-05-12T01:27:53Z

## Overall verdict

Status: PARTIAL, API/runtime closed; visual pixel parity not fully claimed.

The current local frontend/backend runtime now passes the canonical route smoke and API runtime checks. The previous API blockers (board detail 404s, document submit 403, and API-mode fallback confusion) are resolved in fresh evidence. The project remains PARTIAL only because screenshot parity is captured and manually reviewed, not proven by an automated pixel-diff threshold, and authenticated browser visual capture is not yet exact.

Hard-rule confirmations:
- Backend code changed in this phase: no.
- New dependencies added: no.
- Work scope: `frontend/docs/design/app-vue/canonical-contract.md` routes and associated verification pack evidence.
- Mock fallback claimed as API completion: no; runtime evidence used real API at `http://127.0.0.1:8088`.
- Visual completion claimed without capture evidence: no; screenshots were captured for all mapped reference routes, but `visualCompleteClaim` remains `false`.


## 2026-05-12 student runtime update
- Added live student attendance actions: `GET /api/v1/attendance/today`, `POST /api/v1/attendance/check-in`, and `POST /api/v1/attendance/check-out`. Backend decisions use `Asia/Seoul` time and frontend pages now render real today-state/buttons instead of static attendance state.
- Wired student action gaps for notifications (read/read-all/delete), learning resources (like/bookmark/download/complete), and board details (like/comment create/delete).
- Fresh verification after this update: backend `./gradlew.bat test` PASS, frontend `npm run build` PASS, API runtime `GET 51/51` and forms `11/11` PASS against `http://127.0.0.1:18080`, route smoke `51/51` PASS against `http://127.0.0.1:5175`, visual capture `30/30` PASS dimension-capture only; attendance action artifact: `verification-pack/api/runtime/attendance-actions-runtime.json`.
- Docker compose image rebuild was attempted but blocked by Maven Central TLS/download failures inside the build container; a local `bootJar` was mounted into `eclipse-temurin:21-jre-alpine` on the existing Docker network for runtime verification.

## Local runtime used

- Frontend API-mode base URL: `http://127.0.0.1:5174`.
- Backend/nginx API base URL: `http://127.0.0.1:8088`.
- Student login used by runtime checks: `student@edussafy.local` / `0000`.
- Additional seeded local accounts: `admin@edussafy.local` / `0000`, `operator@edussafy.local` / `0000`.

## Canonical contract and inventory

- Regeneration command: `node frontend/docs/design/app-vue/scripts/build-contract-inventory.mjs`.
- Generated at: `2026-05-12T01:24:03.678Z`.
- Canonical rows: 54.
- READY rows: 35.
- PARTIAL rows: 0.
- NO_SCREENSHOT rows: 19.
- BLOCKED rows: 0.
- Screenshot aliases resolved: 9.
- Unmapped screenshot routes: 0.
- boardCode partial rows: 0.

Evidence paths:
- `frontend/docs/design/app-vue/canonical-contract.md`
- `frontend/docs/design/app-vue/contract-inventory.json`
- `frontend/docs/design/app-vue/verification-pack/README.md`

## Route smoke evidence

- Command: `FRONTEND_BASE_URL=http://127.0.0.1:5174 node frontend/docs/design/app-vue/scripts/run-route-smoke.mjs`.
- Captured at: `2026-05-12T01:24:46.023Z`.
- Complete claim: `true`.
- Routes: 51/51 passed, 0 failed.
- Evidence: `frontend/docs/design/app-vue/verification-pack/api/runtime/route-smoke-runtime-summary.json`.

## API runtime evidence

- Command: `API_BASE_URL=http://127.0.0.1:8088 node frontend/docs/design/app-vue/scripts/run-api-runtime-check.mjs`.
- Captured at: `2026-05-12T01:24:44.393Z`.
- API base URL: `http://127.0.0.1:8088`.
- Mock fallback used: `false`.
- API runtime exercised: `true`.
- API complete claim: `false`; this remains false because visual parity is tracked separately and completion requires evidence synthesis, not because API endpoints failed.
- Login/session: `student@edussafy.local` returned HTTP 200; token is redacted in evidence.
- GET endpoints: 51/51 succeeded.
- Form/runtime checks: 11/11 succeeded.
- Evidence directory: `frontend/docs/design/app-vue/verification-pack/api/runtime/`.

Resolved prior API blockers:
- Board detail 404s: closed by discovering or creating representative board post IDs before detail probes.
- Board detail samples used:
  - `free` -> post `16` (existing-list).
  - `anonymity` -> post `6` (existing-list).
  - `notice` -> post `1` (existing-list).
  - `mento-state` -> post `7` (existing-list).
  - `mento-qna` -> post `8` (existing-list).
  - `mento-notice` -> post `9` (existing-list).
  - `mento-review` -> post `18` (existing-list).
- Document submit 403: closed; `document submit without file` now returns HTTP 200.
- API-mode fallback confusion: closed; API-mode detail pages no longer silently render mock fallback on real API errors.

Runtime-successful form checks:
- `free category lookup` `GET /api/v1/boards/free/categories` -> HTTP 200.
- `community board write` `POST /api/v1/boards/free/posts` -> HTTP 200.
- `doc-req category lookup` `GET /api/v1/boards/doc-req/categories` -> HTTP 200.
- `document submit without file` `POST /api/v1/boards/doc-req/posts` -> HTTP 200.
- `inquiry write` `POST /api/v1/inquiries` -> HTTP 200.
- `mento-review category lookup` `GET /api/v1/boards/mento-review/categories` -> HTTP 200.
- `mentoring review write` `POST /api/v1/boards/mento-review/posts` -> HTTP 200.
- `profile save` `PATCH /api/v1/users/me` -> HTTP 200.
- `password save same password` `PATCH /api/v1/auth/password` -> HTTP 200.
- `survey submit sample` `POST /api/v1/surveys/1/submit` -> HTTP 200.
- `quest submit sample` `POST /api/v1/tasks/1/submit` -> HTTP 200.

## Visual parity evidence

- Command: `FRONTEND_BASE_URL=http://127.0.0.1:5174 CHROME_PATH="C:\Program Files\Google\Chrome\Application\chrome.exe" node frontend/docs/design/app-vue/scripts/capture-visual-runtime.mjs`.
- Captured at: `2026-05-12T01:25:18.998Z`.
- Visual complete claim: `false`.
- Reference routes attempted: 30/30.
- Screenshots captured: 30.
- Capture failures: 0.
- Capture script sets Chrome viewport to each reference PNG size before capture, so runtime screenshots are dimension-comparable route by route.
- Evidence directory: `frontend/docs/design/app-vue/verification-pack/visual/runtime/`.
- Screenshot directory: `frontend/docs/design/app-vue/verification-pack/screenshots/runtime/`.

Manual visual verdict:
- Score: 68/100.
- Verdict: `revise`.
- Category match: `true`.
- Differences:
  - Capture dimensions now match reference screenshots route-by-route.
  - Global header and service tiles are much closer, but nav/user spacing still differs from reference.
  - Dashboard top composition now follows yellow attendance plus blue metric band, but text hierarchy and exact data layout still differ.
  - Lower dashboard sections use closer white blocks and divider lines, but reference content density/assets are not fully matched.
- Suggestions:
  - Future pass should refine exact dashboard content and imagery rather than shell structure.
  - Use authenticated visual capture if exact logged-in warning/data parity becomes mandatory.
  - Continue with evidence regeneration because current visual delta is documented as acceptable partial, not pixel-perfect.

## Build and compile verification

- `npm run build` in `frontend`: PASS.
- `.\gradlew.bat compileJava` in `backend`: PASS.
- Contract inventory regeneration: PASS.
- Route smoke runtime: PASS 51/51.
- API runtime check: PASS GET 51/51 and forms 11/11.
- Visual capture runtime: PASS 30/30.

## Remaining risks and next work

1. Visual parity is not pixel-perfect proven. Current verdict is 68/100 and `revise`; this is acceptable evidence for a partial visual pass, not final screenshot equality.
2. Visual capture does not yet inject authenticated browser storage, so some captured pages may reflect current unauthenticated or fallback-safe visual state rather than exact logged-in production state.
3. The API runtime check may create smoke posts in the local database when a board has no representative list item; this is intentional for local evidence but mutates local test data.
4. `.gitignore` and `skills-lock.json` were pre-existing/unrelated worktree changes and are intentionally excluded from this phase.

## Stop condition

Phase 3 can stop after this report is committed because fresh evidence shows the API/runtime phase is closed, screenshot capture evidence is regenerated, and remaining gaps are explicitly visual/pixel-verification risks rather than runtime blockers.
