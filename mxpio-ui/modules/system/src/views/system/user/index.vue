<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增用户
        </a-button>
        <import-button importCode="user" />
        <export-button export="sys:user:export" :getParams="getExportParams" />
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
                label: '详情',
                onClick: handleDetail.bind(null, record),
              },
              {
                label: '修改密码',
                onClick: handleUpdatePassword.bind(null, record),
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
    <UserModal :width="900" @register="registerModal" @success="handleSuccess" />
    <UpdatePasswordModal :width="700" @register="registerPasswordModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { ExportButton, ImportButton } from '@mxpio/common';
  import { getUserList, deleteUser } from '@mxpio/api';
  import { columns, searchFormSchema } from './user.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import UserModal from './UserModal.vue';
  import UpdatePasswordModal from './UpdatePasswordModal.vue';

  defineOptions({ name: 'UserList' });

  const [registerModal, { openModal }] = useModal();
  const [registerPasswordModal, { openModal: openPasswordModal }] = useModal();

  const [registerTable, { reload, getPaginationRef, getForm }] = useTable({
    title: '用户列表',
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

  async function loadData(queryParams) {
    const params = getQueryParams(queryParams, {});
    const res = await getUserList(params);
    console.log(res);
    return res || {};
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
  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      disabled: true,
    });
  }

  function handleUpdatePassword(record: Recordable) {
    openPasswordModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDelete(record: Recordable) {
    deleteUser({ username: record.username }).then(() => {
      reload();
    });
  }

  function handleSuccess() {
    reload();
  }

  // 组装导出参数
  function getExportParams() {
    // 获取搜索条件
    const searchFormValue = getForm().getFieldsValue();
    // 获取分页信息
    const pagination = getPaginationRef();
    // 组装参数
    const params = getQueryParams(
      {
        ...searchFormValue,
        page: 1,
        size: typeof pagination !== 'boolean' ? (pagination?.total || 0) + 999 : undefined,
      },
      {},
    );
    return params;
  }
</script>
