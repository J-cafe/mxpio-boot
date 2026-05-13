<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions">
    <template #action="{ row }">
      <a-button type="link" @click="handleRemove(row)">移除</a-button>
    </template>
  </VxeBasicTable>
</template>
<script lang="ts" setup>
  import { ref, reactive, watch, computed } from 'vue';
  import { VxeBasicTable } from '../../../../../VxeTable';
  import type { BasicVxeTableProps, VxeGridInstance } from '../../../../../VxeTable';

  defineOptions({ name: 'PopoverTable' });

  const props = defineProps({
    dataSource: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
    },
    columns: {
      type: Array,
      required: true,
      default: () => [],
    },
  });

  const tableRef = ref<VxeGridInstance>();
  const emit = defineEmits(['remove']);

  const columns = computed(() => {
    console.log(props.columns);
    // 删除 filterRender 和 filters 属性
    return (props.columns as Recordable[]).map((item: Recordable) => ({
      ...item,
      filterRender: undefined,
      filters: undefined,
    }));
  });

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
      ...columns.value,
      {
        title: '操作',
        field: 'operation',
        slots: { default: 'action' },
        width: 100,
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
