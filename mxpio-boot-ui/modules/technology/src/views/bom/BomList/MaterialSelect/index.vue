<template>
  <a-input-search
    :placeholder="placeholder"
    :value="value"
    :disabled="disabled"
    readonly
    allowClear
    @search="showSelectModal"
  />
  <MaterialModal v-bind="$attrs" @register="registerModal" @success="handleSuccess" />
</template>

<script lang="ts" setup>
  import { useModal } from '@mxpio/components';
  import MaterialModal from './MaterialModal.vue';

  defineOptions({ name: 'MaterialCopySelect' });

  const emit = defineEmits(['update:value', 'change']);

  defineProps({
    value: { type: String },
    disabled: { type: Boolean, default: false },
    placeholder: { type: String, default: '选择物料' },
  });

  const [registerModal, { openModal }] = useModal();

  // 打开选择用户弹窗
  function showSelectModal() {
    openModal(true, {});
  }

  // 选择用户成功
  function handleSuccess(ids: string, rows: Recordable[]) {
    emit('update:value', ids, rows);
    emit('change', ids, rows);
  }
</script>
