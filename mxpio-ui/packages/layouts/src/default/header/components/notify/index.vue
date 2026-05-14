<template>
  <div :class="prefixCls">
    <Popover title="" trigger="click" :overlayClassName="`${prefixCls}__overlay`">
      <Badge :count="count" :offset="[-5, 15]" :numberStyle="numberStyle">
        <BellOutlined />
      </Badge>
      <template #content>
        <NoticeList style="width: 250px" :list="messageData" @title-click="onNoticeClick" />
      </template>
    </Popover>
  </div>
</template>
<script lang="ts" setup>
  import { computed, ref, onMounted, reactive, onBeforeUnmount, watchEffect } from 'vue';
  import { Popover, Badge } from 'ant-design-vue';
  import { BellOutlined } from '@ant-design/icons-vue';
  import NoticeList from './NoticeList.vue';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { useWebSocket } from '@vueuse/core';
  import { useBridge } from '@mxpio/bridge';
  import { myUnread } from '@mxpio/api';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import { useUserStoreWithOut } from '@mxpio/stores';

  // const useUserStoreWithOut = useStore('useUserStoreWithOut');
  const { env } = useBridge();
  const { VITE_GLOB_API_URL: apiUrl } = env;
  const userStore = useUserStoreWithOut();

  const { prefixCls } = useDesign('header-notify');

  const numberStyle = {};
  const count = ref(0);
  const messageData = ref([]);

  const state = reactive({
    server: 'ws://' + window.location.host + apiUrl + '/ws/message',
    sendValue: '',
    recordList: [] as { id: number; time: number; res: string }[],
  });

  const { data, close, status } = useWebSocket(state.server, {
    autoReconnect: false,
    heartbeat: {
      message: JSON.stringify({ type: 'ping' }),
      interval: 3 * 60 * 1000,
      pongTimeout: 1000,
    },
    protocols: [userStore.getToken],
  });

  watchEffect(() => {
    if (data.value) {
      loadData();
    }
  });

  const getIsOpen = computed(() => status.value === 'OPEN');

  async function loadData() {
    const params = getQueryParams({ page: 1, size: 5, field: 'createTime', order: 'descend' }, {});
    const res = await myUnread(params);
    count.value = res.totalElements;
    messageData.value = res.content;
  }

  function onNoticeClick() {
    loadData();
  }

  onMounted(() => {
    if (!getIsOpen.value) {
      // open();
    }
    loadData();
  });

  onBeforeUnmount(() => {
    if (getIsOpen.value) {
      close();
    }
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-header-notify';

  .@{prefix-cls} {
    padding-bottom: 1px;

    &__overlay {
      max-width: 360px;
    }

    .ant-tabs-content {
      width: 300px;
    }

    .ant-badge {
      display: flex;
      align-items: center;
      font-size: 18px;

      .ant-badge-multiple-words {
        padding: 0 4px;
      }

      svg {
        width: 0.9em;
      }
    }
  }
</style>
