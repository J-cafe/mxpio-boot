<template>
  <BasicModal
    destroyOnClose
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref, reactive } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import { addExportColumn, editExportColumn } from '@mxpio/api';
  import { itemFormSchema } from './export.data';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'ExportItemModal' });

  const emit = defineEmits(['success', 'register']);

  const props = defineProps({
    id: { type: String },
  });

  const isUpdate = ref(true);

  let formData = reactive({});

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: itemFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      Object.assign(formData, { ...data.record });
    } else {
      setFieldsValue({
        dataAlign: 1,
      });
      formData = reactive(Object.assign({}, {}));
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增导出字段' : '编辑导出字段'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        values.solutionId = props.id;
        await addExportColumn(Object.assign(formData, values));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        values.solutionId = props.id;
        await editExportColumn(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
