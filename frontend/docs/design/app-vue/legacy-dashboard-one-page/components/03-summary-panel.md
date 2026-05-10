# SummaryPanel

## Vue files

```txt
src/components/dashboard/SummaryPanel.vue
src/components/dashboard/PointLevelSummary.vue
src/components/dashboard/NotificationList.vue
```

## Anatomy

```txt
SummaryPanel
 ├─ PointLevelSummary
 │   ├─ scholarship point
 │   ├─ level & exp
 │   └─ progress line
 └─ NotificationList
     ├─ heading
     └─ notification rows
```

## Visual requirements

- Whole panel background: blue.
- Point and EXP values: yellow.
- Notification panel uses a left divider.
- Notification rows show required badge, title, and date.

## Data

```txt
summary.scholarshipPoint
summary.exp
summary.currentLevel
summary.progress
notifications[]
```

