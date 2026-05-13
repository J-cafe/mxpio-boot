<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { ooLineListApi } from '@mxpio/bizcommon';

  defineOptions({ name: 'BomDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  const detailColumns: VxeGridPropTypes.Columns = [
    {
      title: '序号',
      type: 'seq',
      width: 50,
      align: 'center',
    },
    {
      title: '行号',
      field: 'lineNo',
      width: 50,
      align: 'center',
    },
    {
      title: '物料编码',
      field: 'itemCode',
      width: 120,
    },
    {
      title: '物料名称',
      field: 'itemName',
      width: 100,
    },
    {
      title: '规格型号',
      field: 'itemSpec',
      width: 100,
    },
    {
      title: '图号',
      field: 'drawingNo',
      width: 100,
    },
    {
      title: '单位',
      field: 'unitCode',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '数量',
      field: 'quantity',
      width: 80,
    },
    {
      title: '齐套数量',
      field: 'analysisColor',
      width: 80,
    },
    {
      title: '供料方式',
      field: 'feedingMode',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '材料类型',
      field: 'materialType',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '材料牌号',
      field: 'materialBrand',
      width: 80,
    },
    {
      title: '下料长度',
      field: 'makeLength',
      width: 80,
    },
    {
      title: '可制数量',
      field: 'makeNum',
      width: 80,
    },
    {
      title: '指定仓库',
      field: 'whCode',
      formatter: 'dictText',
      width: 100,
    },
    {
      title: '实际发料数量',
      field: 'actualQuantity',
      width: 100,
    },
    {
      title: '计划发料数量',
      field: 'planQuantity',
      width: 100,
    },
    {
      title: '实际退料数量',
      field: 'actualRejectQuantity',
      width: 100,
    },
    {
      title: '计划退料数量',
      field: 'planRejectQuantity',
      width: 100,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomDetailTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      toolbarConfig: {
        buttons: [],
        import: false,
        print: false,
        export: false,
        refresh: false,
        custom: false,
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    const record = data.record || {};
    if (!record.bizNo) {
      return;
    }
    const res = await ooLineListApi(record.bizNo);
    tableRef.value?.loadData(res);
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  defineExpose({
    setData,
    validate,
  });
</script>
