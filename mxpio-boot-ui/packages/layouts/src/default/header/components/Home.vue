<template>
  <HomeOutlined @click="toHome" />
</template>
<script lang="ts" setup>
  import { HomeOutlined } from '@ant-design/icons-vue';
  import { useRouter } from 'vue-router';
  import { PageEnum } from '@mxpio/enums';

  const { push } = useRouter();
  defineOptions({ name: 'Home' });

  function toHome() {
    push(PageEnum.BASE_HOME);
  }
</script>
