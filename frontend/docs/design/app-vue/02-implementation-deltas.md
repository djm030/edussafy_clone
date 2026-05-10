# Implementation deltas

`FRAME.md`와 `DESIGN.md`가 원본 기준이다. 이 문서는 Vue 구현에서만 필요한 예외와 차이만 적는다.

## MVP에서 placeholder로 처리 가능한 것

- 실제 SSO/login 연동: `/login` route placeholder 또는 mock state로 처리
- 외부 서비스 링크: `type: external`로 분리하고 새 창 또는 disabled placeholder 처리
- 파일 다운로드: 실제 파일이 없으면 `download` action mock 처리
- 서버 mutation: 좋아요/bookmark/read/submit은 local state 또는 mock API로 시작
- 권한 체크: e-book 등 권한 없는 화면은 `/403` placeholder로 우선 구현

## route 이동이 아닌 UI 상태 변경

아래는 router push가 아니라 local state로 처리한다.

- FAQ item 열고 닫기
- 학사규정 accordion expand/collapse
- user profile menu dropdown
- mobile menu open/close
- 주차/월간 toggle
- filter dropdown, search input, pagination query sync

## 데이터 연결 방식

- 우선 `src/data/*.js` mock data로 화면을 맞춘다.
- screenshot 기준 visual clone이 먼저이고, API 연결은 이후 단계다.
- mock data shape은 `FRAME.md`의 최소 props 설계 힌트를 참고하되, 이 문서에 중복 복사하지 않는다.

## 구현 중 충돌 해결

- 화면 구조가 헷갈리면 `FRAME.md` 우선
- 색/간격/타입/카드 visual이 헷갈리면 `DESIGN.md` 우선
- 문서와 실제 screenshot 느낌이 다르면 screenshot을 열어보고 `FRAME.md`/`DESIGN.md`의 해당 섹션을 보강 대상으로 표시
- PrimeVue 기본 스타일이 SSAFY screenshot과 다르면 PrimeVue 기본 스타일을 그대로 쓰지 말고 override한다.

## 금지

- `DESIGN.md` token 값을 이 문서에 다시 복사하지 않는다.
- `FRAME.md` route 목록을 이 문서에 다시 복사하지 않는다.
- screenshot 파일명만 보고 화면을 추측하지 않는다.
