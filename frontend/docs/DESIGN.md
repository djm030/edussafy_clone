---
version: alpha
name: Edu SSAFY Clone Design System
description: SSAFY EDU authenticated portal clone reference based on captured screens; corporate blue LMS dashboard, navy section navigation, dense Korean data lists, board/form pages, and education-status components.
colors:
  canvas: "#FFFFFF"
  surface: "#F5F7FA"
  surfaceSubtle: "#FAFBFC"
  timelineIce: "#F4F9FD"
  border: "#D9E0E6"
  divider: "#EDF1F5"
  textPrimary: "#222222"
  textSecondary: "#5F6F7F"
  textMuted: "#8A97A5"
  primary: "#2F93E8"
  primaryDeep: "#1F7FD1"
  primaryA11y: "#1769AA"
  navy: "#24384A"
  navyDeep: "#1F3448"
  footer: "#202629"
  danger: "#E43D30"
  dangerA11y: "#B82A20"
  fail: "#E85C6A"
  failA11y: "#B93445"
  success: "#27B46E"
  successA11y: "#177245"
  statusSlate: "#8D9DAD"
  statusSlateA11y: "#5F6F7F"
  attendanceYellow: "#FFD84D"
  categoryAlgorithm: "#00B46A"
  categoryCoding: "#8B5CF6"
  categoryProject: "#F044A4"
  categoryEtc: "#5BB8FF"
typography:
  body:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, SamsungOne, Inter, system-ui, sans-serif
    fontSize: 14px
    fontWeight: 400
    lineHeight: 1.55
  metadata:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter, system-ui, sans-serif
    fontSize: 13px
    fontWeight: 400
    lineHeight: 1.45
  micro:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter, system-ui, sans-serif
    fontSize: 12px
    fontWeight: 500
    lineHeight: 1.45
  nav:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter, system-ui, sans-serif
    fontSize: 15px
    fontWeight: 500
    lineHeight: 1.35
  listTitle:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter, system-ui, sans-serif
    fontSize: 16px
    fontWeight: 600
    lineHeight: 1.45
  sectionTitle:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter, system-ui, sans-serif
    fontSize: 20px
    fontWeight: 700
    lineHeight: 1.35
  heroTitle:
    fontFamily: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter, system-ui, sans-serif
    fontSize: 30px
    fontWeight: 700
    lineHeight: 1.2
  dashboardMetric:
    fontFamily: Inter, Pretendard, Noto Sans KR, system-ui, sans-serif
    fontSize: 32px
    fontWeight: 700
    lineHeight: 1.2
rounded:
  none: 0px
  xs: 2px
  sm: 4px
  md: 6px
  pill: 9999px
spacing:
  xxs: 4px
  xs: 6px
  sm: 8px
  md: 12px
  lg: 16px
  xl: 20px
  xxl: 24px
  section: 40px
  pageTop: 56px
  hero: 80px
