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
          :actions="[]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '确认',
              onClick: handleExecute.bind(null, row),
              ifShow: row.noticeStatus === '10',
              auth: authConfig.execute,
            },
            {
              label: '拒绝',
              popConfirm: {
                title: '是否确认拒绝',
                placement: 'left',
                confirm: handleReject.bind(null, row),
              },
              ifShow: row.noticeStatus === '10',
              auth: authConfig.reject,
            },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <PurchaseReturnModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { purchaseReturnPage, purchaseReturnReject, rjLineList } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './purchaseReturn.data';
  import PurchaseReturnModal from './PurchaseReturnModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import ExpandTable from './ExpandTable.vue';

  const componentName = 'PurchaseReturnList';
  const authConfig = {
    add: `erp:${componentName}:add`,
    edit: `erp:${componentName}:edit`,
    delete: `erp:${componentName}:delete`,
    import: `erp:${componentName}:import`,
    export: `erp:${componentName}:export`,
    execute: `erp:${componentName}:execute`,
    reject: `erp:${componentName}:reject`,
  };
  defineOptions({ name: componentName });
  const [registerExecuteModal, { openModal }] = useModal();
  const { tableRef, gridOptions, registerModal, handleDetail, handleSuccess, checkboxChange } =
    useListCrudHook({
      componentName,
      columns,
      authConfig,
      searchFormSchema,
      pageApi: purchaseReturnPage,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'noticeNo',
        },
        expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
        toolbarConfig: {
          buttons: [],
        },
      },
    });

  async function handleReject(row: Recordable) {
    try {
      await purchaseReturnReject(row.noticeNo);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleExecute(row: Recordable) {
    try {
      openModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await rjLineList(row.noticeNo);
    row.childList = res;
    return res;
  }
</script>
