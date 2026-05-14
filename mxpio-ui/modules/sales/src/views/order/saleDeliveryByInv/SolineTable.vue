<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed, watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { solineUnFinishedList, salesOrderExecute } from '@mxpio/bizcommon';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { soLineColumns } from './byInv.data';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'SolineTable' });
  const props = defineProps({
    itemCode: {
      type: String,
      default: '',
    },
    parentData: {
      type: Object,
      default: () => ({}),
    },
  });
  const tableRef = ref<VxeGridInstance>();
  const { createMessage } = useMessage();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'SolineTable',
      keepSource: true,
      minHeight: '500px',
      tableClass: '!px-0 !py-0',
      columns: soLineColumns,
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
      },
      proxyConfig: {
        autoLoad: false,
        ajax: {
          query: async ({ page, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
            return loadData({ page, sorts });
          },
        },
      },
      editRules: {
        executeQuantity: [
          { required: true, message: '请输入发货数量', trigger: 'change' },
          {
            validator: ({ cellValue, row }) => {
              if (cellValue <= 0) {
                return new Error('发货数量不能小于等于0');
              }
              if (cellValue > row.maxQuantity) {
                return new Error('不能大于最大发货数量');
              }
            },
            trigger: 'change',
          },
        ],
      },
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
            // visible: hasPermission(auth.delivery),
          },
        ],
      },
    };
  });

  async function loadData({ page, sorts }: VxeTableQueryParams) {
    if (!props.itemCode) {
      return [];
    }
    const params = getVxeTableQueryParams({
      page,
      sorts,
      filters: {
        'itemCode@EQ': props.itemCode,
      },
    });
    const res = await solineUnFinishedList(params);
    return res;
  }

  async function handleDelivery() {
    try {
      const rows = tableRef.value?.getCheckboxRecords();
      if (rows?.length === 0 || !rows) {
        createMessage.warning('请选择一条数据');
        return;
      }
      const bizNo: string[] = [];
      const closeIds: string[] = [];
      const closeOrder: string[] = [];
      const orderStatus: string[] = [];
      let total = 0;
      const formData: Recordable[] = [];
      rows.forEach((item: Recordable) => {
        bizNo.includes(item.bizNo) ? '' : bizNo.push(item.bizNo);
        item.closeStatus === 'closed' && closeIds.push(item.bizNo + ':' + item.lineNo);
        if (item.salesOrder.closeStatus === 'closed') {
          closeOrder.includes(item.bizNo) ? '' : closeOrder.push(item.bizNo);
        }
        if (item.salesOrder.orderStatus === '40' || item.salesOrder.orderStatus === '50') {
          orderStatus.includes(item.bizNo) ? '' : orderStatus.push(item.bizNo);
        }
        total += item.executeQuantity;
        formData.push(classifyIntoFormData(item));
      });
      if (bizNo.length > 1) {
        return createMessage.warning('只能选择相同订单');
      }
      if (closeOrder.length >= 1) {
        return createMessage.warning(`订单:${closeOrder.join()}已关闭不能发货`);
      }
      if (orderStatus.length >= 1) {
        return createMessage.warning(`订单:${orderStatus.join()}已完成不能发货`);
      }
      if (closeIds.length > 0) {
        return createMessage.warning('行状态为已关闭，不能发货');
      }
      if (total > props.parentData.quantity) {
        return createMessage.warning('发货合计不能大于' + props.parentData.quantity);
      }
      let errMap = await tableRef.value?.validate(rows);
      if (errMap) {
        return;
      }
      await salesOrderExecute(bizNo[0], {
        lines: formData,
      });
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (err) {
      console.log(err);
    }
  }

  function classifyIntoFormData(row: Recordable) {
    return {
      bizOrderNo: row.bizNo,
      lotNo: null,
      whCode: props.parentData.whCode,
      itemCode: row.itemCode,
      bizOrderlineNo: row.lineNo,
      quantity: row.executeQuantity,
      lineQuantity: row.quantity,
    };
  }

  watch(
    () => props.itemCode,
    () => {
      if (props.itemCode) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    },
  );
</script>
