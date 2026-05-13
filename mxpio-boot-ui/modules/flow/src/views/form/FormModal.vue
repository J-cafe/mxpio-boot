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
  import { duplicateCheck } from '@mxpio/api/src/common/common';
  import { addForm, editForm } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'FormModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);

  const formSchema: FormSchema[] = [
    {
      field: 'code',
      label: '编码',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
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
                tableName: 'mb_bpmn_form_model',
                column: 'code_',
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
      field: 'name',
      label: '名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 24,
      },
      componentProps: () => {
        return {
          disabled: isUpdate.value,
        };
      },
    },
    {
      field: 'status',
      label: '发布状态',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'MB_FLOW_DEPLOY_STATUS',
          disabled: true,
        };
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'desc',
      label: '描述',
      component: 'Input',
      colProps: {
        span: 24,
      },
    },
  ];

  let formData: any = {};

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
      formData = { ...data.record };
    } else {
      setFieldsValue({
        status: '01',
        visible: true,
      });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增用户' : '编辑用户'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addForm(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editForm(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
