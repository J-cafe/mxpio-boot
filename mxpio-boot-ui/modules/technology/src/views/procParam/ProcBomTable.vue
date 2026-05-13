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
  import { procBomDetailColumns } from './procParam.data';
  import { procLineList } from '@mxpio/bizcommon';

  defineOptions({ name: 'ProcBomTable' });

  const emit = defineEmits(['change']);
  const props = defineProps({
    currentRow: {
      type: Object,
      default: () => ({}),
    },
  });

  watch(
    () => props.currentRow,
    () => {
      loadData();
    },
  );

  const dataSource = ref([]);

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ProcBomTable',
      tableClass: '!px-0 !py-0',
      height: '300px',
      columns: procBomDetailColumns,
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
      proxyConfig: {
        enabled: false,
      },
      data: dataSource.value,
      pagerConfig: {
        enabled: false,
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
      if (props.currentRow.routId) {
        const res = await procLineList(props.currentRow.routId);
        dataSource.value = res;
      } else {
        dataSource.value = [];
      }
    } catch (error) {
      console.error('获取BOM列表失败', error);
    }
  }

  function onCurrentChange({ row }) {
    emit('change', row);
  }

  function onDataChange({ visibleData }) {
    emit('change', visibleData[0]);
  }
</script>
