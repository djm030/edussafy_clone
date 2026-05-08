<script setup>
defineProps({
  serviceLinks: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div class="service-links" aria-label="외부 서비스 바로가기">
    <a
      v-for="link in serviceLinks"
      :key="link.variant"
      class="service-link"
      :class="link.variant"
      :href="link.href"
    >
      <span>
        <span v-for="line in link.label.split('\n')" :key="line" class="service-line">
          {{ line }}
        </span>
      </span>
    </a>
  </div>
</template>
