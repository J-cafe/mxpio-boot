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
  import { ppSaveApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './purchasePlan.data';

  defineOptions({ name: 'PurchasePlanModal' });

  const emit = defineEmits(['success', 'register']);

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '采购计划',
    formSchema,
    saveApi: ppSaveApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
