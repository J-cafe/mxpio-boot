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
  import { ref, computed } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { updatepass } from '@mxpio/api';
  import { useSystemStore } from '@mxpio/stores/src/modules/system';

  defineOptions({ name: 'UpdatePasswordModal' });

  const emit = defineEmits(['success', 'register']);

  const userinfo = ref<any>({});
  const systemStore = useSystemStore();

  const newPasswordRules = computed(() => {
    const rules: { pattern: string; message: string }[] = [];
    if (systemStore.passwordStrategy) {
      rules.push(...systemStore.passwordStrategy);
    }
    return rules;
  });

  const formSchema: FormSchema[] = [
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
      field: 'confirmNewPassword',
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

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    userinfo.value = data.record;
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      values.username = userinfo.value.username;
      await updatepass(values);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
