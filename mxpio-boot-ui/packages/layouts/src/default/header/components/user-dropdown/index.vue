<template>
  <Dropdown placement="bottomLeft" :overlayClassName="`${prefixCls}-dropdown-overlay`">
    <span :class="[prefixCls, `${prefixCls}--${theme}`]" class="flex">
      <img
        :class="`${prefixCls}__header`"
        v-if="getUserInfo.avatarImage"
        :src="getUserInfo.avatarImage"
      />
      <img :class="`${prefixCls}__header`" v-else :src="headerImg" />
      <span :class="`${prefixCls}__info hidden md:block`">
        <span :class="`${prefixCls}__name`" class="truncate">
          {{ getUserInfo.nickname }}
        </span>
      </span>
    </span>

    <template #overlay>
      <Menu @click="handleMenuClick">
        <MenuItem
          key="doc"
          :text="$t('layout.header.dropdownItemDoc')"
          icon="ion:document-text-outline"
          v-if="getShowDoc"
        />
        <Menu.Divider v-if="getShowDoc" />
        <MenuItem
          v-if="getUseLockPage"
          key="lock"
          :text="$t('layout.header.tooltipLock')"
          icon="ion:lock-closed-outline"
        />
        <MenuItem
          key="center"
          :text="$t('routes.account.accountCenter')"
          icon="ant-design:user-switch-outlined"
        />
        <MenuItem
          key="logout"
          :text="$t('layout.header.dropdownItemLoginOut')"
          icon="ion:power-outline"
        />
      </Menu>
    </template>
  </Dropdown>
  <LockAction @register="register" />
</template>
<script lang="ts" setup>
  import { Dropdown, Menu } from 'ant-design-vue';
  import type { MenuInfo } from 'ant-design-vue/lib/menu/src/interface';
  import { computed } from 'vue';
  import setting from '@mxpio/settings';
  import { useHeaderSetting } from '@mxpio/hooks/src/setting/useHeaderSetting';
  import { $t } from '@mxpio/locales';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { useModal } from '@mxpio/components';
  import { propTypes } from '@mxpio/utils/src/propTypes';
  import { openWindow } from '@mxpio/utils/src';
  import { createAsyncComponent } from '@mxpio/utils/src/factory/createAsyncComponent';
  import { useBridge } from '@mxpio/bridge';
  import { useUserStore } from '@mxpio/stores';
  import headerImg from '../../../../assets/images/avatar.png';

  type MenuEvent = 'logout' | 'doc' | 'lock' | 'api' | 'center';
  const siteSetting = setting.getSiteSetting();
  // const { header } = setting.getAssetsSetting();
  // const { assetsSetting } = setting.getSetting();

  const MenuItem = createAsyncComponent(() => import('./DropMenuItem.vue'));
  const LockAction = createAsyncComponent(() => import('../lock/LockModal.vue'));

  defineOptions({ name: 'UserDropdown' });

  defineProps({
    theme: propTypes.oneOf(['dark', 'light']),
  });

  const { prefixCls } = useDesign('header-user-dropdown');
  const { getShowDoc, getUseLockPage } = useHeaderSetting();
  // const useUserStore = useStore('useUserStore');
  // const headerImg = assetsSetting.header;
  const userStore = useUserStore();
  const { router } = useBridge();
  const getUserInfo = computed(() => {
    const { nickname = '', avatar, desc, avatarImage } = userStore.getUserInfo || {};
    return { nickname, avatar: avatar, desc, avatarImage };
  });

  const [register, { openModal }] = useModal();

  function handleLock() {
    openModal(true);
  }

  //  login out
  function handleLoginOut() {
    userStore.confirmLoginOut();
  }

  // open doc
  function openDoc() {
    openWindow(siteSetting.DOC_URL as string);
  }

  function handleMenuClick(e: MenuInfo) {
    switch (e.key as MenuEvent) {
      case 'logout':
        handleLoginOut();
        break;
      case 'doc':
        openDoc();
        break;
      case 'lock':
        handleLock();
        break;
      case 'center':
        router.push('/account/center');
        break;
    }
  }
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-header-user-dropdown';

  .@{prefix-cls} {
    align-items: center;
    height: @header-height-action;
    padding: 0 0 0 10px;
    padding-right: 10px;
    overflow: hidden;
    font-size: 12px;
    cursor: pointer;

    img {
      width: 24px;
      height: 24px;
      margin-right: 12px;
    }

    &__header {
      border-radius: 50%;
    }

    &__name {
      font-size: 14px;
    }

    &--dark {
      &:hover {
        background-color: @header-dark-bg-hover-color;
      }
    }

    &--light {
      &:hover {
        background-color: @header-light-bg-hover-color;
      }

      .@{prefix-cls}__name {
        color: @text-color-base;
      }

      .@{prefix-cls}__desc {
        color: @header-light-desc-color;
      }
    }

    &-dropdown-overlay {
      .ant-dropdown-menu-item {
        min-width: 160px;
      }
    }
  }
</style>
