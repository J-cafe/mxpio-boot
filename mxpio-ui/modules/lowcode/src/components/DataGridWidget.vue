<template>
  <div class="lowcode-data-grid">
    <BasicTable @register="registerTable" />
  </div>
</template>

<script lang="ts" setup>
import { watch, computed } from 'vue';
import { BasicTable, useTable } from '@mxpio/components';
import type { DataSet } from '../core/DataSet';

const props = defineProps<{
  dataSet: DataSet;
  columns: Array<{
    field: string;
    title: string;
    width?: number;
    align?: string;
  }>;
  pageSize?: number;
}>();

const [registerTable, { setTableData, setPagination }] = useTable({
  columns: computed(() =>
    (props.columns || []).map((col) => ({
      title: col.title,
      dataIndex: col.field,
      key: col.field,
      width: col.width,
      align: col.align || 'left',
    }))
  ),
  showIndexColumn: false,
  pagination: true,
  useSearchForm: false,
  showTableSetting: false,
  clickToRowSelect: true,
  api: undefined as any,
  beforeFetch: () => false,
});

watch(
  () => props.dataSet?.data,
  (newData) => {
    if (newData) {
      setTableData(newData);
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => props.dataSet?.totalCount,
  (total) => {
    setPagination({ total });
  }
);
</script>

<style scoped>
.lowcode-data-grid {
  width: 100%;
  height: 100%;
}
</style>
