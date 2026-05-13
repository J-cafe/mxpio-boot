<template>
  <div>
    <VxeBasicTable class="!px-0 !py-0" ref="tableRef" v-bind="gridOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicVxeTableProps, VxeBasicTable, VxeGridInstance } from '@mxpio/components';
  import type { VxeGridPropTypes } from '@mxpio/components';
  import { columnList, dataList } from '../../api/dbconsole';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile } from '@mxpio/hooks';
  import { reactive, ref, defineProps, watch, nextTick } from 'vue';

  defineOptions({ name: 'DBColumnList' });

  const { restoreStore, updateStore } = useProfile();

  const props = defineProps({
    table: {
      type: Object,
      default: () => ({}),
    },
  });

  watch(
    () => props.table,
    (newVal) => {
      if (newVal.dbInfoId && newVal.tableName) {
        getColumn();
        nextTick(() => {
          tableRef.value?.commitProxy('query');
        });
      } else {
        nextTick(() => {
          tableRef.value?.loadColumn([]);
          tableRef.value?.clearAll();
        });
      }
    },
    { deep: true, immediate: true },
  );

  const tableRef = ref<VxeGridInstance>();
  let columns = reactive<VxeGridPropTypes.Columns>([]);

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'TableDataList',
    columns: [],
    toolbarConfig: {
      buttons: [],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              export: 'sys:TableDataList:export',
            },
          },
        },
      ],
    },
    customConfig: {
      storage: {
        visible: true,
        resizable: true,
        sort: true,
        fixed: true,
      }, // 启用自定义列状态保存功能
      restoreStore: restoreStore,
      updateStore: updateStore,
    },
    maxHeight: 700,
    proxyConfig: {
      autoLoad: false,
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
      other: {
        tableName: props.table?.tableName,
      },
    });
    const res = await dataList(props.table?.dbInfoId, params);
    return res;
  }

  async function getColumn() {
    const res = await columnList(props.table?.dbInfoId, props.table?.tableName);
    columns = [
      {
        title: '序号',
        type: 'seq',
        fixed: 'left',
        width: '50',
        align: 'center',
      },
    ];
    res.forEach((item: Recordable) => {
      columns.push({
        title: item.columnName,
        field: item.columnName,
        width: 100,
      });
    });
    tableRef.value?.loadColumn(columns);
  }
</script>
