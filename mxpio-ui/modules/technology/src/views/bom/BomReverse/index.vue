<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { bomReverseList } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './bomReverse.data';
  import { dateUtil } from '@mxpio/utils';
  import { useMessage } from '@mxpio/hooks';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';

  const componentName = 'BomReverseList';
  defineOptions({ name: componentName });

  const { createMessage } = useMessage();
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: bomReverseList,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bomId',
      },
      toolbarConfig: {
        buttons: [],
      },
      proxyConfig: {
        ajax: {
          query: async ({ page, form, sorts }: VxeTableQueryParams) => {
            return loadData({ page, form, sorts });
          },
        },
        autoLoad: false,
      },
      pagerConfig: {
        enabled: false,
      },
    },
    module: 'erp',
  });

  async function loadData({ form }: VxeTableQueryParams) {
    if (!form?.itemCode || !form?.beginTime) {
      createMessage.error('请输入物料编码和开始时间');
      return [];
    }
    const res = await bomReverseList({
      itemCode: form?.itemCode,
      date: dateUtil(form?.beginTime).format('YYYY-MM-DD') + ' 00:00:00',
    });
    return res;
  }
</script>
