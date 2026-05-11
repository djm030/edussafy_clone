# Edu SSAFY Clone API 명세서

> 기준 문서: `table.md`  
> 목적: 에듀싸피 클론 코딩을 위한 백엔드 API 범위를 정의한다.  
> 원칙: CMS처럼 과하게 화면 설정을 DB/API로 관리하지 않고, 실제 서비스 기능 중심으로 API를 구성한다.

---

# 1. API 공통 규칙

## 1.1 Base URL

```text
/api/v1
```

예시:

```text
GET /api/v1/boards/notice/posts
```

## 1.2 인증 방식

기본 구현은 세션 또는 JWT 중 하나를 선택한다.

권장:

```text
Authorization: Bearer {accessToken}
```

단, Spring Security 세션 기반으로 구현한다면 `Authorization` 헤더 없이 세션 쿠키를 사용할 수 있다.

## 1.3 공통 응답 형식

```json
{
  "success": true,
  "data": {},
  "message": "요청이 성공했습니다."
}
```

목록 응답:

```json
{
  "success": true,
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5
  },
  "message": "요청이 성공했습니다."
}
```

에러 응답:

```json
{
  "success": false,
  "errorCode": "USER_NOT_FOUND",
  "message": "사용자를 찾을 수 없습니다."
}
```

## 1.4 공통 페이지 파라미터

```text
page: 페이지 번호, 0부터 시작
size: 페이지 크기
sort: 정렬 조건, 예: createdAt,desc
keyword: 검색어
```

## 1.5 권한 구분

```text
STUDENT  교육생
MENTOR   멘토
OPERATOR 운영 담당자
ADMIN    관리자
```

관리자 API는 기본적으로 `ADMIN`, `OPERATOR`만 접근 가능하게 한다.

---

# 2. 인증/Auth API

## 2.1 로그인

```http
POST /api/v1/auth/login
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password"
}
```

Response:

```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token",
  "user": {
    "id": 1,
    "name": "홍길동",
    "role": "STUDENT"
  }
}
```

비고:

- 세션 방식이면 토큰 대신 세션 쿠키를 발급한다.
- 로그인 실패 이력 테이블은 만들지 않는다.

## 2.2 로그아웃

```http
POST /api/v1/auth/logout
```

## 2.3 내 정보 조회

```http
GET /api/v1/auth/me
```

Response:

```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "홍길동",
  "studentNo": "1234567",
  "generation": 12,
  "region": "서울",
  "classNo": 1,
  "role": "STUDENT",
  "status": "ACTIVE"
}
```

## 2.4 비밀번호 찾기/임시 비밀번호 발급

```http
POST /api/v1/auth/password/reset-temporary
```

Request:

```json
{
  "email": "user@example.com",
  "name": "홍길동"
}
```

비고:

- 별도 `password_reset_requests` 테이블은 만들지 않는다.
- 사용자 확인 후 임시 비밀번호를 생성하고 `users.password`를 갱신한다.
- 이메일 발송 로그 테이블은 만들지 않는다.

## 2.5 비밀번호 변경

```http
PATCH /api/v1/auth/password
```

Request:

```json
{
  "currentPassword": "old-password",
  "newPassword": "new-password"
}
```

처리:

- 현재 비밀번호 검증
- 새 비밀번호 해시 저장
- `password_change_histories`에 변경 이력 저장

---

# 3. 사용자/User API

## 3.1 내 프로필 조회

```http
GET /api/v1/users/me
```

## 3.2 내 프로필 수정

```http
PATCH /api/v1/users/me
```

Request:

```json
{
  "phoneNumber": "010-0000-0000",
  "emergencyPhoneNumber": "010-1111-1111",
  "zipCode": "00000",
  "address": "서울시 ...",
  "addressDetail": "상세주소"
}
```

## 3.3 Profile image update

Profile images use the common file upload flow first, then connect the returned `fileId` to the current user profile.

1. Upload the image with `POST /api/v1/files` using `targetType=USER_PROFILE` and `fileRole=PROFILE_IMAGE`.
2. Connect or clear `users.profileFile` with the API below.

```http
PATCH /api/v1/users/me/profile-image
Content-Type: application/json
```

Request:

```json
{
  "fileId": 1
}
```

Processing:

- Uses a `files` resource whose target type is `USER_PROFILE` and role is `PROFILE_IMAGE`.
- Connects the file to `users.profileFile`.
- Send `fileId: null` to clear the profile image.

## 3.4 교육생 검색

```http
GET /api/v1/users/students
```

Query:

```text
keyword
region
generation
classNo
page
size
```

비고:

- 교육생 검색 화면용 API.
- 민감 정보는 응답하지 않는다.

## 3.5 멘토 목록 조회

```http
GET /api/v1/mentors
```

Query:

```text
keyword
region
generation
page
size
```

용도:

```text
멘토링 게시판의 멘토 현황 화면에 사용한다. 별도 mentor 테이블을 만들지 않고 users.role = MENTOR 기준으로 조회한다.
```

## 3.6 멘토 상세 조회

```http
GET /api/v1/mentors/{mentorId}
```

## 3.7 마이캠퍼스 요약 조회

```http
GET /api/v1/users/me/campus-summary
```

용도:

```text
마이캠퍼스 메인, 나의 레벨/마일리지, 출결 요약, 학습 진행 요약을 한 번에 표시한다.
```

