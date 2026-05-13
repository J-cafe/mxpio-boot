<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="请确认是否已修复"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { checkEqpRepairApi } from '@mxpio/bizcommon';

  defineOptions({ name: 'RepairTaskConfirmModal' });

  const emit = defineEmits(['success', 'register']);

  const bizNo = ref('');
  const isDisabled = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'isUsable',
      label: '是否修复',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
          options: [
            {
              label: '是',
              value: '1',
            },
            {
              label: '否',
              value: '0',
            },
          ],
        };
      },
      colProps: {
        span: 24,
      },
      defaultValue: '1',
      required: true,
    },
    {
      field: 'vetoReason',
      label: '拒绝原因',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      ifShow: (formData) => {
        return formData.values?.isUsable === '0';
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
    bizNo.value = data.bizNo || '';
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await checkEqpRepairApi({
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
