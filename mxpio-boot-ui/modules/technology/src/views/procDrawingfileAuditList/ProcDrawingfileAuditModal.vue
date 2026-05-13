<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="true" @register="registerForm" />
    <template #footer>
      <a-button @click="closeModal">关闭</a-button>
      <a-button type="dashed" @click="handleSubmit(AuditResultEnum.REJECT)" v-if="!isDisabled"
        >拒绝</a-button
      >
      <a-button type="primary" @click="handleSubmit(AuditResultEnum.PASS)" v-if="!isDisabled"
        >通过</a-button
      >
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import { auditPassDrawingfileSignin } from '@mxpio/bizcommon';
  import { formSchema } from './ProcDrawingfileAudit.data';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'BomAuditModal' });

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const isDisabled = ref(false);
  let formData: Recordable = {};
  enum AuditResultEnum {
    PASS = '03',
    REJECT = '99',
  }
  const [registerForm, { resetFields, setFieldsValue }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isDisabled.value = !!data?.disabled;

    console.log(data);
    setFieldsValue(data.record);
    formData = data.record;
    setModalProps({ confirmLoading: true });
  });

  const getTitle = computed(() => (!unref(isDisabled) ? '审核图纸' : '图纸审核详情'));

  // 提交图纸审核
  async function handleSubmit(result) {
    try {
      setModalProps({ confirmLoading: true });
      await auditPassDrawingfileSignin(formData.code, result);
      closeModal();
      createMessage.success('审核成功');
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
