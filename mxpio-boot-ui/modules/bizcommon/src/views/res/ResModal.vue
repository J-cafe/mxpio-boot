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
  import { BasicModal, BasicForm, FormSchema } from '@mxpio/components';
  import { addRes, editRes } from '../../api/common';
  import { useModalFormCrud } from '@mxpio/common';

  defineOptions({ name: 'ResModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'typeCode',
      label: '类别编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'typeName',
      label: '类别名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'typeClass',
      label: '类别实体',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'typeFlow',
      label: '单据流程',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'typePrefix',
      label: '单号前缀',
      component: 'Input',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'typeSize',
      label: '序列长度',
      required: true,
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'typeTemplate',
      label: '单据模板',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'importTemplate',
      label: '导入模板',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '',
    formSchema,
    addApi: addRes,
    editApi: editRes,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
