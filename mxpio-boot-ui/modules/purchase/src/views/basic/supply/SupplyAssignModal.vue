<template>
  <BasicModal
    width="800px"
    title="批量分配业务员"
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
  import { supplyAssign } from '@mxpio/bizcommon';

  defineOptions({ name: 'SupplyAssignModal' });

  const emit = defineEmits(['success', 'register']);
  var pnCodes: string[] = [];
  const formSchema: FormSchema[] = [
    {
      field: 'bizMan',
      label: '业务员',
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

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    pnCodes = data.pnCodes;
  });

  async function handleSubmit() {
    try {
      let { bizMan } = await validate();
      setModalProps({ confirmLoading: true });
      await supplyAssign(bizMan, pnCodes.join(','));
      closeModal();
      // createMessage.success('操作成功');
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
