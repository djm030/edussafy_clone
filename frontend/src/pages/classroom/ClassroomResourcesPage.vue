<template>
  <PageHero title="학습자료" />
  <SectionTabs :items="classroomTabs" aria-label="Classroom navigation" />

  <section class="classroom-page page-container">
    <div class="resource-tabs"><button class="active" type="button">오프러닝</button></div>
    <div class="sub-tabs"><button class="active" type="button">전체</button><button type="button">커리큘럼 +</button></div>

    <div class="search-bar">
      <label><span>SEARCH</span><input type="search" placeholder="키워드 검색" /></label>
      <button type="button">검색</button>
    </div>

    <div class="list-toolbar">
      <strong>총 <span>{{ learningResources.length }}</span>건</strong>
      <div>
        <label class="check-label"><input type="checkbox" /> 교재</label>
        <select aria-label="Category"><option>전체</option></select>
        <select aria-label="Sort"><option>최신등록순</option></select>
      </div>
    </div>

    <div class="resource-results">
      <RouterLink v-for="item in learningResources" :key="item.id" class="resource-result-item" :to="`/classroom/resources/${item.id}`">
        <div class="resource-image"><span>{{ item.label }}</span><small>BOOK</small></div>
        <div class="resource-body">
          <h2>{{ item.title }} <span v-if="item.textbook">교재</span></h2>
          <p>카테고리 <strong>{{ item.breadcrumb.join(' > ') }}</strong></p>
          <p>{{ item.description }}</p>
          <ul>
            <li>VIEW {{ item.views }}</li>
            <li>LIKE {{ item.likes }}</li>
            <li>BOOKMARK {{ item.bookmarks }}</li>
          </ul>
        </div>
      </RouterLink>
    </div>
  </section>
</template>

<script setup>
import PageHero from '../../components/ui/PageHero.vue'
import SectionTabs from '../../components/ui/SectionTabs.vue'
import { classroomTabs } from '../../constants/navigation'
import { learningResources } from '../../data/classroom'
</script>
