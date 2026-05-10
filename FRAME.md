# Edu SSAFY — FRAME.md

> `edu.ssafy.com` 클론 코딩용 화면 프레임 / 레이아웃 / 컴포넌트 배치 명세  
> Companion design system: `/Users/baeggwan-yeol/Desktop/DESIGN.md`  
> Screenshot source: `/Users/baeggwan-yeol/Desktop/edu_screnshot`

프레임은 `DESIGN.md`와 따로 지정하는 것이 좋다. `DESIGN.md`가 색상, 타이포, radius, 버튼/뱃지 같은 **시각 규칙**을 담당한다면, `FRAME.md`는 페이지가 실제로 어떻게 쌓이는지, 어떤 컴포넌트가 어느 위치에 들어가는지, route별로 어떤 화면 골격을 재사용하는지 담당한다.

이 프로젝트에서는 `FRAME.md`를 다음 기준으로 사용한다.

- 화면 배치 기준서
- route/page skeleton 기준서
- 컴포넌트 트리 기준서
- 게시판/폼/리스트/대시보드 반복 패턴 기준서
- clone-quality checklist
- 프론트 개발자가 mock data와 component props를 설계할 때 참고하는 구조 문서

---

## 1. 문서 역할 분리

### DESIGN.md

담당 범위:

- 색상 token
- typography token
- spacing/radius/shadow
- button, tab, badge, pagination visual rule
- 컴포넌트의 시각 스타일
- Do / Don't

### FRAME.md

담당 범위:

- 전체 앱 shell 구조
- header / hero / subnav / main / footer 배치
- 페이지별 component tree
- route별 화면 구성
- list/table/form/detail/empty state 구조
- 필요한 데이터 props 힌트
- 구현 우선순위

간단히 말하면:

```text
DESIGN.md = 어떻게 보일 것인가
FRAME.md  = 어디에 무엇을 배치할 것인가
```

---

## 2. 공통 App Shell

대부분의 로그인 후 화면은 아래 프레임을 공유한다.

```text
AuthenticatedApp
├── GlobalHeader
│   ├── LogoArea
│   ├── PrimaryNavigation
│   │   ├── 마이캠퍼스
│   │   ├── 강의실
│   │   ├── 커뮤니티
│   │   ├── HELP DESK
│   │   └── 멘토링 게시판
│   ├── HeaderUtilityArea
│   │   ├── NotificationEntry
│   │   └── UserProfileEntry
│   └── ServiceShortcutTiles
│       ├── JOB SSAFY
│       ├── SSAFY GIT
│       └── Meeting! SSAFY
│
├── PageHero optional
│   ├── BackgroundImage
│   ├── DarkOverlay
│   └── CenteredPageTitle
│
├── SectionSubNavigation optional
│   └── SectionTab[]
│
├── MainContainer
│   └── PageContent
│
├── FloatingQuickAction optional
│
└── Footer
```

### 배치 규칙

- `GlobalHeader`는 인증 후 모든 주요 페이지에 고정적으로 노출한다.
- `PageHero`는 강의실, 커뮤니티, HELP DESK, 멘토링 게시판 같은 대분류 하위 페이지에 사용한다.
- `SectionSubNavigation`은 `PageHero` 바로 아래 dark navy bar로 붙인다.
- `MainContainer`는 1180px~1240px 중앙 정렬 container를 기준으로 한다.
- `Footer`는 dark charcoal full-width 영역이다.
- `FloatingQuickAction`은 페이지 우측에 고정되며, 주요 content flow와 분리한다.

---

## 3. GlobalHeader 프레임

```text
GlobalHeader
├── HeaderInner
│   ├── LogoArea
│   │   └── SSAFYLogo
│   ├── PrimaryNavigation
│   │   ├── NavItem(active?)
│   │   └── NavItem[]
│   ├── HeaderUtilities
│   │   ├── NotificationButton
│   │   │   ├── BellIcon
│   │   │   └── CountBadge
│   │   └── UserProfileButton
│   │       ├── UserNumber
│   │       ├── UserName
│   │       └── Chevron
│   └── ServiceShortcutTiles
│       ├── ServiceTile(job)
│       ├── ServiceTile(git)
│       └── ServiceTile(meeting)
```

### 구현 props 힌트

```ts
type HeaderUser = {
  userNumber: string;
  name: string;
  notificationCount: number;
};

type HeaderNavItem = {
  label: string;
  href: string;
  active: boolean;
};
```

### 주의

- 사용자 이름, 학번 등은 실제 값이 아니라 더미 데이터로 처리한다.
- service shortcut은 일반 nav와 다르게 block형 바로가기처럼 보여야 한다.

---

## 4. Section Page 공통 프레임

강의실/커뮤니티/HELP DESK/멘토링 게시판 계열은 이 프레임을 우선 사용한다.

```text
SectionPageLayout
├── GlobalHeader
├── PageHero
│   └── title
├── SectionSubNavigation
│   └── tabs
├── MainContainer
│   ├── OptionalIntroPanel
│   ├── OptionalSearchFilter
│   ├── MainListOrFormOrDetail
│   └── Pagination optional
├── FloatingQuickAction
└── Footer
```

### SectionSubNavigation 메뉴

강의실:

- 내강의 다시보기
- 전체강의 다시보기
- 주차별 커리큘럼
- Quest/평가
- 필수학습
- 학습자료

커뮤니티:

- 설문조사
- 열린 게시판
- 익명 게시판
- 우리반 보기

HELP DESK:

- 공지사항
- FAQ
- 1:1 문의
- 학사규정

멘토링 게시판:

