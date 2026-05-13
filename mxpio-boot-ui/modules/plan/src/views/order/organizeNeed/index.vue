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
              label: '完成',
              onClick: handleFinish.bind(null, row),
              auth: authConfig.finish,
              ifShow:
                row.bpmnStatus === '03' &&
                row.closeStatus !== 'closed' &&
                row.orderStatus !== '40' &&
                row.orderStatus !== '50',
            },
            {
              label: '结算',
              onClick: handleClear.bind(null, row),
              auth: authConfig.clear,
              ifShow:
                row.bpmnStatus === '03' && row.orderStatus === '40' && row.closeStatus !== 'closed',
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
    <OrganizeNeedOrderModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    odDeleteApi,
    odPageApi,
    odConfigApi,
    odSubmitApi,
    odAuditApi,
    odAbandonApi,
    odClearApi,
    odFinishApi,
    odLineListApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './organizeNeed.data';
  import OrganizeNeedOrderModal from './OrganizeNeedOrderModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'OrganizeNeedOrderList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const authConfig = {
    submit: `erp:${componentName}:submit`,
    audit: `erp:${componentName}:audit`,
    abandon: `erp:${componentName}:abandon`,
    close: `erp:${componentName}:close`,
    open: `erp:${componentName}:open`,
    finish: `erp:${componentName}:finish`,
    clear: `erp:${componentName}:clear`,
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
    pageApi: odPageApi,
    deleteApi: odDeleteApi,
    configApi: odConfigApi,
    deleteBefore: deleteBefore,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
    module: 'erp',
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
      await odSubmitApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAudit(row: Recordable) {
    try {
      await odAuditApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAbandon(row: Recordable) {
    try {
      await odAbandonApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleClear(row: Recordable) {
    try {
      await odClearApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleFinish(row: Recordable) {
    try {
      await odFinishApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await odLineListApi(row.bizNo);
    row.childList = res;
    return res;
  }
</script>
