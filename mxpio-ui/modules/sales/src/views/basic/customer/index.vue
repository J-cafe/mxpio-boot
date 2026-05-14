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
    <CustomerModal @register="registerModal" @success="handleSuccess" />
    <AssignModal @register="registerAssignModal" @success="handleSuccess" />
    <TransferModal @register="registerTransferModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { deleteCustomer, customerPage, customerConfig } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './customer.data';
  import CustomerModal from './CustomerModal.vue';
  import AssignModal from './AssignModal.vue';
  import TransferModal from './TransferModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const { createMessage } = useMessage();
  const [registerAssignModal, { openModal: openAssignModal }] = useModal();
  const [registerTransferModal, { openModal: openTransferModal }] = useModal();

  const componentName = 'CustomerList';
  const authConfig = {
    add: 'erp:CustomerList:add',
    edit: 'erp:CustomerList:edit',
    delete: 'erp:CustomerList:delete',
    import: 'erp:CustomerList:import',
    export: 'erp:CustomerList:export',
  };
  defineOptions({ name: componentName });
  const {
    tableRef,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns,
    authConfig,
    searchFormSchema,
    pageApi: customerPage,
    deleteApi: deleteCustomer,
    configApi: customerConfig,
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
        content: '客户转移',
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

  function handleAssign() {
    const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (selectedRows.length === 0) {
      createMessage.warning('请选择客户');
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
