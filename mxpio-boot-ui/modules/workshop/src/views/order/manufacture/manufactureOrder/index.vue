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
              ifShow: row.closeStatus !== 'closed' && row.orderStatus === '10',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '编辑BOM',
              auth: authConfig.editbom,
              onClick: handleEditBom.bind(null, row),
              ifShow: row.orderStatus === '10' && row.closeStatus === 'open',
            },
            {
              label: '编辑订单工艺',
              auth: authConfig.editProcess,
              ifShow: row.orderStatus === '10' && row.closeStatus === 'open',
            },
            {
              label: '下达',
              onClick: handleRelease.bind(null, row),
              auth: authConfig.release,
              ifShow: row.orderStatus === '10' && row.closeStatus !== 'closed',
            },
            {
              label: '关闭',
              onClick: handleClose.bind(null, row),
              auth: authConfig.close,
              ifShow: row.orderStatus !== '40' && row.closeStatus !== 'closed',
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
              ifShow: row.orderStatus === '10' && row.closeStatus !== 'closed',
            },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <ManufactureOrderModal @register="registerModal" @success="handleSuccess" />
    <BomEditModal @register="registerBomModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    deleteManufactureOrder,
    manufactureOrderPage,
    manufactureOrderConfig,
    openManufactureOrder,
    closeManufactureOrder,
    releaseManufactureOrder,
    moLineList,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './manufactureOrder.data';
  import ManufactureOrderModal from './ManufactureOrderModal.vue';
  import BomEditModal from './BomEditModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage, usePermission } from '@mxpio/hooks';

  const componentName = 'ManufactureOrderList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
  const [registerBomModal, { openModal: openBomModal }] = useModal();

  const authConfig = {
    close: `erp:${componentName}:close`,
    open: `erp:${componentName}:open`,
    release: `erp:${componentName}:release`,
    editbom: `erp:${componentName}:editbom`,
    editProcess: `erp:${componentName}:editProcess`,
    rework: `erp:${componentName}:rework`,
  };

  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    checkboxChange,
    handleDetail,
    handleSuccess,
    openModal,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: manufactureOrderPage,
    deleteApi: deleteManufactureOrder,
    configApi: manufactureOrderConfig,
    deleteBefore: deleteBefore,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
    appendButtons: [
      {
        content: '新增返工订单',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'mdi:page-next-outline',
          },
          events: {
            click: () => handleAddRework(),
          },
        },
        visible: hasPermission(authConfig.rework),
      },
    ],
    module: 'erp',
  });

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      // 修复bug 目前返回的数组内容为undefined
      const bizNos: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.orderStatus !== '10') {
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

  async function handleRelease(row: Recordable) {
    try {
      await releaseManufactureOrder(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleOpen(row: Recordable) {
    try {
      await openManufactureOrder(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleClose(row: Recordable) {
    try {
      await closeManufactureOrder(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleEditBom(row: Recordable) {
    openBomModal(true, {
      record: row,
    });
  }

  function handleAddRework() {
    openModal(true, {
      type: 'rework',
    });
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await moLineList(row.bizNo);
    row.childList = res;
    return res;
  }
</script>
