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
  import { addPosttype, editPosttype } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { formSchema } from './posttype.data';

  defineOptions({ name: 'PostModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  let posttypeData = reactive({});
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
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      Object.assign(posttypeData, { ...data.record });
    } else {
      posttypeData = reactive(Object.assign({}, {}));
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增职系' : '编辑职系'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addPosttype([Object.assign(posttypeData, values)]);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editPosttype([Object.assign(posttypeData, values)]);
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
