export const surveys = [
  { id: 1, title: '5월 교육 만족도 설문', period: '2026.05.08 ~ 2026.05.15', status: '진행중', statusTone: 'blue', target: '서울 6반' },
  { id: 2, title: '프로젝트 주제 선호도 조사', period: '2026.05.01 ~ 2026.05.07', status: '완료', statusTone: 'slate', target: '공통 12기' },
  { id: 3, title: '스터디 운영 방식 의견 수렴', period: '2026.04.24 ~ 2026.04.30', status: '결과공개', statusTone: 'green', target: '서울 캠퍼스' }
]

export const openBoardPosts = [
  { id: 101, category: '자유', title: 'Vue 과제 제출 전 체크리스트 공유합니다.', author: '김싸피', date: '2026.05.10', views: 48 },
  { id: 102, category: '정보', title: '이번 주 알고리즘 스터디 문제 모음', author: '박싸피', date: '2026.05.09', views: 73 },
  { id: 103, category: '질문', title: 'Spring Security CORS 설정 질문드립니다.', author: '이싸피', date: '2026.05.08', views: 35 },
  { id: 104, category: '공지', title: '서울 캠퍼스 자습실 이용 안내', author: '프로', date: '2026.05.07', views: 112 }
]

export const anonymousPosts = [
  { id: 201, category: '익명', title: '팀 프로젝트 역할 분배는 어떻게 하고 있나요?', author: '익명', date: '2026.05.10', views: 59 },
  { id: 202, category: '익명', title: '면접 스터디 구하는 팁 있나요?', author: '익명', date: '2026.05.09', views: 41 },
  { id: 203, category: '익명', title: '오늘 라이브 강의 복습 포인트 정리', author: '익명', date: '2026.05.08', views: 67 }
]

export const classMembers = [
  { id: 1, name: '김싸피', role: '반장', track: 'Java 전공', campus: '서울', className: '6반' },
  { id: 2, name: '박싸피', role: '교육생', track: 'Java 비전공', campus: '서울', className: '6반' },
  { id: 3, name: '이싸피', role: '교육생', track: 'Embedded', campus: '서울', className: '6반' },
  { id: 4, name: '최싸피', role: '교육생', track: 'Mobile', campus: '서울', className: '6반' },
  { id: 5, name: '정싸피', role: '교육생', track: 'Python', campus: '서울', className: '6반' },
  { id: 6, name: '윤싸피', role: '교육생', track: 'Java 전공', campus: '서울', className: '6반' }
]

export const notices = [
  { id: 1, category: '공통', title: '5월 교육 운영 및 출결 유의사항 안내', author: 'SSAFY', date: '2026.05.10', views: 342 },
  { id: 2, category: '학사', title: '프로젝트 기간 중 반출입 절차 안내', author: 'SSAFY', date: '2026.05.08', views: 289 },
  { id: 3, category: '시스템', title: 'EDU 시스템 점검 안내', author: 'SSAFY', date: '2026.05.07', views: 198 }
]

export const faqs = [
  { id: 1, category: '출결', question: '지각/조퇴/외출 신청은 어디에서 하나요?', answer: '마이캠퍼스 출석현황에서 날짜를 선택한 뒤 신청할 수 있습니다.' },
  { id: 2, category: '과제', question: 'Quest 제출 파일을 수정할 수 있나요?', answer: '제출 마감 전까지 상세 페이지에서 다시 제출할 수 있습니다.' },
  { id: 3, category: '계정', question: '비밀번호는 어떻게 변경하나요?', answer: '프로필 메뉴의 비밀번호 변경 화면을 사용하세요.' }
]

export const inquiries = [
  { id: 1, category: '출결', title: '병원 진료 증빙 서류 문의', status: '답변완료', statusTone: 'green', date: '2026.05.10' },
  { id: 2, category: '시스템', title: '강의 다시보기 재생 오류', status: '접수', statusTone: 'blue', date: '2026.05.09' },
  { id: 3, category: '학사', title: '과제 제출 인정 기준 문의', status: '처리중', statusTone: 'slate', date: '2026.05.08' }
]

export const ruleCategories = [
  { id: 'attendance', title: '출결 규정', body: '입실, 퇴실, 외출, 조퇴는 EDU 출결 기록과 증빙 자료를 기준으로 관리됩니다.' },
  { id: 'assignment', title: '과제 및 평가', body: 'Quest와 월말평가는 지정된 기간 안에 제출해야 하며 지연 제출은 별도 기준을 따릅니다.' },
  { id: 'campus', title: '캠퍼스 생활', body: '교육장 출입, 좌석, 장비 사용은 캠퍼스 운영 정책을 따릅니다.' }
]

export const mentoringPosts = {
  stories: [
    { id: 301, category: '취업', title: '삼성 SW 직무 합격 후배 인터뷰', author: '멘토A', date: '2026.05.10', views: 81 },
    { id: 302, category: '성장', title: '비전공자가 프로젝트에서 살아남는 법', author: '멘토B', date: '2026.05.09', views: 66 }
  ],
  qna: [
    { id: 401, category: 'Q&A', title: '포트폴리오에 팀 프로젝트를 어떻게 정리할까요?', author: '김싸피', date: '2026.05.10', views: 22 },
    { id: 402, category: 'Q&A', title: '코딩테스트와 프로젝트 병행 전략', author: '박싸피', date: '2026.05.09', views: 34 }
  ],
  notice: [
    { id: 501, category: '공지', title: '5월 멘토링 라이브 일정 안내', author: 'SSAFY', date: '2026.05.08', views: 105 },
    { id: 502, category: '공지', title: '멘토링 질문 등록 가이드', author: 'SSAFY', date: '2026.05.01', views: 87 }
  ],
  reviews: [
    { id: 601, category: '후기', title: '현직자 간담회 후기와 질문 리스트', author: '이싸피', date: '2026.05.07', views: 44 },
    { id: 602, category: '후기', title: '백엔드 직군 멘토링 핵심 요약', author: '최싸피', date: '2026.05.06', views: 39 }
  ]
}

export const meetups = [
  { id: 1, title: '백엔드 개발자 커리어 간담회', mentor: '삼성전자 SW Engineer', date: '2026.05.17 19:00', capacity: '40명', status: '신청가능', statusTone: 'blue' },
  { id: 2, title: '금융권 IT 직무 이해하기', mentor: '핀테크 서비스 리드', date: '2026.05.22 20:00', capacity: '30명', status: '마감임박', statusTone: 'green' },
  { id: 3, title: '프로젝트 코드리뷰 실전', mentor: '플랫폼 개발 멘토', date: '2026.05.29 19:30', capacity: '25명', status: '예정', statusTone: 'slate' }
]
