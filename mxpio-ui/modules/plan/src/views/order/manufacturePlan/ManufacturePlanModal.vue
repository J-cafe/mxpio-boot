<template>
  <BasicModal
    width="900px"
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
  import { pmSaveApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './manufacturePlan.data';

  defineOptions({ name: 'ManufacturePlanModal' });

  const emit = defineEmits(['success', 'register']);

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '制造计划',
    formSchema,
    saveApi: pmSaveApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