components:
  button-primary:
    backgroundColor: "{colors.primaryA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.sm}"
    padding: 12px
  button-primary-hover:
    backgroundColor: "#155D96"
    textColor: "#FFFFFF"
    rounded: "{rounded.sm}"
    padding: 12px
  button-outline-blue:
    backgroundColor: "#FFFFFF"
    textColor: "{colors.primaryA11y}"
    rounded: "{rounded.sm}"
    padding: 10px
  button-outline-green:
    backgroundColor: "#FFFFFF"
    textColor: "{colors.successA11y}"
    rounded: "{rounded.sm}"
    padding: 10px
  subnav-tab:
    backgroundColor: "{colors.navy}"
    textColor: "#FFFFFF"
    rounded: "{rounded.none}"
    padding: 16px
  subnav-tab-active:
    backgroundColor: "{colors.primaryA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.none}"
    padding: 16px
  status-circle-pending:
    backgroundColor: "#FFFFFF"
    textColor: "{colors.primaryA11y}"
    rounded: "{rounded.pill}"
    width: 58px
    height: 58px
  status-circle-complete:
    backgroundColor: "{colors.statusSlateA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.pill}"
    width: 58px
    height: 58px
  badge-pass:
    backgroundColor: "{colors.primaryA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.pill}"
    padding: 6px
  badge-fail:
    backgroundColor: "{colors.failA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.pill}"
    padding: 6px
  pagination-active:
    backgroundColor: "{colors.primaryA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.xs}"
    width: 34px
    height: 34px
  floating-action:
    backgroundColor: "{colors.dangerA11y}"
    textColor: "#FFFFFF"
    rounded: "{rounded.pill}"
    width: 56px
    height: 56px
  surface-card:
    backgroundColor: "{colors.canvas}"
    rounded: "{rounded.sm}"
    padding: 24px
  surface-filter:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.sm}"
    padding: 16px
  surface-hover-row:
    backgroundColor: "{colors.surfaceSubtle}"
    rounded: "{rounded.none}"
    padding: 12px
  curriculum-card:
    backgroundColor: "{colors.timelineIce}"
    rounded: "{rounded.sm}"
    padding: 20px
  board-divider:
    backgroundColor: "{colors.divider}"
    rounded: "{rounded.none}"
    height: 1px
  board-border:
    backgroundColor: "{colors.border}"
    rounded: "{rounded.none}"
    height: 1px
  text-primary-swatch:
    backgroundColor: "{colors.textPrimary}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  text-secondary-swatch:
    backgroundColor: "{colors.textSecondary}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  text-muted-swatch:
    backgroundColor: "{colors.textMuted}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  primary-visual-swatch:
    backgroundColor: "{colors.primary}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  primary-deep-visual-swatch:
    backgroundColor: "{colors.primaryDeep}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  navy-deep-swatch:
    backgroundColor: "{colors.navyDeep}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  footer-block:
    backgroundColor: "{colors.footer}"
    rounded: "{rounded.none}"
    padding: 24px
  danger-visual-swatch:
    backgroundColor: "{colors.danger}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  fail-visual-swatch:
    backgroundColor: "{colors.fail}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  success-visual-swatch:
    backgroundColor: "{colors.success}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  status-slate-visual-swatch:
    backgroundColor: "{colors.statusSlate}"
    rounded: "{rounded.xs}"
    width: 16px
    height: 16px
  attendance-card:
    backgroundColor: "{colors.attendanceYellow}"
    textColor: "{colors.textPrimary}"
    rounded: "{rounded.sm}"
    padding: 24px
  category-algorithm-dot:
    backgroundColor: "{colors.categoryAlgorithm}"
    rounded: "{rounded.pill}"
    width: 10px
    height: 10px
  category-coding-dot:
    backgroundColor: "{colors.categoryCoding}"
    rounded: "{rounded.pill}"
    width: 10px
    height: 10px
  category-project-dot:
    backgroundColor: "{colors.categoryProject}"
    rounded: "{rounded.pill}"
    width: 10px
    height: 10px
  category-etc-dot:
    backgroundColor: "{colors.categoryEtc}"
    rounded: "{rounded.pill}"
    width: 10px
    height: 10px
---

## Overview

이 문서는 `edu.ssafy.com` 인증 후 화면을 기준으로 한 SSAFY EDU 클론 코딩용 디자인 명세다. 기준 스크린샷은 `/Users/baeggwan-yeol/Desktop/edu_screnshot`에 수집된 홈 대시보드, 강의실, 커뮤니티, HELP DESK, 멘토링 게시판, 마이캠퍼스, 알림, 글쓰기/상세 화면이다.

목표는 “예쁜 새 디자인”이 아니라 실제 SSAFY EDU의 화면 문법을 재현하는 것이다. UI는 밝은 흰색 canvas 위에 SSAFY blue active state, dark navy section navigation, dense Korean data lists, 표/게시판/폼 중심 구조, 상태 badge, 학습 카드, floating quick action을 반복한다.

