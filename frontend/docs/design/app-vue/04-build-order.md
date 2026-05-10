# Build order

구현 순서는 실제 SSAFY EDU clone 품질과 검증 난이도를 기준으로 잡는다.

## 0. 공통 기반

1. Router skeleton
2. App shell/header/nav/footer
3. CSS tokens 적용
4. 공통 UI: Button, Badge, Card, Pagination, EmptyState, BoardTable, FormTable

검증:
- `/dashboard`가 깨지지 않아야 한다.
- header/nav 클릭 이동이 `FRAME.md` 기준과 맞아야 한다.

## 1. Dashboard

- 기준 screenshot: `05-screenshot-reference-map.md`의 `/dashboard`
- 기존 `DashboardPage.vue`와 `components/dashboard/*`를 기준으로 보강

검증:
- home dashboard screenshot과 header, 카드 밀도, preview section, footer 느낌이 맞아야 한다.

## 2. Classroom

- curriculum
- quest list/detail
- resources
- replay/required learning empty

검증:
- dark navy subnav, hero, timeline/list/card 상태 UI가 screenshot과 맞아야 한다.

## 3. Helpdesk / Community / Mentoring boards

- board list/detail/write/form/empty state 공통화
- FAQ/rules accordion

검증:
- table row height, search/filter, pagination, write form table이 screenshot과 맞아야 한다.

## 4. MyCampus

- level/points
- attendance
- elearning/bookmark/document empty states
- pledge/status

검증:
- dashboard성 카드와 empty state가 screenshot 기준으로 맞아야 한다.

## 5. Notifications / errors

- notification inbox
- `/403`
- 필요 시 `/404`

검증:
- 권한 없는 e-book screenshot과 notification page screenshot을 기준으로 맞춘다.

## 작업 단위 원칙

페이지 하나를 만들 때마다:

1. `05-screenshot-reference-map.md`에서 해당 route screenshot 확인
2. 이미지를 직접 열어 확인
3. `FRAME.md` 해당 page/route 섹션만 확인
4. `DESIGN.md` 해당 visual token/component 섹션만 확인
5. 구현
6. 같은 viewport로 화면 캡처 후 screenshot과 비교
7. 차이가 큰 간격/밀도/정렬을 수정
