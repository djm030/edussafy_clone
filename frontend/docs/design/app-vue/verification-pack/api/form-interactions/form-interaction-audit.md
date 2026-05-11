# Form Interaction Audit

Generated: 2026-05-11T16:16:45Z  
Task: 8 — Form interaction verifier lane

## Scope

This is a source-level frontend audit. It only writes:

- `frontend/docs/design/app-vue/verification-pack/api/form-interactions/form-interaction-audit.json`
- `frontend/docs/design/app-vue/verification-pack/api/form-interactions/form-interaction-audit.md`

No app code, backend code, or package files were edited.

Real API runtime evidence is **pending elsewhere**: task 6 owns `frontend/docs/design/app-vue/verification-pack/api/runtime/`, and that directory was absent in this worker-2 worktree when this audit ran.

## Shared facts

- API runtime is gated by `VITE_USE_API === 'true'` in `frontend/src/api/client.js:5`.
- API base URL is `/api/v1` in `frontend/src/api/client.js:24-27`.
- `FormTable` supports controlled file inputs and stores the first selected `File` object: `frontend/src/components/ui/FormTable.vue:37-45`, `frontend/src/components/ui/FormTable.vue:108-111`.

## Results

| Interaction | Status | Source coverage | Runtime evidence |
| --- | --- | --- | --- |
| Community board write | FEASIBLE | Text/category submit aligns with canonical free-board endpoints. | Pending elsewhere |
| Document submit with file path | FEASIBLE | File object can upload to `/files`, then attach `fileIds` to doc post. | Pending elsewhere |
| Inquiry write | PARTIAL | Basic submit exists, but selected file is ignored (`fileIds: []`). | Pending elsewhere |
| Mentoring review write | PARTIAL | Basic submit exists, but selected file is ignored (`fileIds: []`). | Pending elsewhere |
| Password save | FEASIBLE | Current/new password maps to `PATCH /auth/password`. | Pending elsewhere |
| Profile save | FEASIBLE | Editable phone maps to `PATCH /users/me`. | Pending elsewhere |
| Survey submit | PARTIAL | Frontend posts answers, but canonical row only lists survey detail GET. | Pending elsewhere |
| Quest submit | PARTIAL | Frontend posts answer data, but canonical row only lists task detail/result GETs. | Pending elsewhere |

## Evidence by interaction

### Community board write — FEASIBLE

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:51` lists `/community/boards/open/write` with category GET and post POST endpoints.
- Handler: `frontend/src/pages/community/BoardWritePage.vue:14-20` wires submit; `frontend/src/pages/community/BoardWritePage.vue:45-66` validates title/content and calls `createCommunityPost`.
- Service: `frontend/src/services/boardService.js:122-139` resolves category and creates a board post; `frontend/src/services/boardService.js:168-170` maps community write to the open board.
- API module: `frontend/src/api/modules.js:52-58` exposes board categories and createPost.

### Document submit with file path — FEASIBLE

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:35` lists category GET, `POST /api/v1/files`, and doc board post endpoints.
- Handler: `frontend/src/pages/mycampus/DocumentWritePage.vue:14-20`, `frontend/src/pages/mycampus/DocumentWritePage.vue:33-40`, and `frontend/src/pages/mycampus/DocumentWritePage.vue:45-66` cover form fields, validation, service call, reset, and errors.
- Service: `frontend/src/services/mycampusService.js:197-203` resolves document category; `frontend/src/services/mycampusService.js:205-214` uploads a selected file; `frontend/src/services/mycampusService.js:360-387` attaches the uploaded file ID to the created board post.
- API module: `frontend/src/api/modules.js:111-114` exposes file upload; `frontend/src/api/modules.js:52-58` exposes board post creation.

### Inquiry write — PARTIAL

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:61` lists `/help/inquiries/write` with `POST /api/v1/inquiries`.
- Handler: `frontend/src/pages/help/HelpInquiryWritePage.vue:14-20`, `frontend/src/pages/help/HelpInquiryWritePage.vue:33-40`, and `frontend/src/pages/help/HelpInquiryWritePage.vue:45-66` cover form fields, validation, service call, reset, and errors.
- Service: `frontend/src/services/userListService.js:87-95` creates the inquiry but always sends `fileIds: []`.
- API module: `frontend/src/api/modules.js:126-131` exposes inquiry create/update/delete.
- Partial reason: basic inquiry submit is covered, but the visible file field is not wired to upload/attachment.

### Mentoring review write — PARTIAL

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:73` lists `/mentoring/meetups/reviews/write` with category GET and post POST endpoints.
- Handler: `frontend/src/pages/mentoring/MeetupReviewWritePage.vue:14-20`, `frontend/src/pages/mentoring/MeetupReviewWritePage.vue:33-40`, and `frontend/src/pages/mentoring/MeetupReviewWritePage.vue:45-68` cover form fields, validation, service call, reset, and errors.
- Service: `frontend/src/services/boardService.js:122-139` creates the board post with `fileIds: []`; `frontend/src/services/boardService.js:172-175` maps the review to the mentoring review board and prepends meetup text.
- API module: `frontend/src/api/modules.js:52-58` exposes board categories and createPost.
- Partial reason: basic mentoring review submit is covered, but the visible file field is not wired to upload/attachment.