Response 예시:

```json
{
  "user": {
    "id": 1,
    "name": "홍길동",
    "generation": 12,
    "region": "서울",
    "classNo": 1
  },
  "stat": {
    "scholarshipPoint": 120,
    "totalExp": 3500,
    "levelName": "Gold",
    "attendanceRate": 98.5,
    "completedLearningCount": 24
  },
  "unreadNotificationCount": 3
}
```

## 3.8 회원정보 수정 전 비밀번호 확인

```http
POST /api/v1/users/me/password/verify
```

Request:

```json
{
  "password": "current-password"
}
```

용도:

```text
에듀싸피 회원정보 수정 화면처럼, 개인정보 수정 화면 진입 전에 현재 비밀번호를 한 번 더 확인한다.
```

---

# 4. 파일/File API

## 4.1 파일 업로드

```http
POST /api/v1/files
Content-Type: multipart/form-data
```

Form Data:

```text
file: 업로드 파일
targetType: USER_PROFILE | BOARD_POST | SURVEY | INQUIRY | COURSE_SESSION | LEARNING_CONTENT | AGREEMENT | ATTENDANCE_APPEAL | USER_ACTIVITY
targetId: 연결 대상 ID
fileRole: ATTACHMENT | EDITOR_IMAGE | THUMBNAIL | PROFILE_IMAGE | VIDEO_THUMBNAIL
```

## 4.2 파일 다운로드

```http
GET /api/v1/files/{fileId}/download
```

## 4.3 파일 삭제

```http
DELETE /api/v1/files/{fileId}
```

권한:

- 작성자 또는 관리자만 삭제 가능.

---

# 5. 출결/Attendance API

## 5.1 내 출결 목록 조회

```http
GET /api/v1/attendance/my
```

Query:

```text
startDate
endDate
status
page
size
```

## 5.2 내 출결 상세 조회

```http
GET /api/v1/attendance/my/{attendanceRecordId}
```

## 5.3 출결 소명 신청

```http
POST /api/v1/attendance/appeals
```

Request:

```json
{
  "attendanceRecordId": 1,
  "appealType": "LATE",
  "reason": "지하철 지연으로 지각했습니다.",
  "attachmentFileId": 10
}
```

## 5.4 내 출결 소명 목록

```http
GET /api/v1/attendance/appeals/my
```

## 5.5 교육 달력 조회

```http
GET /api/v1/education-calendar
```

Query:

```text
courseId
year
month
```

용도:

```text
교육일, 휴일, 행사일을 달력 형태로 표시한다. 출결 화면에서 월별 기준일을 보여줄 때 사용한다.
```

---

# 6. 포인트/경험치 API

## 6.1 내 포인트/레벨 요약 조회

```http
GET /api/v1/points/my/summary
```

Response:

```json
{
  "scholarshipPoint": 120,
  "totalExp": 3500,
  "levelName": "Gold",
  "levelNo": 3,
  "attendanceRate": 98.5,
  "completedLearningCount": 24
}
```

## 6.2 내 포인트/경험치 이력 조회

```http
GET /api/v1/points/my/transactions
```

Query:

```text
transactionType
startDate
endDate
page
size
```

---

# 7. 찜/Bookmark API

## 7.1 내 찜 목록 조회

```http
GET /api/v1/bookmarks/my
```

Query:

```text
targetType
page
size
```

## 7.2 찜 추가

```http
POST /api/v1/bookmarks
```

Request:

```json
{
  "targetType": "LEARNING_CONTENT",
  "targetId": 1
}
```

## 7.3 찜 삭제

```http
DELETE /api/v1/bookmarks
```

Request:

```json
{
  "targetType": "LEARNING_CONTENT",
  "targetId": 1
}
```

---

# 8. 게시판/Board API

## 8.1 게시판 목록 조회

```http
GET /api/v1/boards
```

Response 예시:

```json
[
  {
    "id": 1,
    "name": "공지사항",
    "code": "NOTICE",
    "boardType": "NOTICE"
  },
  {
    "id": 2,
    "name": "익명게시판",
    "code": "ANONYMOUS",
    "boardType": "ANONYMOUS"
  }
]
```

## 8.2 게시판 카테고리 조회

```http
GET /api/v1/boards/{boardCode}/categories
```

## 8.3 게시글 목록 조회

```http
GET /api/v1/boards/{boardCode}/posts
```

Query:

```text
categoryId
keyword
page
size
```

익명게시판 처리:

- `boardType = ANONYMOUS`인 경우 작성자 이름을 응답하지 않는다.
- 응답에는 `displayName = "익명"`으로 내려준다.
- `isMine`은 본인 글 수정/삭제 버튼 제어를 위해 내려줄 수 있다.

Response 예시:

```json
{
  "id": 1,
  "title": "질문 있습니다.",
  "displayName": "익명",
  "isMine": true,
  "viewCount": 10,
  "likeCount": 2,
  "commentCount": 3,
  "createdAt": "2026-01-01T10:00:00"
}
```

## 8.4 게시글 상세 조회

```http
GET /api/v1/boards/{boardCode}/posts/{postId}
```

처리:

- 조회 시 `viewCount` 증가.
- 익명게시판이면 작성자 개인정보를 응답하지 않는다.

## 8.5 게시글 작성

```http
POST /api/v1/boards/{boardCode}/posts
```