핵심 인상:

- 기관형 교육 포털: 화려한 SaaS보다 공지, 평가, 출석, 학습자료를 안정적으로 보여주는 구조.
- 파란색 active state: 탭, pagination, 날짜, 선택 상태, 주요 CTA는 `#2F93E8` 계열.
- dark navy subnav: 대분류 안의 하위 메뉴는 짙은 남색 막대 tab으로 구분.
- hero banner: 강의실/게시판 계열 페이지 상단에 이미지 banner와 중앙 흰색 title을 반복.
- 조밀한 정보 밀도: 13~16px 텍스트, 얇은 divider, 카드형 row, board table, form table을 많이 사용.
- 상태 중심 UI: 예정/완료/PASS/FAIL/제출완료/빈 목록/읽음 여부 등 상태를 badge와 muted color로 명확히 표현.

## Colors

- **Canvas `#FFFFFF`:** 대부분의 페이지 배경, 카드, board, form, list surface. 화면은 전체적으로 white-first다.
- **Surface `#F5F7FA`:** table header, filter box, empty state panel, form label column에 사용한다.
- **Timeline Ice `#F4F9FD`:** 주차별 커리큘럼 카드처럼 학습 일정/선택 영역에 쓰는 아주 연한 blue-gray다.
- **Border `#D9E0E6`:** input, select, table, list row, pagination inactive border. 선은 얇고 차분해야 한다.
- **Divider `#EDF1F5`:** row 사이의 미세한 구분선. border보다 더 연하게 쓴다.
- **Text Primary `#222222`:** 제목, 게시글명, 폼 label, 주요 데이터.
- **Text Secondary `#5F6F7F`:** 날짜, 설명, breadcrumb, metadata.
- **Text Muted `#8A97A5`:** placeholder, disabled, empty state 보조 문구.
- **Primary Blue `#2F93E8`:** active tab, primary button, selected pagination, timeline date, 강조 count, PASS badge.
- **Primary Deep `#1F7FD1`:** hover/pressed blue 또는 조금 더 강한 active state.
- **Dark Navy `#24384A`:** 하위 tab navigation의 기본 배경.
- **Navy Deep `#1F3448`:** tab/nav hover 또는 darker section background.
- **Footer `#202629`:** footer background.
- **Danger `#E43D30`:** floating action button, 알림 강조, 삭제/실패 계열의 강한 red.
- **Fail `#E85C6A`:** FAIL pill badge와 평가 실패 상태.
- **Success `#27B46E`:** 강의 다시보기 outline, Meeting SSAFY, 성공/긍정 액션.
- **Status Slate `#8D9DAD`:** 완료 원형 badge, 비활성 완료 상태.
- **Attendance Yellow `#FFD84D`:** 홈 대시보드 출석 카드 배경.
- **Category colors:** 알고리즘 green, 코딩 purple, 프로젝트 pink, 기타 sky. 범례 dot과 label에 제한적으로 사용한다.

색상 사용 규칙:

- 한 화면에서 primary blue는 active/selected/CTA에만 사용하고, decorative하게 남발하지 않는다.
- 게시판/폼 화면은 white, surface, border, text 중심으로 차분하게 구성한다.
- semantic color는 badge와 작은 label에만 사용한다. 큰 영역 배경은 attendance card 같은 예외 외에는 피한다.
- 링크/선택 가능 텍스트는 blue 또는 underline보다 blue color 자체로 구분한다.

## Typography

기본 폰트는 한국어 가독성이 좋은 sans-serif다. 실제 구현에서는 `Pretendard`, `Noto Sans KR`, `Apple SD Gothic Neo`, `SamsungOne`, `Inter`, `system-ui` 순서를 권장한다.

권장 scale:

