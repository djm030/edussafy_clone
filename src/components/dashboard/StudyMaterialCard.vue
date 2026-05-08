<script setup>
defineProps({
  material: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <a class="material-card" href="#" :aria-label="material.description">
    <div v-if="material.theme === 'blue'" class="material-art blue">
      <small>
        SAMSUNG
        <br />
        SW AI ACADEMY
        <br />
        FOR YOUTH
      </small>
      <span class="hex-label">
        삼성 청년
        <br />
        <strong>SW·AI아카데미</strong>
      </span>
    </div>

    <div v-else class="material-art cyan">
      <small>
        일타싸피
        <br />
        Enabling People
      </small>
      <div>
        <div class="iltaa-title">{{ material.title }}</div>
        <div class="iltaa-sub">{{ material.subtitle }}</div>
      </div>
    </div>
  </a>
</template>
