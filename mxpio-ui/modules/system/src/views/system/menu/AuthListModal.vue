<template>
  <BasicModal
    v-bind="$attrs"
    width="900px"
    :showOkBtn="false"
    @register="registerModal"
    title="权限资源"
  >
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增权限 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'hasCriteria'">
          <span>{{ record.hasCriteria ? '可过滤' : '不可过滤' }}</span>
        </template>
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
                label: '过滤权限设置',
                onClick: handleDataFilter.bind(null, record),
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
    <AuthModal @register="registerAuthModal" @success="handleSuccess" :urlId="urlId" />
    <DatafilterListModal @register="registerDatafilterModal" @success="handleSuccess" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, Ref } from 'vue';
  import {
    BasicModal,
    useModalInner,
    useModal,
    BasicTable,
    useTable,
    TableAction,
  } from '@mxpio/components';
  import { resDataList, deleteAuth } from '@mxpio/api';
  import { authColumns } from './menu.data';
  import AuthModal from './AuthModal.vue';
  import DatafilterListModal from './DatafilterListModal.vue';

  defineOptions({ name: 'AuthListModal' });

  const [registerAuthModal, { openModal }] = useModal();

  const [registerDatafilterModal, { openModal: openDatafilterModal }] = useModal();

  defineEmits(['success', 'register']);

  let urlId: Ref<string> = ref('');

  const [registerTable, { reload }] = useTable({
    api: loadData,
    rowKey: 'id',
    columns: authColumns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    canResize: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: undefined,
    },
    pagination: false,
    maxHeight: 300,
  });

  async function loadData() {
    if (urlId.value) {
      const data = await resDataList(urlId.value);
      return data;
    }
    return [];
  }

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    urlId.value = data.record.id;
  });

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
    deleteAuth(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }

  function handleDataFilter(record: Recordable) {
    openDatafilterModal(true, {
      record,
    });
  }
</script>