Request:

```json
{
  "categoryId": 1,
  "title": "게시글 제목",
  "contentType": "HTML",
  "contentText": "검색용 텍스트",
  "contentHtml": "<p>본문</p>",
  "contentJson": null,
  "fileIds": [1, 2]
}
```

권한:

- 공지사항, FAQ, 학사규정은 관리자만 작성.
- 자유게시판, 익명게시판은 교육생 작성 가능.

## 8.6 게시글 수정

```http
PATCH /api/v1/boards/{boardCode}/posts/{postId}
```

권한:

- 작성자 또는 관리자.

## 8.7 게시글 삭제

```http
DELETE /api/v1/boards/{boardCode}/posts/{postId}
```

처리:

- 물리 삭제보다 `isDeleted = true` 권장.

## 8.8 게시글 좋아요/취소

```http
POST /api/v1/boards/{boardCode}/posts/{postId}/like
DELETE /api/v1/boards/{boardCode}/posts/{postId}/like
```

비고:

- 별도 좋아요 테이블이 없으므로 단순 카운트 중심으로 구현하거나, 중복 방지가 필요하면 추후 확장한다.

## 8.9 댓글 목록 조회

```http
GET /api/v1/boards/{boardCode}/posts/{postId}/comments
```

## 8.10 댓글 작성

```http
POST /api/v1/boards/{boardCode}/posts/{postId}/comments
```

Request:

```json
{
  "parentId": null,
  "content": "댓글 내용"
}
```

## 8.11 댓글 수정

```http
PATCH /api/v1/comments/{commentId}
```

## 8.12 댓글 삭제

```http
DELETE /api/v1/comments/{commentId}
```

---

# 9. 강의실/Course API

## 9.1 내 과정 목록 조회

```http
GET /api/v1/courses/my
```

## 9.2 과정 상세 조회

```http
GET /api/v1/courses/{courseId}
```

## 9.3 주차별 커리큘럼 조회

```http
GET /api/v1/courses/{courseId}/weeks
```

## 9.4 주차별 세션 목록 조회

```http
GET /api/v1/courses/{courseId}/weeks/{weekId}/sessions
```

## 9.5 강의/세션 상세 조회

```http
GET /api/v1/course-sessions/{sessionId}
```

## 9.6 다시보기 목록 조회

```http
GET /api/v1/course-sessions/replays
```

Query:

```text
courseId
weekId
keyword
page
size
```

---

# 10. 이러닝/Learning API

## 10.1 이러닝 카테고리 목록 조회

```http
GET /api/v1/learning/categories
```

## 10.2 학습 콘텐츠 목록 조회

```http
GET /api/v1/learning/contents
```

Query:

```text
categoryId
courseId
keyword
contentType
page
size
```

## 10.3 필수학습 콘텐츠 목록 조회

```http
GET /api/v1/learning/contents/required
```

Query:

```text
courseId
categoryId
page
size
```

용도:

```text
강의실의 필수학습 화면에 사용한다. 내부적으로는 learning_contents.isRequired = true 조건으로 조회한다.
```

## 10.4 오픈러닝 콘텐츠 목록 조회

```http
GET /api/v1/learning/contents/open-learning
```

Query:

```text
categoryId
keyword
contentType
page
size
```

용도:

```text
강의실의 오픈러닝 화면에 사용한다. 일반 학습 콘텐츠 목록과 같은 테이블을 사용하되 화면 목적에 맞게 별도 엔드포인트를 제공한다.
```

## 10.5 학습 콘텐츠 상세 조회

```http
GET /api/v1/learning/contents/{contentId}
```

처리:

- `viewCount` 증가 가능.

## 10.6 학습 시작/진행률 저장

```http
POST /api/v1/learning/contents/{contentId}/progress
```

Request:

```json
{
  "progressRate": 35.5,
  "lastPositionSeconds": 420,
  "progressStatus": "IN_PROGRESS"
}
```

## 10.7 학습 완료 처리

```http
POST /api/v1/learning/contents/{contentId}/complete
```

## 10.8 내 학습 진행 목록

```http
GET /api/v1/learning/progress/my
```

## 10.9 나의 지식 콘텐츠 조회

```http
GET /api/v1/learning/contents/my-knowledge
```

Query:

```text
progressStatus
categoryId
page
size
```

용도:

```text
마이캠퍼스의 나의 지식 콘텐츠 화면에 사용한다. 사용자가 학습한 콘텐츠와 진행 상태를 중심으로 조회한다.
```

## 10.10 찜한 학습 콘텐츠 조회

```http
GET /api/v1/learning/contents/my-selected
```

용도:

```text
에듀싸피의 찜한 콘텐츠/Selected Content 화면에 사용한다. 내부적으로는 user_bookmarks에서 LEARNING_CONTENT 대상만 조회한다.
```

## 10.11 콘텐츠 상호작용 기록

```http
POST /api/v1/learning/contents/{contentId}/interactions
```

Request:

```json
{
  "interactionType": "PLAY"
}
```

가능 값:

```text
VIEW, PLAY, LIKE, DOWNLOAD
```

---

# 11. Quest/평가/과제 API

## 11.1 내 수행 항목 목록 조회

```http
GET /api/v1/tasks/my
```

Query:

```text
courseId
taskType
resultStatus
page
size
```

## 11.2 수행 항목 상세 조회

