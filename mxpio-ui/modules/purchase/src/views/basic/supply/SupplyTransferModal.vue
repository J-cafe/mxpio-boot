<template>
  <BasicModal
    width="800px"
    title="批量转移供应商"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { supplyTransfer } from '@mxpio/bizcommon';

  defineOptions({ name: 'SupplyTransferModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'fromBizMan',
      label: '原业务员',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'toBizMan',
      label: '新业务员',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
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

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    resetFields();
    setModalProps({ confirmLoading: false });
  });

  async function handleSubmit() {
    try {
      let { fromBizMan, toBizMan } = await validate();
      setModalProps({ confirmLoading: true });
      await supplyTransfer(fromBizMan, toBizMan);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
