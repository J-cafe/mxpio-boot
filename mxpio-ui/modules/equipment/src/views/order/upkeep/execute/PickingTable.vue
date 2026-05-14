<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { pickingColumns } from './execute.data';
  import { pickingEqpUpkeepListApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'PickingTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
    task: {
      type: Object,
      default: () => {},
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => [props.bizNo, props.task.orderStatus],
    () => {
      debounceLoadData();
    },
    {
      deep: true,
      immediate: true,
    },
  );

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: pickingColumns,
    pageApi: () => pickingEqpUpkeepListApi(props.bizNo),
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: 280,
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        enabled: false,
      },
      proxyConfig: {
        enabled: false,
      },
      pagerConfig: {
        enabled: false,
      },
    },
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.bizNo) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }
</script>
