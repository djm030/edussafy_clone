<script setup>
import DashboardCard from '../components/ui/DashboardCard.vue'
import StatusBadge from '../components/ui/StatusBadge.vue'
import { dashboardData } from '../data/dashboard'

const data = dashboardData

function formatCount(value) {
  return new Intl.NumberFormat('ko-KR').format(value)
}
</script>

<template>
  <section class="dashboard-page page-container">
    <div class="dashboard-heading">
      <div>
        <p class="eyebrow-text">SSAFY EDU Clone</p>
        <h1>{{ data.user.name }}의 학습 현황</h1>
      </div>
      <p>{{ data.user.generation }}기 {{ data.user.region }} {{ data.user.classNo }}반 · {{ data.pointSummary.levelName }}</p>
    </div>

    <div class="dashboard-top-grid">
      <DashboardCard class="attendance-card" title="출석 체크" action-label="상세" action-to="/mycampus/attendance">
        <div class="attendance-date">{{ data.attendanceSummary.date }} · {{ data.attendanceSummary.dayLabel }}</div>
        <div class="attendance-status-row">
          <strong>{{ data.attendanceSummary.message }}</strong>
          <StatusBadge tone="green">정상</StatusBadge>
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

      <DashboardCard title="장학 포인트" action-label="내역" action-to="/mycampus/level-points">
        <p class="metric-value">{{ formatCount(data.pointSummary.scholarshipPoint) }}</p>
        <p class="metric-caption">누적 포인트</p>
      </DashboardCard>

      <DashboardCard title="레벨 / EXP" action-label="학습" action-to="/mycampus/level-points">
        <p class="metric-value">Lv. {{ data.pointSummary.levelNo }}</p>
        <div class="progress-track"><span :style="{ width: `${Math.min(data.pointSummary.totalExp / 5, 100)}%` }"></span></div>
        <p class="metric-caption">{{ formatCount(data.pointSummary.totalExp) }} EXP</p>
      </DashboardCard>

      <DashboardCard title="알림" action-label="더보기" action-to="/notifications">
        <ul class="compact-list">
          <li v-for="item in data.notifications" :key="item.id">
            <span :class="['list-dot', { unread: !item.isRead }]"></span>
            <div>
              <strong>{{ item.title }}</strong>
              <small>{{ item.createdAt }}</small>
            </div>
          </li>
        </ul>
      </DashboardCard>
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
      <DashboardCard title="학습 자료" action-label="더보기" action-to="/classroom/resources">
        <ul class="resource-list">
          <li v-for="item in data.learningPreview" :key="item.id">
            <span class="resource-thumb">{{ item.contentType }}</span>
            <div>
              <strong>{{ item.title }}</strong>
              <small>{{ item.duration }} <span v-if="item.required">· 필수</span></small>
            </div>
          </li>
        </ul>
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
