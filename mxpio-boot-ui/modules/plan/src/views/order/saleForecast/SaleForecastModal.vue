<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <SalesOrderForm ref="formRef" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner } from '@mxpio/components';
  import { ref, computed, unref } from 'vue';
  import SalesOrderForm from './SaleForecastForm.vue';

  defineOptions({ name: 'SalesOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const title = '销售预测';
  const formRef = ref<InstanceType<typeof SalesOrderForm>>();
  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
    formRef.value?.setFormData(data);
  });

  // 标题计算
  const getTitle = computed(() => {
    if (unref(isDisabled)) return `${title}查看`;
    return unref(isUpdate) ? `${title}编辑` : `新增${title}`;
  });

  async function handleSubmit() {
    try {
      await formRef.value?.submitForm();
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
