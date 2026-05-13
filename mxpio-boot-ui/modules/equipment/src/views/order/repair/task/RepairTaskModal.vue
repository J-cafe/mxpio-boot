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
    addEqpRepairTaskApi,
    editEqpRepairTaskApi,
    adminEditEqpRepairTaskApi,
    faulttypeList,
  } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  // import { dateUtil } from '@mxpio/utils';
  import type { FormSchema } from '@mxpio/components';
  import { ref } from 'vue';
  import { useUserStore } from '@mxpio/stores';

  defineOptions({ name: 'EqpRepairTaskModale' });

  const emit = defineEmits(['success', 'register']);
  const isAdmin = ref(false);
  const userStore = useUserStore();
  const { username, phone } = userStore.getUserInfo || {};
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
              targetName: rows?.eqpName || '',
              targetCode: rows?.eqpCode || '',
              address: rows?.address || '',
              eqpTypeId: rows?.eqpTypeId || '',
              codeAbc: rows?.codeAbc || '',
              useDeptId: rows?.useDeptId || '',
              repairPersonGroupCode: rows?.repairPersonGroupCode || '',
              engineerPersonGroupCode: rows?.engineerPersonGroupCode || '',
              processCode: rows?.processCode || '',
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
      field: 'bizType',
      label: '工单类型',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_REPAIR_BIZ_TYPE',
      },
      colProps: {
        span: 8,
      },
      required: true,
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
      field: 'repairPersonGroupCode',
      label: '维修班组',
      component: 'EqpGroupSelect',
      componentProps: {
        filters: {
          'groupCategory@EQ': 'WX',
        },
      },
      colProps: {
        span: 8,
      },
      ifShow: false,
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
      ifShow: false,
    },
    {
      field: 'applyHalt',
      label: '故障停机',
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
        span: 8,
      },
      required: true,
    },
    {
      field: 'failureDate',
      label: '故障日期',
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
    },
    {
      field: 'applyId',
      label: '报修人',
      component: 'UserByDeptSelect',
      componentProps: ({ formActionType }) => {
        return {
          multiple: false,
          onChange: (val: string, rows: Recordable) => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              chkPersonPhone: rows?.phone || '',
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
      field: 'chkPersonPhone',
      label: '联系电话',
      component: 'Input',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'existingProblems',
      label: '故障描述',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'isBottleneck',
      label: '是否瓶颈设备',
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
        span: 8,
      },
    },
    {
      field: 'attachFile',
      label: '附件',
      component: 'Upload',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pic',
      label: '故障图片',
      component: 'Upload',
      componentProps: {
        accept: ['image/*'],
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'divider-basic',
      component: 'Divider',
      label: '维修详情',
      colProps: {
        span: 24,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'repairHalt',
      label: '故障停机维修',
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
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'temporaryRepair',
      label: '临时维修',
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
          disabled:
            !isAdmin.value &&
            [40, 42, 45, 50, 90].includes(formDataRef.value.repairStatus) &&
            !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'planHalt',
      label: '计划停机',
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
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'faultPhenomenon',
      label: '故障现象',
      component: 'InputTextArea',
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'malfunctionReason',
      label: '故障原因',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'maitainSummary',
      label: '维修过程',
      component: 'InputTextArea',
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'repairHalt',
      label: '维修人',
      component: 'UserByDeptSelect',
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
          multiple: false,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'reportWorkingHours',
      label: '维修时长',
      component: 'InputNumber',
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
          addonAfter: '时',
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'faultTime',
      label: '停机时长',
      component: 'InputNumber',
      colProps: {
        span: 8,
      },
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
          addonAfter: '时',
        };
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'faultTypeCode',
      label: '故障分类',
      component: 'ApiSelect',
      componentProps: () => {
        return {
          api: faulttypeList,
          labelField: 'typeName',
          valueField: 'typeCode',
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'repairAttachFile',
      label: '维修附件',
      component: 'Upload',
      colProps: {
        span: 8,
      },
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
        };
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'repairPic',
      label: '维修图片',
      component: 'Upload',
      componentProps: () => {
        return {
          disabled: !isAdmin.value && !isDisabled.value,
          accept: ['image/*'],
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
    {
      field: 'followRepair',
      label: '跟进维修内容',
      component: 'InputTextArea',
      componentProps: () => {
        return {
          disabled: !isAdmin.value || !isDisabled.value,
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: () => {
        return isUpdate.value && formDataRef.value?.temporaryRepair === 1;
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, isUpdate, handleSubmit, formDataRef } =
    useModalFormCrud({
      title: '维修单',
      formSchema,
      addApi: addEqpRepairTaskApi,
      editApi: (params: Recordable) =>
        isAdmin.value ? adminEditEqpRepairTaskApi(params) : editEqpRepairTaskApi(params),
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data) => {
        const { isAdmin: isAdminData } = data;
        isAdmin.value = isAdminData;
      },
      defaultValues: {
        bpmnStatus: '01',
        orderStatus: '1',
        closeStatus: 'open',
        applyId: username,
        applyHalt: '1',
        isBottleneck: '0',
        chkPersonPhone: phone,
      },
    });
</script>
