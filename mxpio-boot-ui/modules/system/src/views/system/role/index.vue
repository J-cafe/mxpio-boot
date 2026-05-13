<template>
  <PageWrapper dense contentFullHeight contentClass="flex">
    <BasicTable
      class="w-5/10 m-4 mr-0"
      @register="registerTable"
      @selection-change="selectionChange"
    >
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增角色
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
                label: '分配权限',
                onClick: handlePermiss.bind(null, record),
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
    <div class="w-5/10 m-4 pl-4 overflow-hidden bg-white">
      <a-tabs>
        <a-tab-pane key="1" tab="关联用户">
          <RoleWithinUser :roleId="currentRow.id" />
        </a-tab-pane>
        <a-tab-pane key="2" tab="关联部门">
          <RoleWithinDept :roleId="currentRow.id" />
        </a-tab-pane>
        <a-tab-pane key="3" tab="关联岗位">
          <RoleWithinPost :roleId="currentRow.id" />
        </a-tab-pane>
      </a-tabs>
    </div>

    <RoleModal @register="registerModal" @success="handleSuccess" />
    <PermissDrawer @register="registerDrawer" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  import {
    BasicTable,
    useTable,
    TableAction,
    PageWrapper,
    useDrawer,
    useModal,
  } from '@mxpio/components';
  import { Tabs as ATabs } from 'ant-design-vue';
  import { roleList, deleteRole } from '@mxpio/api';
  import RoleModal from './RoleModal.vue';
  import RoleWithinUser from './RoleWithinUser.vue';
  import RoleWithinDept from './RoleWithinDept.vue';
  import RoleWithinPost from './RoleWithinPost.vue';
  import PermissDrawer from './PermissDrawer.vue';
  import { columns } from './role.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'RoleList' });

  const ATabPane = ATabs.TabPane;
  let currentRow: Recordable = reactive({});

  const [registerModal, { openModal }] = useModal();

  const [registerDrawer, { openDrawer: openDrawer }] = useDrawer();

  const [registerTable, { reload }] = useTable({
    title: '角色列表',
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
    canResize: true,
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
    return roleList(params);
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
    deleteRole(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }

  function handlePermiss(record) {
    openDrawer(true, {
      record,
    });
  }
</script>
