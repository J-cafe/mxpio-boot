<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="委外"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addRepairOutsourceApi } from '@mxpio/bizcommon';
  import { ref } from 'vue';

  defineOptions({ name: 'RepairOutsourceModal' });

  const emit = defineEmits(['success', 'register']);
  const task = ref({});
  const formSchema: FormSchema[] = [
    {
      field: 'bizNo',
      label: '保养单编码',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'reason',
      label: '委外原因',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'outManager',
      label: '委外负责人',
      component: 'UserByDeptSelect',
      componentProps: {
        // multiple: false,
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
    task.value = data.record;
    setFieldsValue({
      bizNo: data.record.bizNo,
    });
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await addRepairOutsourceApi({
        ...task.value,
        ...values,
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
