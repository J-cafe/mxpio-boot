<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { flowPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './invFlow.data';

  const componentName = 'InvFlowList';
  defineOptions({ name: componentName });
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: flowPage,
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
