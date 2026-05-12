<script setup>
import { onMounted, ref } from 'vue'
import DashboardCard from '../components/ui/DashboardCard.vue'
import StatusBadge from '../components/ui/StatusBadge.vue'
import { dashboardData } from '../data/dashboard'
import { getApiErrorMessage, isApiEnabled } from '../api/client'
import { loadDashboardData } from '../services/dashboardService'

const data = ref(dashboardData)
const isLoading = ref(false)
const loadError = ref('')

function formatCount(value) {
  return new Intl.NumberFormat('ko-KR').format(value || 0)
}

onMounted(async () => {
  isLoading.value = true
  try {
    data.value = await loadDashboardData()
  } catch (error) {
    loadError.value = getApiErrorMessage(error, '대시보드 데이터를 불러오지 못했습니다.')
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <section class="dashboard-page page-container">
    <div class="dashboard-heading">
      <div>
        <p class="eyebrow-text">SSAFY EDU Clone</p>
        <h1>{{ data.user.name }}님의 학습 현황</h1>
      </div>
      <p>{{ data.user.generation }}기 {{ data.user.region }} {{ data.user.classNo }}반 · {{ data.pointSummary.levelName }}</p>
    </div>

    <p v-if="isLoading" class="dashboard-state">최신 대시보드 정보를 확인하고 있습니다.</p>
    <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <div class="dashboard-top-grid">
      <DashboardCard class="attendance-card" title="출석 체크" action-label="상세" action-to="/mycampus/attendance">
        <div class="attendance-date">{{ data.attendanceSummary.date }} · {{ data.attendanceSummary.dayLabel }}</div>
        <div class="attendance-status-row">
          <strong>{{ data.attendanceSummary.message }}</strong>
          <StatusBadge :tone="data.attendanceSummary.statusTone || 'slate'">{{ data.attendanceSummary.statusLabel }}</StatusBadge>
        </div>
        <dl class="attendance-times">
          <div>
            <dt>입실</dt>
            <dd>{{ data.attendanceSummary.checkInAt }}</dd>
          </div>
          <div>
            <dt>퇴실</dt>
            <dd>{{ data.attendanceSummary.checkOutAt }}</dd>
          </div>
        </dl>
      </DashboardCard>

      <section class="dashboard-blue-panel" aria-label="학습 요약">
        <div class="dashboard-metric-stack">
          <div class="dashboard-panel-header">
            <h2>나의 학습 현황</h2>
            <RouterLink to="/mycampus/level-points">내역</RouterLink>
          </div>
          <div class="dashboard-panel-metric">
            <span>장학 포인트</span>
            <strong>{{ formatCount(data.pointSummary.scholarshipPoint) }}</strong>
            <small>누적 포인트</small>
          </div>
          <div class="dashboard-panel-metric">
            <span>레벨 / EXP</span>
            <strong>Lv. {{ data.pointSummary.levelNo }}</strong>
            <div class="progress-track"><span :style="{ width: `${Math.min(data.pointSummary.totalExp / 5, 100)}%` }"></span></div>
            <small>{{ formatCount(data.pointSummary.totalExp) }} EXP</small>
          </div>
        </div>

        <div class="dashboard-panel-notices">
          <div class="dashboard-panel-header">
            <h2>알림</h2>
            <RouterLink to="/notifications">더보기</RouterLink>
          </div>
          <ul class="compact-list">
            <li v-for="item in data.notifications" :key="item.id">
              <span :class="['list-dot', { unread: !item.isRead }]"></span>
              <div>
                <strong>{{ item.title }}</strong>
                <small>{{ item.createdAt }}</small>
              </div>
            </li>
          </ul>
        </div>
      </section>
    </div>

    <div class="dashboard-content-grid">
      <DashboardCard title="이번 주 커리큘럼" action-label="강의실" action-to="/classroom/curriculum">
        <ol class="timeline-list">
          <li v-for="item in data.curriculumPreview" :key="item.id">
            <span>{{ item.weekNo }}주차</span>
            <strong>{{ item.title }}</strong>
            <small>{{ item.date }}</small>
          </li>
        </ol>
      </DashboardCard>

      <DashboardCard title="Quest / 평가" action-label="목록" action-to="/classroom/quests">
        <ul class="quest-list">
          <li v-for="item in data.questPreview" :key="item.id">
            <div>
              <strong>{{ item.title }}</strong>
              <small>{{ item.closeAt }} · {{ item.taskType }}</small>
            </div>
            <StatusBadge :tone="item.status === 'COMPLETED' ? 'blue' : 'slate'">
              {{ item.status === 'COMPLETED' ? '완료' : '예정' }}
            </StatusBadge>
          </li>
        </ul>
      </DashboardCard>
    </div>

    <div class="dashboard-resource-grid">
      <DashboardCard class="learning-card-wide" title="학습 자료" action-label="더보기" action-to="/classroom/resources">
        <div class="learning-card-list">
          <article v-for="item in data.learningPreview" :key="item.id" class="learning-preview-card">
            <span class="learning-preview-thumb">{{ item.contentType }}</span>
            <div class="learning-preview-body">
              <small>커리큘럼 &gt; 교재</small>
              <strong>{{ item.title }}</strong>
              <p>{{ item.duration }} <span v-if="item.required">· 필수</span></p>
            </div>
          </article>
        </div>
      </DashboardCard>

      <DashboardCard class="elearning-card" title="학습중 이러닝" action-label="MY CAMPUS" action-to="/mycampus/elearning">
        <div class="dashboard-empty-tile">
          <strong>학습 중인 E-Learning이 없습니다.</strong>
          <small>신규 교육이 배정되면 이 영역에 표시됩니다.</small>
        </div>
      </DashboardCard>

      <DashboardCard title="SSAFYcial" action-label="멘토링" action-to="/mentoring/stories">
        <table class="preview-table">
          <tbody>
            <tr v-for="post in data.storyPosts" :key="post.id">
              <td>{{ post.title }}</td>
              <td>{{ post.displayName }}</td>
              <td>{{ post.createdAt }}</td>
            </tr>
          </tbody>
        </table>
      </DashboardCard>

      <DashboardCard title="자유게시판" action-label="게시판" action-to="/community/boards/open">
        <table class="preview-table">
          <tbody>
            <tr v-for="post in data.freeBoardPosts" :key="post.id">
              <td>{{ post.title }}</td>
              <td>{{ post.displayName }}</td>
              <td>{{ post.createdAt }}</td>
            </tr>
          </tbody>
        </table>
      </DashboardCard>

      <DashboardCard title="공지사항" action-label="HELP DESK" action-to="/help/notice">
        <table class="preview-table">
          <tbody>
            <tr v-for="post in data.notices" :key="post.id">
              <td><StatusBadge v-if="post.isNotice" tone="blue">공지</StatusBadge> {{ post.title }}</td>
              <td>{{ post.displayName }}</td>
              <td>{{ post.createdAt }}</td>
            </tr>
          </tbody>
        </table>
      </DashboardCard>
    </div>
  </section>
</template>
