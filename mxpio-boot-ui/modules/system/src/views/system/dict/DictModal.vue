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
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addDict, editDict } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { duplicateCheck } from '@mxpio/api/src/common/common';

  defineOptions({ name: 'DictModal' });

  const emit = defineEmits(['success', 'register']);

  const formSchema: FormSchema[] = [
    {
      field: 'dictCode',
      label: '字典编码',
      component: 'Input',
      required: true,
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
                tableName: 'mb_dict',
                column: 'dict_code_',
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
      field: 'dictName',
      label: '字典名称',
      component: 'Input',
      required: true,
    },
    {
      field: 'dictType',
      label: '字典类别',
      component: 'Select',
      required: true,
      componentProps: {
        options: [
          {
            label: '系统字典',
            value: '1',
          },
          {
            label: '业务字典',
            value: '2',
          },
        ],
      },
    },
    {
      field: 'dictDefaultValue',
      label: '默认值',
      component: 'Input',
    },
    {
      field: 'dictDesc',
      label: '描述',
      component: 'InputTextArea',
    },
  ];

  const isUpdate = ref(true);
  let dictData = reactive({});
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
      Object.assign(dictData, { ...data.record });
    } else {
      dictData = reactive(Object.assign({}, {}));
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增字典' : '编辑字典'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addDict(Object.assign(dictData, values));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editDict(Object.assign(dictData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
