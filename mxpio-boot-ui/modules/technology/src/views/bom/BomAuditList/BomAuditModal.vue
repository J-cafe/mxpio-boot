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
    <a-tabs>
      <a-tab-pane key="detailTable" tab="BOM明细" forceRender>
        <BomDetailTable ref="detailTable" />
      </a-tab-pane>
    </a-tabs>
    <BasicForm :disabled="isDisabled" @register="registerFormAudit" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import { bomAudit, bomAuditDetail } from '@mxpio/bizcommon';
  import BomDetailTable from './BomDetailTable.vue';
  import { formSchema, formAuditSchema } from './bomAudit.data';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'BomAuditModal' });

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const isDisabled = ref(false);
  const detailTable = ref<InstanceType<typeof BomDetailTable>>();
  let formData: Recordable = {};

  const [registerForm, { resetFields, setFieldsValue }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [
    registerFormAudit,
    {
      resetFields: resetFieldsAudit,
      setFieldsValue: setFieldsValueAudit,
      validate,
      getFieldsValue,
    },
  ] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formAuditSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    resetFieldsAudit();
    setModalProps({ confirmLoading: false });
    isDisabled.value = !!data?.disabled;
    const res = await getBomData(data.record);
    setFieldsValue({
      ...res,
    });
    setFieldsValueAudit({
      agree: '1',
    });
    formData = res;
    detailTable.value?.setData({
      record: res || {},
      disabled: true,
    });
  });

  async function getBomData(data: Recordable) {
    return await bomAuditDetail(data.bomId);
  }

  const getTitle = computed(() => (!unref(isDisabled) ? '审核BOM' : 'BOM审核详情'));

  // 提交BOM审核
  async function handleSubmit() {
    try {
      await validate();
      await detailTable.value?.validate();
      setModalProps({ confirmLoading: true });
      const values = getSubmitData();
      await bomAudit(formData.bomId, values);
      closeModal();
      createMessage.success('审核成功');
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function getSubmitData() {
    const data = getFieldsValue();
    return {
      agree: data.agree,
      auditOpinion: data.auditOpinion,
    };
  }
</script>
