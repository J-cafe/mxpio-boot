<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="派单"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { dispatchEqpCheckTaskApi, getEqpGroupMemberByCode } from '@mxpio/bizcommon';

  defineOptions({ name: 'EqpCheckTaskSendModal' });

  const emit = defineEmits(['success', 'register']);

  const bizNo = ref('');
  const task: Recordable = ref({});
  const isDisabled = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'username',
      label: '执行人',
      component: 'ApiSelect',
      componentProps: () => {
        return {
          api: () => {
            return getEqpGroupMemberByCode(task.value?.checkPersonGroupCode);
          },
          labelField: 'workNo',
          valueField: 'userId',
        };
      },
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
    bizNo.value = data.record.bizNo;
    task.value = data.record;
    console.log('task', task.value);
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await dispatchEqpCheckTaskApi(bizNo.value, values.username);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
