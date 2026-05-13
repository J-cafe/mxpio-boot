<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import { saveProcInfo } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums';
  import { formSchema } from './procInfo.data';

  defineOptions({ name: 'ProcInfoModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      formData = { ...data.record };
    } else {
      setFieldsValue({
        batchReportAble: '0',
        manufactureAble: '1',
        outsourceAble: '0',
        autoReportAble: '1',
        finishCheckFlag: '1',
        status: '1',
      });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增工序' : '编辑工序'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await saveProcInfo(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await saveProcInfo(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
