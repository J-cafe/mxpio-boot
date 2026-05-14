<template>
  <div class="m-3">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @checkbox-change="checkboxChange"
      @checkbox-all="checkboxChange"
    >
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
              auth: authConfig.edit,
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              auth: authConfig.delete,
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <SupplyModal width="900px" @register="registerModal" @success="handleSuccess" />
    <SupplyAssignModal @register="registerAssignModal" @success="handleSuccess" />
    <SupplyTransferModal @register="registerTransferModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { deleteSupply, supplyPage, supplyConfig } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './supply.data';
  import SupplyModal from './SupplyModal.vue';
  import SupplyAssignModal from './SupplyAssignModal.vue';
  import SupplyTransferModal from './SupplyTransferModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const { createMessage } = useMessage();
  const [registerAssignModal, { openModal: openAssignModal }] = useModal();
  const [registerTransferModal, { openModal: openTransferModal }] = useModal();

  const componentName = 'SupplyList';
  const authConfig = {
    add: 'erp:SupplyList:add',
    edit: 'erp:SupplyList:edit',
    delete: 'erp:SupplyList:delete',
    import: 'erp:SupplyList:import',
    export: 'erp:SupplyList:export',
  };

  defineOptions({ name: componentName });
  const {
    tableRef,
    gridOptions,
    registerModal,
    checkboxChange,
    // handleAdd,
    handleEdit,
    handleDelete,
    // handleBatchDel,
    handleDetail,
    handleSuccess,
  } = useListCrudHook({
    componentName,
    columns,
    authConfig,
    searchFormSchema,
    pageApi: supplyPage,
    deleteApi: deleteSupply,
    configApi: supplyConfig,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'pnCode',
      },
    },
    appendButtons: [
      {
        content: '批量分配业务员',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'mdi:application-edit-outline',
          },
          events: {
            click: () => handleAssign(),
          },
        },
      },
      {
        content: '供应商转移',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'mdi:source-pull',
          },
          events: {
            click: () => handleTransfer(),
          },
        },
      },
    ],
  });

  // function checkboxChange() {
  //   const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
  //   selectedRowCount.value = selectedRows.length;
  // }

  function handleAssign() {
    const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (selectedRows.length === 0) {
      createMessage.warning('请选择供应商');
      return;
    }
    openAssignModal(true, {
      pnCodes: selectedRows.map((item) => item.pnCode),
    });
  }

  function handleTransfer() {
    openTransferModal(true);
  }

  // watch(selectedRowCount, () => {
  //   const deleteButton = gridOptions?.toolbarConfig?.buttons?.find((btn) => btn.content === '删除');
  //   if (deleteButton) {
  //     deleteButton.visible = hasPermission(authConfig.delete) && selectedRowCount.value > 0;
  //   }
  // });
</script>
