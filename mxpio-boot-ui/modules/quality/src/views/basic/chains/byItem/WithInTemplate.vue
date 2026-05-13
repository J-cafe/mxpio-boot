<template>
  <div>
    <VxeBasicTable ref="tableRef1" v-bind="gridOptions">
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
  </div>
</template>
<script lang="ts" setup>
  import { useListCrudHook } from '@mxpio/common';
  import { VxeBasicTable } from '@mxpio/components';
  import { columns } from './byItem.data';
  import { qtChainTemplateWithinApi, qtLineListApi } from '@mxpio/bizcommon';
  import ExpandTable from './ExpandTable.vue';
  import { watch } from 'vue';

  const componentName = 'QualityChainsWithInTemplate';
  defineOptions({ name: componentName });

  const props = defineProps({
    code: {
      type: String,
      default: '',
    },
    datascope: {
      type: String,
      default: '1',
    },
  });

  watch(
    () => props.code,
    () => {
      if (!props.code) {
        tableRef.value?.loadData([]);
        return;
      }
      tableRef.value?.commitProxy('query');
    },
    {
      deep: true,
    },
  );

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    pageApi: (params) => {
      return qtChainTemplateWithinApi(props.code, props.datascope, params);
    },
    vxeGridOptions: {
      rowConfig: {
        keyField: 'code',
      },
      toolbarConfig: {
        buttons: [],
        export: false,
        import: false,
      },
      proxyConfig: {
        autoLoad: false,
      },
      pagerConfig: {
        enabled: false,
      },
      formConfig: {
        enabled: false,
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
      tableClass: '!px-0 !py-0',
      height: '',
    },
    module: 'erp',
  });

  async function loadContentMethod({ row }) {
    const res = await qtLineListApi(row.code);
    row.childList = res;
    return res;
  }
</script>
