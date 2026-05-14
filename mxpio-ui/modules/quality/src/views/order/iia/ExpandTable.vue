<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps } from '@mxpio/components';

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
      { title: '行号', field: 'lineNo', sortable: true },
      { title: '物料编码', field: 'itemCode' },
      { title: '物料名称', field: 'itemName' },
      { title: '规格型号', field: 'itemSpec' },
      { title: '图号', field: 'drawingNo' },
      { title: '单位', field: 'unitCode', formatter: 'dictText' },
      { title: '数量', field: 'quantity' },
      { title: '批次号', field: 'lotNo' },
      { title: '质检状态', field: 'qcStatus' },
      { title: '报废原因', field: 'scrapReason' },
      { title: '备注', field: 'memo' },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
