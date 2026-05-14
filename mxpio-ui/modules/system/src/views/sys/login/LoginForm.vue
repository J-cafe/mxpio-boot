<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form
    class="p-4 enter-x"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    v-show="getShow"
    @keypress.enter="handleLogin"
  >
    <FormItem name="account" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.account"
        :placeholder="$t('sys.login.userName')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="$t('sys.login.password')"
      />
    </FormItem>
    <ARow class="enter-x" v-if="systemStore.captchaOpenFlag">
      <ACol :span="16">
        <FormItem name="captcha" class="enter-x">
          <Input
            size="large"
            visibilityToggle
            class="!w-full"
            style="min-width: 0"
            v-model:value="formData.captcha"
            :placeholder="$t('sys.login.captcha')"
          />
        </FormItem>
      </ACol>
      <ACol :span="8">
        <FormItem name="captcha" class="enter-x">
          <img
            v-if="requestCodeSuccess"
            style="width: 105px; height: 35px; margin-top: 2px; margin-left: 5px"
            :src="randCodeImage"
            @click="handleChangeCheckCode"
          />
          <img
            v-else
            style="margin-top: 2px; margin-left: 5px"
            :src="checkcode"
            @click="handleChangeCheckCode"
          />
        </FormItem>
      </ACol>
    </ARow>
    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem>
          <!-- No logic, you need to deal with it yourself -->
          <Checkbox v-model:checked="rememberMe" size="small">
            {{ $t('sys.login.rememberMe') }}
          </Checkbox>
        </FormItem>
      </ACol>
      <ACol :span="12">
        <FormItem :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ $t('sys.login.forgetPassword') }}
          </Button>
        </FormItem>
      </ACol>
    </ARow>

    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
        {{ $t('sys.login.loginButton') }}
      </Button>
      <!-- <Button size="large" class="mt-4 enter-x" block @click="handleRegister">
        {{ t('sys.login.registerButton') }}
      </Button> -->
    </FormItem>
    <!-- <ARow class="enter-x" :gutter="[16, 16]">
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)">
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.QR_CODE)">
          {{ t('sys.login.qrSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </Button>
      </ACol>
    </ARow>

    <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider>

    <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <GithubFilled />
      <WechatFilled />
      <AlipayCircleFilled />
      <GoogleCircleFilled />
      <TwitterCircleFilled />
    </div> -->
  </Form>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed, onMounted } from 'vue';

  import { Checkbox, Form, Input, Row, Col, Button } from 'ant-design-vue';
  import LoginFormTitle from './LoginFormTitle.vue';

  import { $t } from '@mxpio/locales';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';

  import { useUserStore } from '@mxpio/stores/src/modules/user';
  import { useSystemStore } from '@mxpio/stores/src/modules/system';
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  //import { onKeyStroke } from '@vueuse/core';
  import checkcode from '../../../assets/images/checkcode.png';
  import { getCaptcha } from '@mxpio/api';

  const ACol = Col;
  const ARow = Row;
  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();
  const systemStore = useSystemStore();
  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);
  const rememberMe = ref(false);

  // mxpio-boot 验证码 -begin
  let requestCodeSuccess = ref(false);
  let randCodeImage = ref();
  const randCode = ref();
  // mxpio-boot 验证码 -end

  const formData = reactive({
    account: '',
    password: '',
    captcha: '',
  });

  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      const userInfo = await userStore.login({
        password: data.password,
        username: data.account,
        captcha: data.captcha,
        uuid: randCode.value,
        mode: 'none', //不要默认的错误提示
      });

      if (userInfo) {
        notification.success({
          message: $t('sys.login.loginSuccessTitle'),
          description: `${$t('sys.login.loginSuccessDesc')}: ${userInfo.realName}`,
          duration: 3,
        });
      }
    } catch (error) {
      createErrorModal({
        title: $t('sys.api.errorTip'),
        content: (error as unknown as Error).message || $t('sys.api.networkExceptionMsg'),
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      });
    } finally {
      loading.value = false;
    }
  }

  async function handleChangeCheckCode() {
    try {
      let res = await getCaptcha();
      if (res.success) {
        randCodeImage.value = 'data:image/jpg;base64,' + res.result.image;
        randCode.value = res.result.code;
        requestCodeSuccess.value = true;
      } else {
        requestCodeSuccess.value = false;
      }
    } catch (error) {
      console.log('error', error);
    }
  }

  onMounted(async () => {
    handleChangeCheckCode();
    await systemStore.getSystemAction();
  });
</script>
