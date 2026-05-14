<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { addWh, editWh } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './wareHouse.data';

  defineOptions({ name: 'WorkCenterModal' });

  const emit = defineEmits(['success', 'register']);

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '仓库',
    formSchema,
    addApi: addWh,
    editApi: editWh,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      whStatus: '1',
      settleStatus: '1',
      mrpStatus: '1',
    },
  });
</script>
