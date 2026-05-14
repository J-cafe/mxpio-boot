<template>
  <BasicModal
    :footer="null"
    :title="$t('layout.header.lockScreen')"
    v-bind="$attrs"
    :class="prefixCls"
    @register="register"
  >
    <div :class="`${prefixCls}__entry`">
      <div :class="`${prefixCls}__header`">
        <img v-if="avatar" :src="avatar" :class="`${prefixCls}__header-img`" />
        <img v-else :src="headerImg" :class="`${prefixCls}__header-img`" />
        <p :class="`${prefixCls}__header-name`">
          {{ getRealName }}
        </p>
      </div>

      <BasicForm @register="registerForm" />

      <div :class="`${prefixCls}__footer`">
        <a-button type="primary" block class="mt-2" @click="handleLock">
          {{ $t('layout.header.lockScreenBtn') }}
        </a-button>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { $t } from '@mxpio/locales';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  // import { useStore } from '@mxpio/bridge';
  import { useUserStore, useLockStore } from '@mxpio/stores';
  import headerImg from '../../../../assets/images/avatar.png';
  // import setting from '@mxpio/settings';

  defineOptions({ name: 'LockModal' });

  // const { header } = setting.getAssetsSetting();
  // const { assetsSetting } = setting.getSetting();
  // const headerImg = assetsSetting.header;
  // const { useUserStore, useLockStore } = useStore(['useUserStore', 'useLockStore']);
  const { prefixCls } = useDesign('header-lock-modal');
  const userStore = useUserStore();
  const lockStore = useLockStore();
  const getRealName = computed(() => userStore.getUserInfo?.nickname);
  const [register, { closeModal }] = useModalInner();

  const [registerForm, { validate, resetFields }] = useForm({
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'password',
        label: $t('layout.header.lockScreenPassword'),
        colProps: {
          span: 24,
        },
        component: 'InputPassword',
        required: true,
      },
    ],
  });

  const handleLock = async () => {
    const { password = '' } = await validate<{
      password: string;
    }>();

    closeModal();

    lockStore.setLockInfo({
      isLock: true,
      pwd: password,
    });

    await resetFields();
  };

  const avatar = computed(() => {
    const { avatarImage } = userStore.getUserInfo;
    return avatarImage;
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-header-lock-modal';

  .@{prefix-cls} {
    &__entry {
      position: relative;
      //height: 240px;
      padding: 130px 30px 30px;
      border-radius: 10px;
    }

    &__header {
      position: absolute;
      top: 0;
      left: calc(50% - 45px);
      width: auto;
      text-align: center;

      &-img {
        width: 70px;
        border-radius: 50%;
      }

      &-name {
        margin-top: 5px;
      }
    }

    &__footer {
      text-align: center;
    }
  }
</style>
