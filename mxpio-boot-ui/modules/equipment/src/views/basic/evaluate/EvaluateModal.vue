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
  import { addEvaluate, editEvaluate } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';

  defineOptions({ name: 'EvaluateModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'content',
      label: '评价内容',
      component: 'Input',
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'type',
      label: '评价类型',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'MB_ERP_EQUIPMENT_EVALUATE_TYPE',
      },
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'sortId',
      label: '排序值',
      component: 'InputNumber',
      colProps: {
        span: 24,
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '评价',
    formSchema,
    addApi: addEvaluate,
    editApi: editEvaluate,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
