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
        width: '50',
        align: 'center',
      },
      {
        title: '用户账号',
        field: 'username',
        width: '100',
      },
      {
        title: '用户姓名',
        field: 'nickname',
        width: '100',
      },
      {
        title: '操作',
        field: 'operation',
        slots: { default: 'operation' },
        width: 120,
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
