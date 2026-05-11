<template>
  <div>
    <PageHero title="커뮤니티" />
    <SectionTabs :items="communityTabs" aria-label="Community sections" />
    <main class="page-container board-page">
      <p v-if="isLoading" class="dashboard-state">교육생 목록을 확인하고 있습니다.</p>
      <p v-else-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

      <div class="board-page-title">
        <p class="eyebrow-text">Class Roster</p>
        <h1>우리반 보기</h1>
        <p>캠퍼스와 반 기준으로 교육생 정보를 확인합니다.</p>
      </div>
      <SearchFilterBar :options="['서울 6반', '서울 전체', '전국 전체']" placeholder="이름을 입력하세요." />
      <section class="roster-grid" aria-label="Class roster">
        <article v-for="member in classMembers" :key="member.id" class="roster-card">
          <div class="avatar-badge">{{ member.name.slice(0, 1) }}</div>
          <h2>{{ member.name }}</h2>
          <p>{{ member.track }}</p>
          <small>{{ member.campus }} {{ member.className }} · {{ member.role }}</small>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import SearchFilterBar from '../../components/ui/SearchFilterBar.vue'
import { communityTabs } from '../../constants/navigation'
import { classMembers as mockClassMembers } from '../../data/boards'
import { loadClassMembers } from '../../services/userListService'

const classMembers = ref(mockClassMembers)
const isLoading = ref(false)
const loadError = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    classMembers.value = await loadClassMembers()
  } catch (error) {
    loadError.value = '교육생 목록을 불러오지 못해 데모 데이터를 표시합니다.'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})
</script>
