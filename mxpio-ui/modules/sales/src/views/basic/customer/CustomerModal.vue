<template>
  <BasicModal
    width="1200px"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="收货地址" forceRender>
        <AddrTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, FormSchema } from '@mxpio/components';
  import { saveCustomer } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { unref, ref } from 'vue';
  import { duplicateCheck } from '@mxpio/api';
  import AddrTable from './AddrTable.vue';

  defineOptions({ name: 'SupplyModal' });

  const tableRef = ref<InstanceType<typeof AddrTable>>();
  const emit = defineEmits(['success', 'register']);

  const formSchema: FormSchema[] = [
    {
      field: 'pnCode',
      label: '客户编码',
      component: 'Input',
      colProps: {
        span: 8,
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
                tableName: 'mb_erp_sales_customer',
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
      label: '客户名称',
      component: 'Input',
      required: true,
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnAbbr',
      label: '简称',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
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
        span: 8,
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
        span: 8,
      },
    },
    {
      field: 'pnKey',
      label: '社会统一信用代码',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnContacts',
      label: '联系人',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnPhone',
      label: '手机',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnTel',
      label: '电话',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnFax',
      label: '传真',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnAddress',
      label: '公司地址',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'pnZipCode',
      label: '邮政编码',
      component: 'Input',
      labelWidth: 140,
      colProps: {
        span: 8,
      },
    },
  ];

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '客户',
      formSchema,
      saveApi: saveCustomer,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      subTables: [
        {
          initSubData: (data: Recordable) => {
            tableRef.value?.setData(data);
          },
          validate: () => {
            return tableRef.value?.validate() || Promise.resolve();
          },
          getSubData: () => {
            return tableRef.value?.getData() || {};
          },
        },
      ],
    });
</script>
