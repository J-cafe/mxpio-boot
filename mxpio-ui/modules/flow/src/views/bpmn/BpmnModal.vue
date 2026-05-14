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
  import { addBpmn, editBpmn } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'BpmnModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: any = {};
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
                tableName: 'mb_bpmn_flow',
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
      field: 'bizType',
      label: '业务分类',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'MB_BPMN_BIZ_TYPE',
        };
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'title',
      label: '流程标题',
      component: 'Input',
      colProps: {
        span: 24,
      },
      helpMessage: ["示例：'['+createBy+']请假'+day+'天'"],
    },
    {
      field: 'visible',
      label: '是否显示',
      component: 'Switch',
      colProps: {
        span: 24,
      },
      required: true,
      defaultValue: true,
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
        status: '01',
        visible: true,
      });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增流程' : '编辑流程'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addBpmn(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editBpmn(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
