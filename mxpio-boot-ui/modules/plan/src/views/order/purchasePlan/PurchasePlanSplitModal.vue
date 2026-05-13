<template>
  <BasicModal
    width="900px"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="制造计划拆分"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { ppSplitApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';

  defineOptions({ name: 'PurchasePlanSplitModal' });

  const emit = defineEmits(['success', 'register']);
  const splitFormSchema: FormSchema[] = [
    {
      field: 'itemCode',
      label: '物料编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'itemName',
      label: '物料名称',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'itemSpec',
      label: '规格型号',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'unitCode',
      label: '单位',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_UNIT_CODE',
          disabled: true,
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'quantity',
      label: '数量',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        type: 'number',
        disabled: true,
      },
    },
    {
      field: 'splitQuantity',
      label: '拆分数量',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
      componentProps: {
        type: 'number',
      },
      rules: [
        {
          validator: (_, value) => {
            console.log('value', _);
            const { quantity } = getFieldsValue();
            if (value >= Number(quantity)) {
              return Promise.reject('不能大于等于' + quantity);
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'startDate',
      label: '开始日期',
      component: 'DatePicker',
      componentProps: () => {
        return {
          valueFormat: 'YYYY-MM-DD',
          class: '!w-full',
          disabled: true,
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'deliverDate',
      label: '交付日期',
      component: 'DatePicker',
      componentProps: () => {
        return {
          valueFormat: 'YYYY-MM-DD',
          class: '!w-full',
          disabled: true,
        };
      },
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
      componentProps: {
        disabled: true,
      },
    },
  ];

  const { registerForm, registerModal, isDisabled, handleSubmit, getFieldsValue } =
    useModalFormCrud({
      title: '制造计划拆分',
      formSchema: splitFormSchema,
      saveApi: (params) => ppSplitApi(params.planNo, params.splitQuantity),
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
    });
</script>
