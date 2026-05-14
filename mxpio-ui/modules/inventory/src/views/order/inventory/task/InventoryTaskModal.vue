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
      <a-tab-pane key="detailTable" tab="盘点明细" forceRender>
        <DetailTable ref="tableRef" :formDataRef="formDataRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { saveWo } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './inventoryTask.data';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'InventoryTaskModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, formDataRef } =
    useModalFormCrud({
      title: '库存盘点',
      formSchema,
      saveApi: saveWo,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      subTables: [
        {
          initSubData: () => {
            tableRef.value?.setData();
          },
          validate: () => {
            return tableRef.value?.validate() || Promise.resolve();
          },
          getSubData: () => {
            return {};
          },
        },
      ],
    });
</script>
