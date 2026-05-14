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
  import { addEqpCheckCategoryApi, editEqpCheckCategoryApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useCommon } from '@mxpio/hooks';
  import { unref } from 'vue';

  const { duplicateCheck } = useCommon();
  defineOptions({ name: 'EqpCheckCategoryModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'code',
      label: '编码',
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
              tableName: 'mb_erp_equipment_check_category',
              column: 'code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'name',
      label: '名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'isEnable',
      label: '是否启用',
      component: 'RadioButtonGroup',
      colProps: {
        span: 24,
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
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '点检方案',
      formSchema,
      addApi: addEqpCheckCategoryApi,
      editApi: editEqpCheckCategoryApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        isEnable: 1,
      },
    });
</script>
