import { routePaths } from './routes'

export const mycampusTabs = [
  { label: '장학포인트/레벨', path: routePaths.mycampusLevelPoints },
  { label: '출결현황', path: routePaths.mycampusAttendance },
  { label: '학습중 이러닝', path: routePaths.mycampusElearning },
  { label: '찜한 목록', path: routePaths.mycampusBookmarks },
  { label: '서류 제출', path: routePaths.mycampusDocuments },
  { label: '서약서', path: routePaths.mycampusPledges },
  { label: '교육현황', path: routePaths.mycampusEducationStatus },
  { label: '회원정보', path: routePaths.mycampusProfile }
]

export const classroomTabs = [
  { label: '내강의 다시보기', path: routePaths.classroomMyReplays },
  { label: '전체강의 다시보기', path: routePaths.classroomAllReplays },
  { label: '주차별 커리큘럼', path: routePaths.classroomCurriculum },
  { label: 'Quest/평가', path: routePaths.classroomQuests },
  { label: '필수학습', path: routePaths.classroomRequiredLearning },
  { label: '학습자료', path: routePaths.classroomResources }
]

export const serviceLinks = [
  { label: 'JOB SSAFY', tone: 'light', url: 'https://job.ssafy.com' },
  { label: 'SSAFY GIT', tone: 'blue', url: 'https://project.ssafy.com' },
  { label: 'Meeting! SSAFY', tone: 'green', url: 'https://meeting.ssafy.com' }
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

export const primaryNavigation = [
  { label: '마이캠퍼스', path: routePaths.mycampusLevelPoints, children: mycampusTabs },
  { label: '강의실', path: routePaths.classroomMyReplays, children: classroomTabs },
  { label: '커뮤니티', path: routePaths.communitySurveys, children: communityTabs },
  { label: 'HELP DESK', path: routePaths.helpNotice, children: helpTabs },
  { label: '멘토링 게시판', path: routePaths.mentoringStories, children: mentoringTabs }
]
