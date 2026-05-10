# 02. Route and navigation map

이 문서는 `FRAME.md`의 `Route 제안`과 `클릭 / 이동 연결 명세`를 Vue Router 기준으로 옮긴다.

## Route groups

```text
/dashboard
/mycampus/*
/classroom/*
/community/*
/help/*
/mentoring/*
/notifications
/403
/404
```

## Header navigation

```text
LogoArea                  -> /dashboard
마이캠퍼스                 -> /mycampus/level-points
강의실                    -> /classroom/my-replays
커뮤니티                  -> /community/surveys
HELP DESK                 -> /help/notice
멘토링 게시판              -> /mentoring/stories
NotificationButton        -> /notifications
회원정보 수정              -> /mycampus/profile
비밀번호 변경              -> /mycampus/password
로그아웃                  -> /login
```

## Section tabs

```text
강의실:
/classroom/my-replays
/classroom/all-replays
/classroom/curriculum
/classroom/quests
/classroom/required-learning
/classroom/resources

커뮤니티:
/community/surveys
/community/boards/open
/community/boards/anonymous
/community/class-roster

HELP DESK:
/help/notice
/help/faq
/help/inquiries
/help/rules

멘토링:
/mentoring/stories
/mentoring/qna
/mentoring/notice
/mentoring/meetups/apply
/mentoring/meetups/info
/mentoring/meetups/reviews
```

## NavigationAction type

```ts
type NavigationAction =
  | { type: 'route'; href: string }
  | { type: 'external'; href: string; target?: '_blank' }
  | { type: 'modal'; modalId: string }
  | { type: 'toggle'; stateKey: string }
  | { type: 'mutation'; action: 'like' | 'bookmark' | 'read' | 'submit' }
  | { type: 'download'; fileId: string };
```

## 구현 규칙

- 화면 전환은 Vue Router `router-link` 또는 `router.push`를 사용한다.
- 좋아요/찜/읽음은 route 이동 없이 mutation으로 처리한다.
- FAQ, 학사규정 accordion은 route 이동 없이 toggle한다.
- 첨부파일은 download action으로 처리한다.
- 외부 SSAFY 서비스는 external link로 분리한다.
