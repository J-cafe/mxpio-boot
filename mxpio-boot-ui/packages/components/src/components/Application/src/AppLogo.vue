<!--
 * @Author: Vben
 * @Description: logo component
-->
<template>
  <div class="anticon" :class="getAppLogoClass" @click="goHome">
    <!-- <img :src="logoUrl || logoBase" class="logo" /> -->
    <div class="ml-2 truncate md:opacity-100" :class="getTitleClass" v-show="showTitle">
      {{ systemStore.appName }}
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { useGo } from '@mxpio/hooks/src/web/usePage';
  import { useMenuSetting } from '@mxpio/hooks/src/setting/useMenuSetting';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { PageEnum } from '@mxpio/enums/src/pageEnum';
  // import { useBridge } from '@mxpio/bridge';
  import { useSystemStore, useUserStore } from '@mxpio/stores';
  // import logoBase from '../../../assets/images/logo.png';

  const props = defineProps({
    /**
     * The theme of the current parent component
     */
    theme: { type: String, validator: (v: string) => ['light', 'dark'].includes(v) },
    /**
     * Whether to show title
     */
    showTitle: { type: Boolean, default: true },
    /**
     * The title is also displayed when the menu is collapsed
     */
    alwaysShowTitle: { type: Boolean },
  });
  const systemStore = useSystemStore();
  const userStore = useUserStore();
  // const { env } = useBridge();
  // const { VITE_GLOB_API_URL: apiUrl } = env;
  const { prefixCls } = useDesign('app-logo');
  const { getCollapsedShowTitle } = useMenuSetting();
  const go = useGo();
  // const logoUrl = computed(() => {
  //   if (systemStore.logo) {
  //     return apiUrl + systemStore.logo;
  //   }
  //   return logoBase;
  // });

  const getAppLogoClass = computed(() => [
    prefixCls,
    props.theme,
    { 'collapsed-show-title': unref(getCollapsedShowTitle) },
  ]);

  const getTitleClass = computed(() => [
    `${prefixCls}__title`,
    {
      'xs:opacity-0': !props.alwaysShowTitle,
    },
  ]);

  function goHome() {
    go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
  }
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-app-logo';

  .@{prefix-cls} {
    display: flex;
    align-items: center;
    padding-left: 7px;
    transition: all 0.2s ease;
    cursor: pointer;

    &.light {
      border-bottom: 1px solid @border-color-base;
    }

    &.collapsed-show-title {
      padding-left: 20px;
    }

    &.light &__title {
      color: @primary-color;
    }

    &.dark &__title {
      color: @white;
    }

    &__title {
      transition: all 0.5s;
      color: #fff; /* 浅蓝色（接近图中字体主色） */
      font-size: 24px; /* 调整字体大小 */
      font-weight: 700;
      font-weight: bold; /* 加粗（可选） */
      line-height: normal;

      /* 多层阴影模拟白色外描边+发光 */
      text-shadow:
        0 0 2px @primary-color,
        /* 内层细白边 */ 0 0 4px @primary-color,
        /* 中层白边 */ 0 0 6px @primary-color; /* 外层轻微发光 */
    }
  }
</style>
