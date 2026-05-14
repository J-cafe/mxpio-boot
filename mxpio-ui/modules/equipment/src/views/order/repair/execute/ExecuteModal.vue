<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="填写维修信息"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { submitEqpRepairApi, faulttypeList } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';

  defineOptions({ name: 'EqpRepairExecuteModal' });
  const emit = defineEmits(['success', 'register']);
  const recordRef = ref<Recordable>({});
  const formSchema: FormSchema[] = [
    {
      field: 'repairHalt',
      label: '是否停机维修',
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
      defaultValue: '1',
      required: true,
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
        };
      },
      colProps: {
        span: 8,
      },
      defaultValue: '0',
      required: true,
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
        };
      },
      colProps: {
        span: 8,
      },
      defaultValue: '0',
      required: true,
    },
    {
      field: 'faultPhenomenon',
      label: '故障现象',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'malfunctionReason',
      label: '故障原因',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'maitainSummary',
      label: '维修过程',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'reportWorkingHours',
      label: '维修工时',
      component: 'InputNumber',
      componentProps: {
        className: 'w-full',
      },
      colProps: {
        span: 8,
      },
      required: true,
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
        };
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'repairAttachFile',
      label: '维修附件',
      component: 'Upload',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'repairPic',
      label: '维修图片',
      component: 'Upload',
      componentProps: () => {
        return {
          accept: ['image/*'],
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'followRepair',
      label: '跟进维修内容',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      required: true,
      ifShow: (formData) => {
        return formData.values?.temporaryRepair === '1';
      },
    },
    {
      field: 'partReplace',
      label: '备用件替换',
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
  ];

  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    title: '填写维修信息',
    formSchema,
    saveApi: submitEqpRepairApi,
    openAfter: (data) => {
      recordRef.value = data.record || {};
    },
    classifyIntoFormData: (values) => {
      return {
        bizNo: recordRef.value.bizNo,
        ...values,
      };
    },
    submitAfter: () => {
      emit('success');
    },
  });
</script>
