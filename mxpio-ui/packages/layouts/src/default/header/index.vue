<template>
  <Layout.Header :class="getHeaderClass">
    <!-- left start -->
    <div :class="`${prefixCls}-left`">
      <!-- logo -->
      <AppName
        v-if="getShowHeaderLogo || getIsMobile"
        :class="`${prefixCls}-logo`"
        :theme="getHeaderTheme"
        :style="getLogoWidth"
      />
      <LayoutTrigger
        v-if="
          (getShowContent && getShowHeaderTrigger && !getSplit && !getIsMixSidebar) || getIsMobile
        "
        :theme="getHeaderTheme"
        :sider="false"
      />
      <LayoutBreadcrumb v-if="getShowContent && getShowBread" :theme="getHeaderTheme" />
    </div>
    <!-- left end -->

    <!-- menu start -->
    <PlatformDropdown />
    <div v-if="getShowTopMenu && !getIsMobile" :class="`${prefixCls}-menu`">
      <LayoutMenu
        :isHorizontal="true"
        :theme="getHeaderTheme"
        :splitType="getSplitType"
        :menuMode="getMenuMode"
      />
    </div>
    <!-- menu-end -->

    <!-- action  -->
    <div :class="`${prefixCls}-action`">
      <AppSearch v-if="getShowSearch" :class="`${prefixCls}-action__item `" />
      <Home :class="`${prefixCls}-action__item `" />
      <ErrorAction v-if="getUseErrorHandle" :class="`${prefixCls}-action__item error-action`" />

      <Notify v-if="getShowNotice" :class="`${prefixCls}-action__item notify-item`" />

      <FullScreen v-if="getShowFullScreen" :class="`${prefixCls}-action__item fullscreen-item`" />

      <AppLocalePicker
        v-if="getShowLocalePicker"
        :reload="true"
        :showText="false"
        :class="`${prefixCls}-action__item`"
      />

      <UserDropDown :theme="getHeaderTheme" />

      <!-- <SettingDrawer v-if="getShowSetting" :class="`${prefixCls}-action__item`" /> -->
    </div>
  </Layout.Header>
</template>
<script lang="ts" setup>
  import { Layout } from 'ant-design-vue';
  import { computed, unref } from 'vue';

  import { AppLocalePicker, AppName, AppSearch } from '@mxpio/components';
  // import { SettingButtonPositionEnum } from '@mxpio/enums/src/appEnum';
  import { MenuModeEnum, MenuSplitTyeEnum } from '@mxpio/enums/src/menuEnum';
  import { useHeaderSetting } from '@mxpio/hooks/src/setting/useHeaderSetting';
  import { useMenuSetting } from '@mxpio/hooks/src/setting/useMenuSetting';
  import { useRootSetting } from '@mxpio/hooks/src/setting/useRootSetting';
  import { useAppInject } from '@mxpio/hooks/src/web/useAppInject';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { useLocale } from '@mxpio/hooks/src/web/useLocale';
  // import { createAsyncComponent } from '@mxpio/utils/src/factory/createAsyncComponent';

  import LayoutMenu from '../menu/index.vue';
  import LayoutTrigger from '../trigger/index.vue';
  import {
    ErrorAction,
    FullScreen,
    LayoutBreadcrumb,
    Notify,
    UserDropDown,
    Home,
    PlatformDropdown,
  } from './components';

  // const SettingDrawer = createAsyncComponent(() => import('../setting/index.vue'), {
  //   loading: true,
  // });
  defineOptions({ name: 'LayoutHeader' });

  const props = defineProps({
    fixed: { type: Boolean, required: false },
  });

  const { prefixCls } = useDesign('layout-header');
  const {
    getShowTopMenu,
    getShowHeaderTrigger,
    getSplit,
    getIsMixMode,
    getMenuWidth,
    getIsMixSidebar,
  } = useMenuSetting();
  const {
    getUseErrorHandle,
    //  getShowSettingButton, getSettingButtonPosition
  } = useRootSetting();

  const {
    getHeaderTheme,
    getShowFullScreen,
    getShowNotice,
    getShowContent,
    getShowBread,
    getShowHeaderLogo,
    // getShowHeader,
    getShowSearch,
  } = useHeaderSetting();

  const { getShowLocalePicker } = useLocale();

  const { getIsMobile } = useAppInject();

  const getHeaderClass = computed(() => {
    const theme = unref(getHeaderTheme);
    return [
      prefixCls,
      {
        [`${prefixCls}--fixed`]: props.fixed,
        [`${prefixCls}--mobile`]: unref(getIsMobile),
        [`${prefixCls}--${theme}`]: theme,
      },
    ];
  });

  // const getShowSetting = computed(() => {
  //   if (!unref(getShowSettingButton)) {
  //     return false;
  //   }
  //   const settingButtonPosition = unref(getSettingButtonPosition);

  //   if (settingButtonPosition === SettingButtonPositionEnum.AUTO) {
  //     return unref(getShowHeader);
  //   }
  //   return settingButtonPosition === SettingButtonPositionEnum.HEADER;
  // });

  const getLogoWidth = computed(() => {
    if (!unref(getIsMixMode) || unref(getIsMobile)) {
      return {};
    }
    const width = unref(getMenuWidth) < 180 ? 180 : unref(getMenuWidth);
    return { width: `${width}px` };
  });

  const getSplitType = computed(() => {
    return unref(getSplit) ? MenuSplitTyeEnum.TOP : MenuSplitTyeEnum.NONE;
  });

  const getMenuMode = computed(() => {
    return unref(getSplit) ? MenuModeEnum.HORIZONTAL : null;
  });
</script>
<style lang="less">
  @import url('./index.less');

  .vben-layout-header {
    .ant-menu {
      background-color: transparent;
    }
  }
</style>
