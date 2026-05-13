<template>
  <div class="m-3">
    <a-card title="销售预测" bordered :bodyStyle="{ padding: '1px' }">
      <VxeBasicTable
        ref="tableRef"
        v-bind="gridOptions"
        @checkbox-change="checkboxChange"
        @checkbox-all="checkboxChange"
        @filter-change="filterChangeEvent"
      >
        <template #action="{ row }">
          <TableAction
            :outside="true"
            :actions="[
              {
                label: '编辑',
                onClick: handleEdit.bind(null, row),
                auth: auth.edit,
                ifShow: row.bpmnStatus === '01' && row.closeStatus !== 'closed',
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
                  row.closeStatus !== 'closed',
              },
              {
                label: '关闭',
                onClick: handleClose.bind(null, row),
                auth: authConfig.close,
                ifShow: row.bpmnStatus !== '50' && row.closeStatus !== 'closed',
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
        <template #name_filter="{ column }">
          <ADatePicker
            v-model="option.data"
            v-for="(option, index) in column.filters"
            :key="index"
            :getPopupContainer="getPopupContainer"
          />
        </template>
      </VxeBasicTable>
    </a-card>
    <SalesOrderModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { getPopupContainer } from '@mxpio/utils';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    spDeleteApi,
    spPageApi,
    commonConfig,
    spSubmitApi,
    spAuditApi,
    spAbandonApi,
    spOpenApi,
    spCloseApi,
    spLineApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns } from './saleForecast.data';
  import SalesOrderModal from './SaleForecastModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'SaleForecastList';
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
    pageApi: spPageApi,
    deleteApi: spDeleteApi,
    deleteBefore: deleteBefore,
    configApi: () => commonConfig('SP1'),
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      floatingFilterConfig: {
        enabled: true,
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
      await spSubmitApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAudit(row: Recordable) {
    try {
      await spAuditApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAbandon(row: Recordable) {
    try {
      await spAbandonApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleOpen(row: Recordable) {
    try {
      await spOpenApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleClose(row: Recordable) {
    try {
      await spCloseApi(row.bizNo);
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await spLineApi(row.bizNo);
    row.childList = res;
    return res;
  }

  async function filterChangeEvent() {
    try {
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }
</script>
