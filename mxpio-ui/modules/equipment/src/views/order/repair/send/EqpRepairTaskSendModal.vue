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
  import { sendEqpRepairApi, getEqpGroupMemberByCode } from '@mxpio/bizcommon';
  import { useUserStore } from '@mxpio/stores';

  defineOptions({ name: 'EqpRepairTaskSendModal' });

  const emit = defineEmits(['success', 'register']);

  const bizNo = ref('');
  const task: Recordable = ref({});
  const isDisabled = ref(false);
  const userStore = useUserStore();
  const { username } = userStore.getUserInfo || {};
  const formSchema: FormSchema[] = [
    {
      field: 'distributePersonId',
      label: '负责人',
      component: 'ApiSelect',
      componentProps: () => {
        return {
          api: () => {
            return getEqpGroupMemberByCode(task.value?.repairPersonGroupCode);
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
    {
      field: 'hourRation',
      label: '计划工时',
      component: 'InputNumber',
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: 'hourUnit',
      label: '工时单位',
      component: 'Select',
      componentProps: {
        options: [
          {
            label: '小时',
            value: 'hour',
          },
          {
            label: '天',
            value: 'day',
          },
          {
            label: '月',
            value: 'month',
          },
        ],
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
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
    const type = data.type;
    setFieldsValue({
      hourRation: 2,
      distributePersonId: type === 'grab' ? username : null,
      hourUnit: 'hour',
    });
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      await sendEqpRepairApi({
        bizNo: bizNo.value,
        ...values,
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
