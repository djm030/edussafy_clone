# AppShell

## Vue target

```text
App.vue
components/layout/GlobalHeader.vue
components/layout/PageHero.vue
components/layout/SectionTabs.vue
components/layout/MainContainer.vue
components/layout/Footer.vue
components/ui/FloatingQuickAction.vue
```

## Structure

```text
AppShell
├── GlobalHeader
├── RouterView page
│   ├── PageHero optional
│   ├── SectionTabs optional
│   └── MainContainer
├── FloatingQuickAction optional
└── Footer
```

## Notes

- 인증 후 대부분의 화면에서 사용한다.
- 로그인 화면은 별도 layout을 사용한다.
- 403/404도 Header/Footer 포함 여부를 project route guard에서 결정한다.
