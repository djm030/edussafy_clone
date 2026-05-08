# Dashboard Frame

## Purpose

The dashboard page places attendance, point/level progress, notifications, curriculum, quest/evaluation, study materials, and current e-learning content in one scannable view.

## Desktop frame

```txt
[Global Header]

[Main: max-width 1200px]
  [Top Row]
    [AttendanceCard: 376px] [SummaryPanel: remaining width]

  [Learning Panel]
    [CurriculumSection: flexible] [QuestEvaluationPanel: 400px]

  [Bottom Row]
    [StudyMaterialsSection: 874px] [ELearningCard: 276px]
```

## Responsive behavior

- `> 1180px`: keep the screenshot-like desktop layout.
- `<= 1180px`: stack main sections and hide the external service block.
- `<= 760px`: show mobile menu and render all sections as one column.

## Source mapping

```txt
src/pages/DashboardPage.vue
src/styles/dashboard.css
src/styles/responsive.css
```

