<template>
  <BasicModal
    v-bind="$attrs"
    title="未关联用户"
    width="900px"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicTable @register="registerTable" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicTable, useTable } from '@mxpio/components';
  import { roleWithoutUser, addRoleActors } from '@mxpio/api';
  import { userColumns } from './role.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import type { Key } from 'ant-design-vue/lib/table/interface';

  defineOptions({ name: 'RoleWithoutUserModal' });

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const props = defineProps({
    roleId: { type: String, required: true },
  });

  const [registerTable, { reload, getSelectRowKeys }] = useTable({
    api: loadData,
    rowKey: 'username',
    rowSelection: { type: 'checkbox' },
    columns: userColumns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    canResize: true,
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    reload();
  });

  async function loadData(queryParams) {
    if (props.roleId) {
      const params = getQueryParams(queryParams, {});
      const data = await roleWithoutUser(props.roleId, params);
      return data;
    }
    return [];
  }

  async function handleSubmit() {
    try {
      const usernames: Key[] = getSelectRowKeys();
      if (usernames.length > 0) {
        await addRoleActors(props.roleId, usernames);
        closeModal();
        emit('success');
      } else {
        createMessage.success('请选择关联用户');
      }
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
