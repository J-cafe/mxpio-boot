<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleImport">
          导入Entity
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '详情',
                onClick: handleDetail.bind(null, record),
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
    <ImportModal @register="registerModal" @success="handleImportSuccess" />
  </div>
</template>

<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import ImportModal from './ImportModal.vue';
  import { useBridge } from '@mxpio/bridge';

  const { http } = useBridge();
  const [registerModal, { openModal }] = useModal();

  const columns = [
    { title: '模型编码', dataIndex: 'modelCode', key: 'modelCode' },
    { title: '模型名称', dataIndex: 'modelName', key: 'modelName' },
    { title: '实体类', dataIndex: 'entityClass', key: 'entityClass' },
    { title: '数据库表', dataIndex: 'tableName', key: 'tableName' },
    { title: '操作', key: 'action', width: 120 },
  ];

  async function loadData(_params: any) {
    const res: any = await http.get({ url: '/lowcode/model/list', params: _params || {} });
    return res || {};

    // const res: any = await http.get({ url: '/lowcode/model/list' });
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

  function handleImport() {
    openModal(true, {});
  }

  function handleImportSuccess() {
    reload();
  }

  function handleDetail(record: any) {
    openModal(true, { detail: record });
  }

  async function handleDelete(record: any) {
    await http.delete({ url: `/lowcode/model/${record.modelCode}` });
    reload();
  }
</script>
