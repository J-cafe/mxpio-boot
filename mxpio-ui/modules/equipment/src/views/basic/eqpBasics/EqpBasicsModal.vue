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
  import { addEqpBasicsApi, editEqpBasicsApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useCommon } from '@mxpio/hooks';
  import { unref } from 'vue';

  const { duplicateCheck } = useCommon();
  defineOptions({ name: 'FaulttypeModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'basicsCode',
      label: '资产编码',
      component: 'Input',
      colProps: {
        span: 6,
      },
      required: true,
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value || unref(isUpdate)) return true;
            return duplicateCheck({
              tableName: 'mb_erp_equipment_eqp_basics',
              column: 'eqp_code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
      componentProps: () => {
        return {
          disabled: unref(isUpdate),
        };
      },
    },
    {
      field: 'eqpName',
      label: '资产名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 6,
      },
    },
    {
      field: 'seqNumber',
      label: '设备序列号',
      component: 'Input',
      colProps: {
        span: 6,
      },
    },
    {
      field: 'codeAbc',
      label: '设备等级',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_EQP_CODE_ABC',
        };
      },
      required: true,
      colProps: {
        span: 6,
      },
    },
    {
      field: 'pnCode',
      label: '供应商',
      component: 'SupplySelect',
      componentProps: ({ formActionType }) => {
        return {
          multiple: false,
          onSelect: (pnCode: string, rows: Recordable) => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              pnName: rows?.pnName || '',
            });
          },
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'pnName',
      label: '供应商名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'manufacturer',
      label: '制造商',
      component: 'Input',
      colProps: {
        span: 6,
      },
    },
    {
      field: 'isRenewal',
      label: '支持批量更新',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
          options: [
            {
              label: '是',
              value: '1',
            },
            {
              label: '否',
              value: '0',
            },
          ],
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'divider-basic',
      component: 'Divider',
      label: '基础信息',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'eqpCode',
      label: '设备编码',
      component: 'EqpInfoSelect',
      componentProps: ({ formActionType }) => {
        return {
          multiple: false,
          onSelect: (pnCode: string, rows: Recordable) => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              eqpTypeId: rows?.eqpTypeCode || '',
              specType: rows?.specType || '',
              specTypeFull: rows?.specTypeFull || '',
              power: rows?.power || '',
              isCalibrate: rows?.isCalibrate || '',
              calibrateCycle: rows?.calibrateCycle || '',
              calibrateAdvance: rows?.calibrateAdvance || '',
              repairPersonGroupCode: rows?.repairPersonGroupCode || '',
              engineerPersonGroupCode: rows?.engineerPersonGroupCode || '',
              checkPersonGroupCode: rows?.checkPersonGroupCode || '',
              inspectDutyUser: rows?.inspectDutyUser || '',
              textMap: {
                eqpCode$DICT_TEXT_: rows?.eqpName || '',
              },
            });
          },
        };
      },
      required: true,
      colProps: {
        span: 6,
      },
    },
    {
      field: 'textMap.eqpCode$DICT_TEXT_',
      label: '设备名称',
      component: 'Input',
      colProps: {
        span: 6,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'specType',
      label: '规格型号（简）',
      component: 'Input',
      colProps: {
        span: 6,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'specTypeFull',
      label: '规格型号（全）',
      component: 'Input',
      colProps: {
        span: 6,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'eqpTypeId',
      label: '设备分类',
      component: 'EqpCategorySelect',
      colProps: {
        span: 6,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'power',
      label: '设备功率',
      component: 'Input',
      colProps: {
        span: 6,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'isSubOrdinate',
      label: '是否附属设备',
      component: 'RadioButtonGroup',
      componentProps: ({ formActionType }) => {
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
          onChange: (val) => {
            if (val === 0) {
              const { setFieldsValue } = formActionType;
              setFieldsValue({
                parentCode: '',
                parentName: '',
              });
            }
          },
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'parentCode',
      label: '父级设备',
      component: 'EqpBasicsSelect',
      componentProps: ({ formActionType }) => {
        return {
          multiple: false,
          onChange: (val: string, rows: Recordable) => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              parentName: rows?.eqpName || '',
            });
          },
        };
      },
      colProps: {
        span: 6,
      },
      ifShow: (formData) => {
        return formData.values?.isSubOrdinate === 1;
      },
      required: true,
    },
    {
      field: 'parentName',
      label: '父级资产名称',
      component: 'Input',
      colProps: {
        span: 6,
      },
      componentProps: {
        disabled: true,
      },
      ifShow: (formData) => {
        return formData.values?.isSubOrdinate === 1;
      },
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
          disabled: true,
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'calibrateCycle',
      label: '检定周期(天)',
      component: 'InputNumber',
      colProps: {
        span: 6,
      },
      ifShow: (formData) => {
        return formData.values?.isCalibrate === 1;
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'calibrateAdvance',
      label: '检定提前期(天)',
      component: 'InputNumber',
      colProps: {
        span: 6,
      },
      ifShow: (formData) => {
        return formData.values?.isCalibrate === 1;
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'lastCalibrateDate',
      label: '上次检定日期',
      component: 'DatePicker',
      colProps: {
        span: 6,
      },
      ifShow: (formData) => {
        return formData.values?.isCalibrate === 1;
      },
      required: true,
      componentProps: {
        class: '!w-full',
      },
    },
    {
      field: 'use-basic',
      component: 'Divider',
      label: '使用信息',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'useDeptId',
      label: '使用单位',
      component: 'DeptSelect',
      required: true,
      colProps: {
        span: 6,
      },
    },
    {
      field: 'productDate',
      label: '出厂日期',
      component: 'DatePicker',
      colProps: {
        span: 6,
      },
      componentProps: {
        class: '!w-full',
      },
    },
    {
      field: 'checkDate',
      label: '启用时间',
      component: 'DatePicker',
      colProps: {
        span: 6,
      },
      componentProps: {
        class: '!w-full',
      },
    },
    {
      field: 'installDate',
      label: '质保期',
      component: 'DatePicker',
      colProps: {
        span: 6,
      },
      componentProps: {
        class: '!w-full',
      },
    },
    {
      field: 'status',
      label: '设备状态',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_EQP_STATUS',
          disabled: unref(isUpdate),
        };
      },
      colProps: {
        span: 6,
      },
      required: true,
    },
    {
      field: 'layAddr',
      label: '安装地点',
      component: 'Input',
      colProps: {
        span: 6,
      },
    },
    {
      field: 'repair-basic',
      component: 'Divider',
      label: '维保信息',
      colProps: {
        span: 24,
      },
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
        span: 6,
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
      },
      required: true,
      colProps: {
        span: 6,
      },
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
        span: 6,
      },
    },
    {
      field: 'inspectDutyUser',
      label: '检定负责人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      required: true,
      colProps: {
        span: 6,
      },
    },
    {
      field: 'lastUpkeepDate',
      label: '上次保养日期',
      component: 'DatePicker',
      required: true,
      colProps: {
        span: 6,
      },
      componentProps: {
        class: '!w-full',
      },
    },
    {
      field: 'other-basic',
      component: 'Divider',
      label: '其他',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'remarks',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 6,
      },
    },
    {
      field: 'pic',
      label: '图片',
      component: 'Upload',
      componentProps: {
        accept: ['image/*'],
      },
      colProps: {
        span: 6,
      },
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
    addApi: addEqpBasicsApi,
    editApi: editEqpBasicsApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      const { isCopy } = data;
      if (isCopy) {
        setFieldsValue({
          ...data.record,
          basicsCode: '',
          seqNumber: '',
        });
      }
    },
    defaultValues: {
      isRenewal: '1',
      isSubOrdinate: 0,
    },
  });
</script>
