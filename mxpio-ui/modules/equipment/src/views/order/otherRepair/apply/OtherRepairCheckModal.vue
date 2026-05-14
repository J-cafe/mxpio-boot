<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="请确认是否已修复"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { checkOtherRepairApi } from '@mxpio/bizcommon';
  import { ref, nextTick } from 'vue';

  defineOptions({ name: 'OtherRepairCheckModal' });

  const emit = defineEmits(['success', 'register']);
  const bizNo = ref('');
  const formSchema: FormSchema[] = [
    {
      field: 'isUsable',
      label: '是否修复',
      component: 'RadioButtonGroup',
      colProps: {
        span: 24,
      },
      componentProps: {
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
      },
      required: true,
    },
    {
      field: 'refuseReason',
      label: '拒绝原因',
      component: 'InputTextArea',
      ifShow: (formData) => {
        return formData.values?.isUsable == '0';
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
  ];

  const [registerForm, { resetFields, validate, setFieldsValue }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    bizNo.value = data.bizNo;
    nextTick(() => {
      setFieldsValue({
        isUsable: '1',
      });
    });
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await checkOtherRepairApi({
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
