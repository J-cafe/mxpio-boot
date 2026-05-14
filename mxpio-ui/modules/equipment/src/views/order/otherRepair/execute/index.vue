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
              label: '完成',
              onClick: handleFinish.bind(null, row),
              auth: authConfig.finish,
              ifShow: row.repairStatus === '20',
            },
            {
              label: '协助',
              onClick: handleAssist.bind(null, row),
              auth: authConfig.assist,
              ifShow: ['20', '25', '30'].includes(row.repairStatus),
            },
            {
              label: '领料',
              onClick: handlePicking.bind(null, row),
              auth: authConfig.picking,
              ifShow: row.repairStatus === '20' || row.repairStatus === '25',
            },
            {
              label: '退料',
              onClick: handleReturn.bind(null, row),
              auth: authConfig.return,
              ifShow: ['20', '25', '30'].includes(row.repairStatus),
            },
            {
              label: '上报',
              onClick: handleProgress.bind(null, row),
              auth: authConfig.progress,
              ifShow: row.repairStatus === '20' || row.repairStatus === '25',
            },
            {
              label: '关闭',
              onClick: handleClose.bind(null, row),
              auth: authConfig.close,
              ifShow: ['20', '25'].includes(row.repairStatus),
            },
          ]"
        />
      </template>
      <template #bpmnSortFlag="{ row }">
        <a-tag v-if="row.bpmnSortFlag === '0'" color="#2db7f5">普通</a-tag>
        <a-tag v-else color="red">紧急</a-tag>
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="维修操作历史">
        <RecordTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="领料记录">
        <PickingTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="3" tab="转派记录">
        <TransferTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="4" tab="执行进度">
        <ProgressTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="5" tab="协助人员">
        <AssistTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
    </a-tabs>
    <OtherRepairApplyModal width="80%" @register="registerModal" />
    <OtherRepairOutsourceModal
      width="600px"
      @register="registerOutsourceModal"
      @success="handleSuccess"
    />
    <OtherRepairResendModal
      width="600px"
      @register="registerResendModal"
      @success="handleSuccess"
    />
    <OtherRepairProgressModal
      width="600px"
      @register="registerProgressModal"
      @success="handleSuccess"
    />
    <OtherRepairCloseModal width="600px" @register="registerCloseModal" @success="handleSuccess" />
    <OtherRepairPickingModal
      width="80%"
      @register="registerPickingModal"
      @success="handleSuccess"
    />
    <OtherRepairReturnModal width="80%" @register="registerReturnModal" @success="handleSuccess" />
    <OtherRepairFinishModal
      width="600px"
      @register="registerFinishModal"
      @success="handleSuccess"
    />
    <OtherRepairAssistModal width="80%" @register="registerAssistModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Tag as ATag } from 'ant-design-vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { otherRepairPageApi, startOtherRepairApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { columns, searchFormSchema } from './execute.data';
  import OtherRepairApplyModal from '../apply/OtherRepairApplyModal.vue';
  import RecordTable from '../apply/RecordTable.vue';
  import ProgressTable from './ProgressTable.vue';
  import PickingTable from './PickingTable.vue';
  import TransferTable from './TransferTable.vue';
  import AssistTable from './AssistTable.vue';
  import OtherRepairOutsourceModal from '../outsource/OtherRepairOutsourceModal.vue';
  import OtherRepairResendModal from './OtherRepairResendModal.vue';
  import OtherRepairProgressModal from './OtherRepairProgressModal.vue';
  import OtherRepairCloseModal from './OtherRepairCloseModal.vue';
  import OtherRepairPickingModal from './OtherRepairPickingModal.vue';
  import OtherRepairReturnModal from './OtherRepairReturnModal.vue';
  import OtherRepairFinishModal from './OtherRepairFinishModal.vue';
  import OtherRepairAssistModal from './OtherRepairAssistModal.vue';

  const componentName = 'OtherRepairExecuteList';
  defineOptions({ name: componentName });

  const authConfig = {
    progress: `erp:${componentName}:progress`,
    close: `erp:${componentName}:close`,
    return: `erp:${componentName}:return`,
    picking: `erp:${componentName}:picking`,
    finish: `erp:${componentName}:finish`,
    assist: `erp:${componentName}:assist`,
    start: `erp:${componentName}:start`,
    outsource: `erp:${componentName}:outsource`,
    resend: `erp:${componentName}:resend`,
  };

  const currentRow = ref<Recordable>({});
  const [registerOutsourceModal, { openModal: openOutsourceModal }] = useModal();
  const [registerResendModal, { openModal: openResendModal }] = useModal();
  const [registerProgressModal, { openModal: openProgressModal }] = useModal();
  const [registerCloseModal, { openModal: openCloseModal }] = useModal();
  const [registerPickingModal, { openModal: openPickingModal }] = useModal();
  const [registerReturnModal, { openModal: openReturnModal }] = useModal();
  const [registerFinishModal, { openModal: openFinishModal }] = useModal();
  const [registerAssistModal, { openModal: openAssistModal }] = useModal();
  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
  const { tableRef, gridOptions, handleDetail, registerModal, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: otherRepairPageApi,
    vxeGridOptions: {
      height: 450,
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
      toolbarConfig: {
        buttons: [
          {
            content: '开始',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:play-circle-outline',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleStart,
              },
            },
            visible: hasPermission(authConfig.start),
          },
          {
            content: '委外',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:file-transfer',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleOutsource,
              },
            },
            visible: hasPermission(authConfig.outsource),
          },
          {
            content: '转派',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:user-multiple',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleResend,
              },
            },
            visible: hasPermission(authConfig.resend),
          },
        ],
      },
    },
    filters: {
      'repairStatus@IN': '15,20,25,30,35',
    },
    module: 'erp',
  });

  async function handleStart() {
    try {
      if (!currentRow.value.bizNo) {
        createMessage.error('请选择维修订单');
        return;
      }
      if (currentRow.value.repairStatus !== '15') {
        createMessage.warning('只能选择待维修状态的单据!');
        return;
      }
      await startOtherRepairApi({ bizNo: currentRow.value.bizNo });
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  function handleOutsource() {
    if (!currentRow.value.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    if (currentRow.value.bizType !== '10') {
      createMessage.warning('只能委外内部维修单!');
      return false;
    }
    if (currentRow.value.repairStatus === '35' || currentRow.value.repairStatus === '30') {
      createMessage.warning('该任务状态，不能委外');
      return false;
    }
    try {
      openOutsourceModal(true, {
        record: currentRow.value,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handleResend() {
    if (!currentRow.value.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    if (!['15', '20'].includes(currentRow.value.repairStatus)) {
      createMessage.warning('只能转派待维修、维修中状态的单据');
      return false;
    }
    try {
      openResendModal(true, {
        record: currentRow.value,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handleProgress(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openProgressModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handleClose(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openCloseModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handlePicking(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openPickingModal(true, {
        bizNo: row.bizNo,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handleReturn(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openReturnModal(true, {
        bizNo: row.bizNo,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handleFinish(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openFinishModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function handleAssist(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openAssistModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setRadioRow(visibleData[0]);
  }
</script>
