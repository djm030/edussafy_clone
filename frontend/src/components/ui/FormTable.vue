<template>
  <div class="form-table">
    <label v-for="field in fields" :key="field.name" class="form-row">
      <span>{{ field.label }}</span>
      <textarea
        v-if="field.type === 'textarea' && isControlled"
        :disabled="disabled || field.disabled"
        :placeholder="field.placeholder"
        :readonly="field.readonly"
        :rows="field.rows || 6"
        :value="fieldValue(field.name)"
        @input="updateField(field.name, $event.target.value)"
      ></textarea>
      <textarea
        v-else-if="field.type === 'textarea'"
        :disabled="disabled || field.disabled"
        :placeholder="field.placeholder"
        :readonly="field.readonly"
        :rows="field.rows || 6"
        @input="updateField(field.name, $event.target.value)"
      ></textarea>
      <select
        v-else-if="field.type === 'select' && isControlled"
        :disabled="disabled || field.disabled"
        :value="fieldValue(field.name)"
        @change="updateField(field.name, $event.target.value)"
      >
        <option v-for="option in field.options" :key="optionValue(option)" :value="optionValue(option)">{{ optionLabel(option) }}</option>
      </select>
      <select
        v-else-if="field.type === 'select'"
        :disabled="disabled || field.disabled"
        @change="updateField(field.name, $event.target.value)"
      >
        <option v-for="option in field.options" :key="optionValue(option)" :value="optionValue(option)">{{ optionLabel(option) }}</option>
      </select>
      <input
        v-else-if="isControlled"
        :disabled="disabled || field.disabled"
        :placeholder="field.placeholder"
        :readonly="field.readonly"
        :type="field.type || 'text'"
        :value="field.type === 'file' ? undefined : fieldValue(field.name)"
        @input="updateInputField(field, $event)"
      />
      <input
        v-else
        :disabled="disabled || field.disabled"
        :placeholder="field.placeholder"
        :readonly="field.readonly"
        :type="field.type || 'text'"
        @input="updateInputField(field, $event)"
      />
    </label>
  </div>
</template>

<script setup>
import { computed, ref, useAttrs } from 'vue'

const props = defineProps({
  fields: {
    type: Array,
    required: true
  },
  modelValue: {
    type: Object,
    default: () => ({})
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])
const attrs = useAttrs()
const fallbackValues = ref({})

const isControlled = computed(() => Boolean(attrs['onUpdate:modelValue']) || Object.keys(props.modelValue).length > 0)

function fieldValue(name) {
  return props.modelValue[name] ?? ''
}

function optionLabel(option) {
  return typeof option === 'object' ? option.label : option
}

function optionValue(option) {
  return typeof option === 'object' ? option.value : option
}

function updateField(name, value) {
  if (!isControlled.value) {
    fallbackValues.value = {
      ...fallbackValues.value,
      [name]: value
    }
  }

  emit('update:modelValue', {
    ...(isControlled.value ? props.modelValue : fallbackValues.value),
    [name]: value
  })
}

function updateInputField(field, event) {
  if (field.type === 'file') {
    updateField(field.name, event.target.files?.[0] || null)
    return
  }

  updateField(field.name, event.target.value)
}
</script>
