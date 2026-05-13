<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <SaleDeliveryModal @register="registerModal" @success="handleSuccess" />
    <SaleReturnModal @register="registerReturnModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { solinePageTwice, openSoLine, closeSoLine } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './saleDeliveryByLine.data';
  import { ref } from 'vue';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import SaleDeliveryModal from './SaleDeliveryModal.vue';
  import SaleReturnModal from './SaleReturnModal.vue';

  const componentName = 'SaleDeliveryByLine';
  defineOptions({ name: componentName });

  const maxDeliveryFilter = ref(['1']);
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
    pageApi: (params) => {
      return solinePageTwice(maxDeliveryFilter.value?.[0] === '1' ? 1 : 0, params);
    },
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [
          {
            content: '发货',
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
                click: handleDelivery,
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
              attrs: {
                class: 'ml-2',
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
              attrs: {
                class: 'ml-2',
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
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleOpen,
              },
            },
            visible: hasPermission(auth.open),
          },
          {
            buttonRender: {
              name: 'ACheckboxGroup',
              props: () => ({
                options: [
                  {
                    label: '最大可发货数大于0',
                    value: '1',
                  },
                ],
                value: maxDeliveryFilter,
              }),
              attrs: {
                class: 'ml-2',
              },
              events: {
                change: () => {
                  // maxDeliveryFilter.value = value;
                  tableRef.value?.commitProxy('query');
                },
              },
            },
          },
        ],
        tools: [],
      },
      rowConfig: {
        keyField: 'id',
      },
    },
    module: 'erp',
  });

  async function handleDelivery() {
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
      if (item.salesOrder.closeStatus === 'closed') {
        closeOrder.push(item.bizNo + ':' + item.lineNo);
      }
      if (item.salesOrder.orderStatus === '40' || item.salesOrder.orderStatus === '50') {
        orderStatus.push(item.bizNo + ':' + item.lineNo);
      }
    });
    // 去重
    const newBizNos = Array.from(new Set(bizNos));
    if (newBizNos.length > 1) {
      createMessage.error('只能选择相同销售单的单据');
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
      createMessage.error('只能选择相同销售单的单据');
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
      await closeSoLine(bizNo.join());
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
      await openSoLine(bizNo.join());
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } else {
      createMessage.error('请选择明细');
    }
  }
</script>
