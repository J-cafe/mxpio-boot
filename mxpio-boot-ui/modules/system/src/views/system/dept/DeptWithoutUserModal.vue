<template>
  <BasicModal
    v-bind="$attrs"
    width="900px"
    @register="registerModal"
    destroyOnClose
    title="未关联用户"
    @ok="handleSubmit"
  >
    <BasicTable @register="registerTable" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicTable, useTable } from '@mxpio/components';
  import { deptWithoutUser, addDeptUser } from '@mxpio/api';
  import { userColumns } from './dept.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import type { Key } from 'ant-design-vue/lib/table/interface';

  defineOptions({ name: 'DeptWithoutUserModal' });

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const props = defineProps({
    deptId: { type: String },
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
    if (props.deptId) {
      const params = getQueryParams(queryParams, {});
      const data = await deptWithoutUser(props.deptId, params);
      return data;
    }
    return [];
  }

  async function handleSubmit() {
    try {
      const usernames: Key[] = getSelectRowKeys();
      if (usernames.length > 0) {
        const list: {
          deptId?: string;
          userId: number | string;
        }[] = [];
        usernames.forEach((userId) => {
          list.push({
            deptId: props.deptId,
            userId: userId,
          });
        });
        await addDeptUser(list);
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
