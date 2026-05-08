<script setup>
import StatusCircle from "../ui/StatusCircle.vue";

defineProps({
  item: {
    type: Object,
    required: true
  }
});

function truncate(text) {
  return text.length > 24 ? `${text.slice(0, 24)}...` : text;
}
</script>

<template>
  <li>
    <a class="quest-link" href="#">
      <StatusCircle :status="item.status" />
      <span>
        <span class="quest-type" :class="{ eval: item.type === '평가' }">{{ item.type }}</span>
        <span class="quest-title" :title="item.title">
          {{ truncate(item.title) }}
        </span>
      </span>
    </a>
  </li>
</template>
