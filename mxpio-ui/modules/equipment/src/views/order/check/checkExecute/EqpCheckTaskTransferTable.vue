<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { transferColumns } from './eqpCheckExecute.data';
  import { eqpCheckTaskTransferPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'EqpCheckTaskTransferTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => props.bizNo,
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
    columns: transferColumns,
    pageApi: eqpCheckTaskTransferPageApi,
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: '300px',
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        import: false,
        buttons: [],
      },
      proxyConfig: {
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
