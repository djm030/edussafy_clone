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
