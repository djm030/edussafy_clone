# 01. Component map

`FRAME.md`의 프레임 컴포넌트를 Vue 파일 구조로 매핑한다.

## Layout

```text
FRAME.md                  Vue component
---------------------------------------------------------
AuthenticatedApp          App.vue + router layout
GlobalHeader              components/layout/GlobalHeader.vue
LogoArea                  components/layout/Logo.vue
PrimaryNavigation         components/layout/PrimaryNav.vue
HeaderUtilityArea         components/layout/UserTools.vue
ServiceShortcutTiles      components/layout/ServiceLinks.vue
PageHero                  components/layout/PageHero.vue
SectionSubNavigation      components/layout/SectionTabs.vue
MainContainer             components/layout/MainContainer.vue
Footer                    components/layout/Footer.vue
FloatingQuickAction       components/ui/FloatingQuickAction.vue
```

## Dashboard

```text
HomeDashboardPage         pages/DashboardPage.vue
AttendanceCheckCard       components/dashboard/AttendanceCard.vue
ScholarshipPointCard      components/dashboard/PointLevelSummary.vue
LevelExpCard              components/dashboard/PointLevelSummary.vue
NotificationPreviewCard   components/dashboard/NotificationList.vue
WeeklyCurriculumPreview   components/dashboard/CurriculumSection.vue
QuestEvaluationPreview    components/dashboard/QuestEvaluationPanel.vue
LearningMaterialPreview   components/dashboard/StudyMaterialsSection.vue
ElearningPreview          components/dashboard/ELearningCard.vue
```

## Classroom

```text
WeeklyCurriculumPage      pages/classroom/WeeklyCurriculumPage.vue
SemesterPhaseStepper      components/classroom/SemesterPhaseStepper.vue
CurriculumToolbar         components/classroom/CurriculumToolbar.vue
WeekCarouselSelector      components/classroom/WeekCarouselSelector.vue
CurriculumTimeline        components/classroom/CurriculumTimeline.vue
QuestEvaluationPage       pages/classroom/QuestEvaluationPage.vue
QuestEvaluationCard       components/classroom/QuestEvaluationCard.vue
LearningResourcePage      pages/classroom/LearningResourcePage.vue
ResourceListItem          components/classroom/ResourceListItem.vue
```

## Board / form common

```text
BoardTable                components/board/BoardTable.vue
BoardPostRow              components/board/BoardPostRow.vue
BoardDetail               components/board/BoardDetail.vue
BoardWriteFormTable       components/form/FormTable.vue
SearchFilterBar           components/ui/SearchFilterBar.vue
Pagination                components/ui/Pagination.vue
EmptyState                components/ui/EmptyState.vue
StatusCircle              components/ui/StatusCircle.vue
Badge                     components/ui/Badge.vue
```

## MyCampus

```text
LevelPointPage            pages/mycampus/LevelPointPage.vue
AttendanceStatusPage      pages/mycampus/AttendanceStatusPage.vue
DocumentSubmissionPage    pages/mycampus/DocumentSubmissionPage.vue
PledgeDocumentPage        pages/mycampus/PledgeDocumentPage.vue
EducationStatusPage       pages/mycampus/EducationStatusPage.vue
```

## 우선 구현

1. `GlobalHeader`, `PageHero`, `SectionTabs`, `MainContainer`, `Footer`
2. `SearchFilterBar`, `BoardTable`, `FormTable`, `Pagination`, `EmptyState`
3. `DashboardPage`
4. `WeeklyCurriculumPage`, `QuestEvaluationPage`, `LearningResourcePage`
5. Board/detail/write pages
