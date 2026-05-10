# 03. Style token map

`DESIGN.md`의 token을 Vue/CSS에 반영하는 기준이다.

## CSS files

```text
src/styles/
├── tokens.css       # DESIGN.md token 반영
├── base.css         # reset/base typography
├── layout.css       # shell, container, header, footer
├── dashboard.css    # dashboard-specific styles
├── responsive.css   # responsive rules
└── index.css        # import entry
```

## Core color variables

```css
:root {
  --color-canvas: #ffffff;
  --color-surface: #f5f7fa;
  --color-surface-subtle: #fafbfc;
  --color-timeline-ice: #f4f9fd;
  --color-border: #d9e0e6;
  --color-divider: #edf1f5;
  --color-text-primary: #222222;
  --color-text-secondary: #5f6f7f;
  --color-text-muted: #8a97a5;
  --color-primary: #2f93e8;
  --color-primary-a11y: #1769aa;
  --color-navy: #24384a;
  --color-footer: #202629;
  --color-danger: #e43d30;
  --color-success: #27b46e;
}
```

## Layout constants

```css
:root {
  --container-width: 1200px;
  --header-height: 96px;
  --hero-height: 150px;
  --section-tab-height: 56px;
  --radius-sm: 4px;
  --radius-pill: 9999px;
}
```

## 적용 규칙

- blue active state는 `--color-primary` 또는 접근성 필요한 텍스트/버튼에서 `--color-primary-a11y`를 쓴다.
- section tab inactive는 `--color-navy`.
- table/form/list border는 `--color-border`와 `--color-divider`.
- 큰 rounded card UI로 바꾸지 않는다. 기본 radius는 4px.
- PrimeVue component를 쓰더라도 CSS override로 SSAFY EDU visual rule을 맞춘다.
