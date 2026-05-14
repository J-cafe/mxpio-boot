<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { lotPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './lot.data';

  const componentName = 'LotList';
  defineOptions({ name: componentName });
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: lotPage,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
    },
  });
</script>
