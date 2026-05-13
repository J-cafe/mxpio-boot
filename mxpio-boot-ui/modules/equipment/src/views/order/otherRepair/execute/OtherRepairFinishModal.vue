<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="上报进度"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { finishOtherRepairApi } from '@mxpio/bizcommon';
  import { ref } from 'vue';

  defineOptions({ name: 'OtherRepairFinishModal' });

  const emit = defineEmits(['success', 'register']);
  const bizNo = ref('');
  const formSchema: FormSchema[] = [
    {
      field: 'finishContent',
      label: '维修过程',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'finishFile',
      label: '完工附件',
      component: 'Upload',
      colProps: {
        span: 24,
      },
    },
    {
      field: 'finishPic',
      label: '完工图片',
      component: 'Upload',
      componentProps: {
        accept: ['image/*'],
      },
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
    bizNo.value = data.record.bizNo;
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await finishOtherRepairApi({
        ...values,
        bizNo: bizNo.value,
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
