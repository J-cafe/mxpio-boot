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
  import { addFaulttype, editFaulttype } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useCommon } from '@mxpio/hooks';
  import { unref } from 'vue';

  const { duplicateCheck } = useCommon();
  defineOptions({ name: 'FaulttypeModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'typeCode',
      label: '故障类型编码',
      component: 'Input',
      colProps: {
        span: 24,
      },
      required: true,
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value || unref(isUpdate)) return true;
            return duplicateCheck({
              tableName: 'mb_erp_equipment_fault_type',
              column: 'type_code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'typeName',
      label: '故障类型名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'memo',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
    },
  ];
  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '故障类型',
      formSchema,
      addApi: addFaulttype,
      editApi: editFaulttype,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
    });
</script>
