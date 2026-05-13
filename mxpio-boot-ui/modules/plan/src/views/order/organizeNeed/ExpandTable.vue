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
      { title: '行状态', field: 'closeStatus', formatter: 'dictText' },
      { title: '物料编码', field: 'itemCode' },
      { title: '物料名称', field: 'itemName' },
      { title: '规格型号', field: 'itemSpec' },
      { title: '单位', field: 'unitCode', formatter: 'dictText' },
      { title: '图号', field: 'drawingNo' },
      { title: '需求数量', field: 'quantity' },
      { title: '需求日期', field: 'demandDate' },
      { title: '备注', field: 'memo' },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
