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
      { title: '使用决策', field: 'applyDecision', width: 120, formatter: 'dictText' },
      { title: '行号', field: 'lineNo', width: 60 },
      { title: '数量', field: 'applyQuantity', width: 100 },
      { title: '质检单号', field: 'inspectionBillNo', width: 120 },
      { title: '业务类型', field: 'inspectionBill.textMap.busiType$DICT_TEXT_', width: 100 },
      { title: '源单类型', field: 'inspectionBill.textMap.originBizType$DICT_TEXT_', width: 100 },
      { title: '源单编号', field: 'inspectionBill.originBizNo', width: 100 },
      { title: '报检组织编号', field: 'inspectionBill.applyOrgCode', width: 120 },
      { title: '报检组织名称', field: 'inspectionBill.applyOrgName', width: 120 },
      { title: '物料编码', field: 'inspectionBill.itemCode', width: 100 },
      { title: '物料名称', field: 'inspectionBill.itemName', width: 100 },
      { title: '规格型号', field: 'inspectionBill.itemSpec', width: 100 },
      { title: '图号', field: 'inspectionBill.drawingNo', width: 100 },
      { title: '单位', field: 'inspectionBill.textMap.unitCode$DICT_TEXT_', width: 100 },
      { title: '报检数量', field: 'inspectionBill.quantity', width: 100 },
      { title: '检验数量', field: 'inspectionBill.checkQuantity', width: 100 },
      { title: '不合格数量', field: 'inspectionBill.unqualifiedQuantity', width: 100 },
      {
        title: '检验员',
        field: 'inspectionBill.textMap.inspector$DICT_TEXT_',
        formatter: 'dictText',
        width: 100,
      },
      { title: '检验开始时间', field: 'inspectionBill.inspectionStartTime', width: 120 },
      { title: '检验完成时间', field: 'inspectionBill.inspectionFinishTime', width: 120 },
    ],
    size: 'small',
    data: props.dataSource,
  });
</script>
