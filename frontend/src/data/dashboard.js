export const dashboardData = {
  user: {
    id: 1,
    email: 'student@edussafy.local',
    name: '김샤피',
    studentNo: '1300001',
    generation: 13,
    region: '서울',
    classNo: 1,
    role: 'STUDENT',
    status: 'ACTIVE'
  },
  attendanceSummary: {
    date: '2026-05-11',
    dayLabel: '월요일',
    checkInAt: '08:51',
    checkOutAt: '18:04',
    status: 'NORMAL',
    message: '오늘의 출석이 정상 처리되었습니다.'
  },
  pointSummary: {
    scholarshipPoint: 120,
    totalExp: 350,
    levelName: '신입 개발자',
    levelNo: 2,
    attendanceRate: 96.5,
    completedLearningCount: 1,
    unreadNotificationCount: 1
  },
  curriculumPreview: [
    { id: 1, weekNo: 19, title: 'Spring Boot API 통합 검증', date: '2026-05-11', type: 'LECTURE' },
    { id: 2, weekNo: 19, title: 'API 검증 다시보기', date: '2026-05-11', type: 'REPLAY' },
    { id: 3, weekNo: 19, title: '월말 평가 안내', date: '2026-05-12', type: 'EVENT' }
  ],
  questPreview: [
    { id: 1, title: 'API 통합 검증 Quest', taskType: 'QUEST', status: 'COMPLETED', score: 95, closeAt: 'D-7' },
    { id: 2, title: '월말 평가 안내', taskType: 'MONTHLY_EVALUATION', status: 'SCHEDULED', score: null, closeAt: 'D-1' }
  ],
  learningPreview: [
    { id: 1, title: 'Spring Boot API Smoke Test', contentType: 'VIDEO', duration: '30분', required: true },
    { id: 2, title: 'Docker Compose 운영 힌트', contentType: 'FILE', duration: 'PDF', required: false },
    { id: 3, title: 'SSAFY EDU 이용 가이드', contentType: 'LINK', duration: '외부 링크', required: false }
  ],
  notifications: [
    { id: 1, title: '데모 환경 준비 완료', content: 'MySQL/Flyway seed 데이터가 준비되었습니다.', notificationType: 'SYSTEM', isRead: false, createdAt: '2026-05-11' },
    { id: 2, title: 'Quest 마감 안내', content: 'API 통합 검증 Quest를 확인하세요.', notificationType: 'QUEST', isRead: true, createdAt: '2026-05-10' },
    { id: 3, title: '출석 확인', content: '오늘의 출결 현황을 확인할 수 있습니다.', notificationType: 'ATTENDANCE', isRead: true, createdAt: '2026-05-09' }
  ],
  freeBoardPosts: [
    { id: 1, categoryName: '일반', title: 'Vue 화면 구현 체크리스트 공유', displayName: '김샤피', viewCount: 24, commentCount: 3, createdAt: '2026-05-11' },
    { id: 2, categoryName: '일반', title: '오늘 학습 자료 정리', displayName: '이멘토', viewCount: 18, commentCount: 1, createdAt: '2026-05-10' },
    { id: 3, categoryName: '일반', title: '프로젝트 환경 실행 팁', displayName: '박운영', viewCount: 31, commentCount: 4, createdAt: '2026-05-09' }
  ],
  notices: [
    { id: 1, categoryName: '공지', title: 'SSAFY EDU 클론 데모 공지', displayName: '운영자', viewCount: 12, createdAt: '2026-05-11', isNotice: true },
    { id: 2, categoryName: '공지', title: '월말 평가 안내', displayName: '운영자', viewCount: 8, createdAt: '2026-05-10', isNotice: true },
    { id: 3, categoryName: 'FAQ', title: '비밀번호를 잊어버렸어요.', displayName: '운영자', viewCount: 5, createdAt: '2026-05-09', isNotice: false }
  ]
}
