<template>
  <div>
    <a-input-search
      :placeholder="placeholder"
      :value="value"
      :disabled="disabled"
      readonly
      allowClear
      @search="showSelectModal"
    />
    <CustomerModal v-bind="$attrs" @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
  import { useModal } from '@mxpio/components';
  import CustomerModal from './CustomerModal.vue';

  defineOptions({ name: 'CustomerSelect' });

  const emit = defineEmits(['update:value', 'change', 'select']);

  defineProps({
    value: { type: String },
    disabled: { type: Boolean, default: false },
    placeholder: { type: String, default: '选择客户' },
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
    // change方法会在表单赋值时触发，需要监听选择事件，请使用onSelect方法
    emit('select', ids, rows);
  }
</script>
