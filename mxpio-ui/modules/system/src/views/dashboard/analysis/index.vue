<template>
  <div class="h-full p-3">
    <a-card class="h-full">
      <h1 style="width: 300px; height: 100%; margin: 100px auto; font-size: 18px; font-weight: 900">
        欢迎进入:{{ getUserInfo.nickname }}
      </h1>
    </a-card>
  </div>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { useUserStore } from '@mxpio/stores/src/modules/user';

  const userStore = useUserStore();
  const getUserInfo = computed(() => {
    const { nickname = '' } = userStore.getUserInfo || {};
    return { nickname };
  });
</script>
<style scoped>
  .aaa {
    width: 100%;
    height: 100%;
  }
</style>
