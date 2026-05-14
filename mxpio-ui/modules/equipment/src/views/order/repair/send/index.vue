<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '派单',
              onClick: handleSend.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <UpkeepTaskModal width="80%" @register="registerModal" />
    <UpkeepTaskRejectModal width="600px" @register="registerRejectModal" @success="handleSuccess" />
    <EqpRepairTaskSendModal width="600px" @register="registerSendModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { eqpRepairTaskPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './send.data';
  import UpkeepTaskModal from '../task/RepairTaskModal.vue';
  import UpkeepTaskRejectModal from '../task/RepairTaskRejectModal.vue';
  import EqpRepairTaskSendModal from './EqpRepairTaskSendModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'EqpRepairTaskSendList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const [registerRejectModal, { openModal: openRejectModal }] = useModal();
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
        buttons: [
          {
            content: '否决',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:close-circle-outline',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: () => handleReject(),
              },
            },
          },
        ],
      },
    },
    filters: {
      'repairStatus@EQ': 10,
    },
    module: 'erp',
  });

  const handleSend = async (row) => {
    try {
      openSendModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  };

  function handleReject() {
    const selectedRows = tableRef.value?.getCheckboxRecords();
    if (!selectedRows || selectedRows.length === 0) {
      createMessage.error('请选择保养任务');
      return;
    }

    const bizNos = selectedRows.map((item) => item.bizNo);

    openRejectModal(true, {
      bizNo: bizNos.join(','),
    });
  }
</script>
