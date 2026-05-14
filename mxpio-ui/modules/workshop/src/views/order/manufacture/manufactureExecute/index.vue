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
              label: '整套领料',
              auth: authConfig.picking,
              onClick: handleWholePicking.bind(null, row),
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
    <ManufactureOrderModal @register="registerModal" @success="handleSuccess" />
    <!-- 领料 -->
    <ManufacturePickingModal @register="registerPickingModal" @success="handleSuccess" />
    <!-- 补料 -->
    <ManufactureSupplementModal @register="registerSupplementModal" @success="handleSuccess" />
    <!-- 整套领料 -->
    <ManufactureWholePickingModal @register="registerWholePickingModal" @success="handleSuccess" />
    <!-- 退料 -->
    <ManufactureReturnModal @register="registerReturnModal" @success="handleSuccess" />
    <!-- 不合格退料 -->
    <ManufactureScrapModal @register="registerScrapModal" @success="handleSuccess" />
    <!-- 报工 -->
    <ManufactureExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    manufactureOrderPage,
    manufactureOrderConfig,
    materialkittingApi,
    finishManufactureOrderApi,
    clearManufactureOrderApi,
    moLineList,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './manufactureExecute.data';
  import ManufactureOrderModal from '../manufactureOrder/ManufactureOrderModal.vue';
  import ManufacturePickingModal from './ManufacturePickingModal.vue';
  import ManufactureSupplementModal from './ManufactureSupplementModal.vue';
  import ManufactureWholePickingModal from './ManufactureWholePickingModal.vue';
  import ManufactureReturnModal from './ManufactureReturnModal.vue';
  import ManufactureScrapModal from './ManufactureScrapModal.vue';
  import ManufactureExecuteModal from './ManufactureExecuteModal.vue';
  import ExpandTable from '../manufactureOrder/ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  const componentName = 'ManufactureExecuteList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const [registerPickingModal, { openModal: openPickingModal }] = useModal();
  const [registerSupplementModal, { openModal: openSupplementModal }] = useModal();
  const [registerWholePickingModal, { openModal: openWholePickingModal }] = useModal();
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
      pageApi: manufactureOrderPage,
      configApi: manufactureOrderConfig,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'bizNo',
        },
        toolbarConfig: {
          buttons: [
            {
              content: '缺件分析',
              buttonRender: {
                name: 'AButton',
                props: {
                  type: 'primary',
                  preIcon: 'mdi:file-chart-check-outline',
                },
                attrs: {
                  class: 'ml-2',
                },
                events: {
                  click: handleMissingItems,
                },
              },
            },
          ],
          tools: [
            {
              toolRender: {
                name: 'ExportButton',
                attrs: {
                  class: 'ml-2',
                },
                props: {
                  export: 'erp:ManufactureExecuteList:export',
                },
              },
            },
          ],
        },
        expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
      },
      module: 'erp',
    });

  // async function handleRelease(row: Recordable) {
  //   try {
  //     await releaseManufactureOrder(row.bizNo);
  //     createMessage.success('操作成功');
  //     tableRef.value?.commitProxy('query');
  //   } catch (error) {
  //     console.log(error);
  //   }
  // }

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

  async function handleWholePicking(row: Recordable) {
    openWholePickingModal(true, {
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
    const res = await moLineList(row.bizNo);
    row.childList = res;
    return res;
  }

  // 缺件分析
  async function handleMissingItems() {
    var closeMessage;
    try {
      const selectionRows = tableRef?.value?.getCheckboxRecords() || [];
      if (selectionRows.length > 0) {
        const bizNos = selectionRows.map((item) => {
          return item.bizNo;
        });
        var param = getQueryParams({
          'bizNo@IN': bizNos.join(','),
        });
        closeMessage = createMessage.loading('计算中..', 0);
        await materialkittingApi(param);
        createMessage.success('计算成功');
        tableRef.value?.commitProxy('query');
      } else {
        createMessage.error('请选择要计算的订单');
      }
    } catch (error) {
      console.log(error);
    } finally {
      closeMessage?.();
    }
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
