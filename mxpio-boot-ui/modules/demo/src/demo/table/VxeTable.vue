<template>
  <PageWrapper
    title="VxeTable表格"
    content="只展示部分操作，详细功能请查看VxeTable官网事例"
    contentFullHeight
    fixedHeight
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" @filter-change="filterChangeEvent">
      <template #action="{ row }">
        <TableAction outside :actions="createActions(row)" />
      </template>
    </VxeBasicTable>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import {
    ActionItem,
    TableAction,
    PageWrapper,
    BasicTableProps,
    VxeBasicTable,
    VxeGridInstance,
  } from '@mxpio/components';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import { vxeTableColumns, vxeTableFormSchema } from './tableData';

  const { createMessage } = useMessage();

  const tableRef = ref<VxeGridInstance>();

  const gridOptions = reactive<BasicTableProps>({
    id: 'VxeTable',
    height: '700px',
    keepSource: true,
    editConfig: { trigger: 'click', mode: 'row', showStatus: true, autoClear: false },
    columns: vxeTableColumns,
    toolbarConfig: {
      buttons: [
        {
          content: '在第一行新增',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              preIcon: 'mdi:page-next-outline',
            },
            events: {
              click: () => {
                tableRef.value?.insert({
                  name: '新增的',
                  beginTime: '2025-10-11',
                  edition: '1,2',
                  // ApiSelect: [],
                  ApiSelect: undefined,
                });
                createMessage.success('新增成功');
              },
            },
          },
        },
        {
          content: '在最后一行新增',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'warning',
            },
            events: {
              click: () => {
                tableRef.value?.insertAt({ name: '新增的' }, -1);
              },
            },
          },
        },
      ],
      refreshOptions: {
        queryMethod: () => {
          tableRef.value?.commitProxy('query');
        },
      },
      ajax: {
        query: async ({ page, form, sorts }) => {
          console.log({ page, form, sorts });
        },
      },
    },
    formConfig: {
      enabled: true,
      items: vxeTableFormSchema,
    },
    filterConfig: {
      remote: true,
    },
    floatingFilterConfig: {
      enabled: true,
    },
    data: [
      {
        name: '测试1',
        beginTime: '2025-10-11',
      },
    ],
    proxyConfig: { enabled: false },
    // proxyConfig: {
    //   ajax: {
    //     query: async ({ page, form }) => {
    //       return demoListApi({
    //         page: page.currentPage,
    //         pageSize: page.pageSize,
    //         ...form,
    //       });
    //     },
    //     queryAll: async ({ form }) => {
    //       return await demoListApi(form);
    //     },
    //   },
    // },
  });

  // 操作按钮（权限控制）
  const createActions = (record) => {
    const actions: ActionItem[] = [
      {
        label: '详情',
        onClick: () => {
          console.log(record);
        },
      },
      {
        label: '编辑',
        onClick: () => {},
      },
      {
        label: '删除',
        color: 'error',
        popConfirm: {
          title: '是否确认删除',
          confirm: () => {
            tableRef.value?.remove(record);
          },
        },
      },
    ];

    return actions;
  };

  async function filterChangeEvent({ page, form, sorts, filters }) {
    try {
      tableRef.value?.commitProxy('query');
      console.log({ page, form, sorts, filters });
      // const checkedFilters = tableRef.value?.getCheckedFilters();
    } catch (error) {
      console.log(error);
    }
  }
</script>
