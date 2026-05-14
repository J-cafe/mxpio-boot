<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="采购明细" forceRender>
        <DetailTable ref="tableRef" :formData="formDataRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { savePurchaseOrder } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './purchaseOrder.data';
  import { dateUtil } from '@mxpio/utils';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'PurchaseOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, formDataRef } =
    useModalFormCrud({
      title: '采购订单',
      formSchema,
      saveApi: savePurchaseOrder,
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
</script>
