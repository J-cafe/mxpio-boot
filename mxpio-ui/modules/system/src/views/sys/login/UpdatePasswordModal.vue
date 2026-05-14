<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="更新密码"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { computed, ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { updatepwdwithcheck } from '@mxpio/api';
  import { useSystemStore } from '@mxpio/stores/src/modules/system';

  defineOptions({ name: 'UpdatePasswordModal' });

  const emit = defineEmits(['success', 'register']);

  const systemStore = useSystemStore();

  const newPasswordRules = computed(() => {
    const rulesss: { pattern: string; message: string }[] = [];
    if (systemStore.passwordStrategy) {
      rulesss.push(...systemStore.passwordStrategy);
    }
    return rulesss;
  });

  const formSchema: FormSchema[] = [
    // {
    //   field: 'oldPassword',
    //   label: '旧密码',
    //   component: 'InputPassword',
    //   colProps: {
    //     span: 24,
    //   },
    //   required: true,
    // },
    {
      field: 'newPassword',
      label: '新密码',
      component: 'InputPassword',
      colProps: {
        span: 24,
      },
      // @ts-ignore
      rules: newPasswordRules,
      required: true,
    },
    {
      field: 'confirmpassword',
      label: '确认新密码',
      component: 'InputPassword',
      colProps: {
        span: 24,
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              const fromModel = getFieldsValue();
              if (!value) return resolve();
              if (value !== fromModel.newPassword) {
                reject('两次输入的密码不一致');
              } else {
                resolve();
              }
            });
          },
          trigger: 'blur',
        },
      ],
      required: true,
    },
  ];

  const [registerForm, { resetFields, validate, getFieldsValue }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const oldPassword = ref('');
  const username = ref('');
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    const { oldPassword: oldPasswordFromData, username: usernameFromData } = data || {};
    oldPassword.value = oldPasswordFromData || '';
    username.value = usernameFromData || '';
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await updatepwdwithcheck({
        newPassword: values.newPassword,
        oldPassword: oldPassword.value,
        username: username.value,
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
