<template>
  <a-card :bodyStyle="{ padding: '12px' }" class="m-3">
    <a-tabs tabPosition="left">
      <a-tab-pane key="1">
        <template #tab>
          <span>
            <user-outlined />
            待办任务
          </span>
        </template>
        <myPendingTask />
      </a-tab-pane>
      <a-tab-pane key="2">
        <template #tab>
          <span>
            <history-outlined />
            审核记录
          </span>
        </template>
        <myFinishedTask />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script lang="ts" setup>
  import { UserOutlined, HistoryOutlined } from '@ant-design/icons-vue';
  import myPendingTask from './myPendingTask.vue';
  import myFinishedTask from './myFinishedTask.vue';

  defineOptions({ name: 'MyTaskList' });
</script>
