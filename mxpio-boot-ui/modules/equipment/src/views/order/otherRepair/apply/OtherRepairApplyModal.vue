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
  import { addOtherRepairApi, editOtherRepairApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useUserStore } from '@mxpio/stores';

  defineOptions({ name: 'OtherRepairApplyModal' });

  const emit = defineEmits(['success', 'register']);
  const userStore = useUserStore();
  const { username, dept } = userStore.getUserInfo || {};
  const formSchema: FormSchema[] = [
    {
      field: 'bizNo',
      label: '工单编码',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'bizType',
      label: '维修范围',
      component: 'DictSelect',
      componentProps: ({ formActionType }) => {
        return {
          dictCode: 'ERP_EQUIPMENT_OTHER_REPAIR_TYPE',
          onSelect: () => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              targetId: '',
              isOutsource: 0,
              outManager: '',
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
      field: 'targetId',
      label: '维修区域',
      component: 'AreaSelect',
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.bizType == 10 || formData.values?.bizType == 30;
      },
      required: true,
    },
    {
      field: 'targetId',
      label: '维修区域',
      component: 'Input',
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.bizType == 20;
      },
      required: true,
    },
    {
      field: 'applyId',
      label: '申请人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'applyDept',
      label: '申请部门',
      component: 'DeptSelect',
      componentProps: {
        multiple: false,
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'isOutsource',
      label: '是否委外',
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
      ifShow: (formData) => {
        return formData.values?.bizType == 10 || formData.values?.bizType == 30;
      },
    },
    {
      field: 'outManager',
      label: '委外负责人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isOutsource === 1;
      },
    },
    {
      field: 'reason',
      label: '委外原因',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.isOutsource === 1;
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
      field: 'existingProblems',
      label: '故障描述',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      required: true,
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
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value && formDataRef.value?.isOutsource === 0) {
              return Promise.reject('请上传故障图片');
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, formDataRef } =
    useModalFormCrud({
      title: '其他维修',
      formSchema,
      addApi: addOtherRepairApi,
      editApi: editOtherRepairApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        isOutsource: '0',
        applyId: username,
        applyDept: dept?.deptCode,
      },
    });
</script>
