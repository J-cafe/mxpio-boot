<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          preIcon="ant-design:plus-outlined"
          :disabled="!props.id"
          @click="handleCreate"
          >新增字段
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
    <ExportItemModal :id="id" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { exportColumnsList, deleteExportColumn } from '@mxpio/api';
  import { watch, unref } from 'vue';
  import ExportItemModal from './ExportItemModal.vue';
  import { itemColumns } from './export.data';

  defineOptions({ name: 'ExportItem' });

  const props = defineProps({
    id: { type: String },
  });

  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '导出字段',
    api: loadData,
    rowKey: 'id',
    columns: itemColumns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
    pagination: false,
  });

  watch(
    () => unref(props.id),
    () => {
      reload();
    },
  );

  async function loadData() {
    if (props.id) {
      const data = await exportColumnsList(props.id);
      return data;
    }
    return [];
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
    deleteExportColumn(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
