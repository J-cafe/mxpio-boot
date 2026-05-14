<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #drawingNo="{ row }">
        <a-tag
          v-if="
            dateUtil().isAfter(dateUtil(row.suggestArriveDate), 'day') &&
            row.actualQuantity !== row.orderQuantity
          "
          color="red"
          >{{ row.drawingNo }}</a-tag
        >
        <span v-else>{{ row.drawingNo }}</span>
      </template>
    </VxeBasicTable>
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { purchaseExecutePage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './purchaseOrderExecute.data';
  import { Tag as ATag } from 'ant-design-vue';
  import { dateUtil } from '@mxpio/utils';

  const componentName = 'PurchaseOrderExecuteList';
  defineOptions({ name: componentName });

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: purchaseExecutePage,
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [],
        import: false,
      },
      sortConfig: {
        defaultSort: {
          field: 'orderDate',
          order: 'desc',
        },
      },
    },
    module: 'erp',
  });
</script>
