# CurriculumSection

## Vue files

```txt
src/components/dashboard/CurriculumSection.vue
src/components/dashboard/LectureCard.vue
src/components/ui/OutlineButton.vue
```

## Anatomy

```txt
CurriculumSection
 ├─ section heading
 ├─ plus action
 ├─ timeline date marker
 ├─ timeline time marker
 └─ LectureCard[]
```

## Visual requirements

- Render inside `learning-panel` with `QuestEvaluationPanel`.
- Use left timeline line and blue markers.
- Lecture cards use a soft blue-gray background.
- Action buttons:
  - `강의 다시보기`: green outline
  - `교재`: blue outline

## Data

```txt
curriculum.date
curriculum.time
curriculum.lectures[]
```

