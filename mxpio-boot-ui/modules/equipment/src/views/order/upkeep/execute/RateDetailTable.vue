<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed, nextTick } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { evaluateListApi } from '@mxpio/bizcommon';
  import { rateDetailColumns } from './execute.data';
  import { getVxeTableQueryParams } from '@mxpio/utils';

  defineOptions({ name: 'EqpUpkeepRateDetailTable' });
  const isDisabled = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const task: Recordable = ref({});
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'EqpUpkeepRateDetailTable',
      keepSource: true,
      minHeight: 400,
      tableClass: '!px-0 !py-0',
      columns: rateDetailColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      toolbarConfig: {
        buttons: [],
        import: false,
        print: false,
        export: false,
      },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      editRules: {
        score: [{ required: true, message: '请输入评分', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    task.value = data.record || {};
    await loadData();
  }

  async function loadData() {
    const params = getVxeTableQueryParams({
      filters: { 'type@EQ': '02' },
    });
    const res = await evaluateListApi(params);
    dataSource.value = res.map((item) => {
      delete item.id;
      return item;
    });
    nextTick(() => {
      tableRef.value?.loadData(dataSource.value);
    });
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
      const { fullData } = tableRef.value?.getTableData() || {};
      return fullData;
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
