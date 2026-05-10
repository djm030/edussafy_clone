# Vue Component Structure

## Framework rule

The previous React implementation has been removed and replaced with Vue 3 Single File Components.

```txt
Before:
  App.jsx
  main.jsx
  *.jsx

After:
  App.vue
  main.js
  *.vue
```

## Component hierarchy

```txt
App.vue
 ├─ Header.vue
 │   ├─ Logo.vue
 │   ├─ PrimaryNav.vue
 │   ├─ UserTools.vue
 │   ├─ ServiceLinks.vue
 │   └─ MobileMenu.vue
 └─ DashboardPage.vue
     ├─ AttendanceCard.vue
     ├─ SummaryPanel.vue
     │   ├─ PointLevelSummary.vue
     │   └─ NotificationList.vue
     ├─ CurriculumSection.vue
     │   └─ LectureCard.vue
     ├─ QuestEvaluationPanel.vue
     │   └─ QuestEvaluationItem.vue
     ├─ StudyMaterialsSection.vue
     │   └─ StudyMaterialCard.vue
     └─ ELearningCard.vue
```

## Data rule

Mock data is centralized in `src/data/dashboardData.js`.

```txt
navigation
profile
serviceLinks
attendance
summary
notifications
curriculum
questEvaluations
materials
```

## Styling rule

The Vue components reuse the class-based CSS split by responsibility.

```txt
src/styles/
  tokens.css
  base.css
  layout.css
  dashboard.css
  responsive.css
```

