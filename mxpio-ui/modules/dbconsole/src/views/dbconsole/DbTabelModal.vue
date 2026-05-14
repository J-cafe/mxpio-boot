<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addTable, editTable } from '../../api/dbconsole';
  import { message } from 'ant-design-vue';

  defineOptions({ name: 'DbTabelModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  let formData: any = {};
  const formSchema: FormSchema[] = [
    {
      field: 'tableName',
      label: '表名',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
      rules: [],
    },
  ];

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
    }
    formData = { ...data.record };
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增数据表' : '编辑数据表'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        await addTable(formData.id, values.tableName);
      } else {
        await editTable(formData.id, formData.tableName, values.tableName);
      }
      closeModal();
      emit('success');
      message.success('操作成功');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
