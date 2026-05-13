<template>
  <div class="relative w-full h-full px-4 login-warp" :class="theme.mode">
    <div class="">
      <div class="logo">
        <img v-if="!!logo" :src="logoUrl" style="width: 200px" />
      </div>
      <div class="header">{{ systemStore.appSystemDesc }}</div>
      <div class="">
        <div class="loginBox rounded-lg">
          <div class="relative login-form">
            <a-form @submit="onSubmit" :model="formData" style="width: 350px">
              <h1 class="mb-3 text-2xl font-bold text-center xl:text-3xl xl:center">欢迎登录</h1>
              <a-alert
                type="error"
                :closable="true"
                v-show="error"
                :message="error"
                showIcon
                style="margin-bottom: 24px"
                @close="error = ''"
              />
              <a-form-item
                name="username"
                :rules="[{ required: true, message: '请输入用户名', whitespace: true }]"
              >
                <p class="label">用户名:</p>
                <a-input
                  autocomplete="autocomplete"
                  size="large"
                  v-model:value="formData.username"
                  placeholder="请输入用户名"
                >
                  <template #prefix>
                    <img :src="userimg" alt="用户名" style="width: 16px; height: 16px" />
                  </template>
                </a-input>
              </a-form-item>
              <a-form-item
                name="password"
                :rules="[{ required: true, message: '请输入密码', whitespace: true }]"
              >
                <p class="label">密码:</p>
                <a-input-password
                  size="large"
                  v-model:value="formData.password"
                  placeholder="请输入密码"
                  autocomplete="autocomplete"
                >
                  <template #prefix>
                    <img :src="pswIcon" alt="密码" style="width: 16px; height: 16px" />
                  </template>
                </a-input-password>
              </a-form-item>
              <a-form-item
                name="captcha"
                :rules="[{ required: true, message: '请输入验证码', whitespace: true }]"
                v-if="systemStore.captchaOpenFlag"
              >
                <p class="label">验证码:</p>
                <a-row :gutter="5">
                  <a-col :span="16">
                    <a-input
                      v-model:value="formData.captcha"
                      size="large"
                      type="text"
                      placeholder="请输入验证码"
                    >
                      <template #prefix>
                        <img :src="yzm" alt="密码" style="width: 16px; height: 16px" />
                      </template>
                    </a-input>
                  </a-col>
                  <a-col :span="8" style="text-align: right">
                    <img
                      v-if="requestCodeSuccess"
                      style="width: 105px; height: 35px; margin-top: 2px"
                      :src="randCodeImage"
                      @click="handleChangeCheckCode"
                    />
                    <img
                      v-else
                      style="margin-top: 2px"
                      :src="checkcode"
                      @click="handleChangeCheckCode"
                    />
                  </a-col>
                </a-row>
              </a-form-item>
              <a-form-item>
                <a-button
                  :loading="logging"
                  style="width: 100%; margin-top: 24px; border-radius: 25px; background: #1c79fe"
                  size="large"
                  htmlType="submit"
                  type="primary"
                  >登录</a-button
                >
              </a-form-item>
              <div> </div>
            </a-form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <UpdatePasswordModal ref="updatePasswordModalRef" @register="registerModal" />
</template>