```http
GET /api/v1/tasks/{taskId}
```

## 11.3 과제/평가 제출

```http
POST /api/v1/tasks/{taskId}/submit
```

Request:

```json
{
  "answerData": {
    "answer": "제출 답안"
  }
}
```

## 11.4 내 제출 결과 조회

```http
GET /api/v1/tasks/{taskId}/my-result
```

---

# 12. 교육현황/Activity API

## 12.1 내 교육현황 조회

```http
GET /api/v1/activities/my
```

Query:

```text
activityType
page
size
```

Response 예시:

```json
{
  "content": [
    {
      "id": 1,
      "activityType": "SW_COMPETENCY",
      "title": "SW 역량 평가",
      "resultText": "A등급",
      "activityDate": "2026-01-01"
    }
  ]
}
```

---

# 13. 설문/신청 API

## 13.1 설문 카테고리 목록 조회

```http
GET /api/v1/survey-categories
```

## 13.2 설문/신청 목록 조회

```http
GET /api/v1/surveys
```

Query:

```text
categoryId
formType
keyword
page
size
```

## 13.3 설문/신청 상세 조회

```http
GET /api/v1/surveys/{surveyId}
```

Response:

```json
{
  "id": 1,
  "title": "간담회 신청",
  "formType": "MEETUP_APPLICATION",
  "openAt": "2026-01-01T09:00:00",
  "closeAt": "2026-01-05T18:00:00",
  "questions": []
}
```

## 13.4 설문 응답/신청 제출

```http
POST /api/v1/surveys/{surveyId}/submit
```

Request:

```json
{
  "answers": {
    "1": "답변 내용",
    "2": ["선택지1", "선택지2"]
  }
}
```

처리:

- `survey_participants.answers`에 저장.
- `participantStatus = SUBMITTED`.

## 13.5 내 설문/신청 내역 조회

```http
GET /api/v1/surveys/my-participations
```

Query:

```text
formType
page
size
```

Response item includes `formType` so clients can distinguish survey participation from meetup applications.

## 13.6 신청 취소

```http
POST /api/v1/surveys/{surveyId}/cancel
```

---

# 14. 1:1 문의/Inquiry API

## 14.1 내 문의 목록 조회

```http
GET /api/v1/inquiries/my
```

Query:

```text
status
page
size
```

## 14.2 문의 상세 조회

```http
GET /api/v1/inquiries/{inquiryId}
```

권한:

- 문의 작성자 또는 관리자만 조회 가능.

## 14.3 문의 등록

```http
POST /api/v1/inquiries
```

Request:

```json
{
  "category": "계정",
  "title": "로그인이 안 됩니다.",
  "content": "비밀번호를 변경했는데 로그인이 되지 않습니다.",
  "fileIds": [1]
}
```

## 14.4 문의 수정

```http
PATCH /api/v1/inquiries/{inquiryId}
```

조건:

- 답변 전 상태에서만 수정 가능.

## 14.5 문의 삭제

```http
DELETE /api/v1/inquiries/{inquiryId}
```

처리:

- `isDeleted = true`.

---

# 15. 알림/Notification API

## 15.1 내 알림 목록 조회

```http
GET /api/v1/notifications/my
```

Query:

```text
isRead
notificationType
page
size
```

## 15.2 안 읽은 알림 개수 조회

```http
GET /api/v1/notifications/my/unread-count
```

## 15.3 알림 읽음 처리

```http
PATCH /api/v1/notifications/{notificationId}/read
```

## 15.4 전체 알림 읽음 처리

```http
PATCH /api/v1/notifications/my/read-all
```

## 15.5 알림 삭제

```http
DELETE /api/v1/notifications/{notificationId}
```

---

# 16. 약관/동의/Agreement API

## 16.1 약관 목록 조회

```http
GET /api/v1/agreements
```

Query:

```text
category
targetType
targetId
requiredOnly
```

## 16.2 약관 상세 조회

```http
GET /api/v1/agreements/{agreementId}
```

## 16.3 약관 동의 처리

```http
POST /api/v1/agreements/{agreementId}/agree
```

처리:

- `user_agreements`에 동의 기록 저장.
- `agreedVersion`은 현재 `agreement.version`을 저장.

## 16.4 내 동의 이력 조회

```http
GET /api/v1/agreements/my
```

---

# 17. 관리자 User API

> 경로 prefix: `/api/v1/admin`

## 17.1 회원 목록 조회

```http
GET /api/v1/admin/users
```

Query:

```text
keyword
role
status
generation
region
classNo
page
size
```

## 17.2 회원 상세 조회

```http
GET /api/v1/admin/users/{userId}
```

## 17.3 회원 단건 등록

```http
POST /api/v1/admin/users
```

Request:

```json
{
  "email": "student001@ssafy.com",
  "name": "홍길동",
  "studentNo": "1200001",
  "generation": 12,
  "region": "서울",
  "classNo": 1,
  "phoneNumber": "010-0000-0000",
  "emergencyPhoneNumber": "010-1111-1111",
  "role": "STUDENT",
  "initialPassword": "0000"
}
```

처리:

- 관리자가 교육생/멘토/운영자/관리자를 직접 등록한다.
- `initialPassword`는 서버에서 BCrypt 등으로 해시 후 `users.password`에 저장한다.
- 등록 직후 `user_stats` 기본 row를 생성한다.
- 주요 변경이므로 `audit_logs`에 `CREATE / USER` 기록.
- 실제 운영에서는 최초 로그인 시 비밀번호 변경을 유도하는 정책을 둘 수 있다.

