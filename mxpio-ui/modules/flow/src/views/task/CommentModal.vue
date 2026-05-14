<template>
  <BasicModal
    title="请填写原因"
    :width="600"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';

  defineOptions({ name: 'FormModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);

  const formSchema: FormSchema[] = [
    {
      field: '$BPMN_COMMENT_',
      label: '原因',
      component: 'InputTextArea',
      required: true,
      colProps: {
        span: 24,
      },
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
    isUpdate.value = !!data?.isUpdate;
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      closeModal();
      emit('success', values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
