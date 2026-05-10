# Edu SSAFY Clone Backend Plan

> 목적: `backend/docs/table.md`와 `backend/docs/api.md`를 기준으로 Spring Boot 백엔드를 TDD 방식으로 구현한다.  
> 원칙: 지금 단계에서는 의존성/구조/구현 순서를 README에만 정리하고, 실제 `build.gradle` 변경이나 코드 구현은 별도 승인 후 진행한다.

---

## 1. 구현 기준 문서

- 테이블/도메인 기준: `backend/docs/table.md`
- API 기준: `backend/docs/api.md`
- 실행 DB 스키마 기준: `backend/src/main/resources/db/migration/V1__init_schema.sql`
- 프론트 화면 보조 기준: `frontend/docs/FRAME.md`, `frontend/docs/DESIGN.md`, `frontend/docs/edu_screnshot/`

백엔드 구현은 기본적으로 `table.md`와 `api.md`를 우선한다. 프론트 스크린샷과 FRAME 문서는 API 응답이 실제 화면에 필요한 데이터를 충분히 주는지 확인할 때만 보조 자료로 사용한다.

---

## 2. TDD 작업 원칙

모든 기능 구현은 RED-GREEN-REFACTOR 순서를 따른다.

1. 실패하는 테스트를 먼저 작성한다.
2. 테스트가 예상한 이유로 실패하는지 확인한다.
3. 테스트를 통과하는 최소 구현만 작성한다.
4. 해당 테스트와 관련 전체 테스트를 실행한다.
5. 필요하면 리팩토링한다.
6. 작은 단위로 커밋한다.

### 우선 테스트 범위

- Domain Entity 행위 테스트
- Application Service 단위 테스트
- Mapper 테스트
- Controller MockMvc 테스트
- Repository/Flyway/MySQL 통합 테스트는 Testcontainers 도입 여부 결정 후 진행

---

## 3. 의존성 채택 계획

### 3.1 초기 세팅에 포함할 후보

아래 라이브러리는 이 프로젝트 규모와 구조상 초기 세팅에 포함하는 방향으로 계획한다.

- Lombok
- Spring Boot Actuator
- Springdoc OpenAPI
- JJWT
- MapStruct
- Jackson JSR310
- Apache Commons Lang
- QueryDSL
- Hypersistence Utils

### 3.2 테스트 전략 확정 후 결정할 후보

아래 라이브러리는 테스트 속도와 방식에 영향을 주므로, 실제 테스트 전략 확정 후 추가한다.

- Testcontainers
- RestAssured

### 3.3 각 라이브러리 사용 목적

#### Lombok

