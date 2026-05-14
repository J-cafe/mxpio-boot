<template>
  <span :class="`${prefixCls}__extra-fold`" @click="openDrawer(true)">
    <Icon icon="ion:settings-outline" />
    <SettingDrawer @register="register" />
  </span>
</template>
<script lang="ts" setup>
  import SettingDrawer from '../../setting/SettingDrawer';
  import { Icon, useDrawer } from '@mxpio/components';

  import { useDesign } from '@mxpio/hooks/src/web/useDesign';

  defineOptions({ name: 'SettingButton' });

  const [register, { openDrawer }] = useDrawer();
  const { prefixCls } = useDesign('multiple-tabs-content');
</script>
