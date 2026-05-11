<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Password</p>
        <h1>비밀번호 변경</h1>
        <p>계정 보안을 위해 새 비밀번호를 설정합니다.</p>
      </div>
      <FormTable :fields="fields" />
      <div class="form-actions"><button class="button-primary" type="button">변경</button></div>
    </main>
  </div>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { mycampusTabs } from '../../data/mycampus'

const fields = [
  { label: '현재 비밀번호', name: 'currentPassword', type: 'password', placeholder: '현재 비밀번호' },
  { label: '새 비밀번호', name: 'newPassword', type: 'password', placeholder: '새 비밀번호' },
  { label: '새 비밀번호 확인', name: 'confirmPassword', type: 'password', placeholder: '새 비밀번호 확인' }
]
</script>