- 반복 생성자/getter/builder 코드를 줄인다.
- Service에는 `@RequiredArgsConstructor`를 사용한다.
- Entity에는 `@Getter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 중심으로 제한한다.
- Entity에서 `@Data`, 무분별한 `@Setter`는 사용하지 않는다.

#### Spring Boot Actuator

- `/actuator/health` 기반 health check에 사용한다.
- Docker Compose, Nginx, 운영 흉내 환경에서 상태 확인에 사용한다.

#### Springdoc OpenAPI

- 실제 구현 API 확인용 Swagger UI를 제공한다.
- `api.md`는 설계 기준, Springdoc은 구현 결과 확인용으로 분리한다.

#### JJWT

- `Authorization: Bearer {accessToken}` 기반 JWT 인증 구현에 사용한다.
- Access token, refresh token 발급/검증을 담당한다.

#### MapStruct

- Entity와 Response DTO 사이 변환을 담당한다.
- Entity와 DTO가 서로 직접 의존하지 않게 Mapper 계층을 둔다.
- Lombok과 같이 사용할 경우 `lombok-mapstruct-binding` 설정을 함께 고려한다.

#### Jackson JSR310

- `LocalDate`, `LocalDateTime` JSON 직렬화/역직렬화 안정화를 위해 사용한다.
- Spring Boot starter에 포함되어 있더라도 명시 의존성 추가를 검토한다.

#### Apache Commons Lang

- 문자열/객체 유틸을 위해 사용한다.
- 예: `StringUtils`, `ObjectUtils`.

#### QueryDSL

- 관리자 검색, 게시판 검색, 출결 필터, 설문 신청자 검색 등 복합 조건 조회에 사용한다.
- 단순 CRUD Repository와 복잡한 화면 조회를 분리하기 위해 QueryRepository에서 사용한다.

#### Hypersistence Utils

- MySQL JSON 컬럼을 `Map`, `List` 등으로 직접 매핑해야 할 때 사용한다.
- 대상 후보 필드:
  - `BoardPost.contentJson`
  - `SurveyQuestion.options`
  - `SurveyParticipant.answers`
  - `Notification.metadata`

#### Testcontainers

- 실제 MySQL 기반 Repository/Flyway 통합 테스트가 필요할 때 사용한다.
- TDD의 빠른 단위 테스트 루프와 분리해서 느린 통합 테스트로 운영한다.

#### RestAssured

- 실제 HTTP 서버를 띄운 E2E API 테스트가 필요할 때 사용한다.
- 초기 Controller 테스트는 MockMvc로 시작하고, API E2E 단계에서 도입 여부를 결정한다.

---

## 4. 의존성 추가 시 검증해야 할 smoke test

실제 `build.gradle`에 의존성을 추가할 때는 넣기만 하지 않고 최소 검증을 함께 한다.

- Lombok: `@RequiredArgsConstructor`, `@Getter` 컴파일 확인
- Actuator: `/actuator/health` 응답 확인
- Springdoc: `/swagger-ui/index.html`, `/v3/api-docs` 접근 확인
- JJWT: token 생성/검증 단위 테스트
- MapStruct: mapper generated code 생성 확인
- Jackson JSR310: `LocalDateTime` JSON 직렬화 테스트
- QueryDSL: QClass 생성 및 단순 QueryRepository 테스트
- Hypersistence: JSON 컬럼 매핑 smoke test
- Testcontainers: MySQL 컨테이너 + Flyway migration 테스트
- RestAssured: health endpoint E2E 테스트

---

## 5. 책임 구조와 의존성 규칙

백엔드는 도메인 중심 구조를 사용하되, DTO/Entity/Repository 의존성이 섞이지 않도록 한다.

### 금지 규칙

- DTO가 Entity를 알면 안 된다.
- Entity가 DTO를 알면 안 된다.
- Service가 Request DTO를 직접 받으면 안 된다.
- Repository가 Response DTO를 반환하면 안 된다.
- Controller가 Entity를 반환하면 안 된다.
- Entity가 Repository나 Service를 호출하면 안 된다.

### 허용 흐름

```text
Controller
→ Request DTO
→ Command / QueryCondition
→ Application Service
→ Domain Entity / Repository / Domain Service
```

조회 응답은 다음 흐름을 따른다.

```text
Application Service
→ Entity
→ Mapper
→ Response DTO
→ Controller
```

---

## 6. 권장 패키지 구조

기준 패키지:

```text
com.edussafy.clone
```

전체 구조:

```text
com.edussafy.clone
├── global
├── domain
└── infra
```

도메인별 구조:

```text
domain/{domain}/
├── api
├── application
│   ├── command
│   └── query
├── domain
│   ├── entity
│   ├── repository
│   ├── service
│   └── enums
├── dto
│   ├── request
│   ├── response
│   └── mapper
├── infra
└── exception
```

### 각 계층 책임

#### api

- HTTP endpoint 정의
- Request validation
- Request DTO → Command 변환 호출
- Response wrapping

#### dto

- HTTP request/response 형태 정의
- Entity를 직접 참조하지 않는다.
- Request DTO는 `toCommand()`까지만 허용한다.
- `toEntity()`는 금지한다.

#### application

- 유스케이스 처리
- 트랜잭션 경계
- Command/QueryCondition 입력
- Repository 호출
- Domain Service/Policy 호출
- Mapper 호출

#### domain

- Entity
- Enum
- Repository
- Domain Service/Policy
- 핵심 상태 변경과 도메인 규칙

#### infra

- 복잡 조회 QueryRepository
- 파일 저장
- 메일
- 외부 API 연동
- 기술 의존 구현체

---

## 7. 구현 단계 플랜

### 1단계: 공통 기반 + Auth/User

목표: 프론트가 로그인, 사용자 정보, 마이캠퍼스 요약을 붙일 수 있는 최소 백엔드 기반을 만든다.

범위:

- `global/response`
- `global/exception`
- `global/security`
- `global/entity`
- `domain/auth`
- `domain/user`
- JWT 기반 인증 뼈대
- User/UserStat/PasswordChangeHistory Entity
- Auth/User API 일부

우선 API:

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`
- `GET /api/v1/auth/me`
- `PATCH /api/v1/auth/password`
- `GET /api/v1/users/me`
- `PATCH /api/v1/users/me`
- `POST /api/v1/users/me/password/verify`
- `GET /api/v1/users/me/campus-summary`

