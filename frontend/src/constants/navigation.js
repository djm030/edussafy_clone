import { routePaths } from './routes'

export const primaryNavigation = [
  { label: '마이캠퍼스', path: routePaths.mycampusLevelPoints },
  { label: '강의실', path: routePaths.classroomMyReplays },
  { label: '커뮤니티', path: routePaths.communitySurveys },
  { label: 'HELP DESK', path: routePaths.helpNotice },
  { label: '멘토링 게시판', path: routePaths.mentoringStories }
]

export const serviceLinks = [
  { label: 'JOB SSAFY', tone: 'light', url: 'https://job.ssafy.com' },
  { label: 'SSAFY GIT', tone: 'blue', url: 'https://project.ssafy.com' },
  { label: 'Meeting! SSAFY', tone: 'green', url: 'https://meeting.ssafy.com' }
]

export const classroomTabs = [
  { label: '내강의 다시보기', path: routePaths.classroomMyReplays },
  { label: '전체강의 다시보기', path: routePaths.classroomAllReplays },
  { label: '주차별 커리큘럼', path: routePaths.classroomCurriculum },
  { label: 'Quest/평가', path: routePaths.classroomQuests },
  { label: '필수학습', path: routePaths.classroomRequiredLearning },
  { label: '학습자료', path: routePaths.classroomResources }
]

export const communityTabs = [
  { label: '설문조사', path: routePaths.communitySurveys },
  { label: '열린 게시판', path: routePaths.communityOpenBoard },
  { label: '익명 게시판', path: routePaths.communityAnonymousBoard },
  { label: '우리반 보기', path: routePaths.communityClassRoster }
]

export const helpTabs = [
  { label: '공지사항', path: routePaths.helpNotice },
  { label: 'FAQ', path: routePaths.helpFaq },
  { label: '1:1 문의', path: routePaths.helpInquiries },
  { label: '학사규정', path: routePaths.helpRules }
]

export const mentoringTabs = [
  { label: '멘토 스토리', path: routePaths.mentoringStories },
  { label: '멘토링', path: routePaths.mentoringQna },
  { label: '멘토링 공지사항', path: routePaths.mentoringNotice },
  { label: '간담회 신청', path: routePaths.mentoringMeetupApply },
  { label: '간담회 정보', path: routePaths.mentoringMeetupInfo },
  { label: '간담회 후기', path: routePaths.mentoringMeetupReviews }
]
