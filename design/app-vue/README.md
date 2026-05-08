# SSAFY Dashboard Vue Mockup Design Notes

This folder documents the Vue 3 implementation for the SSAFY dashboard mockup shown in the reference screenshot.

The root `DESIGN.md` and `design/components/*` files contain the broader PrimeVue/Vue reference. This `app-vue` folder documents the actual page implemented in `src/`.

## Scope

- Target screen: SSAFY My Campus dashboard home
- Implementation scope: one-page visual mockup
- Framework: Vue 3 Single File Components
- Build tool: Vite
- UI approach: custom CSS to match the screenshot

## Files

```txt
app-vue/
  README.md
  00-dashboard-frame.md
  01-vue-component-structure.md
  02-ui-ux-pro-max-pass.md
  components/
    01-header.md
    02-attendance-card.md
    03-summary-panel.md
    04-curriculum-section.md
    05-quest-evaluation-panel.md
    06-study-materials-section.md
    07-elearning-card.md
```

## Code mapping

```txt
src/
  App.vue
  main.js
  pages/DashboardPage.vue
  components/layout/*.vue
  components/dashboard/*.vue
  components/ui/*.vue
  data/dashboardData.js
  styles/*.css
```

## Improvement passes

- [UI/UX Pro Max Pass](./02-ui-ux-pro-max-pass.md)
