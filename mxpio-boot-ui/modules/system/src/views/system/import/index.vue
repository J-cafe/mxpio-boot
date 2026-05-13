<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable
      class="w-4/10 m-4 mr-0"
      @register="registerTable"
      @selection-change="selectionChange"
    >
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增
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
                label: '生成规则映射',
                onClick: handleCreateMappingRule.bind(null, record),
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
    <ImportItem
      :mappingRules="mappingRules || []"
      :importId="currentRow.id"
      @success="handleSuccess"
      class="w-6/10 m-4 overflow-hidden bg-white"
    />
    <ImportModal @register="registerModal" @success="handleSuccess" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { reactive } from 'vue';
  import { BasicTable, useTable, TableAction, PageWrapper, useModal } from '@mxpio/components';
  import { importList, deleteImport, createMappingRuleList } from '@mxpio/api';
  import ImportModal from './ImportModal.vue';
  import ImportItem from './ImportItem.vue';
  import { columns } from './import.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'ImportList' });
  let currentRow: Recordable = reactive({});
  let mappingRules: Recordable[] = reactive([]);

  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload, setSelectedRowKeys }] = useTable({
    title: '导入模板',
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

    actionColumn: {
      width: 100,
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
    pagination: { pageSize: 10 },
  });

  function selectionChange({ rows }) {
    Object.assign(currentRow, rows[0]);
    mappingRules.length = 0;
    currentRow.mappingRules.forEach((item) => {
      mappingRules.push(item);
    });
  }

  async function loadData(queryParams) {
    const params = getQueryParams(queryParams, {});
    const res = await importList(params);
    // 重新出发选中事件
    setTimeout(() => {
      currentRow.id && setSelectedRowKeys([currentRow.id]);
    }, 60);
    return res;
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
    deleteImport(record.id).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }

  async function handleCreateMappingRule(record: Recordable) {
    try {
      await createMappingRuleList(record);
      reload();
    } catch (error) {
      console.log(error);
    }
  }
</script>
