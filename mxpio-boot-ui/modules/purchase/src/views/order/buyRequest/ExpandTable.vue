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
      { title: '行状态', field: 'lineStatus', width: 100, formatter: 'dictText' },
      { title: '物料编码', field: 'itemCode', width: 100 },
      { title: '物料名称', field: 'itemName', width: 100 },
      { title: '规格型号', field: 'itemSpec', width: 100 },
      { title: '图号', field: 'drawingNo', width: 100 },
      { title: '物料组', field: 'itemGroupCode', width: 100 },
      { title: '单位', field: 'unitCode', width: 100, formatter: 'dictText' },
      { title: '数量', field: 'quantity', width: 100 },
      { title: '计划跟踪号', field: 'trackingNo', width: 100 },
      { title: '建议下单日期', field: 'suggestOrderDate', width: 120 },
      { title: '需求到货日期', field: 'arriveDate', width: 120 },
      { title: '用途', field: 'usage', width: 100 },
      { title: '备注', field: 'memo', width: 100 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
