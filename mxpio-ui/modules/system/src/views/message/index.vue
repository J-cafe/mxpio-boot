<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleRead"> 一键已读 </a-button>
        <a-button type="primary" @click="handleSend"> 发送消息 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            :actions="[
              {
                label: '详情',
                onClick: handleDetail.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <MessageModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { BasicTable, useTable, TableAction, useModal } from '@mxpio/components';
  import { messageList, readMessage, sendMessage } from '@mxpio/api';

  import { columns, searchFormSchema } from './message.data';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import MessageModal from './MessageModal.vue';

  defineOptions({ name: 'MessageList' });

  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '消息列表',
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
    return messageList('innerMsg', params);
  }

  function handleRead() {
    readMessage('innerMsg').then(() => {
      reload();
    });
  }

  function handleSend() {
    sendMessage({
      channel: 'innerMsg',
      from: 'admin',
      to: '1',
      title: '我是测试的标题',
      content: '我是测试的内容',
    }).then(() => {
      reload();
      console.log('发送成功');
    });
  }

  function handleSuccess() {
    reload();
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
    });
  }
</script>
