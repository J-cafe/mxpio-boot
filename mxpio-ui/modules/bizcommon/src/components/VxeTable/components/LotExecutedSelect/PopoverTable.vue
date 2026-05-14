<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions">
    <template #operation="{ row }">
      <a-button type="link" @click="handleRemove(row)">移除</a-button>
    </template>
  </VxeBasicTable>
</template>
<script lang="ts" setup>
  import { ref, reactive, watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';

  defineOptions({ name: 'PopoverTable' });

  const props = defineProps({
    dataSource: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
    },
  });

  const tableRef = ref<VxeGridInstance>();
  const emit = defineEmits(['remove']);
  const gridOptions = reactive<BasicVxeTableProps>({
    tableClass: '!px-0 !py-0',
    pagerConfig: {
      enabled: false,
    },
    toolbarConfig: {
      enabled: false,
    },
    proxyConfig: {
      enabled: false,
    },
    columns: [
      {
        title: '序号',
        type: 'seq',
        width: 50,
        align: 'center',
      },
      {
        title: '批次号',
        field: 'lotNo',
        width: 100,
        align: 'center',
      },
      {
        title: '仓库',
        field: 'whCode',
        width: 100,
        align: 'center',
        formatter: 'dictText',
      },
      {
        title: '执行数量',
        field: 'executeQuantity',
        width: 100,
      },
      {
        title: '操作',
        field: 'operation',
        width: 80,
        slots: { default: 'operation' },
        align: 'center',
      },
    ],
    size: 'small',
    data: props.dataSource,
  });

  watch(
    () => props.dataSource,
    (val) => {
      tableRef.value?.loadData(val || []);
    },
    {
      deep: true,
    },
  );

  function handleRemove(row: Recordable) {
    emit('remove', row);
  }
</script>
