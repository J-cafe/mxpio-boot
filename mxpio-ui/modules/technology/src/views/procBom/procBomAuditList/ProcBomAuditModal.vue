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
        <DetailTable ref="detailTable" />
      </a-tab-pane>
    </a-tabs>
    <BasicForm :disabled="isDisabled" @register="registerFormAudit" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import { procBomAudit, procBomAuditDetail } from '@mxpio/bizcommon';
  import DetailTable from './DetailTable.vue';
  import { formSchema, formAuditSchema } from './procBomAudit.data';

  defineOptions({ name: 'ProcBomAuditModal' });

  const emit = defineEmits(['success', 'register']);
  const isDisabled = ref(false);
  const detailTable = ref<InstanceType<typeof DetailTable>>();
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
    return await procBomAuditDetail(data.routId);
  }

  const getTitle = computed(() => (!unref(isDisabled) ? '审核BOM' : 'BOM审核详情'));

  // 提交BOM审核
  async function handleSubmit() {
    try {
      await validate();
      await detailTable.value?.validate();
      setModalProps({ confirmLoading: true });
      const values = getSubmitData();
      await procBomAudit(formData.routId, values);
      closeModal();
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
