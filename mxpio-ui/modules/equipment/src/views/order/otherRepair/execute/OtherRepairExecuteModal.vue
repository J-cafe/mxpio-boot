<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="抢单"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { sendOtherRepairApi } from '@mxpio/bizcommon';

  defineOptions({ name: 'OtherRepairExecuteModal' });

  const emit = defineEmits(['success', 'register']);

  const bizNo = ref('');
  const formSchema: FormSchema[] = [
    {
      field: 'distributePersonId',
      label: '负责人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
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
      await sendOtherRepairApi({
        ...values,
        bizNo: bizNo.value,
        hourRation: getHour(values),
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 计算工时
  // 1. 天转换为小时
  // 2. 月转换为小时
  // 3. 小时转换为小时
  function getHour(formData: Recordable) {
    const key = formData.hourUnit;
    let hourRation = formData.hourRation;
    if (key === 'day') {
      hourRation = (Number(formData.hourRation) * 24).toFixed(2);
    } else if (key === 'month') {
      hourRation = (Number(formData.hourRation) * 24 * 30).toFixed(2);
    }
    return hourRation;
  }
</script>