- **12px micro:** badge 내부, table 보조 label, 작은 metadata.
- **13px metadata:** 날짜, 조회수, 좋아요, breadcrumb, 작성자, 기간.
- **14px body:** 일반 본문, form input, board row text, 설명문.
- **15px nav:** global nav와 subnav label.
- **16px list title:** 게시글 제목, 학습자료 제목, Quest/평가 제목.
- **20px section title:** 대시보드 card heading, 주요 section heading.
- **30px hero title:** banner 중앙 title.
- **32px dashboard metric:** 포인트, EXP, count 등 큰 숫자.

가중치:

- 본문은 400.
- navigation, metadata 강조는 500.
- list title은 500~600.
- hero, section, metric은 700.

타이포그래피 주의점:

- 지나친 letter spacing을 쓰지 않는다. SSAFY EDU는 장식적 타이포보다 실용적 텍스트 UI다.
- 한글 line-height는 body 1.55 전후로 여유를 둔다.
- board/list 제목은 16px 이상으로 두되, row 높이를 과하게 키우지 않는다.
- 숫자/점수/EXP는 Inter나 Pretendard 숫자 폭이 안정적인 폰트를 사용한다.

## Layout

공통 desktop layout:

- **Page width:** 1180px~1240px centered container.
- **Global header:** 약 88~100px. 로고, main nav, 알림, 사용자 영역, 외부 service shortcut을 포함한다.
- **Hero banner:** 약 150px. 이미지 배경 + 어두운 overlay + 중앙 흰색 title.
- **Subnav bar:** 약 56px. dark navy tab row, active tab은 primary blue.
- **Page top gap:** subnav 아래 50~60px.
- **Main content:** white canvas 위 card/list/table 중심. 큰 radius나 강한 shadow는 피한다.
- **Footer:** dark charcoal full-width footer.
- **Floating action:** 우측 하단 또는 우측 중단에 red circular quick button. 페이지 콘텐츠보다 위에 떠야 한다.

반복 page skeleton:

```text
GlobalHeader
HeroBanner(optional on section pages)
SectionSubNavigation(optional)
MainContainer
  PageTitle or Filter/Search Area
  ContentList / BoardTable / FormTable / DashboardGrid
  Pagination(optional)
FloatingQuickAction(optional)
Footer
```

간격 규칙:

- Container 내부 section 간격은 32~40px.
- 리스트 row 간격은 12~20px.
- Board table row vertical padding은 18~24px.
- Form table row padding은 18~24px.
- Card 내부 padding은 20~24px.
- 작은 button은 8~12px vertical/horizontal padding.

## Elevation & Depth

SSAFY EDU는 강한 material shadow를 거의 쓰지 않는다.

- 기본 card/list는 border와 background 차이로 구분한다.
- Dashboard card 정도에만 `0 2px 8px rgba(20, 40, 60, 0.08)` 수준의 약한 shadow를 허용한다.
- Floating button은 떠 있는 느낌을 위해 `0 8px 18px rgba(0, 0, 0, 0.22)` 정도를 사용한다.
- Hero banner는 image + dark overlay로 depth를 만든다.
- Modal/dropdown을 만들 경우 shadow를 강하게 쓰기보다 border + subtle shadow 조합으로 간다.

## Shapes

- **Default radius:** 4px.
- **Input/select/button:** 4px.
- **Card row:** 4px 또는 거의 square.
- **Tabs/subnav/hero:** 0px. 막대형 구조를 유지한다.
- **Pagination:** 2px square button.
- **Status circle:** 9999px.
- **Badge pill:** 9999px.
- **Service shortcut tile:** 0px 또는 매우 작은 radius. 직사각형 block 느낌.

형태 규칙:

- 과한 rounded-xl/card UI로 바꾸지 않는다. SSAFY EDU는 사각형 기반의 공공/교육 포털 느낌이다.
- 원형은 status badge, profile/icon, floating action처럼 의미가 있는 곳에만 사용한다.
- 입력 폼은 table-like row 구조와 rectangular input을 우선한다.

