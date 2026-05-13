<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable
      class="w-6/10 m-4 mr-0"
      @register="registerTable"
      @selection-change="selectionChange"
    >
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增部门 </a-button>
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
                label: '新增子部门',
                onClick: handleAddItem.bind(null, record),
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
    <DeptWithinUser :deptId="currentRow.id" class="w-4/10 m-4 overflow-hidden bg-white" />
    <DeptModal @register="registerModal" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  import { BasicTable, useTable, TableAction, PageWrapper, useModal } from '@mxpio/components';
  import { deptList, deleteDept } from '@mxpio/api';
  import DeptModal from './DeptModal.vue';
  import DeptWithinUser from './DeptWithinUser.vue';
  import { columns } from './dept.data';

  defineOptions({ name: 'DeptList' });
  let currentRow: Recordable = reactive({});
  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '部门列表',
    api: loadData,
    clickToRowSelect: true,
    rowSelection: { type: 'radio' },
    isTreeTable: true,
    accordion: true, // 手风琴效果
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
  });

  function selectionChange({ rows }) {
    Object.assign(currentRow, rows[0]);
  }

  function loadData() {
    return deptList();
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
    deleteDept(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