## 17.4 회원 엑셀 일괄 등록

```http
POST /api/v1/admin/users/import
Content-Type: multipart/form-data
```

Form Data:

```text
file: xlsx 또는 csv 파일
defaultPassword: 전체 사용자에게 적용할 기본 비밀번호, 예: 0000
role: 기본 권한, 예: STUDENT
```

엑셀 컬럼 예시:

```text
email
name
studentNo
generation
region
classNo
phoneNumber
emergencyPhoneNumber
zipCode
address
addressDetail
role
initialPassword
```

처리:

- 관리자가 엑셀 파일로 교육생 정보를 한 번에 등록한다.
- `email`, `studentNo` 중복 여부를 검증한다.
- row별 성공/실패 결과를 반환한다.
- `initialPassword` 컬럼이 있으면 해당 값을 우선 사용하고, 없으면 `defaultPassword`를 사용한다.
- 모든 비밀번호는 서버에서 해시 후 저장한다.
- 성공한 사용자마다 `user_stats` 기본 row를 생성한다.
- 전체 업로드 결과는 별도 테이블을 만들지 않고 응답으로만 반환한다.
- 일괄 등록 행위는 `audit_logs`에 `CREATE / USER_IMPORT`로 1건 기록한다.

Response:

```json
{
  "totalCount": 100,
  "successCount": 97,
  "failCount": 3,
  "failedRows": [
    {
      "rowNo": 5,
      "email": "student005@ssafy.com",
      "reason": "이미 존재하는 이메일입니다."
    }
  ]
}
```

비고:

- 엑셀 업로드 이력 테이블은 만들지 않는다.
- 실패 row 재처리는 같은 API에 수정된 엑셀을 다시 업로드하는 방식으로 처리한다.
- 민감한 기본 비밀번호는 응답에 다시 내려주지 않는다.

## 17.5 회원 정보 수정

```http
PATCH /api/v1/admin/users/{userId}
```

Request:

```json
{
  "name": "홍길동",
  "generation": 12,
  "region": "서울",
  "classNo": 1,
  "role": "STUDENT",
  "status": "ACTIVE"
}
```

처리:

- 주요 변경 시 `audit_logs` 기록.

## 17.6 회원 비밀번호 초기화

```http
POST /api/v1/admin/users/{userId}/reset-password
```

Request:

```json
{
  "newPassword": "0000"
}
```

처리:

- 관리자가 사용자의 비밀번호를 임시 비밀번호 또는 지정 비밀번호로 초기화한다.
- 새 비밀번호는 서버에서 해시 후 `users.password`에 저장한다.
- `password_change_histories` 기록 가능.
- `audit_logs` 기록.

---

# 18. 관리자 출결 API

## 18.1 출결 목록 조회

```http
GET /api/v1/admin/attendance
```

Query:

```text
userId
courseId
startDate
endDate
status
page
size
```

## 18.2 출결 수동 등록

```http
POST /api/v1/admin/attendance
```

Request:

```json
{
  "userId": 1,
  "courseId": 1,
  "calendarDayId": 1,
  "attendanceDate": "2026-01-01",
  "checkInAt": "2026-01-01T09:00:00",
  "checkOutAt": "2026-01-01T18:00:00",
  "status": "NORMAL",
  "note": "관리자 수동 등록"
}
```

처리:

- 출결 기록이 없는 교육생의 일자별 출결을 관리자 수동으로 등록한다.
- `audit_logs` 기록.

## 18.3 출결 수동 수정

```http
PATCH /api/v1/admin/attendance/{attendanceRecordId}
```

Request:

```json
{
  "checkInAt": "2026-01-01T09:00:00",
  "checkOutAt": "2026-01-01T18:00:00",
  "status": "NORMAL",
  "reasonStatus": "APPROVED",
  "note": "관리자 수동 수정"
}
```

처리:

- `audit_logs` 기록.

## 18.4 출결 소명 목록 조회

```http
GET /api/v1/admin/attendance/appeals
```

## 18.5 출결 소명 승인

```http
POST /api/v1/admin/attendance/appeals/{appealId}/approve
```

Request:

```json
{
  "reviewComment": "승인합니다."
}
```

## 18.6 출결 소명 반려

```http
POST /api/v1/admin/attendance/appeals/{appealId}/reject
```

Request:

```json
{
  "reviewComment": "증빙 자료가 부족합니다."
}
```

## 18.7 교육 달력 등록

```http
POST /api/v1/admin/education-calendar
```

## 18.8 교육 달력 수정

```http
PATCH /api/v1/admin/education-calendar/{calendarDayId}
```

## 18.9 교육 달력 삭제

```http
DELETE /api/v1/admin/education-calendar/{calendarDayId}
```

비고:

```text
교육일, 휴일, 방학, 행사일 변경은 출결 집계에 영향을 주므로 관리자 감사 로그를 남긴다.
```

---

# 19. 관리자 포인트 API

## 19.1 사용자 포인트 조회

```http
GET /api/v1/admin/points/users/{userId}
```

## 19.2 포인트/경험치 수동 조정

```http
POST /api/v1/admin/points/transactions
```

Request:

