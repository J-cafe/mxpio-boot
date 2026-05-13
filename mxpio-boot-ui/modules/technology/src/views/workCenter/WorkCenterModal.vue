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
  import { addWorkCenter, editWorkCenter } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';

  defineOptions({ name: 'WorkCenterModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'workCenterCode',
      label: '工作中心编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'workCenterName',
      label: '工作中心名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'workCenterShortName',
      label: '简称',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'workCenterType',
      label: '工作中心类型',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_WORK_CENTER_TYPE',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'dispatchMode',
      label: '派工模式',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_MES_DISPATCH_MODE',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'procMode',
      label: '加工模式',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_MES_PROC_MODE',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'workShopCode',
      label: '所属车间',
      component: 'WorkShopSelect',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'memo',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 12,
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '工作中心',
    formSchema,
    addApi: addWorkCenter,
    editApi: editWorkCenter,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
