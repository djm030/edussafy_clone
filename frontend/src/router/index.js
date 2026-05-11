import { createRouter, createWebHistory } from 'vue-router'
import DashboardPage from '../pages/DashboardPage.vue'
import ClassroomCurriculumPage from '../pages/classroom/ClassroomCurriculumPage.vue'
import ClassroomQuestDetailPage from '../pages/classroom/ClassroomQuestDetailPage.vue'
import ClassroomQuestListPage from '../pages/classroom/ClassroomQuestListPage.vue'
import ClassroomResourceDetailPage from '../pages/classroom/ClassroomResourceDetailPage.vue'
import ClassroomResourcesPage from '../pages/classroom/ClassroomResourcesPage.vue'
import LectureReplayPage from '../pages/classroom/LectureReplayPage.vue'
import RequiredLearningPage from '../pages/classroom/RequiredLearningPage.vue'
import ErrorPage from '../pages/errors/ErrorPage.vue'
import PlaceholderPage from '../pages/PlaceholderPage.vue'
import { routePaths } from '../constants/routes'

function placeholder(title) {
  return {
    component: PlaceholderPage,
    props: { title }
  }
}

const routes = [
  { path: '/', redirect: routePaths.dashboard },
  { path: routePaths.dashboard, name: 'dashboard', component: DashboardPage },
  { path: routePaths.mycampusLevelPoints, name: 'mycampus-level-points', ...placeholder('MyCampus Level & Points') },
  { path: routePaths.mycampusAttendance, name: 'mycampus-attendance', ...placeholder('MyCampus Attendance') },
  { path: routePaths.mycampusElearning, name: 'mycampus-elearning', ...placeholder('MyCampus Elearning') },
  { path: routePaths.mycampusBookmarks, name: 'mycampus-bookmarks', ...placeholder('MyCampus Bookmarks') },
  { path: routePaths.mycampusDocuments, name: 'mycampus-documents', ...placeholder('MyCampus Documents') },
  { path: routePaths.mycampusDocumentWrite, name: 'mycampus-document-write', ...placeholder('Document Submission') },
  { path: routePaths.mycampusPledges, name: 'mycampus-pledges', ...placeholder('Student Pledges') },
  { path: routePaths.mycampusEducationStatus, name: 'mycampus-education-status', ...placeholder('Education Status') },
  { path: routePaths.mycampusProfile, name: 'mycampus-profile', ...placeholder('Profile') },
  { path: routePaths.mycampusPassword, name: 'mycampus-password', ...placeholder('Password') },
  { path: routePaths.classroomMyReplays, name: 'classroom-my-replays', component: LectureReplayPage, props: { variant: 'my' } },
  { path: routePaths.classroomAllReplays, name: 'classroom-all-replays', component: LectureReplayPage, props: { variant: 'all' } },
  { path: routePaths.classroomCurriculum, name: 'classroom-curriculum', component: ClassroomCurriculumPage },
  { path: routePaths.classroomQuests, name: 'classroom-quests', component: ClassroomQuestListPage },
  { path: '/classroom/quests/:id', name: 'classroom-quest-detail', component: ClassroomQuestDetailPage },
  { path: routePaths.classroomRequiredLearning, name: 'classroom-required-learning', component: RequiredLearningPage },
  { path: routePaths.classroomResources, name: 'classroom-resources', component: ClassroomResourcesPage },
  { path: '/classroom/resources/:id', name: 'classroom-resource-detail', component: ClassroomResourceDetailPage },
  { path: routePaths.communitySurveys, name: 'community-surveys', ...placeholder('Community Surveys') },
  { path: routePaths.communityOpenBoard, name: 'community-open-board', ...placeholder('Open Board') },
  { path: routePaths.communityOpenBoardWrite, name: 'community-open-board-write', ...placeholder('Open Board Write') },
  { path: routePaths.communityAnonymousBoard, name: 'community-anonymous-board', ...placeholder('Anonymous Board') },
  { path: routePaths.communityClassRoster, name: 'community-class-roster', ...placeholder('Class Roster') },
  { path: routePaths.helpNotice, name: 'help-notice', ...placeholder('Notice') },
  { path: '/help/notice/:id', name: 'help-notice-detail', ...placeholder('Notice Detail') },
  { path: routePaths.helpFaq, name: 'help-faq', ...placeholder('FAQ') },
  { path: routePaths.helpInquiries, name: 'help-inquiries', ...placeholder('Inquiries') },
  { path: routePaths.helpInquiryWrite, name: 'help-inquiry-write', ...placeholder('Inquiry Write') },
  { path: routePaths.helpRules, name: 'help-rules', ...placeholder('Academic Rules') },
  { path: routePaths.mentoringStories, name: 'mentoring-stories', ...placeholder('Mentoring Stories') },
  { path: routePaths.mentoringQna, name: 'mentoring-qna', ...placeholder('Mentoring Q&A') },
  { path: routePaths.mentoringNotice, name: 'mentoring-notice', ...placeholder('Mentoring Notice') },
  { path: routePaths.mentoringMeetupApply, name: 'mentoring-meetup-apply', ...placeholder('Meetup Apply') },
  { path: routePaths.mentoringMeetupInfo, name: 'mentoring-meetup-info', ...placeholder('Meetup Info') },
  { path: routePaths.mentoringMeetupReviews, name: 'mentoring-meetup-reviews', ...placeholder('Meetup Reviews') },
  { path: routePaths.mentoringMeetupReviewWrite, name: 'mentoring-meetup-review-write', ...placeholder('Meetup Review Write') },
  { path: routePaths.notifications, name: 'notifications', ...placeholder('Notifications') },
  { path: routePaths.forbidden, name: 'forbidden', component: ErrorPage, props: { code: '403 Forbidden', message: 'You do not have permission to access this content.' } },
  { path: '/:pathMatch(.*)*', name: 'not-found', component: ErrorPage, props: { code: '404 Not Found', message: 'The requested page could not be found.' } }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
