<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable
      class="w-4/10 m-4 mr-0"
      @register="registerTable"
      @selection-change="selectionChange"
    >
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '编辑',
                onClick: handleEdit.bind(null, record),
              },
            ]"
            :dropDownActions="[
              {
                label: '删除',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <ExportItem :id="currentRow.id" class="w-6/10 m-4 overflow-hidden bg-white" />
    <ExportModal @register="registerModal" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  import { BasicTable, useTable, TableAction, PageWrapper, useModal } from '@mxpio/components';
  import { exportList, deleteExport } from '@mxpio/api';
  import ExportModal from './ExportModal.vue';
  import ExportItem from './ExportItem.vue';
  import { columns } from './export.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'ExportList' });
  let currentRow: Recordable = reactive({});
  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '导出模板',
    api: loadData,
    clickToRowSelect: true,
    rowSelection: { type: 'radio' },
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
    rowKey: 'id',
    columns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
    pagination: { pageSize: 10 },
  });

  function selectionChange({ rows }) {
    Object.assign(currentRow, rows[0]);
  }

  function loadData(queryParams) {
    const params = getQueryParams(queryParams, {});
    return exportList(params);
  }

  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDelete(record: Recordable) {
    deleteExport(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
