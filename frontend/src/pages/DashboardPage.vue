<script setup>
import { computed, onMounted, ref } from 'vue'
import DashboardCard from '../components/ui/DashboardCard.vue'
import { dashboardData } from '../data/dashboard'
import { getApiErrorMessage, isApiEnabled } from '../api/client'
import { loadDashboardData } from '../services/dashboardService'
import { checkInAttendanceData, checkOutAttendanceData } from '../services/mycampusService'

const emptyDashboardData = {
  ...dashboardData,
  notifications: [],
  curriculumPreview: [],
  questPreview: [],
  learningPreview: [],
  elearningPreview: [],
  storyPosts: [],
  freeBoardPosts: [],
  notices: []
}

const data = ref(isApiEnabled ? emptyDashboardData : dashboardData)
const isLoading = ref(isApiEnabled)
const isAttendanceSubmitting = ref(false)
const loadError = ref('')
const attendanceActionError = ref('')
const BOARD_PREVIEW_LIMIT = 4

function formatCount(value) {
  return new Intl.NumberFormat('ko-KR').format(value || 0)
}

function formatDashboardDate(value) {
  const [, month, day] = String(value || '').split('-')
  if (!month || !day) return { monthDay: '날짜 미정', weekday: '' }
  return { monthDay: `${month}. ${day}`, weekday: data.value.attendanceSummary.dayLabel || '' }
}

function refreshAttendanceSummary(todayAttendance) {
  data.value = {
    ...data.value,
    attendanceSummary: {
      ...data.value.attendanceSummary,
      ...todayAttendance
    }
  }
}

async function refreshDashboard() {
  isLoading.value = true
  loadError.value = ''
  try {
    data.value = await loadDashboardData()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '대시보드 데이터를 불러오지 못했습니다.')
  } finally {
    isLoading.value = false
  }
}

async function submitAttendanceAction(action) {
  if (isAttendanceSubmitting.value) return

  isAttendanceSubmitting.value = true
  attendanceActionError.value = ''
  try {
    const todayAttendance = action === 'checkIn'
      ? await checkInAttendanceData()
      : await checkOutAttendanceData()
    refreshAttendanceSummary(todayAttendance)
  } catch (error) {
    attendanceActionError.value = getApiErrorMessage(error, '출석 상태를 변경하지 못했습니다.')
  } finally {
    isAttendanceSubmitting.value = false
  }
}

const attendanceDate = computed(() => formatDashboardDate(data.value.attendanceSummary.date))
const curriculumItems = computed(() => data.value.curriculumPreview)
const questItems = computed(() => data.value.questPreview.slice(0, BOARD_PREVIEW_LIMIT))
const elearningItems = computed(() => data.value.elearningPreview || [])
const storyItems = computed(() => data.value.storyPosts.slice(0, BOARD_PREVIEW_LIMIT))
const freeBoardItems = computed(() => data.value.freeBoardPosts.slice(0, BOARD_PREVIEW_LIMIT))
const notice = computed(() => data.value.notices[0] || { title: '표시할 공지사항이 없습니다.', createdAt: '' })

function isSubmittedQuest(item) {
  return ['COMPLETED', 'SUBMITTED', 'GRADED', 'PASS', 'FAIL', 'EVALUATED'].includes(item.status)
}

onMounted(refreshDashboard)
</script>

