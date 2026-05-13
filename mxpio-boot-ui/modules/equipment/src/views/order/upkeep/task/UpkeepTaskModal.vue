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
  import {
    addEqpUpkeepTaskApi,
    editEqpUpkeepTaskApi,
    adminEditEqpUpkeepTaskApi,
  } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { dateUtil } from '@mxpio/utils';
  import type { FormSchema } from '@mxpio/components';
  import { ref } from 'vue';

  defineOptions({ name: 'EqpUpkeepTaskModale' });

  const emit = defineEmits(['success', 'register']);
  const orderStatus = ref('');
  const isAdmin = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'bizNo',
      label: '单据编码',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
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
              eqpTypeId: rows?.eqpTypeId || '',
              codeAbc: rows?.codeAbc || '',
              useDeptId: rows?.useDeptId || '',
              targetSpec: rows?.specType || '',
              upkeepPersonGroupCode: rows?.upkeepPersonGroupCode || '',
              engineerPersonGroupCode: rows?.engineerPersonGroupCode || '',
              parentTargetCode: rows?.parentCode || '',
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
      field: 'parentTargetCode',
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
      field: 'upkeepPersonGroupCode',
      label: '保养班组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'BY',
        },
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'engineerPersonGroupCode',
      label: '工程师组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'BY',
        },
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'upkeepType',
      label: '保养类型',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_UPKEEP_TYPE',
      },
      colProps: {
        span: 8,
      },
      required: true,
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
            const { planHourRation } = getFieldsValue();
            if (!planHourRation || !val) {
              return;
            }
            setFieldsValue({
              planEndTime: dateUtil(val).add(Number(planHourRation), 'h'),
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
      field: 'planHourRation',
      label: '工时',
      component: 'InputNumber',
      colProps: {
        span: 8,
      },
      componentProps: ({ formActionType }) => {
        return {
          onChange: (val: string) => {
            const { setFieldsValue, getFieldsValue } = formActionType;
            const { planStartTime } = getFieldsValue();
            if (!planStartTime || !val) {
              return;
            }
            setFieldsValue({
              planEndTime: dateUtil(planStartTime).add(Number(val), 'h'),
            });
          },
        };
      },
      rules: [
        // 调整为正则表达式验证，必须为大于0的数字
        { pattern: /^(?!0(\.0*)?$)\d+(\.\d+)?$/, message: '请输入大于0的数字' },
        { required: true, message: '请输入工时' },
      ],
    },
    {
      field: 'planEndTime',
      label: '计划结束时间',
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
      field: 'advanceTime',
      label: '提前下达时间',
      component: 'InputNumber',
      helpMessage: ['任务自动提前下达，单位：时'],
      required: true,
      componentProps: {
        addonAfter: '时',
      },
      rules: [
        // 调整为正则表达式验证，必须为大于0的数字
        { pattern: /^(?!0(\.0*)?$)\d+(\.\d+)?$/, message: '请输入大于0的数字' },
      ],
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isAutoRelease === 1;
      },
    },
    {
      field: 'actualHourRation',
      label: '保养工时',
      component: 'InputNumber',
      componentProps: () => {
        return {};
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isAdmin.value || isDisabled.value;
      },
    },
    {
      field: 'attachFileBefore',
      label: '保养前照片',
      component: 'Upload',
      colProps: {
        span: 8,
      },
      componentProps: {
        accept: ['image/*'],
      },
      ifShow: () => {
        return isAdmin.value || isDisabled.value;
      },
    },
    {
      field: 'attachFile',
      label: '保养后照片',
      component: 'Upload',
      colProps: {
        span: 8,
      },
      componentProps: {
        accept: ['image/*'],
      },
      ifShow: () => {
        return isAdmin.value || isDisabled.value;
      },
    },
  ];

  const { registerForm, setFieldsValue, registerModal, getTitle, isDisabled, handleSubmit } =
    useModalFormCrud({
      title: '保养任务',
      formSchema,
      addApi: addEqpUpkeepTaskApi,
      editApi: (params: Recordable) =>
        isAdmin.value ? adminEditEqpUpkeepTaskApi(params) : editEqpUpkeepTaskApi(params),
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data) => {
        const { isCopy, isAdmin: isAdminData } = data;
        isAdmin.value = isAdminData;
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