```json
{
  "userId": 1,
  "transactionType": "ADJUST",
  "pointAmount": 10,
  "expAmount": 100,
  "reason": "관리자 수동 지급"
}
```

처리:

- `point_transactions.createdBy`에 관리자 저장.
- `user_stats` 갱신.
- `audit_logs.action = ADJUST` 기록.

## 19.3 포인트/경험치 이력 조회

```http
GET /api/v1/admin/points/transactions
```

Query:

```text
userId
transactionType
startDate
endDate
page
size
```

---

# 20. 관리자 게시판 API

## 20.1 게시판 생성

```http
POST /api/v1/admin/boards
```

Request:

```json
{
  "name": "공지사항",
  "code": "NOTICE",
  "boardType": "NOTICE",
  "description": "공지사항 게시판"
}
```

## 20.2 게시판 수정

```http
PATCH /api/v1/admin/boards/{boardId}
```

## 20.3 게시판 카테고리 생성

```http
POST /api/v1/admin/boards/{boardId}/categories
```

Request:

```json
{
  "name": "일반",
  "code": "GENERAL"
}
```

## 20.4 게시판 카테고리 수정

```http
PATCH /api/v1/admin/board-categories/{categoryId}
```

## 20.5 게시판 카테고리 삭제

```http
DELETE /api/v1/admin/board-categories/{categoryId}
```

비고:

```text
게시판 자체는 서비스 구조에 가까우므로 잦은 삭제 대상이 아니다. 필요하면 초기 데이터로 관리하고, 관리자 화면에서는 카테고리와 게시글 관리 중심으로 구현한다.
```

## 20.6 게시글 관리자 삭제/복구

```http
PATCH /api/v1/admin/board-posts/{postId}/delete
PATCH /api/v1/admin/board-posts/{postId}/restore
```

처리:

- `audit_logs` 기록.

---

# 21. 관리자 강의/학습 API

## 21.1 과정 생성

```http
POST /api/v1/admin/courses
```

## 21.2 과정 수정

```http
PATCH /api/v1/admin/courses/{courseId}
```

## 21.3 과정 삭제

```http
DELETE /api/v1/admin/courses/{courseId}
```

## 21.4 이러닝 카테고리 생성

```http
POST /api/v1/admin/learning/categories
```

## 21.5 이러닝 카테고리 수정

```http
PATCH /api/v1/admin/learning/categories/{categoryId}
```

## 21.6 이러닝 카테고리 삭제

```http
DELETE /api/v1/admin/learning/categories/{categoryId}
```

비고:

```text
이러닝 카테고리는 화면 구성 CMS가 아니라 학습 콘텐츠 분류이므로 관리자 CRUD 범위에 포함한다.
```

## 21.7 주차 생성

```http
POST /api/v1/admin/courses/{courseId}/weeks
```

## 21.8 주차 수정

```http
PATCH /api/v1/admin/course-weeks/{weekId}
```

## 21.9 주차 삭제

```http
DELETE /api/v1/admin/course-weeks/{weekId}
```

## 21.10 세션 생성

```http
POST /api/v1/admin/course-sessions
```

## 21.11 세션 수정

```http
PATCH /api/v1/admin/course-sessions/{sessionId}
```

## 21.12 세션 삭제

```http
DELETE /api/v1/admin/course-sessions/{sessionId}
```

## 21.13 학습 콘텐츠 생성

```http
POST /api/v1/admin/learning/contents
```

Request:

```json
{
  "courseId": 1,
  "sessionId": 1,
  "categoryId": 1,
  "title": "Java 기초",
  "description": "Java 기초 영상",
  "contentType": "VIDEO",
  "contentUrl": "https://...",
  "thumbnailFileId": 1,
  "durationSeconds": 1200,
  "isRequired": true,
  "openAt": "2026-01-01T09:00:00",
  "closeAt": "2026-01-31T23:59:59"
}
```

## 21.14 학습 콘텐츠 수정

```http
PATCH /api/v1/admin/learning/contents/{contentId}
```

## 21.15 학습 콘텐츠 삭제

```http
DELETE /api/v1/admin/learning/contents/{contentId}
```

## 21.16 Quest/평가/과제 생성

```http
POST /api/v1/admin/tasks
```

## 21.17 Quest/평가/과제 수정

```http
PATCH /api/v1/admin/tasks/{taskId}
```

## 21.18 Quest/평가/과제 결과 수정

```http
PATCH /api/v1/admin/task-results/{resultId}
```

처리:

- 점수 수정 등 주요 변경 시 `audit_logs` 기록.

## 21.19 Quest/평가/과제 삭제

```http
DELETE /api/v1/admin/tasks/{taskId}
```

## 21.20 교육현황 기록 등록

```http
POST /api/v1/admin/activities
```

## 21.21 교육현황 기록 수정

```http
PATCH /api/v1/admin/activities/{activityId}
```

## 21.22 교육현황 기록 삭제

```http
DELETE /api/v1/admin/activities/{activityId}
```

용도:

```text
마이캠퍼스 교육현황 화면의 SW 역량 등급, SSAFY 활동, 외부 수상 기록을 관리자에서 입력/수정한다.
```

---

# 22. 관리자 설문/신청 API

## 22.1 설문 카테고리 생성

```http
POST /api/v1/admin/survey-categories
```

## 22.2 설문 카테고리 수정

