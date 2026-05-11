<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { getApiErrorMessage, isApiEnabled } from '../../api/client'
import { learningResources } from '../../data/classroom'
import { loadLearningResourceDetail } from '../../services/classroomService'

const route = useRoute()
const resource = ref(isApiEnabled ? null : learningResources[0])
const isLoading = ref(false)
const loadError = ref('')
const breadcrumb = computed(() => resource.value?.breadcrumb || [])

async function loadResource() {
  isLoading.value = true
  loadError.value = ''
  try {
    resource.value = await loadLearningResourceDetail(route.params.id)
  } catch (error) {
    if (isApiEnabled) {
      resource.value = null
      loadError.value = getApiErrorMessage(error, '학습자료 상세를 불러오지 못했습니다.')
    } else {
      resource.value = learningResources.find((item) => String(item.id) === String(route.params.id)) || learningResources[0]
      loadError.value = '학습자료 상세를 불러오지 못해 데모 데이터를 표시합니다.'
    }
    console.warn(error)
  } finally {
    isLoading.value = false
  }
}

onMounted(loadResource)
watch(() => route.params.id, loadResource)
</script>

<template>
  <PageHero title="학습자료" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <p v-if="isLoading" class="dashboard-state">학습자료 상세를 확인하고 있습니다.</p>
    <p v-if="loadError" class="dashboard-state warning">{{ loadError }}</p>

    <p v-if="!isLoading && !resource" class="dashboard-state">표시할 학습자료가 없습니다.</p>

    <article v-if="!isLoading && resource" class="detail-panel">
      <div class="detail-header">
        <div>
          <p class="eyebrow-text">{{ breadcrumb.join(' > ') }}</p>
          <h1>{{ resource.title }}</h1>
          <p>{{ resource.description }}</p>
        </div>
      </div>
      <div class="resource-detail-preview">{{ resource.label }}</div>
      <RouterLink class="button-primary" to="/classroom/resources">목록</RouterLink>
    </article>
  </section>
</template>
