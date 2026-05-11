export const enumLabels = {
  UserRole: {
    STUDENT: '교육생',
    ADMIN: '관리자',
    OPERATOR: '운영자',
    MENTOR: '멘토'
  },
  UserStatus: {
    ACTIVE: '활성',
    INACTIVE: '비활성',
    WITHDRAWN: '탈퇴'
  },
  AttendanceStatus: {
    NORMAL: '정상',
    LATE: '지각',
    ABSENT: '결석',
    EARLY_LEAVE: '조퇴',
    OUTING: '외출',
    EXCUSED: '공가',
    PENDING: '대기'
  },
  AttendanceIssueType: {
    LATE: '지각',
    EARLY_LEAVE: '조퇴',
    OUTING: '외출',
    ABSENCE: '결석',
    MISSING_CHECK_IN: '입실 누락',
    MISSING_CHECK_OUT: '퇴실 누락'
  },
  AttendanceReasonStatus: {
    NONE: '없음',
    PENDING: '대기',
    APPROVED: '승인',
    REJECTED: '반려',
    UNEXCUSED: '미인정'
  },
  CourseStatus: {
    PLANNED: '예정',
    OPEN: '운영중',
    CLOSED: '종료'
  },
  CourseSessionType: {
    LECTURE: '강의',
    PROJECT: '프로젝트',
    LIVE: '라이브',
    REPLAY: '다시보기',
    CURRICULUM: '커리큘럼',
    SELF_STUDY: '자율학습'
  },
  LearningContentType: {
    VIDEO: '영상',
    EBOOK: 'e-Book',
    LINK: '링크',
    FILE: '파일',
    LIVE_REPLAY: '라이브 다시보기'
  },
  LearningProgressStatus: {
    NOT_STARTED: '학습 전',
    IN_PROGRESS: '학습중',
    COMPLETED: '학습 완료'
  },
  CourseTaskType: {
    QUEST: 'Quest',
    EVALUATION: '평가',
    SUBJECT_EVALUATION: '과목평가',
    MONTHLY_EVALUATION: '월말평가',
    REQUIRED_LEARNING: '필수학습',
    DAILY_ASSIGNMENT: '데일리 과제',
    ASSIGNMENT: '과제'
  },
  TaskResultStatus: {
    SCHEDULED: '예정',
    IN_PROGRESS: '진행중',
    SUBMITTED: '제출',
    COMPLETED: '완료',
    PASSED: 'PASS',
    FAILED: 'FAIL',
    CANCELLED: '취소',
    EXPIRED: '만료'
  },
  FormType: {
    SURVEY: '설문',
    SUBJECT_SURVEY: '과목 설문',
    MEETUP_APPLICATION: 'Meetup 신청',
    EVENT_APPLICATION: '이벤트 신청'
  },
  ParticipantStatus: {
    TARGETED: '대상',
    SUBMITTED: '제출',
    CANCELLED: '취소',
    SELECTED: '선정',
    REJECTED: '미선정'
  },
  NotificationType: {
    CLASS_NOTICE: '반 공지',
    USER_NOTICE: '개인 알림',
    SYSTEM: '시스템',
    QUEST: 'Quest',
    MENTORING: '멘토링',
    ATTENDANCE: '출석'
  },
  AgreementType: {
    TERMS: '이용약관',
    PRIVACY: '개인정보 처리방침',
    OPERATION_RULE: '운영 규정',
    DELETE_CRITERIA: '삭제 기준',
    MARKETING: '마케팅 수신 동의',
    ETC: '기타'
  }
}
