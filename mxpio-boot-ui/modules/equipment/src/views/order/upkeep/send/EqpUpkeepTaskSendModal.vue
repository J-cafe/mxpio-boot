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
  import { dispatchEqpUpkeepApi, getEqpGroupMemberByCode } from '@mxpio/bizcommon';

  defineOptions({ name: 'EqpUpkeepTaskSendModal' });

  const emit = defineEmits(['success', 'register']);

  const bizNo = ref('');
  const task: Recordable = ref({});
  const isDisabled = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'executor',
      label: '执行人',
      component: 'ApiSelect',
      componentProps: () => {
        return {
          api: () => {
            return getEqpGroupMemberByCode(task.value?.upkeepPersonGroupCode);
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
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await dispatchEqpUpkeepApi(bizNo.value, values.executor);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
