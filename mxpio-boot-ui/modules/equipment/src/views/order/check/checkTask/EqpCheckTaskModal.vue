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
  import { addEqpCheckTaskApi, editEqpCheckTaskApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { dateUtil } from '@mxpio/utils';
  import type { FormSchema } from '@mxpio/components';
  import { ref } from 'vue';

  defineOptions({ name: 'EqpCheckTaskModal' });

  const emit = defineEmits(['success', 'register']);
  const orderStatus = ref('');
  const formSchema: FormSchema[] = [
    {
      field: 'targetId',
      label: '资产编码',
      component: 'EqpBasicsSelect',
      componentProps: ({ formActionType }) => {
        return {
          multiple: false,
          filters: {
            'status@NOT_IN': '40,60',
          },
          onChange: (val: string, rows: Recordable) => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              processCode: rows?.processCode || '',
              targetName: rows?.eqpName || '',
              targetCode: rows?.eqpCode || '',
              // targetId: rows?.basicsCode || '',
              eqpTypeId: rows?.eqpTypeId || '',
              codeAbc: rows?.codeAbc || '',
              useDeptId: rows?.useDeptId || '',
              targetSpec: rows?.specType || '',
              checkPersonGroupCode: rows?.checkPersonGroupCode || '',
              parentTargetId: rows?.parentCode || '',
              parentTargetName: rows?.parentName || '',
            });
          },
        };
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'targetName',
      label: '资产名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentTargetId',
      label: '父级资产编码',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentTargetName',
      label: '父级资产名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'targetSpec',
      label: '规格型号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'useDeptId',
      label: '使用单位',
      component: 'DeptSelect',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'eqpTypeId',
      label: '设备分类',
      component: 'EqpCategorySelect',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'codeAbc',
      label: '设备等级',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_EQP_CODE_ABC',
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'checkPersonGroupCode',
      label: '点检群组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'DXJ',
        },
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'planStartTime',
      label: '计划开始时间',
      component: 'DatePicker',
      componentProps: ({ formActionType }) => {
        return {
          format: 'YYYY-MM-DD HH:mm:ss',
          class: '!w-full',
          showTime: true,
          onChange: (val: string) => {
            const { setFieldsValue, getFieldsValue } = formActionType;
            const { failureHour } = getFieldsValue();
            if (!failureHour || !val) {
              return;
            }
            setFieldsValue({
              expCutOffTime: dateUtil(val).add(Number(failureHour), 'h'),
            });
          },
        };
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
      componentProps: ({ formActionType }) => {
        return {
          addonAfter: '时',
          onChange: (val: string) => {
            const { setFieldsValue, getFieldsValue } = formActionType;
            const { planStartTime } = getFieldsValue();
            if (!planStartTime || !val) {
              return;
            }
            setFieldsValue({
              expCutOffTime: dateUtil(planStartTime).add(Number(val), 'h'),
            });
          },
        };
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
      field: 'expCutOffTime',
      label: '失效截至时间',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm:ss',
        disabled: true,
        class: '!w-full',
        showTime: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'isAutoRelease',
      label: '是否自动下达',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'planReleaseTime',
      label: '计划下达时间',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm:ss',
        class: '!w-full',
        showTime: true,
      },
      required: true,
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isAutoRelease === 1;
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
      rules: [
        // 调整为正则表达式验证，必须为正整数
        { pattern: /^[1-9]\d*$/, message: '请输入大于0的整数' },
      ],
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isAutoRelease === 1;
      },
    },
    {
      field: 'eqpStatus',
      label: '开关机状态',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
          options: [
            {
              label: '开机',
              value: '1',
            },
            {
              label: '未开机',
              value: '0',
            },
          ],
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return orderStatus.value === '50';
      },
    },
    {
      field: 'pic',
      label: '点检照片',
      component: 'Upload',
      colProps: {
        span: 8,
      },
      componentProps: {
        accept: ['image/*'],
      },
      ifShow: () => {
        return orderStatus.value === '50';
      },
    },
  ];

  const { registerForm, setFieldsValue, registerModal, getTitle, isDisabled, handleSubmit } =
    useModalFormCrud({
      title: '点检任务',
      formSchema,
      addApi: addEqpCheckTaskApi,
      editApi: editEqpCheckTaskApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data) => {
        const { isCopy } = data;
        orderStatus.value = data.record?.orderStatus || '';
        if (isCopy) {
          setFieldsValue({
            ...data.record,
            eqpCode: '',
          });
        }
      },
      defaultValues: {
        isAutoRelease: 1,
      },
    });
</script>
