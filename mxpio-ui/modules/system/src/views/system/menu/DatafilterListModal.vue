<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    width="900px"
    :showOkBtn="false"
    @register="registerModal"
    title="数据过滤列表"
  >
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增数据过滤 </a-button>
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
    <DatafilterModal @register="registerAuthModal" @success="handleSuccess" :authId="authId" />
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
  import { deleteDatafilter, datafilterList } from '@mxpio/api';
  import { datafilterColumns } from './menu.data';
  import DatafilterModal from './DatafilterModal.vue';

  defineOptions({ name: 'DatafilterListModal' });

  const [registerAuthModal, { openModal }] = useModal();

  defineEmits(['success', 'register']);

  let authId: Ref<string | undefined> = ref();

  const [registerTable, { reload }] = useTable({
    api: loadData,
    rowKey: 'id',
    columns: datafilterColumns,
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
  });

  async function loadData() {
    if (authId.value) {
      const data = await datafilterList(authId.value);
      return data;
    }
    return [];
  }

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    authId.value = data.record.id;
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
    deleteDatafilter(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
