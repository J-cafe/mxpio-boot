<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增任务
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
                label: '启动',
                popConfirm: {
                  title: '是否确认启动',
                  placement: 'left',
                  confirm: handleRun.bind(null, record.id),
                },
                ifShow: (_action) => {
                  return record.status !== 'RUNNING'; // 根据业务控制是否显示: 非enable状态的不显示启用按钮
                },
              },
              {
                label: '立即执行',
                popConfirm: {
                  title: '确认执行',
                  placement: 'left',
                  confirm: handleExecute.bind(null, record.id),
                },
              },
              {
                label: '暂停',
                popConfirm: {
                  title: '是否确认暂停',
                  placement: 'left',
                  confirm: handlePause.bind(null, record.id),
                },
                ifShow: (_action) => {
                  return record.status !== 'PAUSE'; // 根据业务控制是否显示: 非enable状态的不显示启用按钮
                },
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
    <QuartzJobModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { quartzList, deleteQuartz, runQuartz, pauseQuartz, executeQuartz } from '@mxpio/api';
  import QuartzJobModal from './QuartzJobModal.vue';
  import { columns, searchFormSchema } from './quartzJob.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'QuartzJobList' });

  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '定时任务列表',
    api: loadData,
    fetchSetting: {
      sizeField: 'size',
      listField: 'content',
      totalField: 'totalElements',
    },
    columns,
    formConfig: {
      labelWidth: 120,
      schemas: searchFormSchema,
    },
    striped: false,
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    canResize: true,
    actionColumn: {
      width: 120,
      title: '操作',
      dataIndex: 'action',
      fixed: undefined,
    },
    pagination: { pageSize: 10 },
  });

  function loadData(queryParams) {
    const params = getQueryParams(queryParams, {});
    return quartzList(params);
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
    deleteQuartz(record.id).then(() => {
      reload();
    });
  }

  function handleRun(id: string) {
    runQuartz(id).then(() => {
      reload();
    });
  }

  function handlePause(id: string) {
    pauseQuartz(id).then(() => {
      reload();
    });
  }

  function handleExecute(id: string) {
    executeQuartz({ id }).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
