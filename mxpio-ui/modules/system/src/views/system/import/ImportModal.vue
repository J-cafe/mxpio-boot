<template>
  <BasicModal
    width="800px"
    :title="getTitle"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref, reactive } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addImport, editImport, importFactoryList, entityClassList } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { useCommon } from '@mxpio/hooks';

  defineOptions({ name: 'ImportModal' });

  const emit = defineEmits(['success', 'register']);
  const { duplicateCheck } = useCommon();

  const formSchema: FormSchema[] = [
    {
      field: 'code',
      label: '方案编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: () => {
        return {
          disabled: isUpdate.value,
        };
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value || unref(isUpdate)) return Promise.resolve();
            return duplicateCheck({
              tableName: 'mb_excel_importer_solution',
              column: 'code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
      required: true,
    },
    {
      field: 'name',
      label: '方案名称',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'entityManagerFactoryName',
      label: '数据源',
      component: 'ApiSelect',
      componentProps: {
        showSearch: true,
        onChange(val) {
          getEntityClassList(val);
        },
      },
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'entityClassName',
      label: '实体类',
      component: 'ApiSelect',
      componentProps: {
        showSearch: true,
      },
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'excelSheetName',
      label: 'Sheet页名称',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'startRow',
      label: '起始行',
      component: 'InputNumber',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'desc',
      label: '描述',
      component: 'InputTextArea',
      colProps: {
        span: 12,
      },
    },
  ];

  const isUpdate = ref<boolean>(true);
  let formData = reactive<Object>({});

  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
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
      Object.assign(formData, { ...data.record });
    } else {
      setFieldsValue({
        excelSheetName: 'Sheet1',
        startRow: 1,
      });
      formData = reactive(Object.assign({}, {}));
    }
    getFactoryList();
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增导入模板' : '编辑导入模板'));

  // 获取数据源列表
  async function getFactoryList() {
    const resultData = await importFactoryList();
    const list: { label: any; value: any }[] = [];
    resultData.forEach((factory) => {
      list.push({
        label: factory,
        value: factory,
      });
    });
    updateSchema([
      {
        field: 'entityManagerFactoryName',
        componentProps: { options: list },
      },
    ]);
  }

  // 获取数据源列表
  async function getEntityClassList(factory: string) {
    const resultData = await entityClassList(factory);
    const list: { label: any; value: any }[] = [];
    resultData.forEach((entityClass) => {
      list.push({
        label: entityClass,
        value: entityClass,
      });
    });
    updateSchema([
      {
        field: 'entityClassName',
        componentProps: { options: list },
      },
    ]);
  }

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addImport(Object.assign(formData, values));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editImport(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