- 멘토 스토리
- 멘토링
- 멘토링 공지사항
- 간담회 신청
- 간담회 정보
- 간담회 후기

---

## 5. 홈 대시보드 프레임

참고 스크린샷: `01_home_dashboard.png`

```text
HomeDashboardPage
├── GlobalHeader
├── MainContainer
│   ├── DashboardTopGrid
│   │   ├── AttendanceCheckCard
│   │   ├── ScholarshipPointCard
│   │   ├── LevelExpCard
│   │   └── NotificationPreviewCard
│   ├── LearningOverviewGrid
│   │   ├── WeeklyCurriculumPreview
│   │   └── QuestEvaluationPreview
│   ├── ResourceGrid
│   │   ├── LearningMaterialPreview
│   │   ├── ElearningPreview
│   │   ├── SSAFYcialPreview
│   │   ├── FreeBoardPreview
│   │   └── NoticePreview
│   └── OptionalShortcutArea
├── FloatingQuickAction
└── Footer
```

### 데이터 힌트

- `attendanceSummary`: 날짜, 요일, 입실/퇴실 가능 여부, 안내 문구
- `pointSummary`: 장학포인트, 경험치, 레벨
- `notifications`: 최근 알림 목록
- `curriculumPreview`: 이번 주 일정
- `questPreview`: Quest/평가 상태
- `boardPreview`: 자유게시판/공지사항 최신글

### 구현 우선순위

1. Header + dashboard card grid
2. 출석/포인트/레벨 metric card
3. 게시판/공지 preview list
4. Floating quick action + footer

---

## 6. 강의실 — 주차별 커리큘럼 프레임

참고 스크린샷:

- `02_classroom_weekly_curriculum.png`
- `12_classroom_curriculum_week_toggle_attempt.png`

```text
WeeklyCurriculumPage
├── SectionPageLayout(title="주차별 커리큘럼", activeTab="주차별 커리큘럼")
│   └── MainContainer
│       ├── SemesterPhaseStepper
│       ├── CurriculumToolbar
│       │   ├── ViewModeToggle
│       │   │   ├── 주간
│       │   │   └── 월간
│       │   └── CategoryLegend
│       │       ├── 알고리즘
│       │       ├── 코딩과정
│       │       ├── 프로젝트
│       │       └── 기타
│       ├── WeekCarouselSelector
│       └── CurriculumTimeline
│           └── CurriculumDayGroup[]
│               ├── DateColumn
│               ├── TimeColumn
│               └── CurriculumCard[]
│                   ├── CategoryLabel
│                   ├── Title
│                   ├── Description optional
│                   └── ActionButtons
│                       ├── 강의 다시보기
│                       └── 교재
```

### 컴포넌트 props 힌트

```ts
type CurriculumDayGroup = {
  date: string;
  weekday: string;
  items: CurriculumItem[];
};

type CurriculumItem = {
  id: string;
  timeRange: string;
  category: 'ALGORITHM' | 'CODING' | 'PROJECT' | 'ETC';
  title: string;
  hasReplay: boolean;
  hasTextbook: boolean;
};
```

### 클론 포인트

- 날짜/주차/active underline은 blue 강조.
- timeline card는 연한 blue-gray surface.
- `강의 다시보기`와 `교재`는 서로 다른 outline 색상.
- 주간/월간 toggle을 반드시 분리한다.

---

## 7. 강의실 — Quest/평가 프레임

참고 스크린샷:

- `03_quest_evaluation_list.png`
- `38_classroom_quest_detail_completed.png`

```text
QuestEvaluationPage
├── SectionPageLayout(title="Quest/평가", activeTab="Quest/평가")
│   └── MainContainer
│       ├── QuestEvaluationList
│       │   └── QuestEvaluationCard[]
│       │       ├── TypeLabel: Quest | 평가
│       │       ├── StatusCircle: 예정 | 완료
│       │       ├── MainInfo
│       │       │   ├── Title
│       │       │   └── Period
│       │       ├── RewardInfo
│       │       │   ├── Label: 획득 가능 경험치
│       │       │   └── Point
│       │       └── ResultInfo
│       │           ├── StatusText
│       │           ├── PassFailBadge optional
│       │           └── Score optional
│       └── Pagination
```

### 상태 규칙

- 예정: white fill + blue border/text circle.
- 완료: slate fill + white text circle.
- PASS: blue pill.
- FAIL: red/pink pill.
- 제출완료/채점완료/시험기간은 text 또는 small badge로 분리.

---

## 8. 강의실 — 학습자료 프레임

참고 스크린샷: `04_learning_resources.png`

```text
LearningResourcePage
├── SectionPageLayout(title="학습자료", activeTab="학습자료")
│   └── MainContainer
│       ├── ResourceCategoryTabs
│       │   ├── 오픈러닝
│       │   ├── 전체
│       │   └── 커리큘럼
│       ├── ResourceSearchBar
│       │   ├── KeywordInput
│       │   └── SearchButton
│       ├── ResourceListToolbar
│       │   ├── TotalCount
│       │   ├── TextbookCheckbox
│       │   ├── CategorySelect
│       │   └── SortSelect
│       ├── ResourceList
│       │   └── ResourceListItem[]
│       │       ├── Thumbnail
│       │       ├── ContentArea
│       │       │   ├── Title
│       │       │   ├── Badge: 교재 optional
│       │       │   ├── Breadcrumb
│       │       │   ├── Description
│       │       │   └── StatsRow
│       │       │       ├── ViewCount
│       │       │       ├── LikeCount
│       │       │       └── BookmarkCount
│       └── Pagination
```

