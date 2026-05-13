<template>
  <PageWrapper title="测试">
    <a-card>
      <UserByDeptSelect v-model:value="userId" @change="userChange" />
      <DeptSelect v-model:value="depts" :multiple="true" :treeCheckable="true" />
      <BizSelect
        v-model:value="userId2"
        :columns="columns"
        listUrl="/user/list"
        rowKey="username"
        displayKey="nickname"
        :multiple="false"
      />
      <ApiSelectPage
        v-model:value="userId3"
        url="/user/list"
        show-search
        mode="multiple"
        :optionKey="{ label: 'nickname', value: 'username' }"
      />
      <ImportButton importCode="item" />
      <Button type="primary" danger>111</Button>
      <!-- <MaterialSelect v-model:value="itemCode" /> -->
    </a-card>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { ref, Ref } from 'vue';
  import { PageWrapper, BizSelect, ApiSelectPage } from '@mxpio/components';
  import { ImportButton, UserByDeptSelect, DeptSelect } from '@mxpio/common';
  import { Button } from 'ant-design-vue';

  let userId: Ref<string> = ref('admin');

  let userId2: Ref<string> = ref('');
  let userId3: Ref<string> = ref('admin,1');
  // let itemCode: Ref<string> = ref('');
  let depts = ref('bm001,bm001-1');

  const columns = [
    {
      title: '用户账号',
      field: 'username',
      filterRender: {
        name: 'AInput',
      },
      filters: [{ data: '' }],
    },
    {
      title: '用户姓名',
      field: 'nickname',
      filterRender: {
        name: 'AInput',
      },
      filters: [{ data: '' }],
    },
  ];

  function userChange(val) {
    console.log('userChange', val);
  }

  setTimeout(() => {
    userId2.value = 'admin,S0001';
  }, 2000);
</script>
