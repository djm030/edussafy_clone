# Vue file map

이 문서는 `FRAME.md` 내용을 반복하지 않고, **FRAME의 page/component를 Vue 프로젝트에서 어느 파일로 구현할지**만 정의한다.

## App shell

- `src/App.vue`
  - Vue Router outlet, global layout boundary
- `src/components/layout/`
  - `Header.vue`
  - `Logo.vue`
  - `PrimaryNav.vue`
  - `ServiceLinks.vue`
  - `UserTools.vue`
  - `MobileMenu.vue`

## Pages

권장 신규 구조:

```text
src/pages/
├── DashboardPage.vue
├── classroom/
│   ├── WeeklyCurriculumPage.vue
│   ├── QuestListPage.vue
│   ├── QuestDetailPage.vue
│   ├── LearningResourcesPage.vue
│   ├── MyLectureReplayPage.vue
│   ├── AllLectureReplayPage.vue
│   └── RequiredLearningPage.vue
├── community/
│   ├── SurveyPage.vue
│   ├── OpenBoardPage.vue
│   ├── OpenBoardWritePage.vue
│   ├── AnonymousBoardPage.vue
│   └── ClassRosterPage.vue
├── helpdesk/
│   ├── NoticePage.vue
│   ├── NoticeDetailPage.vue
│   ├── FaqPage.vue
│   ├── InquiryPage.vue
│   ├── InquiryWritePage.vue
│   └── AcademicRulesPage.vue
├── mentoring/
│   ├── QnaBoardPage.vue
│   ├── StoryBoardPage.vue
│   ├── NoticeBoardPage.vue
│   ├── MeetupApplyPage.vue
│   ├── MeetupInfoPage.vue
│   ├── MeetupReviewBoardPage.vue
│   └── MeetupReviewWritePage.vue
├── mycampus/
│   ├── LevelPointsPage.vue
│   ├── AttendanceStatusPage.vue
│   ├── LearningElearningPage.vue
│   ├── BookmarksPage.vue
│   ├── DocumentSubmissionPage.vue
│   ├── DocumentSubmissionWritePage.vue
│   ├── StudentPledgePage.vue
│   └── EducationStatusPage.vue
├── notifications/NotificationInboxPage.vue
└── errors/ForbiddenPage.vue
```

## Shared components

```text
src/components/
├── layout/              # header/nav/footer/app shell
├── dashboard/           # 홈 대시보드 전용 preview 카드
├── classroom/           # curriculum, quest, resource, replay UI
├── board/               # board table/list/detail/write 공통
├── form/                # form table, attachment, submit controls
├── helpdesk/            # FAQ/rules accordion 등
├── mycampus/            # points, attendance, pledge, status cards
└── ui/                  # Button, Badge, Card, Pagination, EmptyState, Icon
```

## Router

- route path와 클릭 이동 기준은 `../../FRAME.md`의 route/클릭 이동 섹션을 따른다.
- 이 문서는 Vue 파일 위치만 정의한다.
- route name은 page file명과 맞춰서 추적 가능하게 둔다.

## 기존 소스와 연결

현재 프로젝트에는 이미 대시보드 중심 파일이 있다.

- 기존 `src/pages/DashboardPage.vue`와 `src/components/dashboard/*`는 `/dashboard` 구현의 출발점으로 사용한다.
- 신규 페이지는 위 권장 구조로 추가한다.
- 기존 dashboard 컴포넌트를 모든 페이지에 억지 재사용하지 않는다. 공통화가 필요한 부분만 `src/components/ui` 또는 `src/components/board`로 올린다.
