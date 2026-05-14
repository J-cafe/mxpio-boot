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
  import { addEqpInfoApi, editEqpInfoApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useCommon } from '@mxpio/hooks';
  import { unref } from 'vue';

  const { duplicateCheck } = useCommon();
  defineOptions({ name: 'FaulttypeModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'eqpCode',
      label: '设备编码',
      component: 'Input',
      colProps: {
        span: 8,
      },
      required: true,
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value || unref(isUpdate)) return true;
            return duplicateCheck({
              tableName: 'mb_erp_equipment',
              column: 'eqp_code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
      labelWidth: 130,
    },
    {
      field: 'eqpName',
      label: '设备名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'specType',
      label: '规格型号（简）',
      component: 'Input',
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'specTypeFull',
      label: '规格型号（全）',
      component: 'Input',
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'eqpClass',
      label: '设备类型',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_EQP_CLASS',
        };
      },
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'eqpTypeCode',
      label: '设备分类',
      component: 'EqpCategorySelect',
      required: true,
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'power',
      label: '设备功率',
      component: 'Input',
      componentProps: {
        suffix: 'W',
      },
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'repairPersonGroupCode',
      label: '维修群组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'WX',
        },
      },
      required: true,
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'checkPersonGroupCode',
      label: '点检群组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'DXJ',
        },
      },
      required: true,
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'engineerPersonGroupCode',
      label: '工程师组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'GCS',
        },
      },
      required: true,
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'isKey',
      label: '是否关键设备',
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
      labelWidth: 130,
    },
    {
      field: 'isSubOrdinate',
      label: '是否附属设备',
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
      labelWidth: 130,
    },
    {
      field: 'isCalibrate',
      label: '是否需要检定',
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
      required: true,
      colProps: {
        span: 8,
      },
      labelWidth: 130,
    },
    {
      field: 'calibrateCycle',
      label: '检定周期(天)',
      component: 'InputNumber',
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isCalibrate === 1;
      },
      rules: [
        {
          // 正则校验只能输入正整数
          pattern: /^[1-9]\d*$/,
          message: '请输入大于0的整数',
          trigger: 'blur',
        },
      ],
      required: true,
      labelWidth: 130,
    },
    {
      field: 'calibrateAdvance',
      label: '检定提前期(天)',
      component: 'InputNumber',
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isCalibrate === 1;
      },
      rules: [
        {
          // 正则校验只能输入正整数
          pattern: /^[1-9]\d*$/,
          message: '请输入大于0的整数',
          trigger: 'blur',
        },
      ],
      labelWidth: 130,
      required: true,
    },
    {
      field: 'inspectDutyUser',
      label: '送检负责人',
      component: 'UserByDeptSelect',
      colProps: {
        span: 8,
      },
      componentProps: {
        multiple: false,
      },
      ifShow: (formData) => {
        return formData.values?.isCalibrate === 1;
      },
      required: true,
      labelWidth: 130,
    },
  ];

  const {
    registerForm,
    setFieldsValue,
    registerModal,
    getTitle,
    isDisabled,
    handleSubmit,
    isUpdate,
  } = useModalFormCrud({
    title: '设备信息',
    formSchema,
    addApi: addEqpInfoApi,
    editApi: editEqpInfoApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      const { isCopy } = data;
      if (isCopy) {
        setFieldsValue({
          ...data.record,
          eqpCode: '',
        });
      }
    },
    defaultValues: {
      isKey: 0,
      isSubOrdinate: 0,
      isCalibrate: 0,
    },
  });
</script>
