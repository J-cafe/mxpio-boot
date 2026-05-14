<template>
  <VxeBasicTable
    ref="tableRef"
    v-bind="gridOptions"
    @checkbox-change="checkboxChange"
    @checkbox-all="checkboxChange"
  >
    <template #pic="{ row }">
      <a v-if="row.pic" @click="handleDownload(row)">下载</a>
    </template>
  </VxeBasicTable>
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { bomColumns } from './eqpBasics.data';
  import { eqpBomPageApi, deleteEqpBomApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useCommon } from '@mxpio/hooks';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'EqpInfoBomTable1';
  defineOptions({ name: componentName });

  const props = defineProps({
    eqpCode: {
      type: String,
      default: () => '',
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => props.eqpCode,
    () => {
      debounceLoadData();
    },
    {
      deep: true,
      immediate: true,
    },
  );

  const { downloadByFileNo } = useCommon();
  const { tableRef, gridOptions, checkboxChange } = useListCrudHook({
    componentName,
    columns: bomColumns,
    pageApi: eqpBomPageApi,
    deleteApi: deleteEqpBomApi,
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: 280,
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
      'eqpCode@EQ': props.eqpCode,
    }),
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.eqpCode) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }

  async function handleDownload(row) {
    console.log(row);
    try {
      await downloadByFileNo(row.pic);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }
</script>
