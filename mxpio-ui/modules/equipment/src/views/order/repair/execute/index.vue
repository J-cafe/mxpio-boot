<template>
  <div class="m-3 bg-white">
    <a-tabs
      class="px-6"
      v-model:activeKey="activeKey"
      :tabBarStyle="{ marginBottom: 0 }"
      @change="tabChange"
    >
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane key="seven" tab="近七日" />
    </a-tabs>
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
              ifShow: row.repairStatus === 30,
            },
            {
              label: '提交',
              onClick: handleExecute.bind(null, row),
              auth: authConfig.submit,
              ifShow: row.repairStatus === 31,
            },
            {
              label: '专家验收',
              onClick: handleRate.bind(null, row),
              auth: authConfig.rate,
              ifShow: row.repairStatus === 40,
            },
            {
              label: '暂停',
              onClick: handleStop.bind(null, row),
              auth: authConfig.stopStart,
              ifShow: row.repairStatus === 30,
            },
            {
              label: '恢复',
              onClick: handleEnd.bind(null, row),
              auth: authConfig.stopEnd,
              ifShow: row.repairStatus === 35,
            },
            {
              label: '变更',
              onClick: handleUpdate.bind(null, row),
              // auth: authConfig.stopEnd,
              ifShow: row.repairStatus === 30 || row.repairStatus === 31,
            },
            {
              label: '协助',
              onClick: handleAssist.bind(null, row),
              auth: authConfig.assist,
              ifShow: row.repairStatus === 30 || row.repairStatus === 31,
            },
            {
              label: '领料',
              onClick: handlePicking.bind(null, row),
              auth: authConfig.picking,
              ifShow: isPicking(row.repairStatus),
            },
            {
              label: '退料',
              onClick: handleReturn.bind(null, row),
              auth: authConfig.return,
              ifShow: isPicking(row.repairStatus),
            },
            {
              label: '上报',
              onClick: handleProgress.bind(null, row),
              auth: authConfig.progress,
              ifShow: row.repairStatus === 30 || row.repairStatus === 31,
            },
            {
              label: '管理员编辑',
              onClick: handleAdminEdit.bind(null, row),
              auth: authConfig.adminEdit,
              ifShow: row.repairStatus !== '50',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="操作历史">
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
      <a-tab-pane key="7" tab="评价">
        <RateTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="8" tab="重要备注">
        <RemarkTable :bizNo="currentRow.bizNo" />
      </a-tab-pane>
    </a-tabs>
    <RepairTaskModal width="80%" @register="registerModal" @success="handleSuccess" />
    <ExecuteModal width="80%" @register="registerExecuteModal" @success="handleSuccess" />
    <ResendModal width="600px" @register="registerResendModal" @success="handleSuccess" />
    <ProgressModal width="600px" @register="registerProgressModal" @success="handleSuccess" />
    <PickingModal width="80%" @register="registerPickingModal" @success="handleSuccess" />
    <ReturnModal width="80%" @register="registerReturnModal" @success="handleSuccess" />
    <AssistModal width="80%" @register="registerAssistModal" @success="handleSuccess" />
    <StopModal width="600px" @register="registerStopModal" @success="handleSuccess" />
    <RateModal width="800px" @register="registerRateModal" @success="handleSuccess" />
    <UpdateModal width="800px" @register="registerUpdateModal" @success="handleSuccess" />
    <UpkeepOutsourceModal
      width="600px"
      @register="registerOutsourceModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import { h, ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    eqpRepairTaskPageApi,
    startEqpRepairApi,
    endEqpRepairApi,
    finishEqpRepairApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { columns, searchFormSchema } from './execute.data';
  import RepairTaskModal from '../task/RepairTaskModal.vue';
  import RecordTable from '../task/RecordTable.vue';
  import ProgressTable from './ProgressTable.vue';
  import PickingTable from './PickingTable.vue';
  import TransferTable from './TransferTable.vue';
  import AssistTable from './AssistTable.vue';
  import RateTable from './RateTable.vue';
  import RemarkTable from './RemarkTable.vue';
  import UpkeepOutsourceModal from '../outsource/UpkeepOutsourceModal.vue';
  import ResendModal from './ResendModal.vue';
  import ProgressModal from './ProgressModal.vue';
  import PickingModal from './PickingModal.vue';
  import ReturnModal from './ReturnModal.vue';
  import AssistModal from './AssistModal.vue';
  import StopModal from './StopModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import RateModal from './RateModal.vue';
  import UpdateModal from './UpdateModal.vue';

  const componentName = 'EqpRepairExecuteList';
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
    submit: `erp:${componentName}:submit`,
    rate: `erp:${componentName}:rate`,
    stopStart: `erp:${componentName}:stopStart`,
    stopEnd: `erp:${componentName}:stopEnd`,
    adminEdit: `erp:${componentName}:adminEdit`,
  };
  const activeKey = ref('all');
  const currentRow = ref<Recordable>({});
  const [registerExecuteModal, { openModal: openExecuteModal }] = useModal();
  const [registerResendModal, { openModal: openResendModal }] = useModal();
  const [registerProgressModal, { openModal: openProgressModal }] = useModal();
  const [registerStopModal, { openModal: openStopModal }] = useModal();
  const [registerPickingModal, { openModal: openPickingModal }] = useModal();
  const [registerReturnModal, { openModal: openReturnModal }] = useModal();
  const [registerAssistModal, { openModal: openAssistModal }] = useModal();
  const [registerRateModal, { openModal: openRateModal }] = useModal();
  const [registerOutsourceModal, { openModal: openOutsourceModal }] = useModal();
  const [registerUpdateModal, { openModal: openUpdateModal }] = useModal();

  const { createMessage, createConfirm } = useMessage();
  const { hasPermission } = usePermission();
  const { tableRef, gridOptions, handleDetail, registerModal, openModal, handleSuccess } =
    useListCrudHook({
      componentName,
      columns,
      searchFormSchema,
      pageApi: eqpRepairTaskPageApi,
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
      filters: () => {
        return {
          'repairStatus@IN': '20,30,31,32,35,40,42,45,50,90',
          sevenDaysPastQuery: activeKey.value === 'seven' ? true : undefined,
        };
      },
      module: 'erp',
    });

  // 开始维修
  async function handleStart() {
    try {
      if (!currentRow.value.bizNo) {
        createMessage.error('请选择维修订单');
        return;
      }
      if (currentRow.value.repairStatus !== 20) {
        createMessage.warning('只能选择待维修状态的单据!');
        return;
      }
      await startEqpRepairApi({
        bizNo: currentRow.value.bizNo,
      });
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  // 委外维修
  function handleOutsource() {
    if (!currentRow.value.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    if (![10, 20, 30].includes(currentRow.value.repairStatus)) {
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

  // 转派维修
  function handleResend() {
    if (!currentRow.value.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    if (![20, 30, 35].includes(currentRow.value.repairStatus)) {
      createMessage.warning('该工单状态不能转派!');
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

  // 上报保养进度
  function handleProgress(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
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

  // 保养领料
  function handlePicking(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
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

  // 保养退料
  function handleReturn(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
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

  // 完成保养
  function handleFinish(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    createConfirm({
      title: '完成工单',
      content: () => h('div', { class: '!text-red' }, '待验收完成后请尽快进行维修汇报！！！'),
      iconType: 'warning',
      centered: false,
      onOk: async () => {
        try {
          await finishEqpRepairApi(row);
          handleSuccess();
        } catch (error) {
          console.log(error);
        }
      },
    });
  }

  // 保养填写协助
  function handleAssist(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
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

  // 保养暂停
  function handleStop(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
      return;
    }
    try {
      openStopModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  // 保养恢复执行
  async function handleEnd(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
      return;
    }
    try {
      await endEqpRepairApi({
        bizNo: row.bizNo,
      });
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  // 保养提交完整保养信息
  async function handleExecute(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
      return;
    }
    try {
      openExecuteModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  // 保养评价
  async function handleRate(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
      return;
    }
    try {
      openRateModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  // 管理员编辑
  async function handleAdminEdit(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修工单');
      return;
    }
    try {
      openModal(true, {
        record: row,
        isAdmin: true,
        isUpdate: true,
        disabled: false,
      });
    } catch (error) {
      console.log(error);
    }
  }

  // 保养变更
  function handleUpdate(row: Recordable) {
    if (!row.bizNo) {
      createMessage.error('请选择维修订单');
      return;
    }
    try {
      openUpdateModal(true, {
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
    tableRef.value?.setCurrentRow(visibleData[0]);
  }

  function tabChange() {
    tableRef.value?.commitProxy('query');
  }

  // 保养领料状态
  function isPicking(status: number) {
    return [30, 31, 32, 35, 40, 42, 45].includes(status);
  }
</script>
