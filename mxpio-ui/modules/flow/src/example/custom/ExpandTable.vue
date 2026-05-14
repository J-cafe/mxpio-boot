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
      { title: '行状态', field: 'closeStatus', formatter: 'dictText', width: 100 },
      { title: '物料编码', field: 'itemCode', width: 120 },
      { title: '物料名称', field: 'itemName', width: 120 },
      { title: '规格型号', field: 'itemSpec', width: 120 },
      { title: '图号', field: 'drawingNo', width: 100 },
      {
        title: '单位',
        field: 'unitCode',
        formatter: 'dictText',
        width: 80,
      },
      { title: '应发数量', field: 'quantity', width: 120 },
      { title: '交付日期', field: 'deliverDate', width: 120 },
      { title: '备注', field: 'memo', width: 100 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
