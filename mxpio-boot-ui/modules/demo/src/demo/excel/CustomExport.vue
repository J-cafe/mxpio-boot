<template>
  <PageWrapper title="导出示例" content="可以选择导出格式">
    <BasicTable title="基础表格" :columns="columns" :dataSource="data">
      <template #toolbar>
        <a-button @click="openModal"> 导出 </a-button>
      </template>
    </BasicTable>
    <ExpExcelModal @register="register" @success="defaultHeader" />
  </PageWrapper>
</template>

<script lang="ts" setup>
  import {
    BasicTable,
    jsonToSheetXlsx,
    ExpExcelModal,
    ExportModalResult,
    useModal,
    PageWrapper,
  } from '@mxpio/components';
  import { columns, data } from './data';

  function defaultHeader({ filename, bookType }: ExportModalResult) {
    // 默认Object.keys(data[0])作为header
    jsonToSheetXlsx({
      data,
      filename,
      write2excelOpts: {
        bookType,
      },
    });
  }
  const [register, { openModal }] = useModal();
</script>
