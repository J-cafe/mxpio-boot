<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { mpsTempLineListApi } from '@mxpio/bizcommon';

  defineOptions({ name: 'DetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData = ref<Recordable>({});

  // 使用Hook，传入表格引用和自定义参
  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    { title: '序号', type: 'seq', width: 50, align: 'center' },
    { title: '需求类型', field: 'dataType', width: 100, formatter: 'dictText' },
    { title: '业务单号', field: 'bizNo', width: 100 },
    { title: '业务单行号', field: 'bizLineNo', width: 100 },
    { title: '物料编码', field: 'itemCode', width: 120 },
    { title: '物料名称', field: 'itemName', width: 100 },
    { title: '规格型号', field: 'itemSpec', width: 100 },
    { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
    { title: '图号', field: 'drawingNo', width: 100 },
    { title: '需求数量', field: 'needQuantity', width: 120 },
    { title: '需求日期', field: 'needDate', width: 120 },
    {
      title: '计划交付日期',
      field: 'deliveryDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 140,
    },
    {
      title: '备注',
      field: 'memo',
      editRender: {
        name: 'AInput',
      },
      width: 120,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'MainPlanDetailTable',
      keepSource: true,
      height: 350,
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      proxyConfig: { enabled: false },
      toolbarConfig: {
        buttons: [],
        import: false,
        print: false,
        export: false,
        refresh: false,
        custom: false,
      },
      editRules: {
        deliveryDate: [{ required: true, message: '请选择计划交付日期', trigger: 'change' }],
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
    formData.value = record;
    if (!record.id) {
      return;
    }
    console.log(record.id);
    const res = await mpsTempLineListApi(record.id);
    tableRef.value?.loadData(res);
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function getData() {
    try {
      const data: Recordable | undefined = tableRef.value?.getRecordset();
      if (!data) {
        return false;
      }
      const mpsLineList = setDataCrud(data, isUpdate.value);
      return {
        mpsLineList,
      };
    } catch (err) {
      console.error(err);
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
