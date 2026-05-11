export const mycampusTabs = [
  { label: '레벨&장학포인트', path: '/mycampus/level-points' },
  { label: '출석현황', path: '/mycampus/attendance' },
  { label: '학습중 이러닝', path: '/mycampus/elearning' },
  { label: '찜한 목록', path: '/mycampus/bookmarks' },
  { label: '서류제출', path: '/mycampus/documents' },
  { label: '교육생 서약서', path: '/mycampus/pledges' }
]

export const profileInfo = {
  name: '김싸피',
  email: 'student@edussafy.local',
  phoneNumber: '010-1234-5678',
  campus: '13기 서울 6반'
}
export const pointSummary = {
  level: 'Lv. 5',
  totalPoints: 740,
  scholarshipPoints: 135,
  nextLevel: 900,
  rank: '서울 6반 8위'
}

export const pointHistory = [
  { id: 1, title: '월말평가 우수', type: '장학포인트', amount: '+30', date: '2026.05.09' },
  { id: 2, title: 'Quest 제출 완료', type: '활동포인트', amount: '+10', date: '2026.05.08' },
  { id: 3, title: '출석 인정', type: '기본포인트', amount: '+5', date: '2026.05.07' }
]

export const attendanceSummary = [
  { label: '출석', value: 76, tone: 'blue' },
  { label: '지각', value: 2, tone: 'slate' },
  { label: '외출', value: 1, tone: 'green' },
  { label: '결석', value: 0, tone: 'slate' }
]

export const attendanceDays = [
  { id: 1, date: '2026.05.04', day: '월', status: '출석', checkIn: '08:42', checkOut: '18:03' },
  { id: 2, date: '2026.05.05', day: '화', status: '공휴일', checkIn: '-', checkOut: '-' },
  { id: 3, date: '2026.05.06', day: '수', status: '출석', checkIn: '08:51', checkOut: '18:01' },
  { id: 4, date: '2026.05.07', day: '목', status: '지각', checkIn: '09:08', checkOut: '18:04' },
  { id: 5, date: '2026.05.08', day: '금', status: '출석', checkIn: '08:44', checkOut: '18:00' }
]

export const documents = [
  { id: 1, category: '증빙', title: '병원 진료 확인서', status: '승인', statusTone: 'green', date: '2026.05.03' },
  { id: 2, category: '서류', title: '통장 사본 제출', status: '검토중', statusTone: 'blue', date: '2026.05.01' }
]

export const pledges = [
  { id: 1, title: 'SSAFY 교육생 서약서', status: '서명완료', date: '2026.01.03' },
  { id: 2, title: '보안 및 정보보호 서약서', status: '서명완료', date: '2026.01.03' },
  { id: 3, title: '프로젝트 윤리 서약서', status: '대기', date: '-' }
]

export const educationStatus = [
  { id: 1, title: '출석률', value: '98%', description: '현재까지 정상 출석 기준' },
  { id: 2, title: '과목평가', value: '4/5', description: '완료한 과목평가 수' },
  { id: 3, title: 'Daily Task', value: '18개', description: '이번 학기 제출 완료' },
  { id: 4, title: '프로젝트 활동', value: '진행중', description: '공통 프로젝트 준비 단계' }
]

export const eLearningItems = [
  {
    id: 101,
    title: 'Spring Boot 핵심 개념 이러닝',
    breadcrumb: ['이러닝', 'Framework(Back)'],
    description: 'Framework(Back) 과정 복습을 위한 Spring Boot 핵심 개념 학습 콘텐츠입니다.',
    progress: '65% 진행',
    status: '학습중',
    date: '2026.05.10',
    views: 18,
    likes: 3,
    bookmarks: 1,
    textbook: false,
    label: 'E-LEARNING'
  },
  {
    id: 102,
    title: 'Vue Router 실습 다시보기',
    breadcrumb: ['이러닝', 'Web(Front)'],
    description: 'Vue Router 기반 SPA 화면 전환과 중첩 라우팅을 정리한 실습형 콘텐츠입니다.',
    progress: '수강완료',
    status: '완료',
    date: '2026.05.08',
    views: 11,
    likes: 2,
    bookmarks: 2,
    textbook: false,
    label: 'E-LEARNING'
  }
]

export const bookmarkedLearningItems = [
  {
    id: 201,
    title: '15기 0528_프론트엔드_리뉴얼 실습자료_3',
    breadcrumb: ['찜한 목록', '교재'],
    description: '최근 수업에서 찜한 프론트엔드 실습 교재입니다.',
    progress: '찜한 콘텐츠',
    status: '찜함',
    date: '2026.05.09',
    views: 2,
    likes: 0,
    bookmarks: 1,
    textbook: true,
    label: 'BOOKMARK'
  },
  {
    id: 202,
    title: 'DB 관계 모델링 보충 자료',
    breadcrumb: ['찜한 목록', '학습자료'],
    description: '관통 프로젝트 준비를 위해 저장해 둔 DB 관계 모델링 보충 자료입니다.',
    progress: '찜한 콘텐츠',
    status: '찜함',
    date: '2026.05.07',
    views: 7,
    likes: 1,
    bookmarks: 1,
    textbook: true,
    label: 'BOOKMARK'
  }
]
export const notifications = [
  { id: 1, title: 'Quest 제출 마감이 하루 남았습니다.', body: 'Framework 소개 Quest를 오늘 안에 제출하세요.', date: '2026.05.10', unread: true },
  { id: 2, title: '공지사항이 등록되었습니다.', body: '5월 교육 운영 및 출결 유의사항 안내', date: '2026.05.10', unread: true },
  { id: 3, title: '간담회 신청이 접수되었습니다.', body: '백엔드 개발자 커리어 간담회 신청 내역을 확인하세요.', date: '2026.05.09', unread: false }
]
