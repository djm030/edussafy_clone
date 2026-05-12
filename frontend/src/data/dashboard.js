export const dashboardData = {
  user: {
    id: 1,
    email: 'student@edussafy.local',
    name: '박관열',
    studentNo: '1545067',
    generation: 15,
    region: '부울경',
    classNo: 1,
    role: 'STUDENT',
    status: 'ACTIVE'
  },
  attendanceSummary: {
    date: '2026-05-10',
    dayLabel: '일요일',
    checkInAt: '-',
    checkOutAt: '-',
    status: 'PENDING',
    statusLabel: '대기',
    message: '평일에만 입퇴실이 가능합니다'
  },
  pointSummary: {
    scholarshipPoint: 0,
    totalExp: 1223,
    levelName: 'Silver',
    levelNo: 1,
    attendanceRate: 96.5,
    completedLearningCount: 0,
    unreadNotificationCount: 3
  },
  curriculumPreview: [
    {
      id: 1,
      weekNo: 20,
      title: 'AOP',
      date: '2026.05.11(월)',
      dateLabel: '2026.05.11(월)',
      timeRange: '09:00~18:00',
      type: 'LECTURE',
      categoryName: '코딩과정',
      subject: 'Framework(Back)',
      location: '박사훈 / 서울 605'
    },
    {
      id: 2,
      weekNo: 20,
      title: 'Servlet Filter & Spring Security',
      date: '2026.05.12(화)',
      dateLabel: '2026.05.12(화)',
      timeRange: '09:00~18:00',
      type: 'LECTURE',
      categoryName: '코딩과정',
      subject: 'Framework(Back)',
      location: '박사훈 / 서울 605'
    }
  ],
  questPreview: [
    { id: 1, title: '260511_Java(전공)_9회차 과목평가', taskType: 'EVALUATION', status: 'SCHEDULED', score: null, closeAt: 'D-7' },
    { id: 2, title: '260427_JAVA(전공)_4회차 월말평가', taskType: 'QUEST', status: 'COMPLETED', score: 95, closeAt: 'D-1' },
    { id: 3, title: '260427_Java(전공)_8회차 과목평가', taskType: 'EVALUATION', status: 'COMPLETED', score: 90, closeAt: '완료' },
    { id: 4, title: '260413_JAVA(전공)_7회차 과목평가', taskType: 'QUEST', status: 'COMPLETED', score: 90, closeAt: '완료' }
  ],
  learningPreview: [
    {
      id: 1,
      title: '15기_SW_AI 스타트캠프_비전수립_AI디자인사고_AI아이디어톤',
      contentType: 'BOOK',
      coverVariant: 'academy',
      coverLabel: '삼성 청년 SW·AI아카데미',
      coverImage: '/dashboard-covers/learning-1.png',
      description: '15기_SW_AI스타트캠프_비전수립_AI디자인사고_AI아이디어톤',
      duration: '교재',
      required: true,
      viewCount: 5181,
      likeCount: 48,
      bookmarkCount: 13
    },
    {
      id: 2,
      title: '[SSAFY] 15기 일타싸피 학습자료',
      contentType: 'BOOK',
      coverVariant: 'ssafy',
      coverLabel: '일타싸피',
      coverImage: '/dashboard-covers/learning-2.png',
      description: '[SSAFY] 15기 일타싸피 학습자료',
      duration: '교재',
      required: false,
      viewCount: 3057,
      likeCount: 9,
      bookmarkCount: 6
    },
    {
      id: 3,
      title: '[SSAFY] 15기 일타싸피 OT 1부',
      contentType: 'BOOK',
      coverVariant: 'ssafy',
      coverLabel: '일타싸피 OT 1부',
      coverImage: '/dashboard-covers/learning-3.png',
      description: '[SSAFY] 15기 일타싸피 OT 1부',
      duration: '교재',
      required: false,
      viewCount: 1616,
      likeCount: 6,
      bookmarkCount: 2
    }
  ],
  elearningPreview: [],
  notifications: [
    { id: 1, title: '멘토 스토리 등록 안내', content: '멘토 스토리 등록 안내', notificationType: 'SYSTEM', isRead: false, createdAt: '05.04' },
    { id: 2, title: '멘토링 공지사항에 새글이 등록...', content: '멘토링 공지사항 새글', notificationType: 'MENTORING', isRead: false, createdAt: '04.28' },
    { id: 3, title: '멘토 스토리 등록 안내', content: '멘토 스토리 등록 안내', notificationType: 'MENTORING', isRead: false, createdAt: '04.21' }
  ],
  freeBoardPosts: [
    { id: 1, categoryName: '일반', title: '워킹로그 Ep.1~3 공유합니다!', displayName: '송영 (구미1반/15)', viewCount: 24, commentCount: 3, createdAt: '2026-05-11' },
    { id: 2, categoryName: '일반', title: '[외부공모전 컨설팅 신청]대전_6반_이지원', displayName: '이지원 (대전6반/15)', viewCount: 18, commentCount: 1, createdAt: '2026-05-10' },
    { id: 3, categoryName: '일반', title: '[외부공모전 컨설팅 신청]서울_6반_이연주', displayName: '이연주 (서울6반/15)', viewCount: 31, commentCount: 4, createdAt: '2026-05-09' },
    { id: 4, categoryName: '일반', title: '부울경 1반 업', displayName: '심혁 (부울경1)', viewCount: 12, commentCount: 0, createdAt: '2026-05-08' }
  ],
  storyPosts: [
    { id: 1, categoryName: 'SSAFYcial', title: '영상 포트폴리오, 더 멋있게 만드는 방법(feat. 사운드를', displayName: '운영자', viewCount: 16, commentCount: 0, createdAt: '2026-05-11' },
    { id: 2, categoryName: 'SSAFYcial', title: '부울경SSAFY가이드_4월 (14기 김채아 기자)', displayName: '운영자', viewCount: 9, commentCount: 1, createdAt: '2026-05-10' },
    { id: 3, categoryName: 'SSAFYcial', title: '자율 프로젝트 일상 무료 공개! (feat: setlog) (14기 한', displayName: '운영자', viewCount: 7, commentCount: 0, createdAt: '2026-05-09' },
    { id: 4, categoryName: 'SSAFYcial', title: '14기 특화프로젝트 소개 (14기 김채아 기자)', displayName: '운영자', viewCount: 5, commentCount: 0, createdAt: '2026-05-08' }
  ],
  notices: [
    { id: 1, categoryName: '공지', title: '5월 2주차 Mobile 트랙 시간표', displayName: '운영자', viewCount: 12, createdAt: '2026.05.08', isNotice: true },
    { id: 2, categoryName: '공지', title: '월말 평가 안내', displayName: '운영자', viewCount: 8, createdAt: '2026-05-10', isNotice: true },
    { id: 3, categoryName: 'FAQ', title: '비밀번호를 잊어버렸어요.', displayName: '운영자', viewCount: 5, createdAt: '2026-05-09', isNotice: false }
  ]
}
