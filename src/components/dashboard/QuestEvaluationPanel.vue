<script setup>
import PlusLink from "../ui/PlusLink.vue";
import QuestEvaluationItem from "./QuestEvaluationItem.vue";

defineProps({
  items: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <aside class="quest-section" aria-labelledby="quest-title">
    <div class="section-heading">
      <h2 id="quest-title">Quest/평가</h2>
      <PlusLink label="Quest와 평가 더 보기" />
    </div>

    <ul class="quest-list">
      <QuestEvaluationItem
        v-for="item in items"
        :key="`${item.status}-${item.type}-${item.title}`"
        :item="item"
      />
    </ul>
  </aside>
</template>