## Components

### GlobalHeader

구성:

- 좌측 SSAFY logo / `SAMSUNG SW·AI ACADEMY FOR YOUTH` identity.
- 중앙 main navigation: `마이캠퍼스`, `강의실`, `커뮤니티`, `HELP DESK`, `멘토링 게시판`.
- 우측 알림 count, 사용자 식별 영역.
- service shortcut: `JOB SSAFY`, `SSAFY GIT`, `Meeting! SSAFY`.

규칙:

- Header background는 white.
- Main nav text는 dark text, hover/active는 blue 또는 underline/strong weight.
- 알림 count는 red 또는 blue accent를 작게 사용한다.
- service shortcut은 독립 block처럼 보이며, main nav보다 기능성 링크 느낌이 강해야 한다.

### LoginScreen

구성:

- 중앙 또는 좌우 분할 로그인 box.
- SSAFY brand title.
- 아이디 input, 비밀번호 input.
- `아이디 저장` checkbox.
- primary login button.
- `비밀번호 찾기`, `개인정보처리방침` link.

규칙:

- Login form은 white card 또는 white panel 위에 배치한다.
- Input border는 `#D9E0E6`, focus는 primary blue.
- Login button은 full-width blue CTA.

### HeroBanner

구성:

- 교육/학습 관련 image background.
- dark translucent overlay.
- 중앙 white title: `주차별 커리큘럼`, `Quest/평가`, `학습자료`, `공지사항` 등.

규칙:

- Height는 약 150px.
- Title은 30px/700, white.
- Banner 아래에는 곧바로 dark navy subnav가 붙는 패턴이 많다.

### SectionSubNavigation

구성:

- Dark navy full-width tab row.
- 각 tab은 equal 또는 content-width block.
- active tab은 primary blue background.

예시:

- 강의실: `내강의 다시보기`, `전체강의 다시보기`, `주차별 커리큘럼`, `Quest/평가`, `필수학습`, `학습자료`.
- 커뮤니티: `설문조사`, `열린 게시판`, `익명 게시판`, `우리반 보기`.
- HELP DESK: `공지사항`, `FAQ`, `1:1 문의`, `학사규정`.
- 멘토링 게시판: `멘토 스토리`, `멘토링`, `멘토링 공지사항`, `간담회 신청`, `간담회 정보`, `간담회 후기`.

### DashboardCard

홈 대시보드의 핵심 card. white card, light border/shadow, 20~24px padding.

종류:

- 출석체크 & 현황: yellow background, 날짜/요일/입퇴실 안내.
- 장학포인트/레벨/경험치: metric number와 level label.
- 알림 card: 리스트형 notification preview.
- 주차별 커리큘럼 / Quest/평가 / 학습자료 preview.

규칙:

- Metric은 24~32px bold.
- 카드 제목은 18~20px bold.
- 내부 리스트는 divider로 조밀하게 나눈다.

### CurriculumPhaseStepper

`진행중 1학기`, `예정 1차 Job Fair`, `예정 2학기`, `예정 2차 Job Fair` 같은 단계형 UI.

규칙:

- 진행중은 primary blue 또는 strong text.
- 예정은 muted text와 light border.
- 가로 step line을 사용하되 decorative하게 과하지 않게 만든다.

### ViewModeToggle

`주간`, `월간` 같은 segmented toggle.

규칙:

- Active는 primary blue fill + white text.
- Inactive는 white fill + border + secondary text.
- Radius는 4px 이하.

### CurriculumCategoryLegend

알고리즘/코딩과정/프로젝트/기타를 색 dot과 label로 표시한다.

규칙:

- Dot은 8~10px.
- Label은 13px metadata.
- category color는 dot/작은 label에만 사용한다.

### WeekCarouselSelector

주차 선택 UI.

구성:

- 기간 `2026.05.04~2026.05.10`.
- `18주차` 같은 week label.
- 이전/다음 arrow.
- active underline 또는 blue emphasis.

