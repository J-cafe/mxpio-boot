<template>
  <BasicModal
    width="900px"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="转委外订单"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import { pmConvertOutsourceApi } from '@mxpio/bizcommon';
  import { toOutsourceOrderFormSchema } from './outsourcePlan.data';

  defineOptions({ name: 'ToOutsourceOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const isDisabled = ref(false);
  const plans = ref<Recordable[]>([]);
  const supplyList = ref<Recordable[]>([]);
  const [registerForm, { resetFields, validate, updateSchema }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: toOutsourceOrderFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    plans.value = data?.record || [];
    supplyList.value = data?.supplyList || [];
    updateSchema([
      {
        field: 'pnCode',
        componentProps: ({ formActionType }) => {
          return {
            options: supplyList.value.map((item) => ({
              label: item.pnName,
              value: item.pnCode,
              item,
            })),
            onSelect: (value, options) => {
              console.log(value, options);
              const { item } = options;
              const { setFieldsValue } = formActionType;
              setFieldsValue({
                pnName: item.pnName || '',
                pnAddress: item.pnAddress || '',
                pnTel: item.pnTel || '',
                pnContacts: item.pnContacts || '',
                bizMan: item.bizMan,
              });
            },
          };
        },
      },
    ]);
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      plans.value.forEach((item) => {
        item.pnCode = values.pnCode;
        item.pnName = values.pnName;
        item.bizMan = values.bizMan;
      });
      await pmConvertOutsourceApi(plans.value);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
