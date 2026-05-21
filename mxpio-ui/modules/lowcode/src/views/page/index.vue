<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新建页面
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '设计',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '删除',
                popConfirm: {
                  title: '确定删除？',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
  </div>
</template>

<script lang="ts" setup>
  import { BasicTable, useTable, TableAction } from '@mxpio/components';
  import { useRouter } from 'vue-router';
  import { useBridge } from '@mxpio/bridge';

  const router = useRouter();
  const { http } = useBridge();

  const columns = [
    { title: '页面编码', dataIndex: 'pageCode', key: 'pageCode' },
    { title: '页面名称', dataIndex: 'pageName', key: 'pageName' },
    { title: '关联模型', dataIndex: 'modelCode', key: 'modelCode' },
    { title: '页面类型', dataIndex: 'pageType', key: 'pageType' },
    { title: '操作', key: 'action', width: 180 },
  ];

  async function loadData(_params: any) {
    const res: any = await http.get({ url: '/lowcode/page/list', params: _params || {} });
    return res || {};
    // return { content: res || [], totalElements: (res || []).length };
  }

  const [registerTable, { reload }] = useTable({
    columns,
    api: loadData,
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
    showIndexColumn: false,
    pagination: false,
    useSearchForm: false,
    bordered: true,
  });

  function handleCreate() {
    router.push({ path: '/lowcode/designer' });
  }

  function handleEdit(record: any) {
    router.push({ path: '/lowcode/designer', query: { pageCode: record.pageCode } });
  }

  async function handleDelete(record: any) {
    await http.delete({ url: `/lowcode/page/${record.pageCode}` });
    reload();
  }
</script>
