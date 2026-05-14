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
  import { addEqpCategoryApi, editEqpCategoryApi, eqpCategoryTreeApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import type { FormSchema } from '@mxpio/components';
  import { useCommon } from '@mxpio/hooks';
  import { unref, ref } from 'vue';

  const { duplicateCheck } = useCommon();
  defineOptions({ name: 'EqpCategoryModal' });

  const emit = defineEmits(['success', 'register']);
  const formDataRef = ref<Recordable>({});
  const formSchema: FormSchema[] = [
    {
      field: 'pid',
      label: '父级节点',
      component: 'ApiTreeSelect',
      componentProps: {
        api: eqpCategoryTreeApi,
        labelField: 'name',
        valueField: 'id',
        resultField: 'list',
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              if (!value || !unref(isUpdate)) return resolve();
              if (formDataRef.value.id === value) return reject('不能选择自己');
              return resolve();
            });
          },
          trigger: 'blur',
        },
      ],
      colProps: {
        span: 24,
      },
    },
    {
      field: 'name',
      label: '分类名称',
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
              tableName: 'mb_erp_equipment_category',
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
      field: 'code',
      label: '分类编码',
      component: 'Input',
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
      title: '分类',
      formSchema,
      addApi: addEqpCategoryApi,
      editApi: editEqpCategoryApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        isEnable: 0,
      },
      openAfter: (data) => {
        formDataRef.value = data.record || {};
      },
    });
</script>