### Password save — FEASIBLE

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:39` lists `/mycampus/password` with `PATCH /api/v1/auth/password`.
- Handler: `frontend/src/pages/mycampus/PasswordPage.vue:14-17`, `frontend/src/pages/mycampus/PasswordPage.vue:39-43`, and `frontend/src/pages/mycampus/PasswordPage.vue:45-66` cover submit wiring, password fields, confirm validation, service call, reset, and errors.
- Service: `frontend/src/services/mycampusService.js:272-278` maps current/new password values to `authApi.changePassword`.
- API module: `frontend/src/api/modules.js:24-40` exposes `PATCH /auth/password`.

### Profile save — FEASIBLE

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:38` lists `/mycampus/profile` with `GET /api/v1/users/me` and `PATCH /api/v1/users/me`.
- Handler: `frontend/src/pages/mycampus/ProfilePage.vue:15-21`, `frontend/src/pages/mycampus/ProfilePage.vue:40-45`, `frontend/src/pages/mycampus/ProfilePage.vue:47-58`, and `frontend/src/pages/mycampus/ProfilePage.vue:60-74` cover submit wiring, editable/read-only fields, load, save, and errors.
- Service: `frontend/src/services/mycampusService.js:255-269` loads `usersApi.me` and patches `phoneNumber` via `usersApi.updateMe`.
- API module: `frontend/src/api/modules.js:42-45` exposes `GET/PATCH /users/me`.

### Survey submit — PARTIAL

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:49` lists `/community/surveys/:id` with `GET /api/v1/surveys/{surveyId}` only.
- Handler: `frontend/src/pages/community/SurveyDetailPage.vue:15-38`, `frontend/src/pages/community/SurveyDetailPage.vue:59-73`, and `frontend/src/pages/community/SurveyDetailPage.vue:87-101` cover question rendering, detail load, answer collection, and submit.
- Service: `frontend/src/services/surveyService.js:132-142` loads detail and posts answers through `surveysApi.submit`.
- API module: `frontend/src/api/modules.js:117-123` exposes `GET /surveys/{id}` and `POST /surveys/{id}/submit`.
- Partial reason: source submit exists, but the canonical contract row does not list the submit POST endpoint.

### Quest submit — PARTIAL

- Canonical: `frontend/docs/design/app-vue/canonical-contract.md:44` lists `/classroom/quests/:id` with task detail and my-result GET endpoints only.
- Handler: `frontend/src/pages/classroom/ClassroomQuestDetailPage.vue:19-34`, `frontend/src/pages/classroom/ClassroomQuestDetailPage.vue:36-54`, and `frontend/src/pages/classroom/ClassroomQuestDetailPage.vue:103-113` cover detail load, non-empty answer validation, submit, result merge, and errors.
- Service: `frontend/src/services/classroomService.js:157-167` loads task detail/result; `frontend/src/services/classroomService.js:169-178` posts `answerData` and refreshes result.
- API module: `frontend/src/api/modules.js:85-90` exposes task detail, submit, and my-result.
- Partial reason: source submit exists, but the canonical contract row does not list the submit POST endpoint.

## Commands run

- `omx team api mailbox-list --input '{...}' --json`
- `omx team api mailbox-mark-delivered --input '{...}' --json`
- `omx team api claim-task --input '{...task_id:8...}' --json`
- `rg -n 'board|write|document|inquiry|mentoring|review|password|profile|survey|quest|submit|form' docs/design/app-vue/canonical-contract.md`
- `rg -n 'submit|handleSubmit|save|password|profile|review|survey|quest|document|inquiry|board|create|update|upload|file' src/pages src/services src/api src/data`
- `nl -ba <relevant source files>`
- `find docs/design/app-vue/verification-pack/api/runtime -maxdepth 2 -type f`
