import { createRouter, createWebHistory } from 'vue-router'
import DashboardPage from '../pages/DashboardPage.vue'
import ClassroomCurriculumPage from '../pages/classroom/ClassroomCurriculumPage.vue'
import ClassroomQuestDetailPage from '../pages/classroom/ClassroomQuestDetailPage.vue'
import ClassroomQuestListPage from '../pages/classroom/ClassroomQuestListPage.vue'
import ClassroomResourceDetailPage from '../pages/classroom/ClassroomResourceDetailPage.vue'
import ClassroomResourcesPage from '../pages/classroom/ClassroomResourcesPage.vue'
import LectureReplayPage from '../pages/classroom/LectureReplayPage.vue'
import RequiredLearningPage from '../pages/classroom/RequiredLearningPage.vue'
import BoardListPage from '../pages/community/BoardListPage.vue'
import BoardWritePage from '../pages/community/BoardWritePage.vue'
import ClassRosterPage from '../pages/community/ClassRosterPage.vue'
import CommunitySurveysPage from '../pages/community/CommunitySurveysPage.vue'
import SurveyDetailPage from '../pages/community/SurveyDetailPage.vue'
import HelpFaqPage from '../pages/help/HelpFaqPage.vue'
import HelpInquiriesPage from '../pages/help/HelpInquiriesPage.vue'
import HelpInquiryDetailPage from '../pages/help/HelpInquiryDetailPage.vue'
import HelpInquiryWritePage from '../pages/help/HelpInquiryWritePage.vue'
import HelpNoticeDetailPage from '../pages/help/HelpNoticeDetailPage.vue'
import HelpNoticePage from '../pages/help/HelpNoticePage.vue'
import HelpRulesPage from '../pages/help/HelpRulesPage.vue'
import MentoringBoardPage from '../pages/mentoring/MentoringBoardPage.vue'
import MeetupApplyPage from '../pages/mentoring/MeetupApplyPage.vue'
import MeetupInfoPage from '../pages/mentoring/MeetupInfoPage.vue'
import MeetupReviewWritePage from '../pages/mentoring/MeetupReviewWritePage.vue'
import AttendancePage from '../pages/mycampus/AttendancePage.vue'
import BookmarksPage from '../pages/mycampus/BookmarksPage.vue'
import DocumentWritePage from '../pages/mycampus/DocumentWritePage.vue'
import DocumentsPage from '../pages/mycampus/DocumentsPage.vue'
import EducationStatusPage from '../pages/mycampus/EducationStatusPage.vue'
import ElearningPage from '../pages/mycampus/ElearningPage.vue'
import LevelPointsPage from '../pages/mycampus/LevelPointsPage.vue'
import PasswordPage from '../pages/mycampus/PasswordPage.vue'
import PledgesPage from '../pages/mycampus/PledgesPage.vue'
import ProfilePage from '../pages/mycampus/ProfilePage.vue'
import NotificationsPage from '../pages/notifications/NotificationsPage.vue'
import LoginPage from '../pages/auth/LoginPage.vue'
import PostDetailPage from '../pages/shared/PostDetailPage.vue'
import ErrorPage from '../pages/errors/ErrorPage.vue'
import { routePaths } from '../constants/routes'
import { getAccessToken, getRefreshToken, isApiEnabled } from '../api/client'
import { authApi } from '../api/modules'