```http
PATCH /api/v1/admin/survey-categories/{categoryId}
```

## 22.3 설문 카테고리 삭제

```http
DELETE /api/v1/admin/survey-categories/{categoryId}
```

비고:

```text
설문 카테고리는 CMS 설정이 아니라 설문/신청 업무 분류이므로 관리자 CRUD 범위에 포함한다.
```

## 22.4 설문 생성

```http
POST /api/v1/admin/surveys
```

## 22.5 설문 수정

```http
PATCH /api/v1/admin/surveys/{surveyId}
```

## 22.6 설문 삭제

```http
DELETE /api/v1/admin/surveys/{surveyId}
```

## 22.7 설문 문항 생성

```http
POST /api/v1/admin/surveys/{surveyId}/questions
```

## 22.8 설문 문항 수정

```http
PATCH /api/v1/admin/survey-questions/{questionId}
```

## 22.9 설문 문항 삭제

```http
DELETE /api/v1/admin/survey-questions/{questionId}
```

## 22.10 설문 응답자/신청자 목록 조회

```http
GET /api/v1/admin/surveys/{surveyId}/participants
```

## 22.11 신청자 선정

```http
POST /api/v1/admin/survey-participants/{participantId}/select
```

## 22.12 신청자 탈락 처리

```http
POST /api/v1/admin/survey-participants/{participantId}/reject
```

처리:

- 선정/탈락 시 `participantStatus` 변경.
- `audit_logs` 기록.

---

# 23. 관리자 1:1 문의 API

## 23.1 문의 목록 조회

```http
GET /api/v1/admin/inquiries
```

Query:

```text
status
category
keyword
page
size
```

## 23.2 문의 상세 조회

```http
GET /api/v1/admin/inquiries/{inquiryId}
```

## 23.3 문의 답변 등록/수정

```http
POST /api/v1/admin/inquiries/{inquiryId}/answer
```

Request:

```json
{
  "answerContent": "문의하신 내용에 대해 답변드립니다."
}
```

처리:

- `status = ANSWERED`
- `answeredBy = 현재 관리자`
- `answeredAt = 현재 시간`
- `audit_logs.action = ANSWER`

## 23.4 문의 종료 처리

```http
POST /api/v1/admin/inquiries/{inquiryId}/close
```

---

# 24. 관리자 알림 API

## 24.1 알림 발송

```http
POST /api/v1/admin/notifications
```

Request:

```json
{
  "receiverIds": [1, 2, 3],
  "title": "공지 알림",
  "content": "새 공지가 등록되었습니다.",
  "notificationType": "CLASS_NOTICE",
  "targetType": "BOARD_POST",
  "targetId": 10,
  "isImportant": true
}
```

처리:

- `receiverIds` 수만큼 `notifications` row 생성.
- 별도 `notification_recipients` 테이블은 사용하지 않는다.
- `audit_logs.action = SEND` 기록.

## 24.2 알림 발송 내역 조회

```http
GET /api/v1/admin/notifications
```

---

# 25. 관리자 약관/동의 API

## 25.1 약관 생성

```http
POST /api/v1/admin/agreements
```

## 25.2 약관 수정

```http
PATCH /api/v1/admin/agreements/{agreementId}
```

## 25.3 약관 비활성화

```http
PATCH /api/v1/admin/agreements/{agreementId}/inactive
```

## 25.4 사용자 동의 이력 조회

```http
GET /api/v1/admin/user-agreements
```

Query:

```text
userId
agreementId
page
size
```

---

# 26. 관리자 감사 로그 API

## 26.1 감사 로그 목록 조회

```http
GET /api/v1/admin/audit-logs
```

Query:

```text
actorId
action
targetType
targetId
startDate
endDate
page
size
```

## 26.2 감사 로그 상세 조회

```http
GET /api/v1/admin/audit-logs/{auditLogId}
```

비고:

- 감사 로그는 직접 생성 API를 외부에 열지 않는다.
- 각 관리자 기능 수행 시 서버 내부에서 자동 생성한다.

---

# 27. 에듀싸피 확인 메뉴별 API 대응표

아래는 실제 에듀싸피에서 확인했던 주요 메뉴를 이 문서의 API로 매핑한 것이다.

## 27.1 마이캠퍼스

```text
나의 레벨/마일리지 -> GET /api/v1/users/me/campus-summary, GET /api/v1/points/my/summary
출결 현황 -> GET /api/v1/attendance/my, GET /api/v1/education-calendar
나의 지식 콘텐츠 -> GET /api/v1/learning/contents/my-knowledge
찜한 콘텐츠 -> GET /api/v1/bookmarks/my, GET /api/v1/learning/contents/my-selected
서류 제출 -> 별도 document API 없이 boards/files 또는 agreements/files로 처리
서약서 -> GET /api/v1/agreements, POST /api/v1/agreements/{agreementId}/agree
교육 현황 -> GET /api/v1/activities/my
회원정보 수정 -> GET/PATCH /api/v1/users/me, POST /api/v1/users/me/password/verify
```

## 27.2 강의실

```text
다시보기 -> GET /api/v1/course-sessions/replays
전체 다시보기 -> GET /api/v1/course-sessions/replays
주차별 커리큘럼 -> GET /api/v1/courses/{courseId}/weeks
Quest/평가 -> GET /api/v1/tasks/my, POST /api/v1/tasks/{taskId}/submit
필수학습 -> GET /api/v1/learning/contents/required
오픈러닝 -> GET /api/v1/learning/contents/open-learning
```