규칙:

- Active week는 blue text/underline.
- Disabled arrow는 muted gray.

### CurriculumTimeline

날짜별 강의 일정 list.

구성:

- 좌측 날짜 column: `2026.05.04(월)`.
- 시간: `09:00~18:00`.
- timeline marker/line.
- 우측 curriculum card.
- action button: `강의 다시보기`, `교재`.

규칙:

- 날짜와 marker는 primary blue.
- Card background는 `timelineIce`.
- `강의 다시보기`는 green outline, `교재`는 blue outline.
- Card 내부 text는 14~16px, metadata는 13px.

### QuestEvaluationCard

Quest/평가 목록 row card.

구성:

- Type label: `평가`, `Quest`.
- 원형 status badge: `예정`, `완료`.
- 제목.
- 기간.
- 획득 가능 경험치.
- 결과/상태: `시험기간`, `채점완료`, `PASS 73점`, `FAIL 0점`, `제출완료`.

규칙:

- Card height는 약 135~145px.
- Status circle은 58px 전후.
- `예정`: white fill + blue border/text.
- `완료`: slate fill + white text.
- PASS는 blue pill, FAIL은 red/pink pill.
- 경험치 숫자는 data font와 bold weight.

### LearningMaterialSearch

학습자료/자료실 목록 상단 filter/search.

구성:

- Category tab 또는 chip: `오픈러닝`, `전체`, `커리큘럼`.
- Keyword input.
- Search button.
- Count: `총 605건`.
- Checkbox: `교재`.
- Select: `전체`, `최신등록순`.

규칙:

- Filter area는 white 또는 surface background.
- Input/select는 36~40px height, 4px radius.
- Search button은 blue fill.
- Count는 primary blue 숫자 강조 가능.

### LearningMaterialListItem

학습자료 row.

구성:

- 좌측 thumbnail image.
- Thumbnail 위/옆 category icon overlay 가능.
- 우측 title.
- `교재` badge.
- Breadcrumb: `커리큘럼 > 교재`.
- 설명/요약.
- Stats row: 조회수, 좋아요, 하트/찜.

규칙:

- Thumbnail은 고정 width의 rectangular image.
- Row는 white background + bottom divider.
- Title은 16px/600.
- Metadata는 13px secondary.

### BoardList

공지사항, 열린 게시판, 익명 게시판, 멘토링, FAQ, 학사규정, 1:1 문의 등에 사용한다.

구성:

- Search/filter bar.
- Board table/list.
- 번호, 카테고리, 제목, 작성자, 날짜, 조회수.
- 상단 total count.
- Pagination.
- 필요 시 write button.

규칙:

- Table header background는 surface.
- Row background는 white.
- Row hover는 `surfaceSubtle`.
- 제목은 left aligned.
- 숫자/날짜 column은 center 또는 right aligned.
- 빈 목록은 넓은 white/surface panel + muted 안내문.

### BoardDetail

공지 상세/게시글 상세.

구성:

- 제목 영역.
- 작성자/날짜/조회수 metadata row.
- 본문 영역.
- 첨부파일 영역.
- 목록/수정/삭제/action button row.
- 댓글이 있는 게시판은 comment list/form.

규칙:

- Detail top은 table-like horizontal borders.
- 본문은 충분한 vertical padding.
- Attachment는 작은 file icon + file name + download action.

### BoardWriteForm

열린 게시판 글쓰기, 1:1 문의, 서류제출 등 form page.

구성:

- Form table layout.
- Label column은 light surface background.
- Required mark는 red.
- Input/select/textarea/file upload.
- 하단 button row: 취소/목록/저장/등록.

규칙:

- Label width는 140~180px.
- Row border는 `border` 또는 `divider`.
- Textarea는 최소 180~260px height.
- Primary submit은 blue, secondary는 white/gray outline.

