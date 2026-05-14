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
    <InitstockModal @register="registerModal" @success="handleSuccess" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    deleteInitstockApi,
    initstockPageApi,
    initstockRejectApi,
    isLineListApi,
  } from '@mxpio/bizcommon';
  import { getDictByCode, downloadApi } from '@mxpio/api';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './initstock.data';
  import InitstockModal from './InitstockModal.vue';
  import ExecuteModal from './ExecuteModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { ref, onMounted } from 'vue';

  const componentName = 'InitstockList';
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
  const importFileNo = ref('');
  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
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
    pageApi: initstockPageApi,
    deleteApi: deleteInitstockApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'noticeNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
      toolbarConfig: {
        // buttons: [],
        tools: [
          {
            content: '模版下载',
            toolRender: {
              name: 'AButton',
              props: {
                content: '模版下载',
                type: 'link',
              },
              events: {
                click: handleImportTemplat,
              },
            },
          },
          {
            toolRender: {
              name: 'ImportButton',
              props: () => {
                return { importCode: authConfig.import };
              },
            },
            visible: hasPermission(authConfig.import),
          },
        ],
      },
    },
    deleteBefore,
  });

  async function handleReject(row: Recordable) {
    try {
      await initstockRejectApi(row.noticeNo);
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
    const res = await isLineListApi(row.noticeNo);
    row.childList = res;
    return res;
  }

  async function handleImportTemplat() {
    try {
      if (!importFileNo.value) {
        createMessage.error('请联系管理员配置导入文件编号');
        return;
      }
      createMessage.loading('下载中...');
      await downloadApi(importFileNo.value);
    } catch (error) {
      console.log(error);
    } finally {
      createMessage.destroy();
    }
  }

  async function getImportFileNo() {
    try {
      const res = await getDictByCode('ERP_IMPORT_FILE_NO');
      res?.items?.forEach((item) => {
        if (item.itemText === 'InitList') importFileNo.value = item.itemValue;
      });
    } catch (error) {
      console.log(error);
    }
  }

  onMounted(() => {
    getImportFileNo();
  });
</script>