<script lang="ts" setup>
  import { ref, reactive, computed, onMounted } from 'vue';
  import { Alert as AAlert, Row as ARow, Col as ACol } from 'ant-design-vue';
  import { getCaptcha } from '@mxpio/api';
  import { useUserStore, useSystemStore } from '@mxpio/stores';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import { useBridge } from '@mxpio/bridge';
  import { $t } from '@mxpio/locales';
  import checkcode from '../../../assets/images/checkcode.png';
  import userimg from '../../../assets/images/login/user.png';
  import pswIcon from '../../../assets/images/login/pswIcon.png';
  import yzm from '../../../assets/images/login/yzm.png';
  import logoBase from '../../../assets/images/logo.png';
  import UpdatePasswordModal from './UpdatePasswordModal.vue';
  import { useModal } from '@mxpio/components';

  // 模态框
  const [registerModal, { openModal }] = useModal();
  const { notification } = useMessage();
  const userStore = useUserStore();
  const systemStore = useSystemStore();
  const { env } = useBridge();

  const logging = ref(false);
  const error = ref('');
  const randCodeImage = ref('');
  const requestCodeSuccess = ref(false);
  const randCode = ref('');
  const formData = reactive({
    username: '',
    password: '',
    captcha: '',
  });

  const theme = computed(() => ({
    mode: 'light',
  }));
  const { VITE_GLOB_API_URL: apiUrl } = env;

  const logo = computed(() => systemStore.logo || '');

  const logoUrl = computed(() => {
    if (systemStore.logo) {
      return apiUrl + systemStore.logo;
    }
    return logoBase;
  });

  async function onSubmit(e: Event) {
    e.preventDefault();
    if (!formData.username) {
      error.value = '请输入用户名';
      return;
    }
    if (!formData.password) {
      error.value = '请输入密码';
      return;
    }
    if (systemStore.captchaOpenFlag && !formData.captcha) {
      error.value = '请输入验证码';
      return;
    }

    logging.value = true;
    const data = {
      username: formData.username,
      password: formData.password,
    };
    if (systemStore.captchaOpenFlag) {
      (data as any).captcha = formData.captcha;
      (data as any).uuid = randCode.value;
    }

    try {
      const userInfo = await userStore.login({
        username: data.username,
        password: data.password,
        captcha: (data as any).captcha,
        uuid: (data as any).uuid,
        mode: 'none',
      });

      logging.value = false;
      if (userInfo?.pwdExpiredFlag) {
        openModal(true, {
          oldPassword: formData.password,
          username: formData.username,
        });
      } else {
        loginSuccess(userInfo);
      }
    } catch (err: any) {
      logging.value = false;
      error.value = (err?.data || {}).message || err?.message || '请求出现错误，请稍后再试';
    }
  }

  function loginSuccess(userInfo) {
    notification.success({
      message: '欢迎',
      description: `${$t('sys.login.loginSuccessDesc')}: ${userInfo.nickname}`,
    });
  }

  async function handleChangeCheckCode() {
    try {
      const res = await getCaptcha();
      if (res.success) {
        randCodeImage.value = 'data:image/jpg;base64,' + res.result.image;
        randCode.value = res.result.code;
        requestCodeSuccess.value = true;
      } else {
        requestCodeSuccess.value = false;
      }
    } catch (error) {
      requestCodeSuccess.value = false;
    }
  }

  onMounted(async () => {
    await systemStore.getSystemAction();
    if (systemStore.captchaOpenFlag) {
      handleChangeCheckCode();
    }
  });
</script>
<style lang="less">
  .header {
    .text-white {
      --tw-text-opacity: 1;

      color: rgb(255 255 254 / var(--tw-text-opacity)) !important;
    }
  }

  .loginBox {
    .has-error .ant-input-affix-wrapper .ant-input {
      background-color: rgb(255 255 255 / 40%) !important;
    }

    .ant-input-affix-wrapper {
      border-color: rgb(7 62 194 / 40%) !important;
      background-color: rgb(255 255 255 / 40%) !important;
    }

    .ant-input {
      background-color: transparent;
    }
  }
</style>
<style lang="less" scoped>
  .login-warp {
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: url('../../../assets/images/login/background.jpg');
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }

    .logo {
      position: absolute;
      top: 20px;
      left: 60px;
    }
  }

  .header {
    position: absolute;
    top: 0;
    left: 50%;
    width: 60%;
    height: 100px;
    transform: translateX(-50%);
    background: url('../../../assets/images/login/title_bg.png') no-repeat;
    background-size: 100%;
    color: #fff; /* 浅蓝色（接近图中字体主色） */
    font-size: 45px; /* 调整字体大小 */
    font-weight: bold; /* 加粗（可选） */
    line-height: 1.5;
    text-align: center;

    /* 多层阴影模拟白色外描边+发光 */
    text-shadow:
      0 0 2px @primary-color,
      /* 内层细白边 */ 0 0 4px @primary-color,
      /* 中层白边 */ 0 0 6px @primary-color; /* 外层轻微发光 */
  }

  @media screen and (max-width: 1560px) and (min-width: 1280px) {
    .header {
      width: 80%; /* 增加宽度以适应小屏幕 */
      height: 80px; /* 减小高度 */
      background: url('../../../assets/images/login/title_bg.png') no-repeat;
      background-size: 100% 100%;
      font-size: 38px; /* 减小字体大小 */
    }

    .loginBox {
      right: 60px; /* 调整右侧距离 */
      bottom: 100px; /* 调整底部距离 */
      padding: 50px 40px; /* 减小内边距 */
    }

    .login-form a-form {
      width: 300px !important; /* 减小表单宽度 */
    }
  }

  .loginBox {
    position: absolute;
    right: 120px;
    bottom: 150px;
    box-sizing: border-box;
    padding: 69px 55px;
    background: url('../../../assets/images/login/loginBox.png');
    background-size: 100% 100%;
  }

  .label {
    margin: 0;
    padding: 0;
    color: #1a1a1a;
    font-size: 16px;
  }
</style>
