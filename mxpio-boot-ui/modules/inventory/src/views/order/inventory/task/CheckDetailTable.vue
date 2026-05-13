<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { useListCrudHook } from '@mxpio/common';
  import { invlinePageApi } from '@mxpio/bizcommon';
  import { executeSearchFormSchema, detailColumns } from './inventoryTask.data';

  const componentName = 'InventoryTaskCheckDetailTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    formDataRef: {
      type: Object,
      default: () => ({}),
    },
  });

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: detailColumns,
    searchFormSchema: executeSearchFormSchema,
    pageApi: invlinePageApi,
    vxeGridOptions: {
      height: 500,
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
      sortConfig: {
        defaultSort: {
          field: 'lineNo',
          order: 'asc',
        },
      },
      proxyConfig: {
        autoLoad: false,
      },
    },
    module: 'erp',
    filters: () => ({
      'inventoryCode@EQ': props.formDataRef.inventoryCode,
    }),
  });

  async function setData() {
    tableRef.value?.commitProxy('query');
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  defineExpose({
    setData,
    validate,
  });
</script>
