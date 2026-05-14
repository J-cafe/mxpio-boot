<template>
  <VxeBasicTable
    ref="tableRef"
    v-bind="gridOptions"
    @checkbox-change="checkboxChange"
    @checkbox-all="checkboxChange"
  />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { detailColumns } from './otherRepairApply.data';
  import { recordListRepairApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'RecordTable';
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
    () => [props.bizNo, props.task.repairStatus],
    () => {
      debounceLoadData();
    },
    {
      deep: true,
    },
  );

  const { tableRef, gridOptions, checkboxChange } = useListCrudHook({
    componentName,
    columns: detailColumns,
    pageApi: recordListRepairApi,
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
    filters: () => ({
      'bizNo@EQ': props.bizNo,
    }),
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