### 데이터 힌트

```ts
type LearningResource = {
  id: string;
  title: string;
  thumbnailUrl: string;
  type: '교재' | '영상' | '자료';
  breadcrumb: string[];
  viewCount: number;
  likeCount: number;
  bookmarkCount: number;
};
```

---

## 9. 강의실 — 강의 다시보기 / 필수학습 프레임

참고 스크린샷:

- `13_classroom_my_lecture_replay.png`
- `14_classroom_all_lecture_replay.png`
- `15_classroom_required_learning_empty.png`

```text
LectureReplayPage
├── SectionPageLayout(title="내강의 다시보기" | "전체강의 다시보기")
│   └── MainContainer
│       ├── SearchFilterBar
│       ├── LectureReplayList
│       │   └── LectureReplayItem[]
│       │       ├── Thumbnail or Icon
│       │       ├── Title
│       │       ├── CourseMeta
│       │       ├── PeriodMeta
│       │       └── ActionButton
│       └── Pagination
```

```text
RequiredLearningEmptyPage
├── SectionPageLayout(title="필수학습", activeTab="필수학습")
│   └── MainContainer
│       ├── SearchFilterBar optional
│       └── EmptyState
│           ├── EmptyIcon optional
│           └── Message: 조회된 내용이 없습니다
```

---

## 10. 커뮤니티 — 설문조사 프레임

참고 스크린샷: `05_community_survey.png`

```text
SurveyPage
├── SectionPageLayout(title="설문조사", activeTab="설문조사")
│   └── MainContainer
│       ├── SurveyFilterBar
│       ├── SurveyTable
│       │   ├── TableHeader
│       │   └── SurveyRow[]
│       │       ├── Category
│       │       ├── Title
│       │       ├── Period
│       │       ├── Status
│       │       └── ActionOrResult
│       └── Pagination
```

### 데이터 힌트

- 설문 제목
- 기간
- 참여 상태
- 설문 유형/카테고리
- 결과 공개 여부

---

## 11. 커뮤니티 — 열린 게시판 / 익명 게시판 프레임

참고 스크린샷:

- `06_open_board.png`
- `16_community_anonymous_board.png`
- `35_community_open_board_write_form.png`

```text
CommunityBoardListPage
├── SectionPageLayout(title="열린 게시판" | "익명 게시판")
│   └── MainContainer
│       ├── BoardCategoryTabs optional
│       ├── BoardToolbar
│       │   ├── TotalCount
│       │   ├── SearchSelect
│       │   ├── SearchInput
│       │   └── SearchButton
│       ├── BoardTable
│       │   ├── Header: 번호 / 카테고리 / 제목 / 작성자 / 등록일 / 조회수
│       │   └── BoardPostRow[]
│       ├── BoardActionRow
│       │   └── WriteButton
│       └── Pagination
```

```text
CommunityBoardWritePage
├── SectionPageLayout(title="열린 게시판", activeTab="열린 게시판")
│   └── MainContainer
│       ├── BoardWriteFormTable
│       │   ├── CategorySelectRow
│       │   ├── TitleInputRow
│       │   ├── ContentEditorRow
│       │   ├── AttachmentUploadRow optional
│       │   └── AgreementNoticeRow optional
│       └── FormActionRow
│           ├── CancelButton
│           └── SubmitButton
```

### 클론 포인트

- 게시판은 card feed가 아니라 table/list 기반이다.
- 글쓰기 form은 label column + input column의 table-like layout이다.
- 익명 게시판은 작성자 표시 정책과 동의/운영원칙 안내가 필요하다.

---

## 12. 커뮤니티 — 우리반 보기 프레임

참고 스크린샷: `17_community_class_roster.png`

```text
ClassRosterPage
├── SectionPageLayout(title="우리반 보기", activeTab="우리반 보기")
│   └── MainContainer
│       ├── ClassFilterBar
│       │   ├── CampusSelect
│       │   ├── ClassSelect
│       │   └── SearchInput optional
│       ├── RosterListOrGrid
│       │   └── StudentCardOrRow[]
│       │       ├── ProfileImage optional
│       │       ├── Name
│       │       ├── Campus
│       │       ├── ClassName
│       │       └── Track optional
│       └── Pagination optional
```

### 주의

- 실제 교육생 개인정보를 사용하지 않는다.
- 더미 이름/더미 학번으로 fixture를 만든다.

---

## 13. HELP DESK — 공지사항 프레임

참고 스크린샷:

- `07_helpdesk_notice.png`
- `33_helpdesk_notice_detail.png`

```text
NoticeListPage
├── SectionPageLayout(title="공지사항", activeTab="공지사항")
│   └── MainContainer
│       ├── NoticeCategoryTabs
│       ├── BoardToolbar
│       ├── NoticeTable
│       │   ├── Header: 번호 / 구분 / 제목 / 등록일 / 조회수
│       │   └── NoticeRow[]
│       └── Pagination
```

```text
NoticeDetailPage
├── SectionPageLayout(title="공지사항", activeTab="공지사항")
│   └── MainContainer
│       ├── DetailHeader
│       │   ├── Category
│       │   ├── Title
│       │   └── MetaRow
│       ├── DetailBody
│       ├── AttachmentList optional
│       └── DetailActionRow
│           └── ListButton
```

---

## 14. HELP DESK — FAQ / 1:1 문의 / 학사규정 프레임

참고 스크린샷:

- `08_helpdesk_faq_empty.png`
- `09_helpdesk_inquiry_empty.png`
- `10_helpdesk_inquiry_form.png`
- `18_helpdesk_academic_rules.png`
- `34_helpdesk_academic_rules_expanded_attendance.png`

