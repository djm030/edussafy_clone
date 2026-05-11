import { apiClient, clearAccessToken, setAccessToken, unwrapApiResponse } from './client'

async function request(config) {
  const response = await apiClient(config)
  return unwrapApiResponse(response)
}

function get(url, params) {
  return request({ method: 'get', url, params })
}

function post(url, data) {
  return request({ method: 'post', url, data })
}

function patch(url, data) {
  return request({ method: 'patch', url, data })
}

function remove(url, data) {
  return request({ method: 'delete', url, data })
}

export const authApi = {
  async login(credentials) {
    const data = await post('/auth/login', credentials)
    if (data?.accessToken) setAccessToken(data.accessToken)
    return data
  },
  async logout() {
    try {
      return await post('/auth/logout')
    } finally {
      clearAccessToken()
    }
  },
  me: () => get('/auth/me'),
  resetTemporaryPassword: (payload) => post('/auth/password/reset-temporary', payload),
  changePassword: (payload) => patch('/auth/password', payload)
}

export const usersApi = {
  me: () => get('/users/me'),
  updateMe: (payload) => patch('/users/me', payload),
  verifyPassword: (payload) => post('/users/me/password/verify', payload),
  campusSummary: () => get('/users/me/campus-summary'),
  students: (params) => get('/users/students', params),
  mentors: (params) => get('/mentors', params),
  mentor: (mentorId) => get(`/mentors/${mentorId}`)
}

export const boardsApi = {
  boards: () => get('/boards'),
  categories: (boardCode) => get(`/boards/${boardCode}/categories`),
  posts: (boardCode, params) => get(`/boards/${boardCode}/posts`, params),
  post: (boardCode, postId) => get(`/boards/${boardCode}/posts/${postId}`),
  createPost: (boardCode, payload) => post(`/boards/${boardCode}/posts`, payload),
  updatePost: (boardCode, postId, payload) => patch(`/boards/${boardCode}/posts/${postId}`, payload),
  deletePost: (boardCode, postId) => remove(`/boards/${boardCode}/posts/${postId}`),
  comments: (boardCode, postId) => get(`/boards/${boardCode}/posts/${postId}/comments`),
  createComment: (boardCode, postId, payload) => post(`/boards/${boardCode}/posts/${postId}/comments`, payload)
}

export const classroomApi = {
  myCourses: () => get('/courses/my'),
  course: (courseId) => get(`/courses/${courseId}`),
  weeks: (courseId) => get(`/courses/${courseId}/weeks`),
  sessions: (courseId, weekId) => get(`/courses/${courseId}/weeks/${weekId}/sessions`),
  session: (sessionId) => get(`/course-sessions/${sessionId}`),
  replays: (params) => get('/course-sessions/replays', params)
}

export const learningApi = {
  categories: () => get('/learning/categories'),
  contents: (params) => get('/learning/contents', params),
  required: (params) => get('/learning/contents/required', params),
  openLearning: (params) => get('/learning/contents/open-learning', params),
  content: (contentId) => get(`/learning/contents/${contentId}`),
  saveProgress: (contentId, payload) => post(`/learning/contents/${contentId}/progress`, payload),
  complete: (contentId) => post(`/learning/contents/${contentId}/complete`),
  myProgress: (params) => get('/learning/progress/my', params),
  mySelected: (params) => get('/learning/contents/my-selected', params)
}

export const tasksApi = {
  my: (params) => get('/tasks/my', params),
  detail: (taskId) => get(`/tasks/${taskId}`),
  submit: (taskId, payload) => post(`/tasks/${taskId}/submit`, payload),
  myResult: (taskId) => get(`/tasks/${taskId}/my-result`)
}

export const attendanceApi = {
  my: (params) => get('/attendance/my', params),
  detail: (attendanceRecordId) => get(`/attendance/my/${attendanceRecordId}`),
  appeal: (payload) => post('/attendance/appeals', payload),
  myAppeals: (params) => get('/attendance/appeals/my', params),
  calendar: (params) => get('/education-calendar', params)
}

export const pointsApi = {
  summary: () => get('/points/my/summary'),
  transactions: (params) => get('/points/my/transactions', params)
}

export const bookmarksApi = {
  my: (params) => get('/bookmarks/my', params),
  add: (payload) => post('/bookmarks', payload),
  remove: (payload) => remove('/bookmarks', payload)
}

export const surveysApi = {
  categories: () => get('/survey-categories'),
  list: (params) => get('/surveys', params),
  detail: (surveyId) => get(`/surveys/${surveyId}`),
  submit: (surveyId, payload) => post(`/surveys/${surveyId}/submit`, payload),
  myParticipations: (params) => get('/surveys/my-participations', params),
  cancel: (surveyId) => post(`/surveys/${surveyId}/cancel`)
}

export const inquiriesApi = {
  my: (params) => get('/inquiries/my', params),
  detail: (inquiryId) => get(`/inquiries/${inquiryId}`),
  create: (payload) => post('/inquiries', payload),
  update: (inquiryId, payload) => patch(`/inquiries/${inquiryId}`, payload),
  delete: (inquiryId) => remove(`/inquiries/${inquiryId}`)
}

export const notificationsApi = {
  my: (params) => get('/notifications/my', params),
  unreadCount: () => get('/notifications/my/unread-count'),
  markRead: (notificationId) => patch(`/notifications/${notificationId}/read`),
  markAllRead: () => patch('/notifications/my/read-all'),
  delete: (notificationId) => remove(`/notifications/${notificationId}`)
}

export const agreementsApi = {
  list: () => get('/agreements'),
  detail: (agreementId) => get(`/agreements/${agreementId}`),
  agree: (agreementId) => post(`/agreements/${agreementId}/agree`),
  my: () => get('/agreements/my')
}
