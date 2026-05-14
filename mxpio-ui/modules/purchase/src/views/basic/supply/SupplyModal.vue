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
  import { addSupply, editSupply } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { unref } from 'vue';
  import { duplicateCheck } from '@mxpio/api';

  defineOptions({ name: 'SupplyModal' });

  const emit = defineEmits(['success', 'register']);

  const formSchema: FormSchema[] = [
    {
      field: 'pnCode',
      label: '供应商编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      labelWidth: 140,
      required: true,
      componentProps: () => {
        return {
          disabled: isUpdate.value,
        };
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              if (!value || unref(isUpdate)) return resolve();
              duplicateCheck({
                tableName: 'mb_erp_purc_supply',
                column: 'pn_code_',
                key: value,
                exclude: unref(isUpdate) ? value : '',
              })
                .then((res) => {
                  if (res === 1) {
                    return reject('编码已存在');
                  }
                  return resolve();
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'pnName',
      label: '供应商名称',
      component: 'Input',
      required: true,
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnAbbr',
      label: '简称',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'bizMan',
      label: '业务员',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      labelWidth: 140,
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnArea',
      label: '地区',
      component: 'DictSelect',
      labelWidth: 140,
      componentProps: () => {
        return {
          dictCode: 'ERP_COMMON_PARTNER_AREA',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnKey',
      label: '社会统一信用代码',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnContacts',
      label: '联系人',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnPhone',
      label: '手机',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnTel',
      label: '电话',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnFax',
      label: '传真',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnAddress',
      label: '公司地址',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnZipCode',
      label: '邮政编码',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 12,
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '供应商',
      formSchema,
      addApi: addSupply,
      editApi: editSupply,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        machinedFlag: '0',
      },
    });
</script>
