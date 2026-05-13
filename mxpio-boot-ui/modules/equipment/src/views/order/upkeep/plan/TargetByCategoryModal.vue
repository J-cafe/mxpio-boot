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
  import { addEqpUpkeepPlanTargetByCategoryApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { targetByCategoryFormSchema } from './upkeepPlan.data';

  defineOptions({ name: 'TargetByCategoryModal' });
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    planId: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '按类别新增',
    formSchema: targetByCategoryFormSchema,
    addApi: addEqpUpkeepPlanTargetByCategoryApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    classifyIntoFormData: (formData) => {
      formData.planId = props.planId;
      return {
        ...formData,
      };
    },
  });
</script>
