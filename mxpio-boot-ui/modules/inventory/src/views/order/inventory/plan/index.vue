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
              ifShow: row.isEnable === 0 && row.execStatus === '1',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '生效',
              onClick: handleEnable.bind(null, row),
              auth: authConfig.enable,
              ifShow: row.isEnable === 0 && row.execStatus === '1',
            },
            {
              label: '作废',
              onClick: handleDisable.bind(null, row),
              auth: authConfig.disable,
              ifShow: row.isEnable === 1,
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
              ifShow: row.isEnable === 0 && row.execStatus === '1',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <InventoryPlanModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    deleteInvplanApi,
    invplanPageApi,
    enableInvplanApi,
    disableInvplanApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './inventoryPlan.data';
  import InventoryPlanModal from './InventoryPlanModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'InventoryPlanList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const authConfig = {
    disable: `erp:${componentName}:disable`,
    enable: `erp:${componentName}:enable`,
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
    pageApi: invplanPageApi,
    deleteApi: deleteInvplanApi,
    deleteBefore: deleteBefore,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'code',
      },
      toolbarConfig: {
        import: false,
      },
    },
    module: 'erp',
  });

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      // 修复bug 目前返回的数组内容为undefined
      const bizNos: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.isEnable !== 0 || row.execStatus !== '1') {
          bizNos.push(row.code);
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

  async function handleEnable(row: Recordable) {
    try {
      await enableInvplanApi(row.code);
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleDisable(row: Recordable) {
    try {
      await disableInvplanApi(row.code);
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }
</script>
