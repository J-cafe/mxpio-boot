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
      {
        title: '行号',
        field: 'lineNo',
        width: 50,
        align: 'center',
      },
      { title: '物料编码', field: 'itemCode' },
      { title: '物料名称', field: 'itemName' },
      { title: '规格型号', field: 'itemSpec' },
      { title: '图号', field: 'drawingNo' },
      { title: '单位', field: 'unitCode', formatter: 'dictText' },
      { title: '数量', field: 'quantity' },
      { title: '批次号', field: 'lotNo' },
      { title: '备注', field: 'memo' },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
