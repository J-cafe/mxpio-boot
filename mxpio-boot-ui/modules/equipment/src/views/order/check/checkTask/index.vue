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
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
              auth: auth.edit,
              ifShow: row.orderStatus === '10',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '下达',
              onClick: handleRelease.bind(null, row),
              auth: authConfig.release,
              ifShow: row.orderStatus === '10',
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
              ifShow: row.orderStatus === '10',
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
    <EqpCheckTaskRejectModal
      width="600px"
      @register="registerRejectModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    eqpCheckTaskPageApi,
    deleteEqpCheckTaskApi,
    resConfig,
    releaseEqpCheckTaskApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { columns, searchFormSchema } from './eqpCheckTask.data';
  import EqpCheckTaskModal from './EqpCheckTaskModal.vue';
  import DetailTable from './DetailTable.vue';
  import EqpCheckTaskTransferTable from './EqpCheckTaskTransferTable.vue';
  import EqpCheckTaskRejectModal from './EqpCheckTaskRejectModal.vue';

  const componentName = 'EqpCheckTaskList';
  defineOptions({ name: componentName });

  const authConfig = {
    release: `erp:${componentName}:release`,
    reject: `erp:${componentName}:reject`,
  };

  const currentRow = ref<Recordable>({});
  const [registerRejectModal, { openModal: openRejectModal }] = useModal();
  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
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
    pageApi: eqpCheckTaskPageApi,
    deleteApi: deleteEqpCheckTaskApi,
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
    },
    module: 'erp',
    appendButtons: [
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
        visible: hasPermission(authConfig.reject),
      },
    ],
    filters: {
      'orderStatus@EQ': 10,
    },
  });

  // 同步更新设备
  const handleRelease = async (row: Recordable) => {
    try {
      await releaseEqpCheckTaskApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  };

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }

  function handleReject() {
    if (!currentRow.value.bizNo) {
      createMessage.error('请选择点检任务');
      return;
    }

    // if (currentRow.value.orderStatus !== '10') {
    //   createMessage.error('只能否决待下达点检任务');
    //   return;
    // }

    openRejectModal(true, {
      bizNo: currentRow.value.bizNo,
    });
  }
</script>