const routes = [
  { path: routePaths.login, name: 'login', component: LoginPage, meta: { layout: 'auth' } },
  { path: '/', redirect: routePaths.dashboard },
  { path: routePaths.dashboard, name: 'dashboard', component: DashboardPage },
  { path: routePaths.mycampusLevelPoints, name: 'mycampus-level-points', component: LevelPointsPage },
  { path: routePaths.mycampusAttendance, name: 'mycampus-attendance', component: AttendancePage },
  { path: routePaths.mycampusElearning, name: 'mycampus-elearning', component: ElearningPage },
  { path: routePaths.mycampusBookmarks, name: 'mycampus-bookmarks', component: BookmarksPage },
  { path: routePaths.mycampusDocuments, name: 'mycampus-documents', component: DocumentsPage },
  { path: routePaths.mycampusDocumentWrite, name: 'mycampus-document-write', component: DocumentWritePage },
  { path: routePaths.mycampusPledges, name: 'mycampus-pledges', component: PledgesPage },
  { path: routePaths.mycampusEducationStatus, name: 'mycampus-education-status', component: EducationStatusPage },
  { path: routePaths.mycampusProfile, name: 'mycampus-profile', component: ProfilePage },
  { path: routePaths.mycampusPassword, name: 'mycampus-password', component: PasswordPage },
  { path: routePaths.classroomMyReplays, name: 'classroom-my-replays', component: LectureReplayPage, props: { variant: 'my' } },
  { path: routePaths.classroomAllReplays, name: 'classroom-all-replays', component: LectureReplayPage, props: { variant: 'all' } },
  { path: routePaths.classroomCurriculum, name: 'classroom-curriculum', component: ClassroomCurriculumPage },
  { path: routePaths.classroomQuests, name: 'classroom-quests', component: ClassroomQuestListPage },
  { path: '/classroom/quests/:id', name: 'classroom-quest-detail', component: ClassroomQuestDetailPage },
  { path: routePaths.classroomRequiredLearning, name: 'classroom-required-learning', component: RequiredLearningPage },
  { path: routePaths.classroomResources, name: 'classroom-resources', component: ClassroomResourcesPage },
  { path: '/classroom/resources/:id', name: 'classroom-resource-detail', component: ClassroomResourceDetailPage },
  { path: routePaths.communitySurveys, name: 'community-surveys', component: CommunitySurveysPage },
  { path: '/community/surveys/:id', name: 'community-survey-detail', component: SurveyDetailPage },
  { path: routePaths.communityOpenBoard, name: 'community-open-board', component: BoardListPage, props: { variant: 'open' } },
  { path: routePaths.communityOpenBoardWrite, name: 'community-open-board-write', component: BoardWritePage },
  { path: '/community/boards/open/:id', name: 'community-open-board-detail', component: PostDetailPage, props: { source: 'open' } },
  { path: routePaths.communityAnonymousBoard, name: 'community-anonymous-board', component: BoardListPage, props: { variant: 'anonymous' } },
  { path: '/community/boards/anonymous/:id', name: 'community-anonymous-board-detail', component: PostDetailPage, props: { source: 'anonymous' } },
  { path: routePaths.communityClassRoster, name: 'community-class-roster', component: ClassRosterPage },
  { path: routePaths.helpNotice, name: 'help-notice', component: HelpNoticePage },
  { path: '/help/notice/:id', name: 'help-notice-detail', component: HelpNoticeDetailPage },
  { path: routePaths.helpFaq, name: 'help-faq', component: HelpFaqPage },
  { path: routePaths.helpInquiries, name: 'help-inquiries', component: HelpInquiriesPage },
  { path: '/help/inquiries/:id', name: 'help-inquiry-detail', component: HelpInquiryDetailPage },
  { path: routePaths.helpInquiryWrite, name: 'help-inquiry-write', component: HelpInquiryWritePage },
  { path: routePaths.helpRules, name: 'help-rules', component: HelpRulesPage },
  { path: routePaths.mentoringStories, name: 'mentoring-stories', component: MentoringBoardPage, props: { variant: 'stories' } },
  { path: '/mentoring/stories/:id', name: 'mentoring-story-detail', component: PostDetailPage, props: { source: 'stories' } },
  { path: routePaths.mentoringQna, name: 'mentoring-qna', component: MentoringBoardPage, props: { variant: 'qna' } },
  { path: '/mentoring/qna/:id', name: 'mentoring-qna-detail', component: PostDetailPage, props: { source: 'qna' } },
  { path: routePaths.mentoringNotice, name: 'mentoring-notice', component: MentoringBoardPage, props: { variant: 'notice' } },
  { path: '/mentoring/notice/:id', name: 'mentoring-notice-detail', component: PostDetailPage, props: { source: 'notice' } },
  { path: routePaths.mentoringMeetupApply, name: 'mentoring-meetup-apply', component: MeetupApplyPage },
  { path: routePaths.mentoringMeetupInfo, name: 'mentoring-meetup-info', component: MeetupInfoPage },
  { path: routePaths.mentoringMeetupReviews, name: 'mentoring-meetup-reviews', component: MentoringBoardPage, props: { variant: 'reviews' } },
  { path: '/mentoring/meetups/reviews/:id', name: 'mentoring-meetup-review-detail', component: PostDetailPage, props: { source: 'reviews' } },
  { path: routePaths.mentoringMeetupReviewWrite, name: 'mentoring-meetup-review-write', component: MeetupReviewWritePage },
  { path: routePaths.notifications, name: 'notifications', component: NotificationsPage },
  { path: routePaths.forbidden, name: 'forbidden', component: ErrorPage, props: { code: '403 Forbidden', message: 'You do not have permission to access this content.' } },
  { path: routePaths.notFound, name: 'not-found', component: ErrorPage, props: { code: '404 Not Found', message: 'The requested page could not be found.' } },
  { path: '/:pathMatch(.*)*', name: 'not-found-wildcard', redirect: routePaths.notFound }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to) => {
  if (!isApiEnabled || to.meta.layout === 'auth') return true
  if (getAccessToken()) return true
  if (getRefreshToken()) {
    try {
      await authApi.refresh()
      return true
    } catch {
      // Continue to the login redirect below when refresh is rejected.
    }
  }
  return {
    path: routePaths.login,
    query: {
      redirect: to.fullPath,
      reason: 'session-required'
    }
  }
})

export default router
