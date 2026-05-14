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
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '同步更新',
              onClick: handleSync.bind(null, row),
              auth: authConfig.sync,
            },
            {
              label: '复制',
              onClick: handleCopy.bind(null, row),
              auth: auth.add,
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
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="设备档案">
        <EqpInfoAttrTable :eqpCode="currentRow.eqpCode" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="设备备件">
        <EqpInfoBomTable :eqpCode="currentRow.eqpCode" />
      </a-tab-pane>
    </a-tabs>
    <EqqpInfoModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    deleteEqpInfoApi,
    eqpInfoPageApi,
    resConfig,
    syncEqpInfoApi,
    allSyncEqpInfoApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './eqpInfo.data';
  import EqqpInfoModal from './EqpInfoModal.vue';
  import EqpInfoAttrTable from './EqpInfoAttrTable.vue';
  import EqpInfoBomTable from './EqpInfoBomTable.vue';

  const componentName = 'EqpInfoList';
  defineOptions({ name: componentName });

  const authConfig = {
    sync: `erp:${componentName}:sync`,
    allSync: `erp:${componentName}:allSync`,
  };

  const currentRow = ref<Recordable>({});

  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    openModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpInfoPageApi,
    deleteApi: deleteEqpInfoApi,
    configApi: () => resConfig('Equipment'),
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0',
      rowConfig: {
        keyField: 'eqpCode',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    module: 'erp',
    appendButtons: [
      {
        content: '批量同步更新',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'mdi:application-edit-outline',
          },
          attrs: {
            class: 'ml-2',
          },
          events: {
            click: () => handleAllSync(),
          },
        },
      },
    ],
  });

  // 同步更新设备
  const handleSync = (row: Recordable) => {
    syncEqpInfoApi(row.eqpCode);
  };

  // 批量同步更新设备
  const handleAllSync = () => {
    allSyncEqpInfoApi();
  };

  const handleCopy = (row: Recordable) => {
    openModal(true, {
      record: row,
      isCopy: true,
      isUpdate: false,
    });
  };

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }
</script>
