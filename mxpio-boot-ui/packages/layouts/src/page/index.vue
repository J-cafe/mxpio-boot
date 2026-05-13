<template>
  <RouterView>
    <template #default="{ Component, route }">
      <transition
        :name="
          getTransitionName({
            route,
            openCache,
            enableTransition: getEnableTransition,
            cacheTabs: getCaches,
            def: getBasicTransition,
          })
        "
        mode="out-in"
        appear
      >
        <keep-alive v-if="openCache" :include="getCaches">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
        <component v-else :is="Component" :key="route.fullPath" />
      </transition>
    </template>
  </RouterView>
  <!-- <FrameLayout v-if="getCanEmbedIFramePage" /> -->
</template>

<script lang="ts" setup>
  import { computed, unref } from 'vue';

  // import FrameLayout from '../iframe/index.vue';

  import { useRootSetting } from '@mxpio/hooks/src/setting/useRootSetting';

  import { useTransitionSetting } from '@mxpio/hooks/src/setting/useTransitionSetting';
  import { useMultipleTabSetting } from '@mxpio/hooks/src/setting/useMultipleTabSetting';
  import { getTransitionName } from './transition';
  import { useMultipleTabStore } from '@mxpio/stores';

  defineOptions({ name: 'PageLayout' });

  const { getShowMultipleTab } = useMultipleTabSetting();
  const tabStore = useMultipleTabStore();

  const { getOpenKeepAlive } = useRootSetting();

  const { getBasicTransition, getEnableTransition } = useTransitionSetting();

  const openCache = computed(() => {
    return unref(getShowMultipleTab) && unref(getOpenKeepAlive);
  });

  const getCaches = computed((): string[] => {
    if (!unref(getOpenKeepAlive)) {
      return [];
    }
    return tabStore.getCachedTabList;
  });
</script>
