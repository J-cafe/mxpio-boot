<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" :disabled="!props.deptId" @click="handleCreate">
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
    <DeptWithoutUserModal :deptId="deptId" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { deptWithinUser, deleteDeptUser } from '@mxpio/api';
  import { watch, unref } from 'vue';
  import DeptWithoutUserModal from './DeptWithoutUserModal.vue';

  import { userColumns } from './dept.data';

  defineOptions({ name: 'DeptWithinUser' });

  const props = defineProps({
    deptId: {
      type: String,
      required: true,
    },
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
    rowKey: 'username',
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
    () => unref(props.deptId),
    () => {
      reload();
    },
  );

  async function loadData() {
    if (props.deptId) {
      const data = await deptWithinUser(props.deptId);
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
    deleteDeptUser(props.deptId, record.username).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
