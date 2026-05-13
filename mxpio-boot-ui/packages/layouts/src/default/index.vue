<template>
  <Watermark
    v-if="getShowWatermark"
    :content="userStore.userInfo?.nickname || ''"
    :zIndex="999"
    style="height: 100vh"
  >
    <Layout :class="prefixCls" v-bind="lockEvents">
      <LayoutFeatures />
      <LayoutHeader fixed v-if="getShowFullHeaderRef" />
      <Layout :class="[layoutClass, `${prefixCls}-out`]">
        <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
        <Layout :class="`${prefixCls}-main`">
          <LayoutMultipleHeader />
          <LayoutContent />
          <LayoutFooter />
        </Layout>
      </Layout>
    </Layout>
  </Watermark>
  <Layout v-else :class="prefixCls" v-bind="lockEvents">
    <LayoutFeatures />
    <LayoutHeader fixed v-if="getShowFullHeaderRef" />
    <Layout :class="[layoutClass, `${prefixCls}-out`]">
      <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
      <Layout :class="`${prefixCls}-main`">
        <LayoutMultipleHeader />
        <LayoutContent />
        <LayoutFooter />
      </Layout>
    </Layout>
  </Layout>
</template>

<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { Layout, Watermark } from 'ant-design-vue';
  import { createAsyncComponent } from '@mxpio/utils/src/factory/createAsyncComponent';

  import LayoutHeader from './header/index.vue';
  import LayoutContent from './content/index.vue';
  import LayoutSideBar from './sider/index.vue';
  import LayoutMultipleHeader from './header/MultipleHeader.vue';

  import { useHeaderSetting } from '@mxpio/hooks/src/setting/useHeaderSetting';
  import { useMenuSetting } from '@mxpio/hooks/src/setting/useMenuSetting';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { useLockPage } from '@mxpio/hooks/src/web/useLockPage';
  import { useRootSetting } from '@mxpio/hooks/src/setting/useRootSetting';

  import { useAppInject } from '@mxpio/hooks/src/web/useAppInject';

  import { useMultipleTabSetting } from '@mxpio/hooks/src/setting/useMultipleTabSetting';
  import { useUserStore } from '@mxpio/stores';

  const LayoutFeatures = createAsyncComponent(() => import('./feature/index.vue'));
  const LayoutFooter = createAsyncComponent(() => import('./footer/index.vue'));

  defineOptions({ name: 'DefaultLayout' });

  const { prefixCls } = useDesign('default-layout');
  const { getIsMobile } = useAppInject();
  const { getShowFullHeaderRef } = useHeaderSetting();
  const { getShowSidebar, getIsMixSidebar, getShowMenu } = useMenuSetting();
  const { getAutoCollapse } = useMultipleTabSetting();
  const { getShowWatermark } = useRootSetting();
  const userStore = useUserStore();

  // Create a lock screen monitor
  const lockEvents = useLockPage();

  const layoutClass = computed(() => {
    let cls: string[] = ['ant-layout'];
    if (unref(getIsMixSidebar) || unref(getShowMenu)) {
      cls.push('ant-layout-has-sider');
    }

    if (!unref(getShowMenu) && unref(getAutoCollapse)) {
      cls.push('ant-layout-auto-collapse-tabs');
    }

    return cls;
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-default-layout';

  .@{prefix-cls} {
    display: flex;
    flex-direction: column;
    width: 100%;
    min-height: 100%;
    // background-color: @content-bg;
    background-image: url('../assets/images/content_bg.jpg');
    background-repeat: no-repeat;
    background-size: 100% 100%;

    > .ant-layout {
      min-height: 100%;
    }

    &-main {
      width: 100%;
      margin-left: 1px;
      // background-color: transparent !important;
    }

    .ant-layout {
      background: none !important;
    }
  }

  .@{prefix-cls}-out {
    &.ant-layout-has-sider {
      .@{prefix-cls} {
        &-main {
          margin-left: 1px;
        }
      }
    }
  }
</style>
