# 00. Implementation overview

## 목표

`FRAME.md`와 `DESIGN.md`를 기준으로 eduSSAFY Vue 클론을 구현한다. 기존 홈 대시보드 한 페이지 구현을 출발점으로 삼되, 전체 서비스의 공통 shell, section page, board, form, detail, empty state를 확장 가능한 구조로 만든다.

## 최상위 구현 흐름

```text
App.vue
├── RouterView
└── global styles

pages/
├── DashboardPage.vue
├── classroom/
├── community/
├── help/
├── mentoring/
├── mycampus/
├── NotificationInboxPage.vue
├── ForbiddenPage.vue
└── NotFoundPage.vue

components/
├── layout/
├── dashboard/
├── classroom/
├── board/
├── form/
├── mycampus/
├── mentoring/
└── ui/
```

## 구현 기준

- 페이지 골격은 `FRAME.md`를 따른다.
- 색상/typography/spacing은 `DESIGN.md`를 따른다.
- 기존 `src/components/dashboard/*`는 `HomeDashboardPage` 구현 자산으로 유지한다.
- 신규 페이지는 먼저 mock data로 구성하고, API 연동은 나중에 붙인다.

## 금지

- PrimeVue 기본 theme를 기준으로 화면을 재설계하지 않는다.
- 실제 개인정보, 학번, 평가점수, 사용자명을 fixture에 넣지 않는다.
- `design/reference-primevue/` 문서를 `FRAME.md`보다 우선하지 않는다.
