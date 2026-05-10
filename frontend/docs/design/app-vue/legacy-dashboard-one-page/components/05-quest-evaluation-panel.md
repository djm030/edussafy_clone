# QuestEvaluationPanel

## Vue files

```txt
src/components/dashboard/QuestEvaluationPanel.vue
src/components/dashboard/QuestEvaluationItem.vue
src/components/ui/StatusCircle.vue
```

## Anatomy

```txt
QuestEvaluationPanel
 ├─ title: Quest/평가
 ├─ plus action
 └─ QuestEvaluationItem[]
     ├─ StatusCircle
     ├─ type label
     └─ title
```

## Visual requirements

- Right-side panel background: light gray.
- Scheduled state: white circle with blue border/text.
- Done state: gray filled circle with white text.
- Quest type label is blue, evaluation type label is black.

## Data

```txt
questEvaluations[]
```

