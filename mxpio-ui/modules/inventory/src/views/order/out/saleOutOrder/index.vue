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
              ifShow:
                row.bpmnStatus === '01' && row.closeStatus !== 'closed' && row.orderStatus === '10',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '提交',
              onClick: handleSubmit.bind(null, row),
              auth: authConfig.submit,
              ifShow: row.bpmnStatus === '01' && row.closeStatus !== 'closed',
            },
            {
              label: '审核',
              onClick: handleAudit.bind(null, row),
              auth: authConfig.audit,
              ifShow: row.bpmnStatus === '02' && row.closeStatus !== 'closed',
            },
            {
              label: '弃审',
              onClick: handleAbandon.bind(null, row),
              auth: authConfig.abandon,
              ifShow:
                (row.bpmnStatus === '02' || row.bpmnStatus === '03') &&
                row.closeStatus !== 'closed' &&
                row.orderStatus === '10',
            },
            {
              label: '执行',
              onClick: handleExecute.bind(null, row),
              auth: authConfig.execute,
              ifShow:
                row.bpmnStatus === '03' &&
                row.closeStatus !== 'closed' &&
                row.orderStatus !== '40' &&
                row.orderStatus !== '50',
            },
            {
              label: '关闭',
              onClick: handleClose.bind(null, row),
              auth: authConfig.close,
              ifShow:
                row.bpmnStatus !== '50' && row.orderStatus !== '40' && row.closeStatus !== 'closed',
            },
            {
              label: '打开',
              onClick: handleOpen.bind(null, row),
              auth: authConfig.open,
              ifShow: row.closeStatus === 'closed',
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
              ifShow: row.bpmnStatus === '01',
            },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <SaleOutOrderModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    deleteWo,
    woPage,
    woConfig,
    submitWo,
    auditWo,
    abandonWo,
    openWo,
    closeWo,
    wolineList,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './saleOutOrder.data';
  import SaleOutOrderModal from './SaleOutOrderModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'SaleOutOrderList';
  defineOptions({ name: componentName });
  const [registerExecuteModal, { openModal: openExecuteModal }] = useModal();
  const { createMessage } = useMessage();
  const authConfig = {
    submit: `erp:${componentName}:submit`,
    audit: `erp:${componentName}:audit`,
    abandon: `erp:${componentName}:abandon`,
    close: `erp:${componentName}:close`,
    open: `erp:${componentName}:open`,
    clear: `erp:${componentName}:clear`,
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
    pageApi: woPage,
    deleteApi: deleteWo,
    configApi: woConfig,
    deleteBefore: deleteBefore,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
    module: 'erp',
    filters: {
      'woType@EQ': '20',
    },
  });

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      // 修复bug 目前返回的数组内容为undefined
      const bizNos: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.bpmnStatus !== '01' || row.orderStatus !== '10') {
          bizNos.push(row.bizNo);
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
      await submitWo(row.bizNo);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAudit(row: Recordable) {
    try {
      await auditWo(row.bizNo);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAbandon(row: Recordable) {
    try {
      await abandonWo(row.bizNo);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleOpen(row: Recordable) {
    try {
      await openWo(row.bizNo);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleClose(row: Recordable) {
    try {
      await closeWo(row.bizNo);
      // createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await wolineList(row.bizNo);
    row.childList = res;
    return res;
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
