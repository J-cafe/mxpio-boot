<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          preIcon="ant-design:plus-outlined"
          :disabled="!props.roleId"
          @click="handleCreate"
        >
          新增关联
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
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
    <RoleWithoutUserModal :roleId="roleId" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { roleWithinUser, deleteRoleActors } from '@mxpio/api';
  import { watch, unref } from 'vue';
  import RoleWithoutUserModal from './RoleWithoutUserModal.vue';

  import { userColumns } from './role.data';

  defineOptions({ name: 'RoleWithinUser' });

  const props = defineProps({
    roleId: { type: String, required: true },
  });

  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '已关联用户',
    api: loadData,
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
    rowKey: 'id',
    columns: userColumns,
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
  });

  watch(
    () => unref(props.roleId),
    () => {
      reload();
    },
  );

  async function loadData() {
    if (props.roleId) {
      const data = await roleWithinUser(props.roleId);
      return data;
    }
    return [];
  }

  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleDelete(record: Recordable) {
    if (!props?.roleId) {
      return;
    }
    deleteRoleActors(props.roleId, [record.actorId]).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
