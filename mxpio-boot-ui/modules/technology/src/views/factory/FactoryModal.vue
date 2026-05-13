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
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addFactory, editFactory } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums';

  defineOptions({ name: 'FactoryModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const formSchema: FormSchema[] = [
    {
      field: 'factoryCode',
      label: '工厂编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'factoryName',
      label: '工厂名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'factoryShortName',
      label: '工厂简称',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'factoryType',
      label: '工厂类型',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_FACTORY_TYPE',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'status',
      label: '是否启用',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
          options: [
            {
              label: '是',
              value: '1',
            },
            {
              label: '否',
              value: '0',
            },
          ],
        };
      },
      colProps: {
        span: 12,
      },
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
    isDisabled.value = !!data?.disabled;
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      formData = { ...data.record };
    } else {
      setFieldsValue({
        status: '1',
      });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增工厂' : '编辑工厂'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addFactory(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editFactory(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
