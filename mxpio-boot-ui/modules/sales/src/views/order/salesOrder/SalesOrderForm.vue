<!-- 审核发起单据示例 -->
<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
  <a-tabs>
    <a-tab-pane key="detailTable" tab="销售明细" forceRender>
      <SalesDetailTable ref="tableRef" />
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts" setup>
  import { BasicForm } from '@mxpio/components';
  import { saveSalesOrder } from '@mxpio/bizcommon';
  import { useFormCrudHook } from '@mxpio/common';
  import { formSchema } from './salesOrder.data';
  import { dateUtil } from '@mxpio/utils';
  import SalesDetailTable from './SalesDetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'SalesOrderForm' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof SalesDetailTable>>();

  const { registerForm, isDisabled, setFormData, getFormData, submitForm } = useFormCrudHook({
    formSchema,
    saveApi: saveSalesOrder,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      orderDate: dateUtil().format('YYYY-MM-DD'),
      orderType: '01',
      bpmnStatus: '01',
      orderStatus: '10',
      closeStatus: 'open',
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

  defineExpose({
    setFormData,
    submitForm,
    getFormData,
  });
</script>
