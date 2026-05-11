<template>
  <div>
    <PageHero title="HELP DESK" />
    <SectionTabs :items="helpTabs" aria-label="Help desk sections" />
    <main class="page-container board-page narrow-page">
      <article class="article-detail">
        <p class="eyebrow-text">Notice Detail</p>
        <h1>{{ notice.title }}</h1>
        <dl class="article-meta">
          <div><dt>구분</dt><dd>{{ notice.category }}</dd></div>
          <div><dt>작성자</dt><dd>{{ notice.author }}</dd></div>
          <div><dt>등록일</dt><dd>{{ notice.date }}</dd></div>
          <div><dt>조회</dt><dd>{{ notice.views }}</dd></div>
        </dl>
        <div class="article-body">
          <p>안녕하세요. SSAFY 교육 운영팀입니다.</p>
          <p>해당 공지는 교육 일정, 출결, 시스템 사용과 관련된 주요 안내를 담고 있습니다. 세부 내용은 캠퍼스 운영 기준과 EDU 시스템 알림을 함께 확인해 주세요.</p>
        </div>
        <RouterLink class="outline-button blue" to="/help/notice">목록</RouterLink>
      </article>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { helpTabs } from '../../constants/navigation'
import { notices } from '../../data/boards'

const route = useRoute()
const notice = computed(() => notices.find((item) => String(item.id) === route.params.id) || notices[0])
</script>