<template>
  <section class="dashboard-page page-container">
    <p v-if="isLoading" class="dashboard-state">최신 대시보드 정보를 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <template v-if="!isLoading && !loadError">
    <div class="dashboard-top-grid">
      <DashboardCard class="attendance-card" title="출석체크 & 현황" action-label="＋" action-to="/mycampus/attendance">
        <div class="attendance-date">
          <span>{{ attendanceDate.monthDay }}</span>
          <strong>{{ attendanceDate.weekday }}</strong>
        </div>
        <div class="attendance-status-row">
          <strong>{{ data.attendanceSummary.message }}</strong>
          <small>{{ data.attendanceSummary.statusLabel }}</small>
        </div>
        <dl class="attendance-time-list">
          <div>
            <dt>입실</dt>
            <dd>{{ data.attendanceSummary.checkInAt || '-' }}</dd>
          </div>
          <div>
            <dt>퇴실</dt>
            <dd>{{ data.attendanceSummary.checkOutAt || '-' }}</dd>
          </div>
        </dl>
        <div class="attendance-action-row">
          <button
            type="button"
            :disabled="!data.attendanceSummary.canCheckIn || isAttendanceSubmitting"
            @click="submitAttendanceAction('checkIn')"
          >
            입실
          </button>
          <button
            type="button"
            :disabled="!data.attendanceSummary.canCheckOut || isAttendanceSubmitting"
            @click="submitAttendanceAction('checkOut')"
          >
            퇴실
          </button>
        </div>
        <p v-if="attendanceActionError" class="attendance-action-error">{{ attendanceActionError }}</p>
      </DashboardCard>

      <section class="dashboard-blue-panel" aria-label="학습 요약">
        <div class="dashboard-metric-stack">
          <div class="summary-line scholarship-line">
            <span>장학포인트</span>
            <strong>{{ formatCount(data.pointSummary.scholarshipPoint) }} P</strong>
          </div>
          <div class="summary-line level-line">
            <span>레벨&경험치</span>
            <strong>{{ formatCount(data.pointSummary.totalExp) }} EXP</strong>
          </div>
          <div class="level-progress-row">
            <div class="level-face" aria-hidden="true">☺</div>
            <div class="level-copy">
              <strong>{{ data.pointSummary.levelName }}</strong>
              <small>Lv.{{ data.pointSummary.levelNo }}</small>
            </div>
          </div>
          <div class="game-progress" aria-hidden="true">
            <span class="progress-badge progress-start">S</span>
            <div class="progress-track"><span :style="{ width: `${Math.min(data.pointSummary.totalExp / 18, 100)}%` }"></span></div>
            <span class="progress-badge progress-goal">G</span>
          </div>
        </div>

        <div class="dashboard-panel-notices">
          <h2>알림 <strong>{{ data.pointSummary.unreadNotificationCount || 0 }}</strong></h2>
          <ul v-if="data.notifications.length" class="notice-preview-list">
            <li v-for="item in data.notifications" :key="item.id">
              <span class="required-label">필독</span>
              <strong>{{ item.title }}</strong>
              <small>⌂ {{ item.createdAt }}</small>
            </li>
          </ul>
          <p v-else class="dashboard-panel-empty">표시할 알림이 없습니다.</p>
        </div>
      </section>
    </div>

    <div class="dashboard-content-grid">
      <DashboardCard class="dashboard-curriculum-card" title="주차별 커리큘럼" action-label="＋" action-to="/classroom/curriculum">
        <ol v-if="curriculumItems.length" class="timeline-list" tabindex="0" aria-label="주차별 커리큘럼 미리보기">
          <li v-for="item in curriculumItems" :key="item.id" class="timeline-day">
            <div class="timeline-date-dot" aria-hidden="true"></div>
            <div class="timeline-date-label">
              <strong>{{ item.dateLabel || item.date }}</strong>
            </div>
            <small class="timeline-time">{{ item.timeRange }}</small>
            <article class="timeline-session-card">
              <span class="session-category">{{ item.categoryName }}</span>
              <div>
                <small>{{ item.subject }}</small>
                <strong>{{ item.title }}</strong>
                <p>{{ item.location }}</p>
              </div>
              <RouterLink :to="`/classroom/resources?sessionId=${item.sessionId || item.id}&type=textbook`" class="session-material-button">교재</RouterLink>
            </article>
          </li>
        </ol>
        <p v-else class="dashboard-card-empty">표시할 커리큘럼이 없습니다.</p>
      </DashboardCard>

      <DashboardCard class="quest-card" title="Quest/평가" action-label="＋" action-to="/classroom/quests">
        <ul v-if="questItems.length" class="quest-list dashboard-quest-list">
          <li v-for="item in questItems" :key="item.id">
            <RouterLink :to="`/classroom/quests/${item.id}`" class="dashboard-quest-link">
              <span :class="['quest-status-circle', isSubmittedQuest(item) ? 'is-complete' : 'is-planned']">
                {{ item.statusLabel || (isSubmittedQuest(item) ? '제출' : '예정') }}
              </span>
              <div>
                <b>{{ item.taskType === 'QUEST' ? 'Quest' : '평가' }}</b>
                <strong>{{ item.title }}</strong>
              </div>
            </RouterLink>
          </li>
        </ul>
        <p v-else class="dashboard-card-empty">표시할 Quest/평가가 없습니다.</p>
      </DashboardCard>
    </div>

    <div class="dashboard-resource-grid">
      <DashboardCard class="learning-card-wide" title="학습자료" action-label="＋" action-to="/classroom/resources">
        <div v-if="data.learningPreview.length" class="learning-card-list">
          <RouterLink v-for="item in data.learningPreview" :key="item.id" class="learning-preview-card" :to="`/classroom/resources/${item.id}`">
            <span :class="['learning-preview-thumb', `learning-thumb-${item.coverVariant || item.contentType?.toLowerCase()}`]">
              <img v-if="item.coverImage" :src="item.coverImage" alt="" aria-hidden="true">
              <span v-else>{{ item.coverLabel || item.contentType }}</span>
              <i aria-hidden="true">▣</i>
            </span>
            <div class="learning-preview-body">
              <small>커리큘럼 &gt; 교재</small>
              <strong>{{ item.title }}</strong>
              <p>{{ item.description || item.duration }}</p>
              <div class="learning-card-meta">
                <span>◎ {{ formatCount(item.viewCount || 0) }}</span>
                <span>♡ {{ item.likeCount || 0 }}</span>
                <span>♡ {{ item.bookmarkCount || 0 }}</span>
              </div>
            </div>
          </RouterLink>
        </div>
        <p v-else class="dashboard-card-empty">표시할 학습자료가 없습니다.</p>
      </DashboardCard>

      <DashboardCard class="elearning-card" title="학습중 이러닝" action-label="＋" action-to="/mycampus/elearning">
        <div v-if="elearningItems.length" class="elearning-preview-list">
          <RouterLink v-for="item in elearningItems" :key="item.id" :to="`/classroom/resources/${item.id}`" class="elearning-preview-item">
            <strong>{{ item.title }}</strong>
            <span>{{ item.progress }}</span>
          </RouterLink>
        </div>
        <div v-else class="dashboard-empty-tile">
          <span class="empty-chair" aria-hidden="true">♙</span>
          <strong>학습중 이러닝이 없습니다</strong>
        </div>
      </DashboardCard>

      <DashboardCard class="community-preview-card" title="SSAFYcial" action-label="＋" action-to="/mentoring/stories">
        <ul class="text-preview-list with-icons">
          <li v-for="post in storyItems" :key="post.id">
            <RouterLink :to="`/mentoring/stories/${post.postId || post.id}`">
              <strong>{{ post.title }}</strong>
              <span><i aria-hidden="true">✎</i>{{ post.displayName }}</span>
            </RouterLink>
          </li>
        </ul>
      </DashboardCard>

      <DashboardCard class="community-preview-card" title="자유게시판" action-label="＋" action-to="/community/boards/open">
        <ul class="text-preview-list avatar-list">
          <li v-for="post in freeBoardItems" :key="post.id">
            <RouterLink :to="`/community/boards/open/${post.postId || post.id}`">
              <strong>{{ post.title }}</strong>
              <span><i aria-hidden="true"></i>{{ post.displayName }}</span>
            </RouterLink>
          </li>
        </ul>
      </DashboardCard>
    </div>

    <aside class="dashboard-notice-strip">
      <strong>공지사항</strong>
      <span>{{ notice.title }}</span>
      <time>{{ notice.createdAt }}</time>
      <RouterLink to="/403" class="dashboard-ebook-button">▣ e-book</RouterLink>
    </aside>
    </template>
  </section>
</template>
