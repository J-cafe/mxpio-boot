<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm, FormSchema, Rule } from '@mxpio/components';
  import { addPlan, editPlan, getPlan } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'PlanForm' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const minRule: Rule = {
    type: 'number',
    min: 0,
    trigger: 'blur',
    transform: (value) => Number(value),
    message: '请输入大于等于0的数字',
  };

  const formSchema: FormSchema[] = [
    {
      field: 'feedingMode',
      label: '供料方式',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_FEEDING_MODE',
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'fixedLotSize',
      label: '固定批量',
      component: 'InputNumber',
      required: true,
      rules: [minRule],
      componentProps: ({ formModel }) => {
        return {
          onChange: (value) => {
            if (value !== 0) {
              formModel.minPlanQuantity = '';
              formModel.incrementLotSize = '';
            }
          },
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'minPlanQuantity',
      label: '最小订货量',
      component: 'InputNumber',
      rules: [minRule],
      componentProps: ({ formModel }) => {
        return {
          disabled: formModel.inventoryPlanMode === 0,
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'incrementLotSize',
      label: '批量增量',
      component: 'InputNumber',
      componentProps: ({ formModel }) => {
        return {
          disabled: formModel.inventoryPlanMode === 0,
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'inventoryPlanMode',
      label: '库存计划法',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_INVENTORY_PLAN_MODE',
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'planMethod',
      label: '计划方法',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_PLAN_METHOD',
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'planer',
      label: '计划员',
      component: 'UserByDeptSelect',
      componentProps: () => {
        return {
          multiple: false,
        };
      },
      colProps: {
        span: 6,
      },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  async function updateForm(data: Recordable) {
    resetFields();
    isDisabled.value = !!data?.disabled;
    const record = data.record || {};
    let res: Recordable = {};
    if (record.itemCode) {
      res = await getPlan(record.itemCode);
    }
    if (res && res.code) {
      formData = { ...res };
    } else {
      formData = {
        incrementLotSize: 1,
      };
    }
    // 判断是否存在数据，存在则为编辑，不存在则为新增
    isUpdate.value = !!res?.code;
    setFieldsValue({
      ...formData,
    });
  }

  async function submitForm(itemCode: string) {
    try {
      let values = await validate();

      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        values.code = itemCode;
        values.propType = 'item';
        return await addPlan(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        return await editPlan(Object.assign(formData, values));
      }
    } catch (error) {
      return Promise.reject(error);
    }
  }

  defineExpose({
    updateForm,
    submitForm,
    validate,
  });
</script>