```text
FaqPage
├── SectionPageLayout(title="FAQ", activeTab="FAQ")
│   └── MainContainer
│       ├── FaqCategoryTabs
│       ├── SearchFilterBar
│       ├── FaqAccordionList or EmptyState
│       └── Pagination optional
```

```text
InquiryListPage
├── SectionPageLayout(title="1:1 문의", activeTab="1:1 문의")
│   └── MainContainer
│       ├── InquiryToolbar
│       ├── InquiryTable or EmptyState
│       ├── WriteButton
│       └── Pagination optional
```

```text
InquiryWritePage
├── SectionPageLayout(title="1:1 문의", activeTab="1:1 문의")
│   └── MainContainer
│       ├── InquiryFormTable
│       │   ├── TypeSelectRow
│       │   ├── TitleInputRow
│       │   ├── ContentTextareaRow
│       │   ├── AttachmentUploadRow
│       │   └── PrivateNoticeRow optional
│       └── FormActionRow
```

```text
AcademicRulesPage
├── SectionPageLayout(title="학사규정", activeTab="학사규정")
│   └── MainContainer
│       ├── RuleCategoryNavigation
│       ├── RuleAccordionList
│       │   └── RuleAccordionItem[]
│       │       ├── RuleTitle
│       │       └── RuleContent expanded optional
│       └── OptionalAttachmentArea
```

---

## 15. 멘토링 게시판 프레임

참고 스크린샷:

- `11_mentoring_story_board.png`
- `19_mentoring_qna_board.png`
- `20_mentoring_notice_board.png`
- `21_mentoring_meetup_apply.png`
- `22_mentoring_meetup_info_empty.png`
- `23_mentoring_meetup_review_board.png`
- `24_mentoring_meetup_review_write_form.png`

```text
MentoringBoardPage
├── SectionPageLayout(title="멘토링 게시판", activeTab=variant)
│   └── MainContainer
│       ├── OptionalIntroPanel
│       ├── BoardToolbar
│       ├── BoardTable or CardList or EmptyState
│       ├── WriteOrApplyButton optional
│       └── Pagination optional
```

### variant별 처리

- 멘토 스토리: 일반 board list.
- 멘토링: Q&A board list.
- 멘토링 공지사항: notice board list.
- 간담회 신청: 신청 가능한 목록 또는 신청 form 진입.
- 간담회 정보: empty state 가능.
- 간담회 후기: board list + write form.

### 간담회 후기 글쓰기

```text
MentoringReviewWritePage
├── SectionPageLayout(title="간담회 후기", activeTab="간담회 후기")
│   └── MainContainer
│       ├── BoardWriteFormTable
│       │   ├── TitleInputRow
│       │   ├── ContentEditorRow
│       │   └── AttachmentUploadRow optional
│       └── FormActionRow
```

---

## 16. 마이캠퍼스 프레임

참고 스크린샷:

- `25_mycampus_level_points_dashboard.png`
- `26_mycampus_attendance_status.png`
- `27_mycampus_learning_elearning_empty.png`
- `28_mycampus_bookmarks_empty.png`
- `29_mycampus_document_submission_empty.png`
- `30_mycampus_document_submission_write_form.png`
- `31_mycampus_student_pledge_list.png`
- `32_mycampus_education_status.png`
- `40_attendance_detail_from_home_more.png`

마이캠퍼스는 section hero가 없거나 약할 수 있지만, 내부적으로는 동일한 container/list/table/form 패턴을 사용한다.

### 레벨&장학포인트

```text
LevelPointPage
├── GlobalHeader
├── MainContainer
│   ├── MyCampusLocalNavigation optional
│   ├── LevelSummaryCard
│   │   ├── CurrentLevel
│   │   ├── ExpMetric
│   │   ├── PointMetric
│   │   └── ProgressBar
│   ├── PointTransactionToolbar
│   └── PointTransactionTable
└── Footer
```

### 출석현황

```text
AttendanceStatusPage
├── GlobalHeader
├── MainContainer
│   ├── AttendanceSummaryPanel
│   ├── MonthSelector
│   ├── AttendanceCalendarOrTable
│   ├── AttendanceLegend
│   └── AppealActionArea optional
└── Footer
```

### 학습중 이러닝 / 찜한 목록 empty

```text
MyLearningEmptyPage
├── GlobalHeader
├── MainContainer
│   ├── SearchFilterBar optional
│   └── EmptyState
│       └── Message: 조회된 내용이 없습니다
└── Footer
```

### 서류제출

```text
DocumentSubmissionPage
├── GlobalHeader
├── MainContainer
│   ├── DocumentRequirementList or EmptyState
│   └── Pagination optional
└── Footer
```

```text
DocumentSubmissionWritePage
├── GlobalHeader
├── MainContainer
│   ├── FormTable
│   │   ├── DocumentTypeRow
│   │   ├── TitleRow
│   │   ├── AttachmentUploadRow
│   │   └── ContentTextareaRow optional
│   └── FormActionRow
└── Footer
```

### 교육생 서약서

```text
PledgeDocumentPage
├── GlobalHeader
├── MainContainer
│   ├── PledgeDocumentTable
│   │   ├── DocumentTitle
│   │   ├── SubmissionStatus
│   │   ├── SubmittedAt optional
│   │   └── ActionButton
│   └── Pagination optional
└── Footer
```

### 교육현황

