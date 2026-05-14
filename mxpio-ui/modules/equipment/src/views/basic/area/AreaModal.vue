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
  import { addAreaApi, editAreaApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useCommon } from '@mxpio/hooks';
  import { unref } from 'vue';

  const { duplicateCheck } = useCommon();
  defineOptions({ name: 'FaulttypeModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'pid',
      label: '父级节点',
      component: 'AreaSelect',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'areaCode',
      label: '区域编码',
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
              tableName: 'mb_erp_equipment_area',
              column: 'area_code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'areaName',
      label: '区域名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'factory',
      label: '厂区',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_FACTORY',
      },
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'remarks',
      label: '描述',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'isEnable',
      label: '是否启用',
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
        span: 24,
      },
    },
  ];
  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '区域',
      formSchema,
      addApi: addAreaApi,
      editApi: editAreaApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        isEnable: 1,
      },
    });
</script>
