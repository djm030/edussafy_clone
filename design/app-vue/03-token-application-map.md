# Token application map

Token의 실제 값은 `../../DESIGN.md`가 원본이다. 이 문서는 **어느 CSS 파일에 어떤 역할로 적용할지**만 정의한다.

## CSS file responsibility

- `src/styles/tokens.css`
  - `DESIGN.md`의 colors, typography, spacing, radius, shadow를 CSS custom properties로 노출
  - 예: `--color-primary`, `--space-md`, `--radius-card`, `--shadow-card`
- `src/styles/base.css`
  - body, heading, link, button reset, 기본 font smoothing
- `src/styles/layout.css`
  - app shell, header, nav, page container, footer
- `src/styles/dashboard.css`
  - 대시보드 전용 layout/section/card 조합
- `src/styles/responsive.css`
  - breakpoint별 header/nav/card/list 대응
- 신규 page별 CSS가 필요하면 `src/styles/pages/*.css`로 분리 가능

## Mapping rule

- CSS 변수 이름은 semantic하게 유지한다.
- hex 값을 component CSS에 직접 쓰지 않는다.
- 접근성 보정 색상은 `DESIGN.md`의 A11y token을 따른다.
- 상태 badge/pass/fail/empty/error 색상도 `DESIGN.md` token을 참조한다.

## Component application

- Button, Badge, Card, Pagination, EmptyState 같은 공통 UI는 `src/components/ui`와 token CSS를 함께 맞춘다.
- Page CSS는 배치와 조합만 담당하고, 색상/타입 값은 token 변수로 가져온다.
- screenshot과 차이가 날 때는 값 자체를 임의 수정하지 말고 `DESIGN.md` token 보강 여부를 먼저 판단한다.
