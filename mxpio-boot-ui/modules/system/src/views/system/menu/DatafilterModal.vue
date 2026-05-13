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
  import { addDatafilter, editDatafilter } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { datafilterFormSchema } from './menu.data';

  defineOptions({ name: 'DatafilterModal' });

  const emit = defineEmits(['success', 'register']);

  const props = defineProps({
    /**
     * Whether to refresh the interface when changing
     */
    authId: { type: String },
  });

  const isUpdate = ref(true);
  let datafilterData = reactive({});
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: datafilterFormSchema,
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
      Object.assign(datafilterData, { ...data.record });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增数据过滤' : '编辑数据过滤'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addDatafilter(
          Object.assign(datafilterData, values, { dataResourceId: props.authId }),
        );
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editDatafilter(
          Object.assign(datafilterData, values, { dataResourceId: props.authId }),
        );
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
