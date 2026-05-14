<!-- 审核发起单据示例 -->
<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
  <a-tabs>
    <a-tab-pane key="detailTable" tab="销售明细" forceRender>
      <DetailTable ref="tableRef" />
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts" setup>
  import { BasicForm } from '@mxpio/components';
  import { saveSalesOrder, auditSalesOrder, abandonSalesOrder } from '@mxpio/bizcommon';
  import { useFormCrudHook } from '@mxpio/common';
  import { formSchema } from './salesOrder.data';
  import { dateUtil } from '@mxpio/utils';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'SalesOrderForm' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, isDisabled, setFormData, validate, getFormData, submitForm, formDataRef } =
    useFormCrudHook({
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

  // 示例：审批完成或弃审后，回写业务单状态为已审核或已终止
  const bpmnSubmitAfter = async ({ state }) => {
    try {
      if (state === 'COMPLETED') {
        // 审批完成，回写业务单状态为已审核，状态码为03
        const res = await auditSalesOrder(formDataRef.value.bizNo);
        return res || {};
      } else if (state === 'INTERNALLY_TERMINATED') {
        // 审批被驳回，回写业务单状态为已关闭，状态码为99，逻辑后续实现
        // const res = await abandonSalesOrder(formDataRef.value.bizNo);
        // return res || {};
      } else if (state === 'INTERNALLY_CANCEL') {
        // 审批被撤回，回写业务单状态为开立，状态码为01
        const res = await abandonSalesOrder(formDataRef.value.bizNo);
        return res || {};
      }
      return {};
    } catch (error) {
      return error;
    }
  };

  defineExpose({
    setFormData,
    submitForm,
    getFormData,
    validate,
    bpmnSubmitAfter,
  });
</script>
