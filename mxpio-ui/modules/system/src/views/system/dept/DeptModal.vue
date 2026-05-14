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
  import { addDept, editDept, deptList } from '@mxpio/api';
  import { duplicateCheck } from '@mxpio/api/src/common/common';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'DeptModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const formSchema: FormSchema[] = [
    {
      field: 'faDeptId',
      label: '父部门',
      component: 'TreeSelect',
      componentProps: {
        fieldNames: {
          label: 'deptName',
          // key: 'id',
          value: 'id',
        },
        getPopupContainer: () => document.body,
      },
    },
    {
      field: 'deptCode',
      label: '部门编码',
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
                tableName: 'mb_dept',
                column: 'dept_code_',
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
      field: 'deptName',
      label: '部门名称',
      component: 'Input',
      required: true,
    },
    {
      field: 'deptLevel',
      label: '部门级别',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'MB_SYSTEM_DEPT_LEVEL',
      },
      required: true,
    },
    {
      field: 'deptType',
      label: '部门类型',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'MB_SYSTEM_DEPT_TYPE',
        numberToString: true,
      },
      required: true,
    },
    {
      field: 'deptManager',
      label: '部门负责人',
      component: 'UserByDeptSelect',
      componentProps: {
        multiple: false,
      },
      itemProps: {
        autoLink: false,
      },
    },
    {
      field: 'deptDesc',
      label: '描述',
      component: 'InputTextArea',
    },
  ];

  let deptData = reactive({});
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
      Object.assign(deptData, { ...data.record });
    } else {
      setFieldsValue({
        faDeptId: data.record?.id,
      });
      deptData = reactive(Object.assign({}, {}));
    }
    const treeData = await deptList();
    updateSchema([
      {
        field: 'faDeptId',
        componentProps: { treeData },
      },
    ]);
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增部门' : '编辑部门'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addDept([Object.assign(deptData, values)]);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editDept([Object.assign(deptData, values)]);
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
