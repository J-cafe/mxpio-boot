<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="编辑保养目标"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { editEqpUpkeepPlanTargetApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { targetFormSchema } from './upkeepPlan.data';

  defineOptions({ name: 'TargetEditModal' });
  const emit = defineEmits(['success', 'register']);

  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    title: '编辑保养目标',
    formSchema: targetFormSchema,
    editApi: editEqpUpkeepPlanTargetApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
