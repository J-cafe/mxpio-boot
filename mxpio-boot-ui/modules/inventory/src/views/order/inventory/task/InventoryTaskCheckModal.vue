<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="库存盘点复核"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="盘点明细" forceRender>
        <CheckDetailTable ref="tableRef" :formDataRef="formDataRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { reviewInvTaskApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { checkFormSchema } from './inventoryTask.data';
  import CheckDetailTable from './CheckDetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'InventoryTaskCheckModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof CheckDetailTable>>();
  let formData: Recordable = {};
  const { registerForm, registerModal, isDisabled, handleSubmit, formDataRef } = useModalFormCrud({
    formSchema: checkFormSchema,
    saveApi: (data) => {
      return reviewInvTaskApi(formData.inventoryCode, data);
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      formData = data.record;
    },
    subTables: [
      {
        initSubData: () => {
          tableRef.value?.setData();
        },
        validate: () => {
          return Promise.resolve();
        },
        getSubData: () => {
          return {};
        },
      },
    ],
  });
</script>