```text
EducationStatusPage
├── GlobalHeader
├── MainContainer
│   ├── StatusTabNavigation
│   │   ├── 출결
│   │   ├── 과목평가
│   │   ├── 월말평가
│   │   ├── 데일리 과제
│   │   ├── SW 역량 등급
│   │   ├── SSAFY 활동
│   │   └── 외부 시상
│   ├── StatusSummaryPanel
│   └── StatusTable
└── Footer
```

---

## 17. 알림 / 프로필 / 오류 프레임

참고 스크린샷:

- `36_notification_inbox.png`
- `37_profile_click_no_dropdown_notification_page.png`
- `39_mycampus_ebook_403_forbidden.png`

```text
NotificationInboxPage
├── GlobalHeader
├── MainContainer
│   ├── PageTitle
│   ├── NotificationFilter optional
│   ├── NotificationList
│   │   └── NotificationItem[]
│   │       ├── ReadStateDot
│   │       ├── Title
│   │       ├── Preview
│   │       └── CreatedAt
│   └── Pagination optional
└── Footer
```

```text
ForbiddenPage
├── GlobalHeader
├── MainContainer
│   └── ErrorStatePanel
│       ├── ErrorCode: 403
│       ├── Message
│       └── BackOrHomeButton
└── Footer
```

### 오류 화면 규칙

- 과하게 꾸미지 말고, 중앙 안내 panel + action button으로 처리한다.
- 접근 권한 없음, 조회 결과 없음, 데이터 없음은 각각 다른 메시지를 사용한다.

---

## 18. 공통 패턴 컴포넌트

### SearchFilterBar

```text
SearchFilterBar
├── Select optional
├── Select optional
├── KeywordInput
└── SearchButton
```

사용처:

- 학습자료
- 게시판
- 공지사항
- FAQ
- 1:1 문의
- 멘토링 게시판

### BoardTable

```text
BoardTable
├── TableHeader
│   ├── ColumnLabel[]
└── TableBody
    ├── BoardRow[]
    └── EmptyState optional
```

공통 column 후보:

- 번호
- 구분/카테고리
- 제목
- 작성자
- 등록일
- 조회수
- 첨부 여부

### FormTable

```text
FormTable
└── FormRow[]
    ├── LabelCell
    └── ControlCell
        ├── Input | Select | Textarea | FileUpload | RichEditor
```

규칙:

- label column은 고정 폭.
- required mark는 label 옆에 red로 표시.
- control cell은 가로 전체를 사용한다.
- 하단 action button row는 우측 정렬 또는 중앙 정렬.

### EmptyState

```text
EmptyState
├── EmptyIcon optional
└── Message
```

메시지 예시:

- 조회된 내용이 없습니다.
- 등록된 게시글이 없습니다.
- 제출할 서류가 없습니다.
- 접근 권한이 없습니다.

### Pagination

```text
Pagination
├── First optional
├── Prev
├── PageNumber[]
├── Next
└── Last optional
```

규칙:

- 중앙 정렬.
- active page는 blue fill.
- disabled arrow는 muted gray.

---

## 19. Route 제안

실제 `.do` URL을 그대로 쓰지 않고 프론트 클론에서는 아래처럼 읽기 좋은 route로 매핑한다.

### 홈

- `/` 또는 `/dashboard`

### 마이캠퍼스

- `/mycampus/level-points`
- `/mycampus/attendance`
- `/mycampus/elearning`
- `/mycampus/bookmarks`
- `/mycampus/documents`
- `/mycampus/documents/write`
- `/mycampus/pledges`
- `/mycampus/education-status`
- `/mycampus/profile`
- `/mycampus/password`

### 강의실

- `/classroom/my-replays`
- `/classroom/all-replays`
- `/classroom/curriculum`
- `/classroom/quests`
- `/classroom/quests/:id`
- `/classroom/required-learning`
- `/classroom/resources`
- `/classroom/resources/:id`

### 커뮤니티

- `/community/surveys`
- `/community/boards/open`
- `/community/boards/open/write`
- `/community/boards/anonymous`
- `/community/class-roster`

### HELP DESK

- `/help/notice`
- `/help/notice/:id`
- `/help/faq`
- `/help/inquiries`
- `/help/inquiries/write`
- `/help/rules`

### 멘토링 게시판

- `/mentoring/stories`
- `/mentoring/qna`
- `/mentoring/notice`
- `/mentoring/meetups/apply`
- `/mentoring/meetups/info`
- `/mentoring/meetups/reviews`
- `/mentoring/meetups/reviews/write`

### 알림 / 오류

- `/notifications`
- `/403`
- `/404`

---

## 20. 클릭 / 이동 연결 명세

기존 route 제안은 "어떤 페이지가 있는지"를 정의한다. 이 섹션은 "어떤 버튼, 탭, row를 누르면 어디로 이동하는지"를 정의한다. 프론트 구현 시 `Link`, `router.push`, `navigate`의 기준으로 사용한다.

### 20.1 GlobalHeader 이동

```text
LogoArea 클릭
→ /dashboard

PrimaryNavigation: 마이캠퍼스 클릭
→ /mycampus/level-points

PrimaryNavigation: 강의실 클릭
→ /classroom/my-replays

PrimaryNavigation: 커뮤니티 클릭
→ /community/surveys

PrimaryNavigation: HELP DESK 클릭
→ /help/notice

PrimaryNavigation: 멘토링 게시판 클릭
→ /mentoring/stories

NotificationButton 클릭
→ /notifications

UserProfileButton 클릭
→ dropdown open
  ├── 회원정보 수정 클릭 → /mycampus/profile
  ├── 비밀번호 변경 클릭 → /mycampus/password
  └── 로그아웃 클릭 → /login 또는 로그인 화면으로 redirect

ServiceTile: JOB SSAFY 클릭
→ external link 또는 /external/job-ssafy placeholder

ServiceTile: SSAFY GIT 클릭
→ external link 또는 /external/ssafy-git placeholder

ServiceTile: Meeting! SSAFY 클릭
→ external link 또는 /external/meeting-ssafy placeholder
```

