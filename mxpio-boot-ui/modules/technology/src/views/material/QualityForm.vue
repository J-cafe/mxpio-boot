<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm, FormSchema, Rule } from '@mxpio/components';
  import { addQuality, editQuality, getQuality } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'QualityForm' });

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
      field: 'firstInspectNeeded',
      label: '是否首检',
      component: 'RadioButtonGroup',
      componentProps: ({ formModel }) => {
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
          onChange: (value) => {
            if (value === 0) {
              formModel.firstInspectType = '';
              formModel.firstInspectNum = null;
            }
          },
        };
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'firstInspectType',
      label: '首检类型',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_MES_PROD_INSP_TYPE',
        };
      },
      ifShow: (formData) => {
        return formData.values?.firstInspectNeeded === 1;
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'firstInspectNum',
      label: '首检数量',
      component: 'InputNumber',
      required: true,
      rules: [minRule],
      ifShow: (formData) => {
        return formData.values?.firstInspectNeeded === 1;
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'inQualityControl',
      label: '是否入库检验',
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
      res = await getQuality(record.itemCode);
    }
    if (res && res.code) {
      formData = { ...res };
    } else {
      formData = {
        supplyManager: 1,
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
        return await addQuality(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        return await editQuality(Object.assign(formData, values));
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
