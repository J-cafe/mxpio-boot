<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { qualityRecordPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './qualityRecord.data';

  const componentName = 'QualityRecordList';
  defineOptions({ name: componentName });

  const { gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: qualityRecordPageApi,
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [],
        import: false,
      },
    },
    module: 'erp',
  });
</script>
