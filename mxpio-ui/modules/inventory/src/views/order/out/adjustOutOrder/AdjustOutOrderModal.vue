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
    <BasicForm
      :disabled="isDisabled"
      @field-value-change="handleFormChange"
      @register="registerForm"
      ref="formInstance"
    />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="出库明细" forceRender>
        <DetailTable ref="tableRef" :formData="formDataRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, FormActionType } from '@mxpio/components';
  import { saveWo } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './adjustOutOrder.data';
  import { dateUtil } from '@mxpio/utils';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'AdjustOutOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const formInstance = ref<FormActionType>();
  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, formDataRef } =
    useModalFormCrud({
      title: '调整出库',
      formSchema,
      saveApi: saveWo,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        orderDate: dateUtil().format('YYYY-MM-DD'),
        woType: '100',
        accessType: 'out',
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

  function handleFormChange() {
    if (tableRef.value) {
      tableRef.value.clearLot();
    }
  }
</script>
