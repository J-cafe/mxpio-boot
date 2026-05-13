<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addStore, editStore, getStore } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'StoreForm' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const formSchema: FormSchema[] = [
    {
      field: 'safeStock',
      label: '安全库存',
      component: 'InputNumber',
      rules: [
        {
          type: 'number',
          min: 0,
          trigger: 'blur',
          transform: (value) => Number(value),
          message: '请输入大于等于0的数字',
        },
      ],
      colProps: {
        span: 6,
      },
    },
    {
      field: 'toleranceRange',
      label: '允差范围',
      component: 'InputNumber',
      rules: [
        {
          type: 'number',
          min: 0,
          max: 100,
          trigger: 'blur',
          transform: (value) => Number(value),
          message: '请输入0-100之间的数字',
        },
      ],
      required: true,
      colProps: {
        span: 6,
      },
    },
    {
      field: 'maxStock',
      label: '最大库存',
      component: 'InputNumber',
      rules: [
        {
          type: 'number',
          min: 0,
          trigger: 'blur',
          // transform: (value) => Number(value),
          message: '请输入大于等于0的数字',
        },
      ],
      colProps: {
        span: 6,
      },
    },
    {
      field: 'batchControl',
      label: '是否批次控制',
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
    {
      field: 'locationControl',
      label: '是否库位控制',
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
      res = await getStore(record.itemCode);
    }
    if (res && res.code) {
      formData = {
        ...res,
        // safeStock: Number(res.safeStock),
      };
    } else {
      formData = {
        safeStock: Number(0),
        batchControl: 1,
        locationControl: 0,
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
        return await addStore(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        return await editStore(Object.assign(formData, values));
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
