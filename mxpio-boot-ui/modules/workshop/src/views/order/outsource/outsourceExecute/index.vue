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
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '领料',
              auth: authConfig.picking,
              onClick: handlePicking.bind(null, row),
              ifShow: isCanPicking(row),
            },
            {
              label: '补料',
              auth: authConfig.supplement,
              onClick: handleSupplement.bind(null, row),
              ifShow: isCanPicking(row),
            },
            {
              label: '合格退料',
              auth: authConfig.return,
              onClick: handleReturn.bind(null, row),
              ifShow: isCanPicking(row),
            },
            {
              label: '不良退料',
              auth: authConfig.scrap,
              onClick: handleScrap.bind(null, row),
              ifShow: isCanPicking(row),
            },
            {
              label: '报工',
              auth: authConfig.execute,
              onClick: handleExecute.bind(null, row),
              ifShow: isCanExecute(row),
            },
            {
              label: '完工',
              auth: authConfig.finish,
              onClick: handleFinish.bind(null, row),
              ifShow: isCanExecute(row),
            },
            {
              label: '结算',
              auth: authConfig.clear,
              onClick: handleClear.bind(null, row),
              ifShow: isCanclear(row),
            },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <OutsourceOrderModal @register="registerModal" @success="handleSuccess" />
    <!-- 领料 -->
    <OutsourcePickingModal @register="registerPickingModal" @success="handleSuccess" />
    <!-- 补料 -->
    <OutsourceSupplementModal @register="registerSupplementModal" @success="handleSuccess" />
    <!-- 退料 -->
    <OutsourceReturnModal @register="registerReturnModal" @success="handleSuccess" />
    <!-- 不合格退料 -->
    <OutsourceScrapModal @register="registerScrapModal" @success="handleSuccess" />
    <!-- 报工 -->
    <OutsourceExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    outsourceOrderPageApi,
    outsourceOrderConfigApi,
    finishManufactureOrderApi,
    clearManufactureOrderApi,
    ooLineListApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './outsourceExecute.data';
  import OutsourceOrderModal from '../outsourceOrder/OutsourceOrderModal.vue';
  import OutsourcePickingModal from './OutsourcePickingModal.vue';
  import OutsourceSupplementModal from './OutsourceSupplementModal.vue';
  import OutsourceReturnModal from './OutsourceReturnModal.vue';
  import OutsourceScrapModal from './OutsourceScrapModal.vue';
  import OutsourceExecuteModal from './OutsourceExecuteModal.vue';
  import ExpandTable from '../outsourceOrder/ExpandTable.vue';

  const componentName = 'outsourceExecuteList';
  defineOptions({ name: componentName });
  const [registerPickingModal, { openModal: openPickingModal }] = useModal();
  const [registerSupplementModal, { openModal: openSupplementModal }] = useModal();
  const [registerReturnModal, { openModal: openReturnModal }] = useModal();
  const [registerScrapModal, { openModal: openScrapModal }] = useModal();
  const [registerExecuteModal, { openModal: openExecuteModal }] = useModal();

  const authConfig = {
    execute: `erp:${componentName}:execute`,
    picking: `erp:${componentName}:picking`, // 领料权限
    supplement: `erp:${componentName}:supplement`, // 补料权限
    return: `erp:${componentName}:return`, // 退料权限
    scrap: `erp:${componentName}:scrap`, // 不合格退料权限
    kitting: `erp:${componentName}:kitting`, // 齐套分析
    lacksItems: `erp:${componentName}:lacksItems`, // 缺件分析
    finish: `erp:${componentName}:finish`, // 完工
    clear: `erp:${componentName}:clear`, // 结算
  };

  const { tableRef, gridOptions, registerModal, handleDetail, handleSuccess, checkboxChange } =
    useListCrudHook({
      componentName,
      columns,
      searchFormSchema,
      pageApi: outsourceOrderPageApi,
      configApi: outsourceOrderConfigApi,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'bizNo',
        },
        toolbarConfig: {
          buttons: [],
          import: false,
        },
        expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
      },
      module: 'erp',
    });

  async function handlePicking(row: Recordable) {
    openPickingModal(true, {
      bizNo: row.bizNo,
    });
  }

  async function handleSupplement(row: Recordable) {
    openSupplementModal(true, {
      bizNo: row.bizNo,
    });
  }

  async function handleReturn(row: Recordable) {
    openReturnModal(true, {
      bizNo: row.bizNo,
    });
  }

  async function handleScrap(row: Recordable) {
    openScrapModal(true, {
      bizNo: row.bizNo,
    });
  }

  async function handleExecute(record: Recordable) {
    openExecuteModal(true, {
      record,
    });
  }

  async function handleFinish(row: Recordable) {
    try {
      await finishManufactureOrderApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleClear(row: Recordable) {
    try {
      await clearManufactureOrderApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await ooLineListApi(row.bizNo);
    row.childList = res;
    return res;
  }

  function isCanExecute(row: Recordable) {
    return row.closeStatus !== 'closed' && row.orderStatus !== '40' && row.orderStatus !== '50';
  }

  function isCanPicking(row: Recordable) {
    return row.closeStatus !== 'closed' && row.orderStatus !== '50';
  }

  function isCanclear(row: Recordable) {
    return row.closeStatus !== 'closed' && row.orderStatus === '40' && row.planQuantity <= 0;
  }
</script>
