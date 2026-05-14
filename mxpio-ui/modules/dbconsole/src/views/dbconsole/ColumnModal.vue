<template>
  <BasicModal
    :title="getTitle"
    v-bind="$attrs"
    destroyOnClose
    :showOkBtn="!isDisabled"
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addColumn, editColumn, columnTypeList } from '../../api/dbconsole';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  import { message } from 'ant-design-vue';

  defineOptions({ name: 'ColumnModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const table = ref<Recordable>({});

  const typesOptions = ref([]);
  let formData: any = {};
  const formSchema: FormSchema[] = [
    {
      field: 'columnName',
      label: '列名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
      rules: [],
    },
    {
      field: 'columnType',
      label: '类型',
      component: 'Select',
      componentProps: () => {
        return {
          options: typesOptions.value,
        };
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'columnSize',
      label: '长度',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'isprimaryKey',
      label: '是否主键',
      component: 'Switch',
      colProps: {
        span: 24,
      },
      defaultValue: false,
    },
    {
      field: 'isnullAble',
      label: '是否可为空',
      component: 'Switch',
      colProps: {
        span: 24,
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
    table.value = data.table;
    getDbTypesList();
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      formData = { ...data.record };
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增数据字段' : '编辑数据字段'));

  async function getDbTypesList() {
    const res = await columnTypeList(table.value?.dbInfoId);
    typesOptions.value = res.map((item: any) => {
      return {
        label: item.columnType,
        value: item.columnType,
      };
    });
  }

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addColumn(table.value.dbInfoId, table.value.tableName, values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editColumn(
          table.value.dbInfoId,
          table.value.tableName,
          Object.assign(formData, values),
        );
      }
      closeModal();
      emit('success');
      message.success('操作成功');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
