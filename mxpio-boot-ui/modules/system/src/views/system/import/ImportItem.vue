<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          preIcon="ant-design:plus-outlined"
          :disabled="!importId"
          @click="handleCreate"
          >新增字段
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
    <ImportItemModal :importId="importId" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { deleteRule } from '@mxpio/api';
  import { defineProps, defineEmits, PropType } from 'vue';
  import ImportItemModal from './ImportItemModal.vue';
  import { itemColumns } from './import.data';

  defineOptions({ name: 'ImportItem' });

  const emit = defineEmits(['success']);

  const props = defineProps({
    mappingRules: { type: Array as PropType<Recordable[]>, default: () => [] }, //导入规则列表
    importId: { type: String }, // 导入模版id
  });

  const [registerModal, { openModal }] = useModal();
  const [registerTable] = useTable({
    title: '导入字段',
    dataSource: props.mappingRules || [],
    rowKey: 'id',
    columns: itemColumns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    // canResize: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
    pagination: false,
  });

  async function loadData() {
    emit('success');
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
    deleteRule(record.id).then(() => {
      loadData();
    });
  }

  function handleSuccess() {
    loadData();
  }
</script>
