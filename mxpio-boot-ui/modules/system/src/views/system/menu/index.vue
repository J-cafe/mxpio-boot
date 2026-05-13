<template>
  <div class="p-4">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'sys:menu:add'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增菜单
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
                label: '新增子菜单',
                onClick: handleAddItem.bind(null, record),
              },
              {
                label: '权限资源',
                onClick: handleAuth.bind(null, record),
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
    <MenuModal @register="registerModal" @success="handleSuccess" />
    <AuthListModal @register="registerAuthModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { menuList, deleteMenu } from '@mxpio/api';
  import MenuModal from './MenuModal.vue';
  import AuthListModal from './AuthListModal.vue';
  import { columns } from './menu.data';

  defineOptions({ name: 'MenuList' });

  const [registerModal, { openModal }] = useModal();

  const [registerAuthModal, { openModal: openAuthModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '菜单列表',
    api: loadData,
    isTreeTable: true,
    accordion: true, // 手风琴效果
    rowKey: 'id',
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
    columns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    canResize: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
    pagination: { pageSize: 10 },
  });

  function loadData() {
    // const params = getQueryParams(queryParams, {});
    return menuList();
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

  function handleAddItem(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: false,
    });
  }

  function handleDelete(record: Recordable) {
    deleteMenu({ id: record.id }).then(() => {
      reload();
    });
  }

  function handleAuth(record: Recordable) {
    openAuthModal(true, {
      record,
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
