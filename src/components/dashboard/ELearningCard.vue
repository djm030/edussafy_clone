<script setup>
import PlusLink from "../ui/PlusLink.vue";
import IconEmptyLearning from "../ui/IconEmptyLearning.vue";
</script>

<template>
  <aside aria-labelledby="elearning-title">
    <div class="elearning-heading">
      <PlusLink label="이러닝 더 보기" />
      <h2 id="elearning-title" class="bottom-title">학습중 이러닝</h2>
    </div>
    <div class="elearning-empty" role="status" aria-label="현재 학습중인 이러닝이 없습니다">
      <IconEmptyLearning />
    </div>
  </aside>
</template>
