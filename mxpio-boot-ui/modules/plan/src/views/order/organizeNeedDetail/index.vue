<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <PickingModal @register="registerPickingModal" @success="handleSuccess" />
    <ReturnModal @register="registerReturnModal" @success="handleSuccess" />
    <SupplementModal @register="registerSupplementModal" @success="handleSuccess" />
    <ScrapModal @register="registerScrapModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { odlinePageTwiceApi, openSoLine, closeSoLine } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './organizeNeedDetail.data';
  import { ref } from 'vue';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import PickingModal from './PickingModal.vue';
  import ReturnModal from './ReturnModal.vue';
  import SupplementModal from './SupplementModal.vue';
  import ScrapModal from './ScrapModal.vue';

  const componentName = 'OrganizeNeedDetailList';
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
  const [registerPickingModal, { openModal: openPickingModal }] = useModal();
  const [registerReturnModal, { openModal: openReturnModal }] = useModal();
  const [registerSupplementModal, { openModal: openSupplementModal }] = useModal();
  const [registerScrapModal, { openModal: openScrapModal }] = useModal();

  const { tableRef, gridOptions, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: (params) => {
      return odlinePageTwiceApi(maxDeliveryFilter.value?.[0] === '1' ? 1 : 0, params);
    },
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [
          {
            content: '领料',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:login',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handlePicking,
              },
            },
            visible: hasPermission(auth.delivery),
          },
          {
            content: '补料',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:login-variant',
              },
              events: {
                click: handleSupplement,
              },
            },
            visible: hasPermission(auth.return),
          },
          {
            content: '退料',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:logout',
              },
              events: {
                click: handleReturn,
              },
            },
            visible: hasPermission(auth.return),
          },
          {
            content: '不良退料',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:logout-variant',
              },
              events: {
                click: handleScrap,
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
                value: maxDeliveryFilter.value,
              }),
              events: {
                change: (data, value) => {
                  maxDeliveryFilter.value = value;
                  console.log('maxDeliveryFilter', value?.[0]);
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

  async function handlePicking() {
    const rows = tableRef.value?.getCheckboxRecords();
    if (rows?.length === 0 || !rows) {
      createMessage.error('请选择一条数据');
      return;
    }
    // const closeOrder: string[] = [];
    // const orderStatus: string[] = [];
    const closeIds: string[] = [];
    const bizNos: string[] = [];
    rows.forEach((item: Recordable) => {
      bizNos.push(item.bizNo);
      if (item.closeStatus === 'closed') {
        closeIds.push(item.bizNo + ':' + item.lineNo);
      }
      // if (item.salesOrder.closeStatus === 'closed') {
      //   closeOrder.push(item.bizNo + ':' + item.lineNo);
      // }
      // if (item.salesOrder.orderStatus === '40' || item.salesOrder.orderStatus === '50') {
      //   orderStatus.push(item.bizNo + ':' + item.lineNo);
      // }
    });
    // 去重
    const newBizNos = Array.from(new Set(bizNos));
    if (newBizNos.length > 1) {
      createMessage.error('只能选择相同需求单的明细');
      return;
    }
    // if (closeIds.length > 0) {
    //   return createMessage.error('请选择行状态为未关闭的明细');
    // }
    // if (closeOrder.length > 0) {
    //   return createMessage.error(`订单:${closeOrder.join()}已关闭不能领料`);
    // }
    // if (orderStatus.length >= 1) {
    //   return createMessage.error(`订单:${orderStatus.join()}已完成不能领料`);
    // }
    openPickingModal(true, {
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
      createMessage.error('只能选择相同需求单的明细');
      return;
    }
    openReturnModal(true, {
      bizNo: newBizNos[0],
      record: rows,
    });
  }

  async function handleSupplement() {
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
      createMessage.error('只能选择相同需求单的明细');
      return;
    }
    openSupplementModal(true, {
      bizNo: newBizNos[0],
      record: rows,
    });
  }

  async function handleScrap() {
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
      createMessage.error('只能选择相同需求单的明细');
      return;
    }
    openScrapModal(true, {
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
