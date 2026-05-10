# app-vue implementation adapter

이 폴더는 SSAFY EDU 클론을 Vue/Vite로 구현할 때 필요한 **adapter 문서**만 둔다.
`DESIGN.md`와 `FRAME.md`의 내용을 다시 복사하지 않는다.

## 기준 우선순위

1. `../../FRAME.md` — 페이지 구조, route, 클릭/이동, 화면 skeleton의 원본 기준
2. `../../DESIGN.md` — 색상, typography, spacing, radius, shadow, component visual rule의 원본 기준
3. `05-screenshot-reference-map.md` — route/page별 실제 화면 screenshot 기준
4. 이 폴더의 adapter 문서 — Vue 파일 매핑, 구현 예외, token 적용 위치, 작업 순서
5. `../reference-primevue/` — PrimeVue/Vue 라이브러리 참고용. SSAFY 클론 기준이 아님

## 읽기 전략

매 작업마다 긴 문서를 전부 읽지 않는다.

- 작업 시작: `design/README.md`와 이 `README.md`만 확인한다.
- 페이지 구현: `05-screenshot-reference-map.md`에서 해당 route의 screenshot을 찾고, `FRAME.md`의 해당 page/route 섹션만 확인한다.
- 스타일 구현: `DESIGN.md` 전체가 아니라 필요한 token/component 섹션만 확인한다.
- Vue 작업: `01-vue-file-map.md`, `02-implementation-deltas.md`, `03-token-application-map.md`만 확인한다.
- 구현 순서 판단: `04-build-order.md`를 확인한다.

## 반드시 지킬 원칙

- Screenshot 경로가 있는 경우 파일명만 보고 추측하지 말고, 반드시 이미지를 열어 확인한다.
- Screenshot은 간격, 밀도, 정렬, 비율, 상태 UI를 맞추기 위한 실제 기준이다.
- `FRAME.md`/`DESIGN.md`와 충돌하는 내용이 있으면 이 폴더 문서가 아니라 루트 문서를 우선한다.
- 이 폴더에는 Vue 구현에서 달라지는 부분만 추가한다.

## 파일 구성

- `01-vue-file-map.md` — `FRAME.md`의 page/component를 Vue 파일로 어디에 둘지 매핑
- `02-implementation-deltas.md` — 원본 기준과 다르게 처리하는 MVP/구현 예외
- `03-token-application-map.md` — `DESIGN.md` token을 CSS 변수/파일에 적용하는 위치
- `04-build-order.md` — 구현 순서와 검증 흐름
- `05-screenshot-reference-map.md` — route/page별 필수 screenshot 참조 맵
- `PROMPT_TEMPLATE.md` — 에이전트에게 페이지 구현을 맡길 때 쓰는 프롬프트 템플릿
- `legacy-dashboard-one-page/` — 이전 한 페이지 대시보드 문서 보관
- `legacy-expanded-app-vue-docs/` — 중복 설명이 많았던 이전 세부 문서 보관. 신규 구현 기준으로 매번 읽지 않는다.
