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
  import { addEqpCheckPlanApi, editEqpCheckPlanApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { dateUtil } from '@mxpio/utils';

  defineOptions({ name: 'EqpCheckPlanModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'code',
      label: '计划编码',
      component: 'Input',
      colProps: {
        span: 8,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'name',
      label: '计划名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'planType',
      label: '计划类型',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_CHECK_TYPE',
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'startTime',
      label: '开始日期',
      component: 'DatePicker',
      componentProps: {
        class: '!w-full',
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'rate',
      label: '频率',
      component: 'InputNumber',
      required: true,
      rules: [
        // 调整为正则表达式验证，必须为正整数
        { pattern: /^[1-9]\d*$/, message: '请输入大于0的整数' },
      ],
      colProps: {
        span: 8,
      },
    },
    {
      field: 'rateUnit',
      label: '频率单位',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_CHECK_TASK_RATE_UNIT',
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'endLoopType',
      label: '结束类型',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_PLAN_END_LOOP_TYPE',
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'endTimes',
      label: '结束次数',
      component: 'InputNumber',
      required: true,
      colProps: {
        span: 8,
      },
      rules: [
        // 调整为正则表达式验证，必须为正整数
        { pattern: /^[1-9]\d*$/, message: '请输入大于0的整数' },
      ],
      ifShow: (formData) => {
        return formData.values?.endLoopType === 2 || formData.values?.endLoopType === '2';
      },
    },
    {
      field: 'endDate',
      label: '结束日期',
      component: 'DatePicker',
      componentProps: {
        class: '!w-full',
      },
      required: true,
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.endLoopType === 3 || formData.values?.endLoopType === '3';
      },
    },
    {
      field: 'isAuto',
      label: '是否自动下达',
      component: 'RadioButtonGroup',
      colProps: {
        span: 8,
      },
      componentProps: {
        options: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
      },
    },
    {
      field: 'advanceTime',
      label: '提前下达时间',
      component: 'InputNumber',
      helpMessage: ['任务自动提前下达，单位：时'],
      required: true,
      componentProps: {
        addonAfter: '时',
      },
      colProps: {
        span: 8,
      },
      rules: [
        // 调整为正则表达式验证，必须为正整数
        { pattern: /^[1-9]\d*$/, message: '请输入大于0的整数' },
      ],
      ifShow: (formData) => {
        return formData.values?.isAuto === 1 || formData.values?.isAuto === '1';
      },
      labelWidth: 130,
    },
    {
      field: 'taskPlanStartTime',
      label: '任务开始时间',
      component: 'TimePicker',
      componentProps: {
        class: '!w-full',
        valueFormat: 'HH:mm:ss',
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'failureHour',
      label: '失效时间',
      component: 'InputNumber',
      helpMessage: ['超出时长任务自动失效，单位：时'],
      required: true,
      componentProps: {
        addonAfter: '时',
      },
      rules: [
        // 调整为正则表达式验证，必须为正整数
        { pattern: /^[1-9]\d*$/, message: '请输入大于0的整数' },
      ],
      colProps: {
        span: 8,
      },
    },
    {
      field: 'isEnable',
      label: '生效状态',
      component: 'RadioButtonGroup',
      colProps: {
        span: 8,
      },
      componentProps: {
        options: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
        disabled: true,
      },
    },
    {
      field: 'memo',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '点检计划',
    formSchema,
    addApi: addEqpCheckPlanApi,
    editApi: editEqpCheckPlanApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      isEnable: 1,
      isAuto: 0,
      targetType: 'equipment',
      startTime: dateUtil().format('YYYY-MM-DD'),
    },
  });
</script>
