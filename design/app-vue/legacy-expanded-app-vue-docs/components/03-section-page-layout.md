# SectionPageLayout

## Vue target

```text
components/layout/SectionPageLayout.vue
components/layout/PageHero.vue
components/layout/SectionTabs.vue
components/layout/MainContainer.vue
```

## Structure

```text
SectionPageLayout
├── PageHero(title)
├── SectionTabs(tabs, active)
└── MainContainer
    └── slot
```

## Used by

- classroom pages
- community pages
- help desk pages
- mentoring pages

## Props

```ts
type SectionPageLayoutProps = {
  title: string;
  tabs: PageTab[];
  activeTab: string;
};
```
