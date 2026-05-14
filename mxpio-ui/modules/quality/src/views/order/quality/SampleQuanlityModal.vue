<template>
  <BasicModal
    title="请填写检验数量"
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { qualityGenerateApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { sampleFormSchema } from './qualityOrder.data';

  defineOptions({ name: 'SampleQuanlityModal' });

  const emit = defineEmits(['success', 'register']);

  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    formSchema: sampleFormSchema,
    saveApi: (data) => {
      return qualityGenerateApi(data.bizNo, data.checkQuantity);
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
