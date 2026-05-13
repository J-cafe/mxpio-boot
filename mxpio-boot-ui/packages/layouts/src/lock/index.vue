<template>
  <transition name="fade-bottom" mode="out-in">
    <LockPage v-if="getIsLock" />
  </transition>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import LockPage from './LockPage.vue';
  // import { useStore } from '@mxpio/bridge';
  import { useLockStore } from '@mxpio/stores';

  // const useLockStore = useStore('useLockStore');
  const lockStore = useLockStore();
  const getIsLock = computed(() => lockStore?.getLockInfo?.isLock ?? false);
</script>
