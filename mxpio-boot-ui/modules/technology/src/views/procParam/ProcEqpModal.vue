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
  import { saveProcEqp } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { ref, unref } from 'vue';
  import { CuryTypeEnum } from '@mxpio/enums';

  defineOptions({ name: 'ProcEqpModal' });

  const props = defineProps({
    prodrout: {
      type: Object,
      default: () => ({}),
    },
  });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'eqpCode',
      label: '设备编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'eqpName',
      label: '设备名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'eqpClass',
      label: '设备类型',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_EQP_CLASS',
          disabled: true,
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'eqpNum',
      label: '数量',
      component: 'InputNumber',
      required: true,
      colProps: {
        span: 12,
      },
    },
  ];
  const formData = ref({});
  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '工装夹具',
      formSchema,
      saveApi: saveProcEqp,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data) => {
        const { record } = data;
        formData.value = record || {};
      },
      classifyIntoFormData: (values) => {
        return [
          {
            ...formData.value,
            ...values,
            routProcId: props.prodrout.id,
            crudType: unref(isUpdate) ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
          },
        ];
      },
    });
</script>