### 20.2 SectionSubNavigation 이동

강의실 tab:

```text
내강의 다시보기 → /classroom/my-replays
전체강의 다시보기 → /classroom/all-replays
주차별 커리큘럼 → /classroom/curriculum
Quest/평가 → /classroom/quests
필수학습 → /classroom/required-learning
학습자료 → /classroom/resources
```

커뮤니티 tab:

```text
설문조사 → /community/surveys
열린 게시판 → /community/boards/open
익명 게시판 → /community/boards/anonymous
우리반 보기 → /community/class-roster
```

HELP DESK tab:

```text
공지사항 → /help/notice
FAQ → /help/faq
1:1 문의 → /help/inquiries
학사규정 → /help/rules
```

멘토링 게시판 tab:

```text
멘토 스토리 → /mentoring/stories
멘토링 → /mentoring/qna
멘토링 공지사항 → /mentoring/notice
간담회 신청 → /mentoring/meetups/apply
간담회 정보 → /mentoring/meetups/info
간담회 후기 → /mentoring/meetups/reviews
```

### 20.3 홈 대시보드 카드 이동

```text
출석체크 & 현황 카드의 더보기/상세 클릭
→ /mycampus/attendance

장학포인트 카드 클릭
→ /mycampus/level-points

레벨&경험치 카드 클릭
→ /mycampus/level-points

알림 preview 카드의 더보기 클릭
→ /notifications

주차별 커리큘럼 preview 클릭
→ /classroom/curriculum

Quest/평가 preview 클릭
→ /classroom/quests

학습자료 preview 클릭
→ /classroom/resources

학습중 이러닝 preview 클릭
→ /mycampus/elearning

SSAFYcial preview 클릭
→ 실제 route가 없으면 /community/boards/open 또는 external placeholder

자유게시판 preview 클릭
→ /community/boards/open

공지사항 preview 클릭
→ /help/notice

e-book 버튼 클릭
→ e-book 외부 링크 또는 권한 없으면 /403

FloatingQuickAction 클릭
→ 빠른 메뉴 popover open 또는 configured quick route
```

### 20.4 강의실 내부 이동

주차별 커리큘럼:

```text
주간/월간 toggle 클릭
→ 같은 route에서 viewMode query 변경
→ /classroom/curriculum?view=week
→ /classroom/curriculum?view=month

WeekCarousel 이전/다음 클릭
→ 같은 route에서 week query 변경
→ /classroom/curriculum?week=prev 또는 ?week=YYYY-WW

CurriculumCard 클릭
→ 상세 route가 있으면 /classroom/curriculum/:id
→ 상세 구현이 없으면 card expanded state

강의 다시보기 버튼 클릭
→ /classroom/my-replays?sessionId={sessionId}

교재 버튼 클릭
→ /classroom/resources?sessionId={sessionId}&type=textbook
```

Quest/평가:

```text
QuestEvaluationCard 클릭
→ /classroom/quests/:id

응시/제출/결과보기 버튼 클릭
→ /classroom/quests/:id

목록 버튼 클릭
→ /classroom/quests
```

학습자료:

```text
ResourceListItem 썸네일/제목 클릭
→ /classroom/resources/:id

교재 badge 클릭
→ /classroom/resources?type=textbook

좋아요 버튼 클릭
→ route 이동 없음, like mutation 후 현재 row 갱신

찜 버튼 클릭
→ route 이동 없음, bookmark mutation 후 현재 row 갱신

검색 버튼 클릭
→ /classroom/resources?keyword={keyword}&category={category}&sort={sort}

Pagination page 클릭
→ /classroom/resources?page={page}
```

강의 다시보기/필수학습:

```text
LectureReplayItem 클릭
→ /classroom/my-replays/:id 또는 video modal open

전체강의 다시보기 item 클릭
→ /classroom/all-replays/:id 또는 video modal open

필수학습 item 클릭
→ /classroom/required-learning/:id
```

### 20.5 게시판 / 공지 / FAQ / 문의 이동

공통 게시판:

```text
BoardPostRow 제목 클릭
→ 해당 게시글 상세

/community/boards/open row 클릭
→ /community/boards/open/:id

/community/boards/anonymous row 클릭
→ /community/boards/anonymous/:id

/mentoring/stories row 클릭
→ /mentoring/stories/:id

/mentoring/qna row 클릭
→ /mentoring/qna/:id

/mentoring/notice row 클릭
→ /mentoring/notice/:id

/mentoring/meetups/reviews row 클릭
→ /mentoring/meetups/reviews/:id

글쓰기 버튼 클릭
→ 현재 board의 write route

검색 버튼 클릭
→ 현재 route + ?searchType={type}&keyword={keyword}&page=1

카테고리 tab 클릭
→ 현재 route + ?category={categoryCode}&page=1

Pagination 클릭
→ 현재 route + ?page={page}
```

공지사항:

```text
NoticeRow 제목 클릭
→ /help/notice/:id

NoticeDetail 목록 버튼 클릭
→ /help/notice

NoticeDetail 첨부파일 클릭
→ file download action, route 이동 없음
```

FAQ:

