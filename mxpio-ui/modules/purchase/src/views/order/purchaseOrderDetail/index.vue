<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #drawingNo="{ row }">
        <a-tag
          v-if="
            dateUtil().isAfter(dateUtil(row.suggestArriveDate), 'day') &&
            row.actualQuantity !== row.quantity
          "
          color="red"
          >{{ row.drawingNo }}</a-tag
        >
        <span v-else>{{ row.drawingNo }}</span>
      </template>
      <template #actualQuantity="{ row }">
        <a-tag v-if="row.actualQuantity < row.quantity" color="yellow">{{
          row.actualQuantity
        }}</a-tag>
        <span v-else>{{ row.actualQuantity }}</span>
      </template>
    </VxeBasicTable>
    <ReceiveModal @register="registerModal" @success="handleSuccess" />
    <ReturnModal @register="registerReturnModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { poLinePage, openPoLine, closePoLine } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './purchaseOrderDetail.data';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import ReceiveModal from './ReceiveModal.vue';
  import ReturnModal from './ReturnModal.vue';
  import { Tag as ATag } from 'ant-design-vue';
  import { dateUtil } from '@mxpio/utils';

  const componentName = 'PurchaseOrderDetailList';
  defineOptions({ name: componentName });

  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
  const auth = {
    close: `erp:${componentName}:close`,
    open: `erp:${componentName}:open`,
    return: `erp:${componentName}:return`,
    delivery: `erp:${componentName}:delivery`,
    export: `erp:${componentName}:export`,
  };

  const [registerReturnModal, { openModal: openReturnModal }] = useModal();
  const { tableRef, gridOptions, registerModal, openModal, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: poLinePage,
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [
          {
            content: '到货',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:page-next-outline',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleReceive,
              },
            },
            visible: hasPermission(auth.delivery),
          },
          {
            content: '退货',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:page-next-outline',
              },
              events: {
                click: handleReturn,
              },
            },
            visible: hasPermission(auth.return),
          },
          {
            content: '关闭行',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:lock-outline',
              },
              events: {
                click: handleClose,
              },
            },
            visible: hasPermission(auth.close),
          },
          {
            content: '打开行',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:lock-open-variant-outline',
              },
              events: {
                click: handleOpen,
              },
            },
            visible: hasPermission(auth.open),
          },
        ],
        tools: [],
      },
    },
    module: 'erp',
  });

  async function handleReceive() {
    const rows = tableRef.value?.getCheckboxRecords();
    if (rows?.length === 0 || !rows) {
      createMessage.error('请选择一条数据');
      return;
    }
    const closeOrder: string[] = [];
    const orderStatus: string[] = [];
    const closeIds: string[] = [];
    const bizNos: string[] = [];
    rows.forEach((item: Recordable) => {
      bizNos.push(item.bizNo);
      if (item.closeStatus === 'closed') {
        closeIds.push(item.bizNo + ':' + item.lineNo);
      }
      if (item.purchaseOrder.closeStatus === 'closed') {
        closeOrder.push(item.bizNo + ':' + item.lineNo);
      }
      if (item.purchaseOrder.orderStatus === '40' || item.purchaseOrder.orderStatus === '50') {
        orderStatus.push(item.bizNo + ':' + item.lineNo);
      }
    });
    // 去重
    const newBizNos = Array.from(new Set(bizNos));
    if (newBizNos.length > 1) {
      createMessage.error('只能选择相同采购单的单据');
      return;
    }
    if (closeIds.length > 0) {
      return createMessage.error('请选择行状态为未关闭采购单的明细');
    }
    if (closeOrder.length > 0) {
      return createMessage.error(`订单:${closeOrder.join()}已关闭不能到货`);
    }
    if (orderStatus.length >= 1) {
      return createMessage.error(`订单:${orderStatus.join()}已完成不能到货`);
    }
    openModal(true, {
      bizNo: newBizNos[0],
      record: rows,
    });
  }

  async function handleReturn() {
    const rows = tableRef.value?.getCheckboxRecords();
    if (rows?.length === 0 || !rows) {
      createMessage.error('请选择一条数据');
      return;
    }
    const bizNos = rows.map((item: Recordable) => {
      return item.bizNo;
    });
    // 去重
    const newBizNos = Array.from(new Set(bizNos));
    if (newBizNos.length > 1) {
      createMessage.error('只能选择相同采购单的单据');
      return;
    }
    openReturnModal(true, {
      bizNo: newBizNos[0],
      record: rows,
    });
  }

  async function handleClose() {
    const select: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (select.length > 0) {
      const bizNo: string[] = [];
      const closeIds: string[] = [];
      select.forEach((item) => {
        bizNo.push(item.bizNo + ':' + item.lineNo);
        item.closeStatus === 'closed' && closeIds.push(item.bizNo + ':' + item.lineNo);
      });
      if (closeIds.length > 0) {
        return createMessage.error('请选择行状态为未关闭明细');
      }
      await closePoLine(bizNo.join());
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } else {
      createMessage.error('请选择明细');
    }
  }

  async function handleOpen() {
    const select: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (select.length > 0) {
      const bizNo: string[] = [];
      const openIds: string[] = [];
      select.forEach((item) => {
        bizNo.push(item.bizNo + ':' + item.lineNo);
        item.closeStatus !== 'closed' && openIds.push(item.bizNo + ':' + item.lineNo);
      });
      if (openIds.length > 0) {
        return createMessage.error('请选择行状态为已关闭明细');
      }
      await openPoLine(bizNo.join());
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } else {
      createMessage.error('请选择明细');
    }
  }
</script>
