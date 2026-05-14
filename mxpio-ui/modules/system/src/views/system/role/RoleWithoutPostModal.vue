<template>
  <BasicModal
    v-bind="$attrs"
    width="900px"
    title="未关联用户"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicTable @register="registerTable" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicTable, useTable } from '@mxpio/components';
  import { roleWithoutPost, addRoleActors } from '@mxpio/api';
  import { postColumns } from './role.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import type { Key } from 'ant-design-vue/lib/table/interface';

  defineOptions({ name: 'RoleWithoutPostModal' });

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const props = defineProps({
    roleId: { type: String, required: true },
  });

  const [registerTable, { reload, getSelectRowKeys }] = useTable({
    api: loadData,
    rowKey: 'id',
    rowSelection: { type: 'checkbox' },
    columns: postColumns,
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
      const data = await roleWithoutPost(props.roleId, params);
      return data;
    }
    return [];
  }

  async function handleSubmit() {
    try {
      const ids: Key[] = getSelectRowKeys();
      if (ids.length > 0) {
        await addRoleActors(props.roleId, ids);
        closeModal();
        emit('success');
      } else {
        createMessage.success('请选择关联岗位');
      }
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