```text
FaqCategoryTab 클릭
→ /help/faq?category={categoryCode}

FaqAccordionItem 클릭
→ route 이동 없음, accordion expand/collapse

FAQ 검색 버튼 클릭
→ /help/faq?keyword={keyword}&category={categoryCode}
```

1:1 문의:

```text
1:1 문의 글쓰기 버튼 클릭
→ /help/inquiries/write

InquiryRow 클릭
→ /help/inquiries/:id

InquiryWrite 취소/목록 버튼 클릭
→ /help/inquiries

InquiryWrite 등록 버튼 클릭
→ submit 성공 후 /help/inquiries 또는 /help/inquiries/:id
```

학사규정:

```text
RuleCategory 클릭
→ /help/rules?category={categoryCode}

RuleAccordionItem 클릭
→ route 이동 없음, accordion expand/collapse
```

### 20.6 커뮤니티 / 설문 / 우리반 이동

```text
SurveyRow 클릭
→ /community/surveys/:id

설문 참여 버튼 클릭
→ /community/surveys/:id/respond

설문 결과 버튼 클릭
→ /community/surveys/:id/result

ClassRoster filter 검색 클릭
→ /community/class-roster?campus={campus}&class={class}&keyword={keyword}

Student row/card 클릭
→ 개인정보 노출 우려가 있으므로 상세 페이지 없이 profile popover 또는 route 이동 없음
```

### 20.7 마이캠퍼스 이동

```text
마이캠퍼스 local nav: 레벨&장학포인트
→ /mycampus/level-points

마이캠퍼스 local nav: 출석현황
→ /mycampus/attendance

마이캠퍼스 local nav: 학습중 이러닝
→ /mycampus/elearning

마이캠퍼스 local nav: 찜한 목록
→ /mycampus/bookmarks

마이캠퍼스 local nav: 서류제출
→ /mycampus/documents

마이캠퍼스 local nav: 교육생 서약서
→ /mycampus/pledges

마이캠퍼스 local nav: 교육현황
→ /mycampus/education-status

출석현황 날짜 cell 클릭
→ /mycampus/attendance?date={yyyy-mm-dd} 또는 detail panel open

출석 이의신청 버튼 클릭
→ /mycampus/attendance/appeals/write?date={yyyy-mm-dd}

학습중 이러닝 item 클릭
→ /classroom/resources/:id 또는 /mycampus/elearning/:id

찜한 목록 item 클릭
→ 원본 콘텐츠 route로 이동
→ 학습자료면 /classroom/resources/:id

서류제출 등록 버튼 클릭
→ /mycampus/documents/write

서류제출 row 클릭
→ /mycampus/documents/:id

서류제출 form 취소 버튼 클릭
→ /mycampus/documents

교육생 서약서 row/action 클릭
→ /mycampus/pledges/:id

교육현황 tab 클릭
→ /mycampus/education-status?tab={attendance|subject-eval|monthly-eval|daily-task|sw-grade|activity|award}
```

### 20.8 멘토링 / 간담회 이동

```text
간담회 신청 row 클릭
→ /mentoring/meetups/apply/:id

간담회 신청 버튼 클릭
→ /mentoring/meetups/apply/:id/form 또는 modal open

간담회 정보 row 클릭
→ /mentoring/meetups/info/:id

간담회 후기 글쓰기 버튼 클릭
→ /mentoring/meetups/reviews/write

간담회 후기 form 취소 버튼 클릭
→ /mentoring/meetups/reviews

간담회 후기 등록 성공
→ /mentoring/meetups/reviews 또는 /mentoring/meetups/reviews/:id
```

### 20.9 인증 / 오류 / 권한 이동

```text
비로그인 상태에서 인증 필요 route 접근
→ /login

로그인 성공
→ redirect query가 있으면 해당 route
→ 없으면 /dashboard

로그아웃 성공
→ /login

권한 없는 메뉴/e-book 접근
→ /403

존재하지 않는 route
→ /404

403 BackButton 클릭
→ history.back()

403 HomeButton 클릭
→ /dashboard
```

### 20.10 NavigationAction 타입 권장

구현 시 버튼/row/link의 이동 의도를 명시적으로 props에 담는다.

```ts
type NavigationAction =
  | { type: 'route'; href: string }
  | { type: 'external'; href: string; target?: '_blank' }
  | { type: 'modal'; modalId: string }
  | { type: 'toggle'; stateKey: string }
  | { type: 'mutation'; action: 'like' | 'bookmark' | 'read' | 'submit' }
  | { type: 'download'; fileId: string };
```

원칙:

- 화면 전환은 `route`.
- 외부 SSAFY 서비스는 `external`.
- 영상 재생, quick menu, profile menu는 `modal` 또는 `popover`.
- 좋아요/찜/읽음 처리는 route 이동 없이 `mutation`.
- 첨부파일은 `download`.

---

## 21. 최소 데이터 props 설계 힌트

이 문서는 DB 설계가 아니라 프론트 프레임 문서이므로, 아래는 component props 관점의 최소 데이터다.

```ts
type PageTab = {
  label: string;
  href: string;
  active: boolean;
};

type PaginationState = {
  page: number;
  pageSize: number;
  totalCount: number;
};

type BoardPostListItem = {
  id: string;
  category?: string;
  title: string;
  authorName: string;
  createdAt: string;
  viewCount: number;
  hasAttachment?: boolean;
  isNotice?: boolean;
};

type EmptyStateProps = {
  message: string;
  actionLabel?: string;
  actionHref?: string;
};

type FormAction = {
  label: string;
  variant: 'primary' | 'secondary' | 'danger';
  type: 'submit' | 'button' | 'link';
};
```

---

