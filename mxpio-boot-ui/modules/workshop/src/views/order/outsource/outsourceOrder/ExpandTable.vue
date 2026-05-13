<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, reactive, h } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { Tag as ATag } from 'ant-design-vue';

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
      { title: '行号', field: 'lineNo', width: 60, sortable: true },
      { title: '物料编码', field: 'itemCode', width: 100 },
      { title: '物料名称', field: 'itemName', width: 100 },
      { title: '规格型号', field: 'itemSpec', width: 100 },
      { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
      { title: '图号', field: 'drawingNo', width: 100 },
      { title: '数量', field: 'quantity', width: 80 },
      {
        title: '齐套数量',
        field: 'analysisColor',
        width: 100,
        slots: {
          default: ({ row }) => {
            const color = row.analysisColor;
            if (!row.analysisColor) {
              return '';
            }
            return h(ATag, { color: color }, () => row.kittingQuantity);
          },
        },
      },
      { title: '供料方式', field: 'feedingMode', formatter: 'dictText', width: 100 },
      { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 100 },
      { title: '材料牌号', field: 'materialBrand', width: 100 },
      { title: '下料长度', field: 'makeLength', width: 100 },
      { title: '下料宽度', field: 'makeArea', width: 100 },
      { title: '可制数量', field: 'makeNum', width: 100 },
      { title: '指定仓库', field: 'whCode', width: 120, formatter: 'dictText' },
      { title: '实际发料数量', field: 'actualQuantity', width: 120 },
      { title: '计划发料数量', field: 'planQuantity', width: 120 },
      { title: '实际退料数量', field: 'actualRejectQuantity', width: 120 },
      { title: '计划退料数量', field: 'planRejectQuantity', width: 120 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