### AttendanceStatus

마이캠퍼스 출석현황.

구성:

- 출석 summary card.
- 월/기간 selector.
- Calendar 또는 date list.
- 상태 label: 출석, 지각, 결석, 외출 등.
- 이의신청/상세 action.

규칙:

- 상태는 semantic badge로 작게 표시한다.
- Calendar cell은 border 중심으로 구현한다.
- 출석 데이터는 숫자/날짜 가독성을 우선한다.

### LevelPointDashboard

레벨&장학포인트.

구성:

- 현재 level / EXP / point metric.
- Progress bar.
- Point transaction list.
- Level/benefit 안내.

규칙:

- Metric은 dashboardMetric typography.
- Progress bar active는 blue.
- Transaction row는 board/list와 같은 divider rhythm을 사용한다.

### NotificationInbox

알림함.

구성:

- 알림 list.
- 읽음/안읽음 상태.
- 제목, 내용 preview, 날짜.
- Empty state.

규칙:

- unread는 blue dot 또는 light blue background로 표현한다.
- read는 muted text와 white background.
- count badge는 red 또는 blue small badge.

### AgreementPledgeList

교육생 서약서/약관류 화면.

구성:

- 문서/서약서 list.
- 상태: 제출/미제출/확인 필요.
- 첨부파일 또는 상세 보기.
- 동의/제출 action.

규칙:

- 법적/서약 문서는 board table보다 form/detail tone을 사용한다.
- 상태 badge는 작고 명확하게 둔다.

### Pagination

구성:

- 이전/다음 arrow.
- page number square.
- active page blue fill + white text.

규칙:

- 중앙 정렬.
- Button size 32~34px.
- Inactive는 white + border + secondary text.
- Disabled는 muted gray.

### FloatingQuickActionButton

구성:

- 우측 floating red circular button.
- e-book 또는 quick service button과 함께 보일 수 있다.

규칙:

- 56px circle.
- Red fill.
- White icon/text.
- `shadow-fab` 수준의 shadow.
- Main content를 가리지 않도록 우측 여백 확보.

### Footer

구성:

- Dark charcoal full-width footer.
- SSAFY/Samsung 관련 정보, address, policy links.

규칙:

- Background `#202629`.
- Text는 light gray.
- Link는 white 또는 muted gray.
- Footer는 페이지의 공식/기관 느낌을 강화한다.

## Do's and Don'ts

### Do

- 실제 SSAFY EDU처럼 white canvas + blue active + navy subnav 조합을 유지한다.
- 페이지마다 `HeroBanner → SectionSubNavigation → MainContainer` 패턴을 우선 재사용한다.
- 게시판/공지/문의/서류제출/멘토링 화면은 공통 `BoardList`, `BoardDetail`, `BoardWriteForm` 패턴으로 통합한다.
- 상태 값은 badge/circle/pill로 명확히 표현한다.
- 리스트와 table은 dense하되 13~16px 가독성을 유지한다.
- form은 table-like layout으로 구현한다.
- 첨부파일, 조회수, 좋아요, 찜, 기간, 작성자 같은 metadata row를 빠뜨리지 않는다.
- 빈 목록 화면을 구현한다. SSAFY EDU에는 empty state가 자주 보인다.
- 스크린샷 폴더의 실제 화면명을 기준으로 route/component를 매핑한다.

### Don't

- Vercel/Linear 스타일의 극단적 black-white minimal UI로 만들지 않는다.
- 큰 radius, glassmorphism, gradient-heavy card, neumorphism을 쓰지 않는다.
- Primary blue를 모든 장식 요소에 남발하지 않는다.
- 게시판을 modern feed card로 바꾸지 않는다. table/list 중심의 포털 느낌을 유지한다.
- Hero banner가 필요한 section page를 단순 title text만 있는 페이지로 축소하지 않는다.
- Dark navy subnav를 일반 breadcrumb나 sidebar로 대체하지 않는다.
- PASS/FAIL/예정/완료 같은 상태 UI를 단순 텍스트만으로 처리하지 않는다.
- 실제 개인정보, 사용자 이름, 학번, 평가 결과 등 민감정보를 문서나 fixture에 그대로 넣지 않는다. 클론용 더미 데이터로 대체한다.

