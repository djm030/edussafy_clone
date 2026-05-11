<template>
  <div>
    <PageHero title="마이캠퍼스" />
    <SectionTabs :items="mycampusTabs" aria-label="MyCampus sections" />
    <main class="page-container mycampus-page narrow-page">
      <div class="board-page-title">
        <p class="eyebrow-text">Profile</p>
        <h1>회원정보 수정</h1>
        <p>교육생 기본 정보를 확인하고 연락처를 수정합니다.</p>
      </div>

      <p v-if="isLoading" class="dashboard-state">회원 정보를 확인하고 있습니다.</p>
      <p v-else-if="message" :class="['dashboard-state', messageTone]">{{ message }}</p>

      <form @submit.prevent="saveProfile">
        <FormTable v-model="profile" :disabled="isSaving || isLoading" :fields="fields" />
        <div class="form-actions">
          <RouterLink class="outline-button blue" to="/mycampus/password">비밀번호 변경</RouterLink>
          <button class="button-primary" :disabled="isSaving || isLoading" type="submit">저장</button>
        </div>
      </form>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import FormTable from '../../components/ui/FormTable.vue'
import { mycampusTabs, profileInfo } from '../../data/mycampus'
import { loadProfileData, saveProfileData } from '../../services/mycampusService'

const profile = ref({ ...profileInfo })
const isLoading = ref(false)
const isSaving = ref(false)
const message = ref('')
const messageTone = ref('')

const fields = [
  { label: '이름', name: 'name', placeholder: '김싸피', readonly: true },
  { label: '이메일', name: 'email', type: 'email', placeholder: 'ssafy@example.com', readonly: true },
  { label: '전화번호', name: 'phoneNumber', placeholder: '010-0000-0000' },
  { label: '캠퍼스', name: 'campus', placeholder: '서울 6반', readonly: true }
]

onMounted(async () => {
  isLoading.value = true
  try {
    profile.value = await loadProfileData()
  } catch (error) {
    message.value = '회원 정보를 불러오지 못해 예시 데이터를 표시합니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isLoading.value = false
  }
})

async function saveProfile() {
  isSaving.value = true
  message.value = ''
  try {
    profile.value = await saveProfileData(profile.value)
    message.value = '회원 정보가 저장되었습니다.'
    messageTone.value = ''
  } catch (error) {
    message.value = '회원 정보를 저장하지 못했습니다.'
    messageTone.value = 'warning'
    console.warn(error)
  } finally {
    isSaving.value = false
  }
}
</script>