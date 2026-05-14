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
      { title: '检验顺序', field: 'lineOrder', width: 100 },
      { title: '检测项目编号', field: 'code', width: 140 },
      { title: '检测项目名称', field: 'inspectionItem.name', width: 120 },
      {
        title: '项目类型',
        field: 'inspectionItem.textMap.itemType$DICT_TEXT_',
        width: 100,
        formatter: 'dictText',
      },
      {
        title: '项目分类',
        field: 'inspectionItem.textMap.itemClass$DICT_TEXT_',
        width: 100,
        formatter: 'dictText',
      },
      {
        title: '缺陷等级',
        field: 'inspectionItem.textMap.defectGrade$DICT_TEXT_',
        width: 100,
        formatter: 'dictText',
      },
      { title: '检验标准', field: 'inspectionItem.standard', width: 100 },
      { title: '检测工具', field: 'inspectionItem.detectionTool', width: 100 },
      { title: '检验方法', field: 'inspectionItem.detectionMethod', width: 100 },
      { title: '比较符', field: 'comparator', width: 120 },
      { title: '目标值', field: 'targetValue', width: 120 },
      { title: '单位', field: 'units', width: 120 },
      { title: '最大值', field: 'maxValue', width: 120 },
      { title: '最小值', field: 'minValue', width: 120 },
      { title: '备注', field: 'memo', width: 120 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
