<template>
  <table class="board-table">
    <thead>
      <tr>
        <th v-for="column in columns" :key="column.key" :style="{ width: column.width || undefined }">
          {{ column.label }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in items" :key="item.id">
        <td v-for="column in columns" :key="column.key" :class="column.className">
          <RouterLink v-if="column.key === titleKey && detailBase" :to="`${detailBase}/${item.id}`">
            {{ item[column.key] }}
          </RouterLink>
          <span v-else-if="column.key === 'status'" :class="['table-status', item.statusTone || 'slate']">
            {{ item[column.key] }}
          </span>
          <span v-else>{{ item[column.key] }}</span>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
defineProps({
  columns: {
    type: Array,
    required: true
  },
  items: {
    type: Array,
    required: true
  },
  detailBase: {
    type: String,
    default: ''
  },
  titleKey: {
    type: String,
    default: 'title'
  }
})
</script>
