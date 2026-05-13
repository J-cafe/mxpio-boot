<template>
  <div class="exception-page" :style="`min-height: ${minHeight}`">
    <div class="img">
      <img src="https://gw.alipayobjects.com/zos/rmsportal/KpnpchXsobRgLElEozzI.svg" />
    </div>
    <div class="content" v-if="state === 1">
      <h3>认证页面跳转中...</h3>
    </div>
    <div class="content" v-if="state === 2">
      <h3>认证成功，正在为您跳转至首页</h3>
    </div>
    <div class="content" v-if="state === 3">
      <h3>登录失败</h3>
      <div class="desc">抱歉，登录失败</div>
      <div class="action">
        <a-button type="primary" href="/">点击重试</a-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { ref, onMounted } from 'vue';
  import { useBridge } from '@mxpio/bridge';
  import { useUserStoreWithOut } from '@mxpio/stores/src/modules/user';
  import { Button as AButton } from 'ant-design-vue';
  // import { router } from '@mxpio/router';
  import setting from '@mxpio/settings';

  const { env, router } = useBridge();
  const { VITE_GLOB_APP_SSO: enableSSO } = env;

  const { ssoLoginUrl, ssoClientId } = setting.getSSOSetting();
  const userStore = useUserStoreWithOut();
  const state = ref(1);
  const minHeight = ref('100vh');

  function getAuthCode() {
    if (enableSSO === 'feishu') {
      return feishu();
    } else if (enableSSO === 'wechat') {
      return wechat();
    } else if (enableSSO === 'dingtalk') {
      return dingding();
    } else if (enableSSO === 'oauth') {
      return oauth();
    } else if (enableSSO === 'MSAL') {
      return msal();
    }
  }

  // 飞书集成实现
  function feishu() {}
  // 企业微信集成实现
  function wechat() {}
  // OAuth 集成实现
  async function oauth() {
    const code = getUrlKey('code');
    if (code) {
      return code;
    } else {
      if (!ssoLoginUrl) {
        return;
      }
      const redirect_uri = encodeURI(`${window.location.origin}/loginSSO`);
      location.href = `${ssoLoginUrl}?client_id=${ssoClientId}&response_type=code&scope=openid&redirect_uri=${redirect_uri}&state=70641054-0d46-41d6-9b57-97a14dfd14d1&nonce=a9ac2e26-68b4-44e1-8c20-8d98ae389175&prompt=select_account&response_mode=query`;
    }
  }
  // 钉钉集成实现
  async function dingding() {
    return new Promise((resolve, reject) => {
      // @ts-ignore
      window.dd.runtime.permission.requestAuthCode({
        corpId: '',
        onSuccess: function (result) {
          resolve(result.code);
        },
        onFail: function (err) {
          console.log(err);
          reject(err);
        },
      });
    });
  }
  // MSAL 集成实现
  async function msal() {
    try {
      const redirect_uri = encodeURI(`${window.location.origin}/loginSSO`);
      // @ts-ignore
      const myMSALObj = new window.msal.PublicClientApplication({
        auth: {
          // // 'Application (client) ID' of app registration in Azure portal - this value is a GUID
          clientId: ssoClientId,
          // // Full directory URL, in the form of https://login.microsoftonline.com/<tenant-id>
          authority: ssoLoginUrl,
          // // Full redirect URL, in form of http://localhost:3000
          redirectUri: `${redirect_uri}`,
        },
        cache: {
          cacheLocation: 'sessionStorage', // This configures where your cache will be stored
          storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
        },
        system: {
          loggerOptions: {
            loggerCallback: (level, message, containsPii) => {
              if (containsPii) {
                return;
              }
              switch (level) {
                // @ts-ignore
                case window.msal?.LogLevel.Error:
                  console.error(message);
                  return;
                // @ts-ignore
                case window.msal?.LogLevel.Info:
                  console.info(message);
                  return;
                // @ts-ignore
                case window.msal?.LogLevel.Verbose:
                  console.debug(message);
                  return;
                // @ts-ignore
                case window.msal?.LogLevel.Warning:
                  console.warn(message);
                  return;
              }
            },
          },
        },
      });
      const response = await myMSALObj.loginPopup({
        scopes: ['User.Read'],
      });
      return response.account.username;
    } catch (error) {
      console.error(error);
    }
  }
  // 登录实现
  async function doLogin(code) {
    return userStore
      .login({
        authCode: code,
        thirdPlatformType: enableSSO,
      })
      .then((userInfo) => {
        if (userInfo) {
          toPage();
          state.value = 2;
        } else {
          state.value = 3;
        }
      });
  }

  // 跳转页面
  function toPage() {
    router.push('/');
  }

  // 获取URL参数
  function getUrlKey(name: string) {
    return (
      decodeURIComponent(
        // eslint-disable-next-line no-sparse-arrays
        (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [
          ,
          '',
        ])[1].replace(/\+/g, '%20'),
      ) || null
    );
  }

  onMounted(async () => {
    try {
      const token = userStore.getToken;
      if (token) {
        toPage();
        return;
      }
      // 获取第三方用户权限code
      const code = await getAuthCode();
      if (code) {
        // 系统自动登录
        await doLogin(code);
      }
    } catch (e) {
      // TODO handle the exception
      state.value = 3;
      // this.$message.warning('登录失败，请重试');
    }
  });
</script>
<style lang="less" scoped>
  .exception-page {
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;

    .img {
      padding-right: 52px;
      zoom: 1;

      img {
        max-width: 430px;
      }
    }

    .content {
      h3 {
        margin-bottom: 24px;
        color: #434e59;
        font-size: 40px;
        font-weight: 600;
        line-height: 40px;
      }

      .desc {
        margin-bottom: 16px;
        font-size: 20px;
        line-height: 28px;
      }
    }
  }
</style>
