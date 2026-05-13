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
  import { duplicateCheck } from '@mxpio/api/src/common/common';
  import { addUser, editUser, postList } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'UserModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'username',
      label: '用户账号',
      component: 'Input',
      required: true,
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
            return new Promise((resolve, reject) => {
              if (!value || unref(isUpdate)) return resolve();
              duplicateCheck({
                tableName: 'mb_user',
                column: 'username_',
                key: value,
                exclude: unref(isUpdate) ? value : '',
              })
                .then((res) => {
                  if (res === 1) {
                    return reject('编码已存在');
                  }
                  return resolve();
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'nickname',
      label: '用户姓名',
      component: 'Input',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'phone',
      label: '联系方式',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'email',
      label: '邮箱',
      component: 'Input',
      colProps: {
        span: 12,
      },
      rules: [
        {
          type: 'email',
          trigger: 'change',
        },
      ],
    },

    {
      field: 'postId',
      label: '职位',
      component: 'ApiSelect',
      componentProps: {
        api: postList,
        resultField: 'content',
        labelField: 'name',
        valueField: 'id',
      },
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'concurrentPostIds',
      label: '兼任',
      component: 'ApiSelect',
      componentProps: {
        api: postList,
        resultField: 'content',
        labelField: 'name',
        valueField: 'id',
        mode: 'multiple',
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'rank',
      label: '级别',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'MB_SYSTEM_RANK',
      },
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'thirdId',
      label: '第三方账号',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      label: '是否可用',
      field: 'accountNonLocked',
      component: 'Switch',
      colProps: {
        span: 12,
      },
      defaultValue: true,
    },
    {
      label: '是否管理员',
      field: 'administrator',
      component: 'Switch',
      colProps: {
        span: 12,
      },
      defaultValue: false,
    },
    {
      field: 'introduction',
      label: '简介',
      component: 'InputTextArea',
      componentProps: () => {
        return {
          rows: 4,
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'avatar',
      label: '头像',
      component: 'ImageUpload',
      // componentProps: {
      //   api: uploadApi,
      // },
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
        concurrentPostIds: data.record.concurrentPostIds?.split(','),
        avatar: data.record.avatar?.split(','),
      });
      // updateSchema({ field: 'username', componentProps: { disabled: true } });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增用户' : '编辑用户'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        values.concurrentPostIds = values.concurrentPostIds?.join(',');
        // values.avatar = values.avatar?.join(',');
        await addUser(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        values.concurrentPostIds = values.concurrentPostIds?.join(',');
        // values.avatar = values.avatar?.join(',');
        await editUser(values);
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
