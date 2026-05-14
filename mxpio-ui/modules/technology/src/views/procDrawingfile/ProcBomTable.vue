<template>
  <VxeBasicTable
    ref="tableRef"
    v-bind="gridOptions"
    @current-change="onCurrentChange"
    @data-change="onDataChange"
  >
    <template #schemeName="{ row }">
      <a>{{ row.schemeName }}</a>
    </template>
  </VxeBasicTable>
</template>
<script lang="ts" setup>
  import { computed, ref, watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { procBomDetailColumns } from './procDrawingfile.data';
  import { procLineList } from '@mxpio/bizcommon';
  import { useDebounceFn } from '@vueuse/core';

  defineOptions({ name: 'ProcBomTable' });

  const emit = defineEmits(['change']);
  const tableRef = ref();

  const props = defineProps({
    currentRow: {
      type: Object,
      default: () => ({}),
    },
  });

  const debounceLoadData = useDebounceFn(() => {
    tableRef.value?.commitProxy('query');
  }, 200);

  watch(
    () => props.currentRow,
    () => {
      // 增加防抖，避免频繁请求
      debounceLoadData();
    },
    {
      deep: true,
    },
  );

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ProcBomTable',
      tableClass: '!px-0 !py-0',
      height: 300,
      columns: procBomDetailColumns,
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
      proxyConfig: {
        ajax: {
          query: async () => {
            return loadData();
          },
        },
      },
      pagerConfig: {
        enabled: false,
      },
      radioConfig: {
        trigger: 'row',
      },
      sortConfig: {
        trigger: 'cell',
        remote: false,
        defaultSort: { field: 'processOrder', order: 'asc' },
      },
    };
  });

  async function loadData() {
    try {
      if (!props.currentRow.routId) {
        return [];
      }
      const res = await procLineList(props.currentRow.routId);
      return res;
    } catch (error) {
      console.error('获取BOM列表失败', error);
    }
  }

  function onCurrentChange({ row }) {
    emit('change', row);
  }

  function onDataChange({ visibleData }) {
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
    emit('change', visibleData[0]);
  }
</script>
