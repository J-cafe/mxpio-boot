<template>
  <div
    :class="[`${prefixCls}__placeholder`]"
    :style="getPlaceholderDomStyle"
    v-if="getIsShowPlaceholderDom"
  ></div>
  <div :style="getWrapStyle" :class="getClass">
    <LayoutHeader v-if="getShowInsetHeaderRef" />
    <MultipleTabs v-if="getShowTabs" :key="tabStore.getLastDragEndIndex" />
  </div>
</template>
<script lang="ts" setup>
  import { unref, computed, CSSProperties } from 'vue';

  import LayoutHeader from './index.vue';
  import MultipleTabs from '../tabs/index.vue';

  import { useHeaderSetting } from '@mxpio/hooks/src/setting/useHeaderSetting';
  import { useMenuSetting } from '@mxpio/hooks/src/setting/useMenuSetting';
  import { useFullContent } from '@mxpio/hooks/src/web/useFullContent';
  import { useMultipleTabSetting } from '@mxpio/hooks/src/setting/useMultipleTabSetting';
  import { useAppInject } from '@mxpio/hooks/src/web/useAppInject';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { useLayoutHeight } from '../content/useContentViewHeight';
  import { useMultipleTabStore } from '@mxpio/stores';
  // import { useStore } from '@mxpio/bridge';

  const HEADER_HEIGHT = 60;

  const TABS_HEIGHT = 32;

  defineOptions({ name: 'LayoutMultipleHeader' });

  const { setHeaderHeight } = useLayoutHeight();
  // const useMultipleTabStore = useStore('useMultipleTabStore');
  const tabStore = useMultipleTabStore();
  const { prefixCls } = useDesign('layout-multiple-header');

  const { getCalcContentWidth, getSplit, getShowMenu } = useMenuSetting();
  const { getIsMobile } = useAppInject();
  const { getFixed, getShowInsetHeaderRef, getShowFullHeaderRef, getHeaderTheme, getShowHeader } =
    useHeaderSetting();

  const { getFullContent } = useFullContent();

  const { getShowMultipleTab, getAutoCollapse } = useMultipleTabSetting();

  const getShowTabs = computed(() => {
    return unref(getShowMultipleTab) && !unref(getFullContent);
  });

  const getIsShowPlaceholderDom = computed(() => {
    return unref(getFixed) || unref(getShowFullHeaderRef);
  });

  const getWrapStyle = computed((): CSSProperties => {
    const style: CSSProperties = {};
    if (unref(getFixed)) {
      style.width = unref(getIsMobile) ? '100%' : unref(getCalcContentWidth);
    }
    if (unref(getShowFullHeaderRef)) {
      style.top = `${HEADER_HEIGHT}px`;
    }
    return style;
  });

  const getIsFixed = computed(() => {
    return unref(getFixed) || unref(getShowFullHeaderRef);
  });

  const getIsUnFold = computed(() => !unref(getShowMenu) && !unref(getShowHeader));

  const getPlaceholderDomStyle = computed((): CSSProperties => {
    let height = 0;
    if (!(unref(getAutoCollapse) && unref(getIsUnFold))) {
      if (
        (unref(getShowFullHeaderRef) || !unref(getSplit)) &&
        unref(getShowHeader) &&
        !unref(getFullContent)
      ) {
        height += HEADER_HEIGHT;
      }
      if (unref(getShowMultipleTab) && !unref(getFullContent)) {
        height += TABS_HEIGHT;
      }
      setHeaderHeight(height);
    }
    return {
      height: `${height}px`,
    };
  });

  const getClass = computed(() => {
    return [
      prefixCls,
      `${prefixCls}--${unref(getHeaderTheme)}`,
      { [`${prefixCls}--fixed`]: unref(getIsFixed) },
    ];
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-layout-multiple-header';

  .@{prefix-cls} {
    flex: 0 0 auto;
    transition: width 0.2s;

    &--dark {
      margin-left: -1px;
    }

    &--fixed {
      position: fixed;
      z-index: @multiple-tab-fixed-z-index;
      top: 0;
      width: 100%;
    }

    &__placeholder {
      transition: height 0.6s ease-in-out;
    }
  }
</style>
