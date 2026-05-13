<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import Big from 'big.js';
  import { isEmpty } from '@mxpio/utils';

  defineOptions({ name: 'ExpandTable' });

  const props = defineProps({
    dataSource: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
    },
  });

  const gridOptions = reactive<BasicVxeTableProps>({
    pagerConfig: {
      enabled: false,
    },
    toolbarConfig: {
      enabled: false,
    },
    proxyConfig: {
      enabled: false,
    },
    columns: [
      { type: 'seq', width: 40 },
      { title: '行号', field: 'lineNo', width: 100, sortable: true },
      { title: '物料编码', field: 'itemCode', width: 120 },
      { title: '物料名称', field: 'itemName', width: 100 },
      { title: '规格型号', field: 'itemSpec', width: 100 },
      { title: '图号', field: 'drawingNo', width: 100 },
      { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
      { title: '报检数量', field: 'quantity', width: 100 },
      { title: '验收人', field: 'acceptanceBy', formatter: 'dictText', width: 150 },
      { title: '验收时间', field: 'acceptanceTime', formatter: 'dictText', width: 150 },
      { title: '检验数量', field: 'checkQuantity', width: 120 },
      { title: '不合格数量', field: 'unqualifiedQuantity', width: 140 },
      { title: '让步接收', field: 'concessionQuantity', width: 120 },
      {
        title: '判退数量',
        field: 'returnQuantity',
        width: 120,
        slots: {
          default: ({ row }) => {
            if (isEmpty(row.unqualifiedQuantity) && isEmpty(row.concessionQuantity)) {
              return '';
            }
            const returnQuantity = Big(row.unqualifiedQuantity || 0)
              .minus(row.concessionQuantity || 0)
              .toNumber();
            return returnQuantity;
          },
        },
      },
      { title: '检验结论', field: 'checkResult', width: 120, formatter: 'dictText' },
      {
        title: '接收数量',
        field: 'receiveQuantity',
        width: 120,
        slots: {
          default: ({ row }) => {
            if (row.checkResult === '1') {
              if (isEmpty(row.unqualifiedQuantity) && isEmpty(row.concessionQuantity)) {
                return '';
              }
              const receiveQuantity = Big(row.quantity || 0)
                .minus(row.unqualifiedQuantity || 0)
                .plus(row.concessionQuantity || 0)
                .toNumber();
              return receiveQuantity;
            } else {
              return 0;
            }
          },
        },
      },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