## 22. 구현 우선순위

### 1차 — 공통 shell

- GlobalHeader
- PageHero
- SectionSubNavigation
- MainContainer
- Footer
- FloatingQuickAction

### 2차 — 공통 content pattern

- SearchFilterBar
- BoardTable
- FormTable
- EmptyState
- Pagination
- DetailHeader/DetailBody

### 3차 — 주요 페이지

- HomeDashboardPage
- WeeklyCurriculumPage
- QuestEvaluationPage
- LearningResourcePage
- NoticeList/Detail
- CommunityBoardList/Write

### 4차 — 마이캠퍼스/멘토링 확장

- LevelPointPage
- AttendanceStatusPage
- DocumentSubmissionPage
- PledgeDocumentPage
- EducationStatusPage
- MentoringBoardPage

### 5차 — 상태/인터랙션

- 알림 읽음/안읽음
- 좋아요/찜/조회수
- 첨부파일 표시
- form validation
- empty/403/404 state

---

## 23. 스크린샷 매핑

- `01_home_dashboard.png`: 홈 대시보드
- `02_classroom_weekly_curriculum.png`: 주차별 커리큘럼
- `03_quest_evaluation_list.png`: Quest/평가 목록
- `04_learning_resources.png`: 학습자료
- `05_community_survey.png`: 설문조사
- `06_open_board.png`: 열린 게시판
- `07_helpdesk_notice.png`: 공지사항 목록
- `08_helpdesk_faq_empty.png`: FAQ empty
- `09_helpdesk_inquiry_empty.png`: 1:1 문의 empty
- `10_helpdesk_inquiry_form.png`: 1:1 문의 form
- `11_mentoring_story_board.png`: 멘토 스토리
- `12_classroom_curriculum_week_toggle_attempt.png`: 커리큘럼 주간/월간 toggle
- `13_classroom_my_lecture_replay.png`: 내강의 다시보기
- `14_classroom_all_lecture_replay.png`: 전체강의 다시보기
- `15_classroom_required_learning_empty.png`: 필수학습 empty
- `16_community_anonymous_board.png`: 익명 게시판
- `17_community_class_roster.png`: 우리반 보기
- `18_helpdesk_academic_rules.png`: 학사규정
- `19_mentoring_qna_board.png`: 멘토링 Q&A
- `20_mentoring_notice_board.png`: 멘토링 공지사항
- `21_mentoring_meetup_apply.png`: 간담회 신청
- `22_mentoring_meetup_info_empty.png`: 간담회 정보 empty
- `23_mentoring_meetup_review_board.png`: 간담회 후기
- `24_mentoring_meetup_review_write_form.png`: 간담회 후기 form
- `25_mycampus_level_points_dashboard.png`: 레벨&장학포인트
- `26_mycampus_attendance_status.png`: 출석현황
- `27_mycampus_learning_elearning_empty.png`: 학습중 이러닝 empty
- `28_mycampus_bookmarks_empty.png`: 찜한 목록 empty
- `29_mycampus_document_submission_empty.png`: 서류제출 empty
- `30_mycampus_document_submission_write_form.png`: 서류제출 form
- `31_mycampus_student_pledge_list.png`: 교육생 서약서
- `32_mycampus_education_status.png`: 교육현황
- `33_helpdesk_notice_detail.png`: 공지사항 상세
- `34_helpdesk_academic_rules_expanded_attendance.png`: 학사규정 expanded
- `35_community_open_board_write_form.png`: 열린 게시판 글쓰기
- `36_notification_inbox.png`: 알림함
- `37_profile_click_no_dropdown_notification_page.png`: 프로필/알림 상단 동작 참고
- `38_classroom_quest_detail_completed.png`: Quest/평가 상세 완료
- `39_mycampus_ebook_403_forbidden.png`: 403 접근 제한
- `40_attendance_detail_from_home_more.png`: 출석 상세

---

## 24. 클론 품질 체크리스트

- Header의 5개 main nav와 3개 service shortcut이 있는가?
- Section page에 `HeroBanner + dark navy SectionSubNavigation`이 있는가?
- 홈 대시보드는 출석/포인트/레벨/알림/학습 preview card grid로 구성되어 있는가?
- 주차별 커리큘럼은 stepper, toggle, category legend, week carousel, timeline을 포함하는가?
- Quest/평가는 원형 상태 badge와 PASS/FAIL badge를 포함하는가?
- 학습자료는 thumbnail, badge, breadcrumb, stats row를 포함하는가?
- 게시판은 table/list 기반이며 검색, pagination, 글쓰기 버튼이 있는가?
- 상세 화면은 title/meta/body/attachment/action row 구조인가?
- 글쓰기/문의/서류제출 form은 table-like form layout인가?
- FAQ, 1:1 문의, 필수학습, 찜 목록 등 empty state가 구현되어 있는가?
- 마이캠퍼스 출석/포인트/서약서/교육현황 페이지가 별도 프레임을 가지는가?
- 알림함과 403 error state가 준비되어 있는가?
- 실제 개인정보와 평가 결과가 더미 데이터로 대체되어 있는가?

---

## 25. 최종 원칙

SSAFY EDU 클론에서 중요한 것은 “새로운 UI로 개선”하는 것이 아니라 “실제 포털의 반복 프레임을 놓치지 않는 것”이다. 따라서 구현자는 먼저 이 `FRAME.md`의 page skeleton을 맞추고, 그 다음 `DESIGN.md`의 visual token을 적용한다.

우선순위는 다음 순서다.

```text
Frame accuracy > Component reuse > Visual polish > Micro interaction
```
