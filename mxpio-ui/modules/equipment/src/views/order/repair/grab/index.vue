<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" @data-change="onDataChange">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '抢单',
              onClick: handleGrab.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <RepairTaskModal width="80%" @register="registerModal" />
    <EqpRepairTaskSendModal width="600px" @register="registerSendModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { eqpRepairTaskPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './grab.data';
  import RepairTaskModal from '../task/RepairTaskModal.vue';
  import EqpRepairTaskSendModal from '../send/EqpRepairTaskSendModal.vue';

  const componentName = 'EqpRepairTaskGrabList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const [registerSendModal, { openModal: openSendModal }] = useModal();
  const { tableRef, gridOptions, handleDetail, registerModal, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpRepairTaskPageApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
      toolbarConfig: {
        buttons: [],
      },
    },
    filters: {
      'repairStatus@EQ': 10,
    },
    module: 'erp',
  });

  const handleGrab = async (row) => {
    try {
      openSendModal(true, {
        record: row,
        type: 'grab',
      });
    } catch (error) {
      console.log(error);
    }
  };

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setRadioRow(visibleData[0]);
  }
</script>
