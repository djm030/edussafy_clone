export const classroomPhases = [
  { label: '1학기', status: '진행중', active: true },
  { label: '1차 Job Fair', status: '예정', active: false },
  { label: '2학기', status: '예정', active: false },
  { label: '2차 Job Fair', status: '예정', active: false }
]

export const curriculumWeeks = [
  { label: '15주차', active: false },
  { label: '16주차', active: false },
  { label: '17주차', active: false },
  { label: '18주차', active: true, period: '2026.05.04~2026.05.10' },
  { label: '19주차', active: false },
  { label: '20주차', active: false },
  { label: '21주차', active: false }
]

export const curriculumDays = [
  {
    date: '2026.05.04(월)',
    timeRange: '09:00~18:00',
    message: '이번주 수업을 알려드립니다.',
    items: [
      { id: 'cur-1', category: '코딩과정', categoryTone: 'coding', track: 'Framework(Back)', title: 'Framework소개', instructor: '박사홍 / 서울 605', hasReplay: true, hasTextbook: true }
    ]
  },
  {
    date: '2026.05.06(수)',
    timeRange: '09:00~18:00',
    items: [
      { id: 'cur-2', category: '코딩과정', categoryTone: 'coding', track: 'Framework(Back)', title: 'Spring Architecture DI', instructor: '박사홍 / 서울 605', hasReplay: true, hasTextbook: true }
    ]
  },
  {
    date: '2026.05.07(목)',
    timeRange: '09:00~18:00',
    items: [
      { id: 'cur-3', category: '코딩과정', categoryTone: 'coding', track: 'Framework(Back)', title: 'Spring Boot', instructor: '박사홍 / 서울 605', hasReplay: true, hasTextbook: true }
    ]
  },
  {
    date: '2026.05.08(금)',
    timeRange: '09:00~18:00',
    items: [
      { id: 'cur-4', category: '프로젝트', categoryTone: 'project', track: '관통 PJT', title: 'DB 관통', instructor: '박사홍 / 서울 605', hasReplay: true, hasTextbook: true }
    ]
  }
]

export const questItems = [
  { id: 1, type: '평가', scope: '[과목]', title: '260511_Java(전공)_9회차_과목평가', period: '2026.05.11 09:00 ~ 2026.05.11 10:00', status: '예정', reward: 100, result: '시험기간', score: null, pass: null },
  { id: 2, type: 'Quest', scope: '[개인]', title: '260427_JAVA(전공)_4회차_월말평가', period: '2026.04.27 10:00 ~ 2026.04.27 12:00', status: '완료', reward: 100, result: '채점완료', score: null, pass: null },
  { id: 3, type: '평가', scope: '[과목]', title: '260427_Java(전공)_8회차_과목평가', period: '2026.04.27 09:00 ~ 2026.04.27 10:00', status: '완료', reward: 100, result: '제출완료', score: 73, pass: 'PASS' },
  { id: 4, type: 'Quest', scope: '[개인]', title: '260413_JAVA(전공)_7회차_과목평가', period: '2026.04.13 09:30 ~ 2026.04.13 12:40', status: '완료', reward: 100, result: '채점완료', score: 77, pass: 'PASS' },
  { id: 5, type: 'Quest', scope: '[개인]', title: '260303 Java(전공) 2회차 월말평가', period: '2026.03.03 09:00 ~ 2026.03.03 11:00', status: '완료', reward: 100, result: '채점완료', score: 0, pass: 'FAIL' }
]

export const learningResources = [
  { id: 1, title: '15기_0528_임베디드_리눅스커널프로그래밍_3', breadcrumb: ['커리큘럼', '교재'], description: '15기_0528_임베디드_리눅스커널프로그래밍_3', views: 2, likes: 0, bookmarks: 0, textbook: true, label: 'SW·AI 아카데미' },
  { id: 2, title: '15기_0527_임베디드_리눅스커널프로그래밍_2', breadcrumb: ['커리큘럼', '교재'], description: '15기_0527_임베디드_리눅스커널프로그래밍_2', views: 0, likes: 0, bookmarks: 0, textbook: true, label: 'SW·AI 아카데미' },
  { id: 3, title: '15기_0526_임베디드_리눅스커널프로그래밍_1', breadcrumb: ['커리큘럼', '교재'], description: '15기_0526_임베디드_리눅스커널프로그래밍_1', views: 0, likes: 0, bookmarks: 0, textbook: true, label: 'SW·AI 아카데미' },
  { id: 4, title: '15기_0514_Javascript_Basic_Syntax_01', breadcrumb: ['커리큘럼', '교재'], description: '15기_0514_Javascript_Basic_Syntax_01 교재입니다.', views: 1, likes: 0, bookmarks: 0, textbook: true, label: 'Javascript' }
]

export const replayGroups = [
  'Java 전공 다시보기 - [Java] 강의 다시보기',
  'Java 전공 다시보기 - [SW 문제 해결 기본] 강의 다시보기',
  'Java 전공 다시보기 - [관통 PJT] 강의 다시보기',
  'Java 전공 다시보기 - [SW 문제 해결 응용] 강의 다시보기',
  'Java 전공 다시보기 - [Web(Front)] 강의 다시보기',
  'Java 전공 다시보기 - [AI 챌린지 OT] 강의 다시보기',
  'Java 전공 다시보기 - [AI 챌린지] 강의 다시보기',
  'Java 전공 다시보기 - [Web (Back)] 강의 다시보기',
  'Java 전공 다시보기 - [DB] 강의 다시보기',
  'Java 전공 다시보기 - [Framework(Back)] 강의 다시보기'
]

export const allReplayItems = [
  { id: 1, title: 'Framework(Back) 강의 다시보기', meta: '13기 서울 1반 · Spring Boot', period: '2026.05.04 ~ 2026.05.10' },
  { id: 2, title: 'Web(Front) 강의 다시보기', meta: '13기 서울 1반 · Vue', period: '2026.04.20 ~ 2026.04.24' },
  { id: 3, title: 'DB 관통 강의 다시보기', meta: '13기 서울 1반 · Project', period: '2026.04.13 ~ 2026.04.17' }
]
