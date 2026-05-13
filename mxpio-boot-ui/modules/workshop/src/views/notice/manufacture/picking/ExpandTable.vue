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
      { title: '物料编码', field: 'itemCode', width: 80 },
      { title: '物料名称', field: 'itemName', width: 80 },
      { title: '规格型号', field: 'itemSpec', width: 80 },
      { title: '图号', field: 'drawingNo', width: 80 },
      { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
      { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 80 },
      { title: '材料牌号', field: 'materialBrand', width: 80 },
      { title: '下料长度', field: 'makeLength', width: 80 },
      { title: '可制数量', field: 'makeNum', width: 80 },
      { title: '直径', field: 'materialDiameter', width: 80 },
      { title: '壁厚', field: 'materialThickness', width: 80 },
      { title: '数量', field: 'quantity', width: 80 },
      { title: '批次号', field: 'lotNo', width: 80 },
      { title: '备注', field: 'memo', width: 80 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
