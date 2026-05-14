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
  import { addWorkunit, editWorkunit } from '@mxpio/bizcommon';
  import { duplicateCheck } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums';

  defineOptions({ name: 'WorkUnitModal' });

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const formSchema: FormSchema[] = [
    {
      field: 'workUnitCode',
      label: '作业单元编码',
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
                tableName: 'mb_erp_technology_work_unit',
                column: 'work_unit_code_',
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
      field: 'workUnitName',
      label: '作业单元名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 12,
      },
    },
    {
      field: 'mainWorkshop',
      label: '所属车间',
      required: true,
      component: 'WorkShopSelect',
      componentProps: ({ formModel }) => {
        return {
          onChange: () => {
            formModel.workCenterCode = '';
          },
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'workCenterCode',
      label: '所属工作中心',
      required: true,
      component: 'WorkCenterSelect',
      componentProps: ({ formModel }) => {
        return {
          disabled: !formModel.mainWorkshop,
          filters: {
            'workShopCode@EQ': formModel.mainWorkshop,
          },
          alwaysLoad: true,
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'workUnitType',
      label: '作业单元类型',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_WORKUNIT_TYPE',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'ipAddress',
      label: 'IP地址',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'processCode',
      label: '工序编码',
      required: true,
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'processName',
      label: '工序名称',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'reportMode',
      label: '报工方式',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_REPORT_MODE',
        };
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'eqpCode',
      label: '设备编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'eqpName',
      label: '设备名称',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'assetId',
      label: '设备资产',
      component: 'Input',
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

  const getTitle = computed(() => (!unref(isUpdate) ? '新增班组' : '编辑班组'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addWorkunit(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editWorkunit(Object.assign(formData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
