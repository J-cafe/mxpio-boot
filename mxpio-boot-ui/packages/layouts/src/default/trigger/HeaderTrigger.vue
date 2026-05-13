<template>
  <span :class="[prefixCls, theme]" @click="toggleCollapsed">
    <MenuUnfoldOutlined v-if="getCollapsed" /> <MenuFoldOutlined v-else />
  </span>
</template>
<script lang="ts" setup>
  import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons-vue';
  import { useMenuSetting } from '@mxpio/hooks/src/setting/useMenuSetting';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { propTypes } from '@mxpio/utils/src/propTypes';

  defineProps({
    theme: propTypes.oneOf(['light', 'dark']),
  });
  const { getCollapsed, toggleCollapsed } = useMenuSetting();
  const { prefixCls } = useDesign('layout-header-trigger');
</script>
