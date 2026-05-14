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
              ifShow: row.noticeStatus === '10',
              auth: authConfig.edit,
            },
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
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              ifShow: row.noticeStatus === '10',
              auth: authConfig.delete,
            },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <PickingModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { deleteOpApi, opPageApi, opRejectApi, opLineListApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './picking.data';
  import PickingModal from './PickingModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'OutsourcePickingList';

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
  const { createMessage } = useMessage();
  const [registerExecuteModal, { openModal }] = useModal();
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
    pageApi: opPageApi,
    deleteApi: deleteOpApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'noticeNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
      toolbarConfig: {
        buttons: [],
      },
    },
    deleteBefore,
    filters: { 'pickingType@EQ': '45' },
  });

  async function handleReject(row: Recordable) {
    try {
      await opRejectApi(row.noticeNo);
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

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      const noticeNos: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.noticeStatus !== '10') {
          noticeNos.push(row.noticeNo);
        }
      });
      if (noticeNos.length > 0) {
        createMessage.error(`单据:${noticeNos.join(',')} 状态不允许删除`);
        return Promise.reject(`单据:${noticeNos.join(',')} 状态不允许删除`);
      }
      return Promise.resolve();
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await opLineListApi(row.noticeNo);
    row.childList = res;
    return res;
  }
</script>
