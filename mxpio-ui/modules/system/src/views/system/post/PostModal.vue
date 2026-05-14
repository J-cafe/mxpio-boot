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
  import { addPost, editPost } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { formSchema } from './post.data';

  defineOptions({ name: 'PostModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  let postData = reactive({});
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
      Object.assign(postData, { ...data.record });
    } else {
      postData = reactive(Object.assign({}, {}));
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增岗位' : '编辑岗位'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addPost([Object.assign(postData, values)]);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editPost([Object.assign(postData, values)]);
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
