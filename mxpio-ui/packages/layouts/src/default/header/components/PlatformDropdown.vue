<template>
  <div id="platform-dropdown">
    <a-select v-model:value="platformPath" @change="handlePlatform" :options="platformOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { usePermissionStore } from '@mxpio/stores';
  import { getCurrentParentPath } from '@mxpio/components';
  import type { Menu as MenuType } from '@mxpio/types';
  import { useBridge } from '@mxpio/bridge';
  import { listenerRouteChange } from '@mxpio/logics';

  defineOptions({ name: 'PlatformDropdown' });
  const permissionStore = usePermissionStore();
  const { router } = useBridge();
  const platformPath = ref('');

  const backMenuList = computed(() => permissionStore.getBackMenuList);

  // 平台菜单（footer菜单）
  const platformOptions = computed(() => {
    return backMenuList.value
      .filter(
        (item: MenuType) => item.meta?.urlScope === 'platform' && item.meta?.hideMenu !== true,
      )
      .map((item: MenuType) => ({
        label: item.meta?.title,
        value: item.path,
      }));
  });

  // 处理平台切换
  async function handlePlatform(path: string) {
    if (path) {
      await router.push(path);
    }
  }

  // 监听vue路由，获取当前路由的父路径

  listenerRouteChange(async (route) => {
    platformPath.value = await getCurrentParentPath(route.path);
  });
</script>
<style lang="less">
  #platform-dropdown {
    width: 150px;
    margin-left: 180px;

    .ant-select {
      width: 100%;
    }

    .ant-select-selector {
      border: none;
      background-color: transparent;
      box-shadow: none;
    }

    .ant-select-arrow {
      color: #000;
    }

    .ant-select-selection-item {
      font-size: 16px;
      font-weight: 600;
    }
  }
</style>
