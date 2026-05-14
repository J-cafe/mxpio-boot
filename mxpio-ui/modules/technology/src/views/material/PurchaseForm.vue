<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addPurchase, editPurchase, getPurchase } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'PurchaseForm' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let formData: Recordable = {};
  const formSchema: FormSchema[] = [
    {
      field: 'supplyManager',
      label: '供应链管理',
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
      res = await getPurchase(record.itemCode);
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
        return await addPurchase(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        return await editPurchase(Object.assign(formData, values));
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
