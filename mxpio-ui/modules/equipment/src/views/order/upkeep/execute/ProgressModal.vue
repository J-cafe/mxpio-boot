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
  import { progressAddEqpUpkeepApi } from '@mxpio/bizcommon';
  import { ref } from 'vue';
  import { CuryTypeEnum } from '@mxpio/enums';
  import { dateUtil } from '@mxpio/utils';
  import { useUserStore } from '@mxpio/stores';

  defineOptions({ name: 'EqpUpkeepProgressModal' });

  const emit = defineEmits(['success', 'register']);
  const bizNo = ref('');
  const userStore = useUserStore();
  const { username } = userStore.getUserInfo || {};
  const formSchema: FormSchema[] = [
    {
      field: 'reporter',
      label: '上报人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
        disabled: true,
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'reportTime',
      label: '上报时间',
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        class: 'w-full',
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'status',
      label: '状态',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_EQUIPMENT_UPKEEPTASK_STATUS',
        disabled: true,
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'content',
      label: '上报内容',
      component: 'InputTextArea',
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
    bizNo.value = data.record.bizNo;
    setFieldsValue({
      reporter: username,
      reportTime: dateUtil().format('YYYY-MM-DD HH:mm:ss'),
      status: data.record.orderStatus,
    });
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await progressAddEqpUpkeepApi({
        ...values,
        bizNo: bizNo.value,
        crudType: CuryTypeEnum.SAVE,
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
