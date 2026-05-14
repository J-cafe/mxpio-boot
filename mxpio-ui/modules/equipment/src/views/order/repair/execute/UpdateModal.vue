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
  import { updateEqpRepairApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { updateFormSchema } from './execute.data';
  import { ref } from 'vue';

  defineOptions({ name: 'EqpRepairUpdateModal' });
  const bizNo = ref('');
  const emit = defineEmits(['success', 'register']);

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '维修变更',
    formSchema: updateFormSchema,
    addApi: updateEqpRepairApi,
    editApi: updateEqpRepairApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      bizNo.value = data.record?.bizNo || '';
    },
    classifyIntoFormData: (formData) => {
      formData.bizNo = bizNo.value;
      return {
        ...formData,
      };
    },
  });
</script>
