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
  import { addEqpUpkeepPlanDetailByCategoryApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { detailByCategoryFormSchema } from './upkeepPlan.data';

  defineOptions({ name: 'DetailByCategoryModal' });
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    planId: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '按类别新增',
    formSchema: detailByCategoryFormSchema,
    addApi: addEqpUpkeepPlanDetailByCategoryApi,
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
