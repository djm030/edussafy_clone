# GlobalHeader

## Vue target

```text
components/layout/GlobalHeader.vue
components/layout/Logo.vue
components/layout/PrimaryNav.vue
components/layout/UserTools.vue
components/layout/ServiceLinks.vue
```

## Navigation

```text
LogoArea -> /dashboard
마이캠퍼스 -> /mycampus/level-points
강의실 -> /classroom/my-replays
커뮤니티 -> /community/surveys
HELP DESK -> /help/notice
멘토링 게시판 -> /mentoring/stories
알림 -> /notifications
```

## Props

```ts
type HeaderUser = {
  userNumber: string;
  name: string;
  notificationCount: number;
};
```

## Style

- white background
- main nav는 dark text + active blue
- service links는 block형 shortcut
- 개인정보는 더미 데이터 사용
