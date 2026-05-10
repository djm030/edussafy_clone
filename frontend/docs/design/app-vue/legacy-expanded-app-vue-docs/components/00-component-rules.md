# Component rules

공통 컴포넌트 작성 기준이다.

## 원칙

- 컴포넌트 구조는 `FRAME.md`를 따른다.
- 스타일은 `DESIGN.md` token을 따른다.
- 페이지 전용 컴포넌트와 공통 컴포넌트를 분리한다.
- 실제 데이터 연동 전에는 mock data props로 구현한다.

## Props 규칙

- route 이동이 있는 요소는 `href` 또는 `action: NavigationAction`을 명시한다.
- table/list row는 `id`, `title`, `meta`, `href`를 분리한다.
- 좋아요/찜/다운로드는 route와 mutation/download action을 분리한다.

## 스타일 규칙

- class 이름은 역할 기반으로 작성한다.
- PrimeVue class에 의존하지 않고 앱 CSS에서 override 가능하게 한다.
- `scoped` CSS보다 공통 token/class 재사용을 우선한다.
