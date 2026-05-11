<template>
  <div class="form-table">
    <label v-for="field in fields" :key="field.name" class="form-row">
      <span>{{ field.label }}</span>
      <textarea v-if="field.type === 'textarea'" :placeholder="field.placeholder" :rows="field.rows || 6"></textarea>
      <select v-else-if="field.type === 'select'">
        <option v-for="option in field.options" :key="option">{{ option }}</option>
      </select>
      <input v-else :type="field.type || 'text'" :placeholder="field.placeholder" />
    </label>
  </div>
</template>

<script setup>
defineProps({
  fields: {
    type: Array,
    required: true
  }
})
</script>