검증:

- Auth service 단위 테스트
- User domain 테스트
- User controller MockMvc 테스트
- JWT util 테스트

---

### 2단계: Board 도메인

목표: 공지사항, FAQ, 학사규정, 자유게시판, 익명게시판, 멘토링 게시판을 하나의 Board 구조로 처리한다.

범위:

- `domain/board`
- Board/BoardCategory/BoardPost/BoardComment Entity
- 게시글 목록/상세/작성/수정/삭제
- 댓글 목록/작성/수정/삭제
- 좋아요
- 익명게시판 응답 정책

우선 API:

- `GET /api/v1/boards`
- `GET /api/v1/boards/{boardCode}/categories`
- `GET /api/v1/boards/{boardCode}/posts`
- `GET /api/v1/boards/{boardCode}/posts/{postId}`
- `POST /api/v1/boards/{boardCode}/posts`
- `PATCH /api/v1/boards/{boardCode}/posts/{postId}`
- `DELETE /api/v1/boards/{boardCode}/posts/{postId}`
- `POST /api/v1/boards/{boardCode}/posts/{postId}/like`
- 댓글 API

검증:

- BoardPostService 테스트
- AnonymousBoardPolicy 테스트
- BoardPostController MockMvc 테스트

---

### 3단계: File 기반

목표: 프로필 이미지, 게시글 첨부, 문의 첨부, 출결 소명 파일, 학습 콘텐츠 파일을 공통 FileResource로 처리한다.

범위:

- `global/file` 또는 `domain/file`
- `infra/storage`
- FileResource Entity
- LocalFileStorage
- 파일 업로드/다운로드/삭제

API:

- `POST /api/v1/files`
- `GET /api/v1/files/{fileId}/download`
- `DELETE /api/v1/files/{fileId}`

검증:

- FileService 테스트
- LocalFileStorage 테스트
- FileController MockMvc 테스트

---

### 4단계: Course/Learning/Task

목표: 강의실, 주차별 커리큘럼, 다시보기, 이러닝, Quest/평가 기능을 구현한다.

범위:

- `domain/course`
- `domain/learning`
- `domain/task`
- Course/CourseWeek/CourseSession
- LearningCategory/LearningContent/UserLearningProgress/UserContentInteraction
- CourseTask/UserTaskResult

우선 API:

