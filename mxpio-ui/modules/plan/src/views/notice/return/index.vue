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
            // {
            //   label: '编辑',
            //   onClick: handleEdit.bind(null, row),
            //   ifShow: row.noticeStatus === '10',
            //   auth: authConfig.edit,
            // },
          ]"
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
            // {
            //   label: '删除',
            //   color: 'error',
            //   popConfirm: {
            //     title: '是否确认删除',
            //     placement: 'left',
            //     confirm: handleDelete.bind(null, row),
            //   },
            //   ifShow: row.noticeStatus === '10',
            //   auth: authConfig.delete,
            // },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <SaleReturnModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { saleReturnPage, saleReturnReject, rbLineList } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './saleReturn.data';
  import SaleReturnModal from './SaleReturnModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import ExpandTable from './ExpandTable.vue';
  // import { useMessage } from '@mxpio/hooks';

  const componentName = 'SaleReturnList';
  const authConfig = {
    add: 'erp:SaleReturnList:add',
    edit: 'erp:SaleReturnList:edit',
    delete: 'erp:SaleReturnList:delete',
    import: 'erp:SaleReturnList:import',
    export: 'erp:SaleReturnList:export',
    execute: 'erp:SaleReturnList:execute',
    reject: 'erp:SaleReturnList:reject',
  };
  defineOptions({ name: componentName });
  // const { createMessage } = useMessage();
  const [registerExecuteModal, { openModal }] = useModal();
  const {
    tableRef,
    gridOptions,
    registerModal,
    // handleEdit,
    // handleDelete,
    handleDetail,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns,
    authConfig,
    searchFormSchema,
    pageApi: saleReturnPage,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'noticeNo',
      },
      toolbarConfig: {
        buttons: [],
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
  });

  async function handleReject(row: Recordable) {
    try {
      await saleReturnReject(row.noticeNo);
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
    const res = await rbLineList(row.noticeNo);
    row.childList = res;
    return res;
  }
</script>
