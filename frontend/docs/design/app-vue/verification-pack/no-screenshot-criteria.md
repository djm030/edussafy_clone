# NO_SCREENSHOT route criteria

Generated policy date: 2026-05-12.

These routes have no primary screenshot reference in `05-screenshot-reference-map.md`. They remain valid runtime routes, but they cannot be claimed as pixel-perfect. Completion for these routes is criteria-only.

## Shared gate

- Route smoke must return HTTP 200 for the representative path, except redirects/wildcards governed by the canonical contract.
- API-mode evidence must use real backend data and must report `usedMockFallback: false` when an API endpoint exists.
- The page must render the correct authenticated shell, title/state, and primary action without console-breaking runtime errors.
- Dynamic detail routes must use a representative seeded/sample ID in route-smoke and API evidence.
- Visual evidence may capture the route for regression, but it is not a pixel-parity claim without a reference screenshot.

## `/login`

- Route kind: `page`.
- Representative URL: `/login`.
- API endpoint evidence: `POST /api/v1/auth/login`, `POST /api/v1/auth/refresh`, `GET /api/v1/auth/me`
- Auth rule: verify unauthenticated form rendering and authenticated route-guard redirects separately from protected-page screenshots.

## `/mycampus/profile`

- Route kind: `page`.
- Representative URL: `/mycampus/profile`.
- API endpoint evidence: `GET /api/v1/users/me`, `PATCH /api/v1/users/me`
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/mycampus/password`

- Route kind: `page`.
- Representative URL: `/mycampus/password`.
- API endpoint evidence: `PATCH /api/v1/auth/password`
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/classroom/resources/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/classroom/resources/1`.
- API endpoint evidence: `GET /api/v1/learning/contents/{contentId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/community/surveys/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/community/surveys/1`.
- API endpoint evidence: `GET /api/v1/surveys/{surveyId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/community/boards/open/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/community/boards/open/1`.
- API endpoint evidence: `GET /api/v1/boards/free/posts/{postId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/community/boards/anonymous/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/community/boards/anonymous/1`.
- API endpoint evidence: `GET /api/v1/boards/anonymity/posts/{postId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/help/inquiries/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/help/inquiries/1`.
- API endpoint evidence: `GET /api/v1/inquiries/{inquiryId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/mentoring/stories/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/mentoring/stories/1`.
- API endpoint evidence: `GET /api/v1/boards/mento-state/posts/{postId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/mentoring/qna/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/mentoring/qna/1`.
- API endpoint evidence: `GET /api/v1/boards/mento-qna/posts/{postId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/mentoring/notice/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/mentoring/notice/1`.
- API endpoint evidence: `GET /api/v1/boards/mento-notice/posts/{postId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/mentoring/meetups/reviews/:id`

- Route kind: `dynamic-page`.
- Representative URL: `/mentoring/meetups/reviews/1`.
- API endpoint evidence: `GET /api/v1/boards/mento-review/posts/{postId}`
- Dynamic ID rule: use an existing seeded ID or a smoke-created sample ID; do not treat a 404 sample as a visual failure.
- UI rule: render the expected form/detail/profile state using the authenticated app shell.

## `/404`

- Route kind: `error`.
- Representative URL: `/404`.
- API endpoint evidence: `N/A`; route-smoke only unless the page calls a shared API at runtime.
- Error rule: verify the canonical not-found message and home/back action; `/403` owns the screenshot-backed forbidden error reference.
