<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';

  defineOptions({ name: 'ExpandTable' });

  const props = defineProps({
    dataSource: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
    },
  });

  const tableRef = ref<VxeGridInstance>();

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
      { title: '行状态', field: 'closeStatus', width: 100, formatter: 'dictText' },
      { title: '物料编码', field: 'itemCode', width: 100 },
      { title: '物料名称', field: 'itemName', width: 100 },
      { title: '规格型号', field: 'itemSpec', width: 100 },
      { title: '图号', field: 'drawingNo', width: 100 },
      { title: '单位', field: 'unitCode', width: 100, formatter: 'dictText' },
      { title: '采购数量', field: 'quantity', width: 100 },
      { title: '请购单号', field: 'reqBizNo', width: 100 },
      { title: '请购单行号', field: 'reqLineNo', width: 100 },
      { title: '请购单数量', field: 'reqQuantity', width: 100 },
      { title: '到货日期', field: 'suggestArriveDate', width: 100 },
      { title: '备注', field: 'memo', width: 100 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
