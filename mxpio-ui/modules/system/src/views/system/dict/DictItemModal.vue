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
  import { addDictItem, editDictItem } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { dictItemFormSchema } from './dict.data';

  defineOptions({ name: 'DictItemModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  let dictItemData = reactive({});
  let dict: Recordable = reactive({});
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: dictItemFormSchema,
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
      Object.assign(dictItemData, { ...data.record });
    } else {
      dictItemData = reactive(Object.assign({}, {}));
    }
    dict = Object.assign(dict, { ...data.dict });
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增字典项' : '编辑字典项'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addDictItem(dict.dictCode, Object.assign(dictItemData, values));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editDictItem(dict.dictCode, Object.assign(dictItemData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
