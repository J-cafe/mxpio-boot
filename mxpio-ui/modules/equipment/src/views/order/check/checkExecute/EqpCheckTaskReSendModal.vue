<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="否决"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { resendEqpCheckTaskApi } from '@mxpio/bizcommon';

  defineOptions({ name: 'EqpCheckTaskReSendModal' });

  const emit = defineEmits(['success', 'register']);

  const bizNo = ref('');
  const isDisabled = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'userId',
      label: '执行人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'reason',
      label: '转单原因',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      required: true,
    },
  ];

  const [registerForm, { resetFields, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    bizNo.value = data.bizNo;
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await resendEqpCheckTaskApi(bizNo.value, values.userId, values.reason);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
