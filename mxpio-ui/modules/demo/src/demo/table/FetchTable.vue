<template>
  <PageWrapper contentBackground contentClass="flex" dense contentFullHeight fixedHeight>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleReloadCurrent"> 刷新当前页 </a-button>
        <a-button type="primary" @click="handleReload"> 刷新并返回第一页 </a-button>
      </template>
    </BasicTable>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, PageWrapper } from '@mxpio/components';
  import { getBasicColumns } from './tableData';

  import { demoListApi } from '../../api/table';

  const [registerTable, { reload }] = useTable({
    title: '远程加载示例',
    api: demoListApi,
    columns: getBasicColumns(),
    pagination: { pageSize: 10 },
  });
  function handleReloadCurrent() {
    reload();
  }

  function handleReload() {
    reload({ page: 1 });
  }
</script>
