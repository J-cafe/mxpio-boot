<template>
  <div class="m-3 bg-white">
    <a-tabs
      class="px-6"
      v-model:activeKey="activeKey"
      :tabBarStyle="{ marginBottom: 0 }"
      @change="tabChange"
    >
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane key="seven" tab="当天" />
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
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
              auth: auth.edit,
              ifShow: row.repairStatus === 10,
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '验收',
              onClick: handleConfirm.bind(null, row),
              auth: authConfig.confirm,
              ifShow: row.repairStatus === 32,
            },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              auth: auth.delete,
              ifShow: row.repairStatus === 10,
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="操作历史">
        <RecordTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="8" tab="重要备注">
        <RemarkTable :bizNo="currentRow.bizNo" />
      </a-tab-pane>
    </a-tabs>
    <RepairTaskModal width="80%" @register="registerModal" @success="handleSuccess" />
    <RepairTaskConfirmModal
      width="600px"
      @register="registerConfirmModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { eqpRepairTaskPageApi, deleteEqpRepairTaskApi, resConfig } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './repairTask.data';
  import RepairTaskModal from './RepairTaskModal.vue';
  import RepairTaskConfirmModal from './RepairTaskConfirmModal.vue';
  import RecordTable from './RecordTable.vue';
  import RemarkTable from '../execute/RemarkTable.vue';

  const componentName = 'EqpRepairTaskList';
  defineOptions({ name: componentName });

  const authConfig = {
    confirm: `erp:${componentName}:confirm`,
    reject: `erp:${componentName}:reject`,
  };

  const [registerConfirmModal, { openModal: openConfirmModal }] = useModal();
  const currentRow = ref<Recordable>({});
  const closeStatus = ref(['1']);
  const activeKey = ref('all');
  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpRepairTaskPageApi,
    deleteApi: deleteEqpRepairTaskApi,
    configApi: () => resConfig('CI1'),
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0 !py-0',
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    module: 'erp',
    appendButtons: [
      {
        buttonRender: {
          name: 'ACheckboxGroup',
          props: () => ({
            options: [
              {
                label: '过滤已否决',
                value: '1',
              },
            ],
            value: closeStatus, // 直接传 ref，自动双向绑定
          }),
          attrs: {
            class: 'ml-2',
          },
          events: {
            change: () => {
              tableRef.value?.commitProxy('query');
            },
          },
        },
      },
    ],
    filters: () => {
      return {
        sevenDaysPastQuery: activeKey.value === 'seven' ? true : undefined,
      };
    },
  });

  function tabChange() {
    tableRef.value?.commitProxy('query');
  }

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0] || {});
    tableRef.value?.setRadioRow(visibleData[0] || {});
  }

  function handleConfirm(row) {
    openConfirmModal(true, {
      bizNo: row.bizNo,
    });
  }
</script>