## Screenshot Coverage Notes

현재 디자인 기준으로 반영한 화면군:

- `01_home_dashboard.png`: 홈 대시보드, 출석/포인트/커리큘럼/공지 preview.
- `02_classroom_weekly_curriculum.png`, `12_classroom_curriculum_week_toggle_attempt.png`: 주차별 커리큘럼, stepper, week carousel, timeline.
- `03_quest_evaluation_list.png`, `38_classroom_quest_detail_completed.png`: Quest/평가 list/detail, status circle, PASS/FAIL.
- `04_learning_resources.png`: 학습자료 검색/filter/list item.
- `05_community_survey.png`: 설문조사 list.
- `06_open_board.png`, `16_community_anonymous_board.png`, `35_community_open_board_write_form.png`: 게시판 list/write.
- `07_helpdesk_notice.png`, `33_helpdesk_notice_detail.png`: 공지사항 list/detail.
- `08_helpdesk_faq_empty.png`, `18_helpdesk_academic_rules.png`, `34_helpdesk_academic_rules_expanded_attendance.png`: FAQ empty, 학사규정 accordion/detail.
- `09_helpdesk_inquiry_empty.png`, `10_helpdesk_inquiry_form.png`: 1:1 문의 empty/form.
- `13_classroom_my_lecture_replay.png`, `14_classroom_all_lecture_replay.png`, `15_classroom_required_learning_empty.png`: 강의 다시보기/필수학습 list/empty.
- `17_community_class_roster.png`: 우리반 보기 roster/list.
- `19_mentoring_qna_board.png`, `20_mentoring_notice_board.png`, `21_mentoring_meetup_apply.png`, `22_mentoring_meetup_info_empty.png`, `23_mentoring_meetup_review_board.png`, `24_mentoring_meetup_review_write_form.png`: 멘토링 게시판/간담회 계열.
- `25_mycampus_level_points_dashboard.png`, `26_mycampus_attendance_status.png`, `27_mycampus_learning_elearning_empty.png`, `28_mycampus_bookmarks_empty.png`, `29_mycampus_document_submission_empty.png`, `30_mycampus_document_submission_write_form.png`, `31_mycampus_student_pledge_list.png`, `32_mycampus_education_status.png`, `40_attendance_detail_from_home_more.png`: 마이캠퍼스 화면군.
- `36_notification_inbox.png`, `37_profile_click_no_dropdown_notification_page.png`: 알림/프로필 상단 동작.
- `39_mycampus_ebook_403_forbidden.png`: 접근 불가/403 화면도 error/empty state 참고용.

구현자가 이 문서를 사용할 때는 위 screenshot file name을 기준으로 컴포넌트와 route를 연결하면 된다.

## Clone-Quality Checklist

- Global header의 대분류 5개와 service shortcut 3개가 재현되어 있는가?
- Section page에 hero banner와 dark navy subnav가 있는가?
- Active tab/page/selected state가 primary blue로 일관되는가?
- Board/list/form/table UI가 SSAFY EDU처럼 dense하고 rectangular한가?
- Quest/평가의 원형 status와 PASS/FAIL badge가 구현되어 있는가?
- 주차별 커리큘럼의 phase stepper, week selector, timeline card가 구현되어 있는가?
- 학습자료의 thumbnail, badge, breadcrumb, stats row가 구현되어 있는가?
- Empty state, pagination, search/filter, file attachment UI가 빠지지 않았는가?
- Floating quick action과 dark footer가 주요 페이지에 들어가는가?
- 실제 개인 정보/평가 결과를 더미 데이터로 대체했는가?
