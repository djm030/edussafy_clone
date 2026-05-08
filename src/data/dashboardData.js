export const dashboardData = {
  navigation: [
    { label: "마이캠퍼스", href: "#", active: true },
    { label: "강의실", href: "#" },
    { label: "커뮤니티", href: "#" },
    { label: "HELP DESK", href: "#" },
    { label: "멘토링 게시판", href: "#" }
  ],
  profile: {
    id: "1545067",
    name: "백관일님",
    unreadNotificationCount: 0
  },
  serviceLinks: [
    { label: "JOB\nSSAFY", href: "#", variant: "job" },
    { label: "SSAFY\nGIT", href: "#", variant: "git" },
    { label: "Meeting!\nSSAFY", href: "#", variant: "meeting" }
  ],
  attendance: {
    date: "05. 08",
    weekday: "금요일",
    time: "08:44",
    status: "정상 출석"
  },
  summary: {
    scholarshipPoint: "0 P",
    exp: "1,218 EXP",
    currentLevel: "Silver\nLv.1",
    startLevel: "S",
    goalLevel: "G",
    progress: 30
  },
  notifications: [
    { required: true, title: "멘토스토리 등록 안내", date: "05.04", dateTime: "2026-05-04" },
    { required: true, title: "멘토링 공지사항에 새글이 등록되었습니다", shortTitle: "멘토링 공지사항에 새글이...", date: "04.28", dateTime: "2026-04-28" },
    { required: true, title: "멘토스토리 등록 안내", date: "04.21", dateTime: "2026-04-21" }
  ],
  curriculum: {
    date: "2026.05.08(금)",
    time: "09:00~18:00",
    lectures: [
      {
        id: "lecture-1",
        variant: "compact",
        instructor: "국사홍 / 서울 605"
      },
      {
        id: "lecture-2",
        variant: "main",
        category: "프로젝트",
        name: "관통 PJT",
        title: "DB 관통",
        meta: "박사홍 / 서울 605"
      }
    ]
  },
  questEvaluations: [
    { status: "예정", type: "평가", title: "260511_Java(전공)_9회차_과목평가" },
    { status: "완료", type: "Quest", title: "260427_JAVA(전공)_4회차_월말평가" },
    { status: "완료", type: "평가", title: "260427_Java(전공)_8회차_과목평가" },
    { status: "완료", type: "Quest", title: "260413_JAVA(전공)_7회차_과목평가" }
  ],
  materials: [
    {
      id: "material-1",
      title: "삼성 청년 SW·AI아카데미",
      description: "삼성 청년 SW AI 아카데미 학습자료 열기",
      theme: "blue"
    },
    {
      id: "material-2",
      title: "일타싸피",
      subtitle: "삼각함수",
      description: "일타싸피 삼각함수 학습자료 열기",
      theme: "cyan"
    },
    {
      id: "material-3",
      title: "일타싸피",
      subtitle: "OT 1부",
      description: "일타싸피 OT 1부 학습자료 열기",
      theme: "cyan"
    }
  ]
};
