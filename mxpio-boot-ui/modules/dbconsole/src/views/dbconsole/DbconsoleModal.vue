<template>
  <BasicModal
    :title="getTitle"
    v-bind="$attrs"
    destroyOnClose
    :showOkBtn="!isDisabled"
    @register="registerModal"
    @ok="handleSubmit"
  >
    <template #insertFooter>
      <a-button type="primary" v-if="!isDisabled" @click="handleTest">测试</a-button>
    </template>
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addDB, editDB, dbTypesList, dbTest } from '../../api/dbconsole';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { message } from 'ant-design-vue';

  defineOptions({ name: 'DbconsoleModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);

  const typesOptions = ref([]);
  let formData: any = {};
  const formSchema: FormSchema[] = [
    {
      field: 'name',
      label: '连接名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
      rules: [],
    },
    {
      field: 'dbType',
      label: '数据库类型',
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
      field: 'driverClass',
      label: 'JDBC驱动类',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'url',
      label: '数据库连接URL',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'username',
      label: '数据库用户名',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
    },
    {
      field: 'password',
      label: '数据库密码',
      component: 'InputPassword',
      required: true,
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
    getDbTypesList();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      formData = { ...data.record };
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增数据连接' : '编辑数据连接'));

  async function getDbTypesList() {
    const res = await dbTypesList();
    typesOptions.value = res.map((item: any) => {
      return {
        label: item.dbType,
        value: item.dbType,
      };
    });
  }

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addDB(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editDB(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
      message.success('操作成功');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  async function handleTest() {
    try {
      let values = await validate();
      await dbTest(values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
