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
  import { duplicateCheck } from '@mxpio/api';
  import { addItemGroup, editItemGroup, itemGroup } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'MaterialGroupModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const formSchema: FormSchema[] = [
    {
      field: 'faGroupCode',
      label: '父节点',
      component: 'TreeSelect',
      componentProps: {
        fieldNames: {
          label: 'groupName',
          value: 'groupCode',
        },
        getPopupContainer: () => document.body,
      },
    },
    {
      field: 'groupCode',
      label: '编码',
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
                tableName: 'mb_erp_inventory_item_group',
                column: 'group_code_',
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
      field: 'groupName',
      label: '名称',
      component: 'Input',
      required: true,
    },
    {
      field: 'groupDesc',
      label: '描述',
      component: 'InputTextArea',
    },
  ];

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
    isDisabled.value = !!data?.disabled;
    const record = data?.record;
    if (unref(isUpdate)) {
      setFieldsValue({
        ...record,
      });
      formData = { ...data.record };
    } else if (data.faGroupCode) {
      setFieldsValue({
        faGroupCode: data.faGroupCode,
      });
    }
    const treeData = await itemGroup();
    updateSchema([
      {
        field: 'faGroupCode',
        componentProps: { treeData },
      },
    ]);
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增物料组' : '编辑物料组'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addItemGroup(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editItemGroup(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
