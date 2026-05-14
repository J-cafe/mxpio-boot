<template>
  <PageWrapper title="关于">
    <template #headerContent>
      <div class="flex justify-between items-center">
        <span class="flex-1">
          是一个基于Vue3.0、Vite、 Ant-Design-Vue 、TypeScript
          的后台解决方案，目标是为中大型项目开发,提供现成的开箱解决方案及丰富的示例,原则上不会限制任何代码用于商用1。
        </span>
        <a-button type="primary" @click="handleOpen">点击加载数据</a-button>
        <a-button type="primary" @click="handleSet">点击设置</a-button>
      </div>
    </template>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { onMounted } from 'vue';
  import { PageWrapper } from '@mxpio/components';
  import { useAppStore } from '@mxpio/stores/src/modules/app';
  import { ThemeEnum } from '@mxpio/enums/src/appEnum';

  const appStore = useAppStore();
  function loadData() {
    console.log('加载数据');
  }

  function handleOpen(id) {
    window.open('http://localhost:5173/gh-pages/index.html#/edit/0g?id=' + id, '_blank');
  }

  function handleSet() {
    appStore.setDarkMode(ThemeEnum.DARK);
  }

  onMounted(() => {
    loadData();
  });
</script>
