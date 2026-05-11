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

      <p v-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <form @submit.prevent="changePassword">
        <FormTable v-model="passwords" :disabled="isSaving" :fields="fields" />
        <div class="form-actions"><button class="button-primary" :disabled="isSaving" type="submit">변경</button></div>
      </form>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { mycampusTabs } from '../../data/mycampus'
import { changePasswordData } from '../../services/mycampusService'

const passwords = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const isSaving = ref(false)
const message = ref('')
const messageTone = ref('')

const fields = [
  { label: '현재 비밀번호', name: 'currentPassword', type: 'password', placeholder: '현재 비밀번호' },
  { label: '새 비밀번호', name: 'newPassword', type: 'password', placeholder: '새 비밀번호' },
  { label: '새 비밀번호 확인', name: 'confirmPassword', type: 'password', placeholder: '새 비밀번호 확인' }
]

async function changePassword() {
  message.value = ''
  if (passwords.value.newPassword !== passwords.value.confirmPassword) {
    message.value = '새 비밀번호가 일치하지 않습니다.'
    messageTone.value = 'warning'
    return
  }

  isSaving.value = true
  try {
    await changePasswordData(passwords.value)
    passwords.value = { currentPassword: '', newPassword: '', confirmPassword: '' }
    message.value = '비밀번호가 변경되었습니다.'
    messageTone.value = ''
  } catch (error) {
    message.value = '비밀번호를 변경하지 못했습니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSaving.value = false
  }
}
</script>