- `GET /api/v1/courses/my`
- `GET /api/v1/courses/{courseId}`
- `GET /api/v1/courses/{courseId}/weeks`
- `GET /api/v1/courses/{courseId}/weeks/{weekId}/sessions`
- `GET /api/v1/course-sessions/replays`
- `GET /api/v1/learning/categories`
- `GET /api/v1/learning/contents`
- `GET /api/v1/learning/required`
- `GET /api/v1/learning/open`
- `POST /api/v1/learning/contents/{contentId}/progress`
- `GET /api/v1/tasks/my`
- `GET /api/v1/tasks/{taskId}`
- `POST /api/v1/tasks/{taskId}/submit`

검증:

- Course query 테스트
- Learning progress 테스트
- Task submission 테스트

---

### 5단계: Attendance/Point/Bookmark

목표: 홈 대시보드와 마이캠퍼스에 필요한 출결, 포인트, 찜 기능을 구현한다.

범위:

- `domain/attendance`
- `domain/point`
- `domain/bookmark`
- AttendanceRecord/AttendanceAppeal/EducationCalendarDay
- PointTransaction
- UserBookmark

API:

- `GET /api/v1/attendance/my`
- `GET /api/v1/attendance/my/{attendanceRecordId}`
- `POST /api/v1/attendance/appeals`
- `GET /api/v1/attendance/appeals/my`
- `GET /api/v1/education-calendar`
- `GET /api/v1/points/my/summary`
- `GET /api/v1/points/my/transactions`
- `GET /api/v1/bookmarks/my`
- `POST /api/v1/bookmarks`
- `DELETE /api/v1/bookmarks/{bookmarkId}`

검증:

- Attendance status policy 테스트
- Attendance appeal 테스트
- Point transaction 테스트
- Bookmark 중복 방지 테스트

---

### 6단계: Survey/Inquiry/Notification/Agreement

목표: 설문/신청, 1:1 문의, 알림, 약관 동의 기능을 구현한다.

범위:

- `domain/survey`
- `domain/inquiry`
- `domain/notification`
- `domain/agreement`

API:

- 설문/신청 API
- 1:1 문의 API
- 알림 API
- 약관/동의 API

검증:

- Survey submission policy 테스트
- Inquiry ownership 테스트
- Notification read 테스트
- Agreement version agreement 테스트

---

### 7단계: Admin API + AuditLog

목표: 진짜 클론 범위에 맞는 관리자 기능을 구현한다. 단, CMS식 메뉴/배너 관리처럼 불필요한 과설계는 피한다.

범위:

- `domain/admin`
- `global/audit`
- 관리자 회원 관리
- 관리자 출결 관리
- 관리자 포인트 조정
- 관리자 게시판 관리
- 관리자 강의/학습 관리
- 관리자 설문 관리
- 관리자 문의 답변
- 관리자 알림 발송
- 관리자 약관 관리

중요 규칙:

- Admin 전용 Entity를 중복 생성하지 않는다.
- Admin API는 기존 도메인 Service/Repository를 재사용한다.
- 관리자 변경 작업은 AuditLog를 기록한다.

검증:

- Admin permission 테스트
- AuditLog 기록 테스트
- 관리자 변경 API MockMvc 테스트

---

## 8. 커밋 단위

권장 커밋 단위:

```text
chore: add backend dependency plan
feat: add backend common response and exception
feat: add auth and user domain foundation
feat: implement board APIs
feat: implement file APIs
feat: implement course learning task APIs
feat: implement campus activity APIs
feat: implement survey inquiry notification APIs
feat: implement admin APIs
```

각 커밋 전에는 관련 테스트를 통과시킨다.

---

## 9. 당장 다음 승인 대상

다음 실제 작업을 시작한다면 1차 세팅 범위만 먼저 진행한다.

1차 실제 작업 후보:

- `build.gradle` 의존성 추가
- smoke test 작성
- `global/response`, `global/exception`, `global/entity` 생성
- `domain/auth`, `domain/user` 패키지 생성
- User 관련 Entity/Enum/Repository 최소 구현
- Auth/User API 테스트 작성

이 README는 계획 문서이며, 위 작업은 별도 승인 후 진행한다.
