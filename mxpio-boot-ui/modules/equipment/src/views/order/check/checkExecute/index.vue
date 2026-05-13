<template>
  <div class="m-3 bg-white">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @current-change="onCurrentChange"
      @data-change="onDataChange"
    >
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '转单',
              onClick: handleReSend.bind(null, row),
              auth: authConfig.reSend,
              ifShow: ['30', '40'].includes(row.orderStatus),
            },
            {
              label: '执行',
              onClick: handleExecute.bind(null, row),
              auth: authConfig.execute,
              ifShow: ['40'].includes(row.orderStatus),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="点巡检任务明细">
        <DetailTable :bizNo="currentRow.bizNo" :checkTask="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="转单明细">
        <EqpCheckTaskTransferTable :bizNo="currentRow.bizNo" />
      </a-tab-pane>
    </a-tabs>
    <EqpCheckTaskModal width="80%" @register="registerModal" @success="handleSuccess" />
    <EqpCheckTaskReSendModal
      width="600px"
      @register="registerReSendModal"
      @success="handleSuccess"
    />
    <EqpCheckExecuteModal width="80%" @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { eqpCheckTaskPageApi, resConfig } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useMessage } from '@mxpio/hooks';
  import { columns, searchFormSchema } from './eqpCheckExecute.data';
  import EqpCheckTaskModal from '../checkTask/EqpCheckTaskModal.vue';
  import DetailTable from './DetailTable.vue';
  import EqpCheckTaskTransferTable from './EqpCheckTaskTransferTable.vue';
  import EqpCheckTaskReSendModal from './EqpCheckTaskReSendModal.vue';
  import EqpCheckExecuteModal from './EqpCheckExecuteModal.vue';

  const componentName = 'EqpCheckExecuteList';
  defineOptions({ name: componentName });

  const authConfig = {
    reSend: `erp:${componentName}:reSend`,
    execute: `erp:${componentName}:execute`,
  };

  const currentRow = ref<Recordable>({});
  const [registerReSendModal, { openModal: openReSendModal }] = useModal();
  const [registerExecuteModal, { openModal: openExecuteModal }] = useModal();
  const { createMessage } = useMessage();
  const { tableRef, gridOptions, registerModal, handleDetail, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpCheckTaskPageApi,
    configApi: () => resConfig('Equipment'),
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0',
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
      toolbarConfig: {
        buttons: [],
        import: false,
      },
    },
    module: 'erp',
    filters: {
      'orderStatus@IN': '30,40,50',
    },
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }

  function handleReSend(row) {
    if (!['30', '40'].includes(row.orderStatus)) {
      createMessage.error('该工单状态不能转派');
      return;
    }

    openReSendModal(true, {
      bizNo: row.bizNo,
    });
  }

  function handleExecute(row) {
    if (!['40'].includes(row.orderStatus)) {
      createMessage.error('该工单状态不能执行');
      return;
    }

    openExecuteModal(true, {
      record: row,
    });
  }
</script>
