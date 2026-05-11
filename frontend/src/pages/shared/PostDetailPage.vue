<template>
  <div>
    <PageHero :title="heroTitle" />
    <SectionTabs :items="tabs" aria-label="Section tabs" />
    <main class="page-container board-page narrow-page">
      <article class="article-detail">
        <p class="eyebrow-text">Detail</p>
        <h1>{{ post.title }}</h1>
        <dl class="article-meta">
          <div><dt>구분</dt><dd>{{ post.category }}</dd></div>
          <div><dt>작성자</dt><dd>{{ post.author }}</dd></div>
          <div><dt>등록일</dt><dd>{{ post.date }}</dd></div>
          <div><dt>조회</dt><dd>{{ post.views }}</dd></div>
        </dl>
        <div class="article-body">
          <p>{{ body }}</p>
          <p>이 화면은 실제 API 연동 전, 목록-상세 이동과 레이아웃 밀도를 검증하기 위한 mock detail입니다.</p>
        </div>
        <RouterLink class="outline-button blue" :to="backPath">목록</RouterLink>
      </article>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { communityTabs, mentoringTabs } from '../../constants/navigation'
import { anonymousPosts, openBoardPosts, mentoringPosts } from '../../data/boards'

const props = defineProps({
  source: {
    type: String,
    required: true
  }
})

const route = useRoute()

const sourceMap = {
  open: { hero: '커뮤니티', tabs: communityTabs, items: openBoardPosts, back: '/community/boards/open' },
  anonymous: { hero: '커뮤니티', tabs: communityTabs, items: anonymousPosts, back: '/community/boards/anonymous' },
  stories: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.stories, back: '/mentoring/stories' },
  qna: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.qna, back: '/mentoring/qna' },
  notice: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.notice, back: '/mentoring/notice' },
  reviews: { hero: '멘토링 게시판', tabs: mentoringTabs, items: mentoringPosts.reviews, back: '/mentoring/meetups/reviews' }
}

const current = computed(() => sourceMap[props.source] || sourceMap.open)
const post = computed(() => current.value.items.find((item) => String(item.id) === route.params.id) || current.value.items[0])
const heroTitle = computed(() => current.value.hero)
const tabs = computed(() => current.value.tabs)
const backPath = computed(() => current.value.back)
const body = computed(() => `${post.value.title}에 대한 상세 내용입니다. SSAFY EDU 화면 구조와 동일하게 제목, 작성자, 등록일, 본문 영역을 구분합니다.`)
</script>
