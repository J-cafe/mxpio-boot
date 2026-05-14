<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions">
    <template #action>
      <TableAction
        :outside="true"
        :actions="[
          // {
          //   label: '删除',
          //   color: 'error',
          //   popConfirm: {
          //     title: '是否确认删除',
          //     placement: 'left',
          //     confirm: handleDelete.bind(null, row),
          //   },
          //   auth: authConfig.assist,
          //   ifShow: ['20', '25', '30'].includes(task.repairStatus),
          // },
        ]"
      />
    </template>
  </VxeBasicTable>
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction } from '@mxpio/components';
  import { assistColumns } from './execute.data';
  import { assistPageEqpUpkeepApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'OtherRepairAssistTable';
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
    columns: assistColumns,
    pageApi: assistPageEqpUpkeepApi,
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
