<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
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
    <PickingModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    deleteOtherPickupApi,
    otherPickupPageApi,
    otherPickupRejectApi,
    otherPickupLineApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './picking.data';
  import PickingModal from './PickingModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'OtherPickupNoticeList';

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
  const { tableRef, gridOptions, registerModal, handleDetail, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    authConfig,
    searchFormSchema,
    pageApi: otherPickupPageApi,
    deleteApi: deleteOtherPickupApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'noticeNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
      toolbarConfig: {
        buttons: [],
        import: false,
      },
    },
    deleteBefore,
  });

  async function handleReject(row: Recordable) {
    try {
      await otherPickupRejectApi(row.noticeNo);
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
    const res = await otherPickupLineApi(row.noticeNo);
    row.childList = res;
    return res;
  }
</script>
