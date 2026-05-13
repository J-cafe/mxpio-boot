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
              auth: auth.edit,
              ifShow: row.inventoryStatus === '01',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '盘点',
              onClick: handleExecute.bind(null, row),
              auth: authConfig.execute,
              ifShow: row.inventoryStatus === '02',
            },
            {
              label: '提交',
              onClick: handleCommit.bind(null, row),
              auth: authConfig.commit,
              ifShow: row.inventoryStatus === '01',
            },
            {
              label: '提交复核',
              onClick: handleSubmit.bind(null, row),
              auth: authConfig.submit,
              ifShow: row.inventoryStatus === '02',
            },
            {
              label: '复核',
              onClick: handleCheck.bind(null, row),
              auth: authConfig.check,
              ifShow: row.inventoryStatus === '03',
            },
            {
              label: '关闭',
              onClick: handleClose.bind(null, row),
              auth: authConfig.close,
              ifShow: row.inventoryStatus !== '99' && row.inventoryStatus !== '40',
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
              ifShow: row.inventoryStatus === '01',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <InventoryTaskModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
    <InventoryTaskCheckModal @register="registerCheckModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    deleteInvTaskApi,
    invTaskPageApi,
    invTaskConfigApi,
    submitInvTaskApi,
    commitInvTaskApi,
    closeInvTaskApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './inventoryTask.data';
  import InventoryTaskModal from './InventoryTaskModal.vue';
  import InventoryTaskCheckModal from './InventoryTaskCheckModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'InventoryTaskList';
  defineOptions({ name: componentName });
  const [registerExecuteModal, { openModal: openExecuteModal }] = useModal();
  const [registerCheckModal, { openModal: openCheckModal }] = useModal();

  const { createMessage } = useMessage();
  const authConfig = {
    submit: `erp:${componentName}:submit`,
    commit: `erp:${componentName}:commit`,
    check: `erp:${componentName}:check`,
    close: `erp:${componentName}:close`,
    execute: `erp:${componentName}:execute`,
  };
  const {
    tableRef,
    auth,
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
    searchFormSchema,
    pageApi: invTaskPageApi,
    deleteApi: deleteInvTaskApi,
    configApi: invTaskConfigApi,
    deleteBefore: deleteBefore,
    module: 'erp',
    vxeGridOptions: {
      rowConfig: {
        keyField: 'inventoryCode',
      },
      toolbarConfig: {
        buttons: [],
        import: false,
      },
    },
  });

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      const bizNos: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.inventoryStatus !== '01') {
          bizNos.push(row.inventoryCode);
        }
      });
      if (bizNos.length > 0) {
        createMessage.error(`单据:${bizNos.join(',')} 状态不允许删除`);
        return Promise.reject(`单据:${bizNos.join(',')} 状态不允许删除`);
      }
      return Promise.resolve();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleSubmit(row: Recordable) {
    try {
      await submitInvTaskApi(row.inventoryCode);
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleCommit(row: Recordable) {
    try {
      await commitInvTaskApi(row.inventoryCode);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleCheck(row: Recordable) {
    try {
      console.log(row);
      openCheckModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  async function handleClose(row: Recordable) {
    try {
      await closeInvTaskApi(row.inventoryCode);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleExecute(row: Recordable) {
    try {
      openExecuteModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }
</script>
