export const mycampusTabs = [
  { label: '레벨&장학포인트', path: '/mycampus/level-points' },
  { label: '출석현황', path: '/mycampus/attendance' },
  { label: '학습중 이러닝', path: '/mycampus/elearning' },
  { label: '찜한 목록', path: '/mycampus/bookmarks' },
  { label: '서류제출', path: '/mycampus/documents' },
  { label: '교육생 서약서', path: '/mycampus/pledges' }
]

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

export const notifications = [
  { id: 1, title: 'Quest 제출 마감이 하루 남았습니다.', body: 'Framework 소개 Quest를 오늘 안에 제출하세요.', date: '2026.05.10', unread: true },
  { id: 2, title: '공지사항이 등록되었습니다.', body: '5월 교육 운영 및 출결 유의사항 안내', date: '2026.05.10', unread: true },
  { id: 3, title: '간담회 신청이 접수되었습니다.', body: '백엔드 개발자 커리어 간담회 신청 내역을 확인하세요.', date: '2026.05.09', unread: false }
]
