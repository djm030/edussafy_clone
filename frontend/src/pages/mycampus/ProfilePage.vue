<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Profile</p>
        <h1>회원정보 수정</h1>
        <p>교육생 기본 정보를 확인합니다.</p>
      </div>
      <FormTable :fields="fields" />
      <div class="form-actions"><button class="button-primary" type="button">저장</button></div>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { mycampusTabs } from '../../data/mycampus'

const fields = [
  { label: '이름', name: 'name', placeholder: '김싸피' },
  { label: '이메일', name: 'email', type: 'email', placeholder: 'ssafy@example.com' },
  { label: '전화번호', name: 'phone', placeholder: '010-0000-0000' },
  { label: '캠퍼스', name: 'campus', placeholder: '서울 6반' }
]
</script>
