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
      { title: '物料编码', field: 'itemCode', width: 100 },
      { title: '物料名称', field: 'itemName', width: 100 },
      { title: '规格型号', field: 'itemSpec', width: 100 },
      { title: '图号', field: 'drawingNo', width: 100 },
      { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
      { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 80 },
      { title: '材料牌号', field: 'materialBrand', width: 80 },
      { title: '下料长度', field: 'makeLength', width: 80 },
      { title: '直径', field: 'materialDiameter', width: 80 },
      { title: '壁厚', field: 'materialThickness', width: 80 },
      { title: '数量', field: 'quantity', width: 80 },
      { title: '批次号', field: 'lotNo', width: 120 },
      { title: '发现人', field: 'reporter', width: 100 },
      { title: '发现工序', field: 'reportProc', width: 100 },
      { title: '异常分类', field: 'exceptionType', width: 100 },
      { title: '不良原因', field: 'defectiveReason', width: 100 },
      { title: '不良原因描述', field: 'defectiveDesc', width: 150 },
      { title: '责任单位', field: 'responseUnit', width: 100 },
      { title: '备注', field: 'memo', width: 100 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
