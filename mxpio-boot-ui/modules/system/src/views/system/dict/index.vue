<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增字典
        </a-button>
      </template>
      <template #expandedRowRender="{ record }">
        <div class="dict-item-table">
          <BasicTable :dataSource="record.items" @register="registerItemTable">
            <template #bodyCell="{ column, record: item }">
              <template v-if="column.key === 'ItemAction'">
                <TableAction
                  :actions="[
                    {
                      label: '编辑',
                      onClick: handleEditItem.bind(null, item),
                    },
                    {
                      label: '删除',
                      color: 'error',
                      popConfirm: {
                        title: '是否确认删除',
                        placement: 'left',
                        confirm: handleDeleteItem.bind(null, item),
                      },
                    },
                  ]"
                />
              </template>
            </template>
          </BasicTable>
        </div>
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
                label: '新增字典项',
                onClick: handleAddItem.bind(null, record),
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
    <DictModal @register="registerModal" @success="handleSuccess" />
    <DictItemModal @register="registerItemModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { dictList, deleteDict, deleteDictItem } from '@mxpio/api';

  import DictModal from './DictModal.vue';
  import DictItemModal from './DictItemModal.vue';
  import { columns, searchFormSchema, dictItemColumns } from './dict.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'PostList' });

  const [registerModal, { openModal }] = useModal();
  const [registerItemModal, { openModal: openItemModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '字典列表',
    api: loadData,
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
    columns,
    formConfig: {
      labelWidth: 120,
      schemas: searchFormSchema,
    },
    striped: false,
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    canResize: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: undefined,
    },
    pagination: { pageSize: 10 },
  });

  const [registerItemTable] = useTable({
    columns: dictItemColumns,
    striped: false,
    // showTableSetting: true,
    bordered: true,
    canResize: false,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'ItemAction',
      fixed: undefined,
    },
    pagination: false,
  });

  async function loadData(queryParams) {
    const params = getQueryParams(queryParams, {});
    const dataSource = await dictList(params);
    return dataSource;
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
    deleteDict(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }

  function handleAddItem(record: Recordable) {
    openItemModal(true, {
      dict: record,
      isUpdate: false,
    });
  }

  function handleDeleteItem(record: Recordable) {
    deleteDictItem(record.id).then(() => {
      reload();
    });
  }

  function handleEditItem(record: Recordable) {
    openItemModal(true, {
      record,
      isUpdate: true,
    });
  }
</script>
<style lang="less">
  .dict-item-table {
    .ant-table.ant-table-middle {
      margin-left: 0 !important;
    }
  }
</style>
