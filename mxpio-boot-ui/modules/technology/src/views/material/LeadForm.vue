<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm, FormSchema, Rule } from '@mxpio/components';
  import { addLeadtime, editLeadtime, getLeadtime } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'LeadForm' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const minRule: Rule = {
    type: 'number',
    min: 0,
    trigger: 'change',
    transform: (value) => {
      if (value === '' || value === undefined || value === null) {
        return 0;
      } else {
        return Number(value);
      }
    },
    message: '请输入大于等于0的数字',
  };

  const formSchema: FormSchema[] = [
    {
      field: 'fixedLeadTime',
      label: '固定提前期',
      component: 'Input',
      required: true,
      componentProps: {
        suffix: '天',
        type: 'number',
      },
      rules: [minRule],
      colProps: {
        span: 6,
      },
    },
    {
      field: 'leadLotSize',
      label: '提前期批量',
      component: 'Input',
      componentProps: {
        suffix: '天',
        type: 'number',
      },
      rules: [minRule],
      colProps: {
        span: 6,
      },
    },
    {
      field: 'varLeadTime',
      label: '变量提前期',
      component: 'Input',
      componentProps: {
        suffix: '天',
        type: 'number',
      },
      rules: [minRule],
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
      res = await getLeadtime(record.itemCode);
    }
    if (res && res.code) {
      formData = { ...res };
    } else {
      formData = {
        fixedLeadTime: 0,
        leadLotSize: 1,
        varLeadTime: 0,
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
        return await addLeadtime(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        return await editLeadtime(Object.assign(formData, values));
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