## 27.3 커뮤니티

```text
설문 -> GET /api/v1/surveys, POST /api/v1/surveys/{surveyId}/submit
자유게시판 -> GET/POST /api/v1/boards/free/posts
익명게시판 -> GET/POST /api/v1/boards/anonymity/posts, 응답에서 작성자 익명 처리
교육생 검색 -> GET /api/v1/users/students
```

## 27.4 HELP DESK

```text
공지사항 -> GET /api/v1/boards/notice/posts
FAQ -> GET /api/v1/boards/faq/posts
1:1 문의 -> /api/v1/inquiries 계열
학사규정 -> GET /api/v1/boards/rule/posts
```

## 27.5 멘토링 게시판

```text
멘토 현황 -> GET /api/v1/mentors, GET /api/v1/mentors/{mentorId}
멘토 Q&A -> GET/POST /api/v1/boards/mento-qna/posts
멘토링 공지사항 -> GET /api/v1/boards/mento-notice/posts
간담회 신청 -> GET /api/v1/surveys?formType=MEETUP_APPLICATION
간담회 결과 -> GET /api/v1/surveys/my-participations 또는 관리자 선정 결과 조회
간담회 후기 -> GET/POST /api/v1/boards/mento-review/posts
```

## 27.6 알림/외부 링크

```text
알림 -> GET /api/v1/notifications/my
JOB SSAFY, SSAFY GIT, Meeting! SSAFY -> 백엔드 API 없이 프론트 외부 링크로 처리
```

---

# 28. 권한 정책 요약

## 28.1 교육생 가능 기능

```text
내 정보 조회/수정
내 출결 조회
출결 소명 신청
내 포인트/경험치 조회
게시글/댓글 작성
설문/신청 제출
1:1 문의 작성
알림 조회
약관 동의
학습 진행 저장
과제/평가 제출
```

## 28.2 멘토 가능 기능

```text
멘토링 게시판 작성/답변
일반 게시판 조회
본인 관련 정보 조회
```

## 28.3 운영자/관리자 가능 기능

```text
회원 관리
출결 관리
포인트/경험치 조정
게시판 관리
강의/학습 콘텐츠 관리
설문/신청 관리
1:1 문의 답변
알림 발송
약관 관리
감사 로그 조회
```

---

# 29. 익명게시판 처리 정책

익명게시판은 프론트에서만 이름을 숨기면 안 된다.

처리 원칙:

```text
DB에는 실제 작성자 저장: board_posts.user
게시판 구분: boards.boardType = ANONYMOUS
일반 사용자 응답: 작성자 개인정보 제외, displayName = "익명"
관리자 응답: 필요 시 실제 작성자 확인 가능
```

일반 사용자 응답 예시:

```json
{
  "id": 1,
  "title": "익명 질문입니다.",
  "displayName": "익명",
  "isMine": true
}
```

---

# 30. 감사 로그 기록 대상

감사 로그는 모든 행동을 기록하지 않는다. 관리자 주요 변경 행위만 기록한다.

기록 대상:

```text
회원 상태/권한 변경
회원 비밀번호 초기화
출결 수동 수정
출결 소명 승인/반려
포인트/경험치 수동 조정
게시글 관리자 삭제/복구
강의/학습 콘텐츠 생성/수정/삭제
설문 생성/수정/삭제
신청자 선정/탈락
1:1 문의 답변
알림 발송
약관 생성/수정/비활성화
```

기록하지 않는 것:

```text
로그인 성공/실패
페이지 조회
게시글 조회
파일 다운로드
일반 교육생 댓글 작성
일반 설문 제출
이메일 발송 결과
```

---

# 31. 구현 우선순위

## 1차 MVP

```text
인증/내 정보
게시판/댓글
강의실/이러닝 조회
학습 진행률
설문/신청
1:1 문의
알림 조회
```

## 2차 관리자 기능

```text
회원 관리
게시판 관리
문의 답변
출결 소명 승인/반려
포인트 조정
설문/신청 관리
알림 발송
감사 로그 조회
```

## 3차 확장

```text
강의/콘텐츠 관리자 CRUD
과제/평가 결과 관리
약관/동의 관리
교육현황 관리
```

---

# 32. 제외한 API/기능

과한 운영 서비스/CMS 느낌을 줄이기 위해 제외한다.

```text
메뉴 관리 API
배너 관리 API
게시판 정책 설정 API
게시판 에디터 설정 API
파일 확장자 정책 관리 API
로그인 시도 이력 API
이메일 발송 로그 API
알림 수신자 별도 관리 API
비밀번호 재설정 토큰 관리 API
```

---

# 33. API 구현 시 주의사항

1. 비밀번호는 반드시 해시로 저장한다.
2. 익명게시판은 API 응답에서 작성자 개인정보를 제거한다.
3. 관리자 API는 반드시 권한 검증을 수행한다.
4. 파일 다운로드는 대상 리소스 접근 권한을 확인한다.
5. 삭제는 가능하면 soft delete를 우선 고려한다.
6. 관리자 주요 변경 행위는 `audit_logs`에 기록한다.
7. 목록 API는 모두 pagination을 적용한다.
8. 프론트 화면 구성용 메뉴/배너 API는 만들지 않는다.
