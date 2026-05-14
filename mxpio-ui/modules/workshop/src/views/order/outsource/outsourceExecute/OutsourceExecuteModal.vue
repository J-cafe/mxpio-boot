<template>
  <BasicModal
    width="1200px"
    title="委外报工"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { outsourceExecuteApi, itemByCodeInfoApi, positiveNumberPattern } from '@mxpio/bizcommon';
  import Big from 'big.js';

  defineOptions({ name: 'OutsourceExecuteModal' });

  const emit = defineEmits(['success', 'register']);

  const formData = ref<Recordable>({});
  const executeformSchema: FormSchema[] = [
    {
      field: 'bizOrderNo',
      label: '订单编号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemCode',
      label: '物料编码',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemName',
      label: '物料名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemSpec',
      label: '规格型号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'productUnitCode',
      label: '单位',
      component: 'DictSelect',
      componentProps: {
        disabled: true,
        dictCode: 'ERP_TECH_UNIT_CODE',
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'productDrawingNo',
      label: '图号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'maxQuantity',
      label: '最大完工数',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'whCode',
      label: '入库仓库',
      component: 'WareHouseSelect',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'quantity',
      label: '完工数量',
      component: 'InputNumber',
      colProps: {
        span: 8,
      },
      rules: [
        { pattern: positiveNumberPattern as RegExp, message: '请输入大于0的数字', trigger: 'blur' },
        {
          validator: (_, value) => {
            const { maxQuantity } = getFieldsValue();
            if (value > Number(maxQuantity)) {
              return Promise.reject('不能大于最大完工数');
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
      required: true,
    },
    {
      field: 'completionFlag',
      label: '是否完工',
      required: true,
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
        span: 8,
      },
    },
  ];
  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: executeformSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    const { record } = data;
    formData.value = record;
    const maxQuantity = Big(record.orderQuantity)
      .minus(record.actualQuantity)
      .minus(record.planQuantity)
      .toNumber();
    setFieldsValue({
      completionFlag: '0',
      bizOrderNo: record.bizNo,
      itemName: record.productItemName,
      itemCode: record.productItemCode,
      itemSpec: record.itemSpec,
      productUnitCode: record.productUnitCode,
      productDrawingNo: record.productDrawingNo,
      maxQuantity: maxQuantity.toFixed(2),
    });
    getItemInfo();
  });

  async function getItemInfo() {
    const res = await itemByCodeInfoApi(formData.value.productItemCode);
    setFieldsValue({
      whCode: res.defaultWhCode,
    });
  }

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });

      await outsourceExecuteApi(formData.value.bizNo, {
        lines: [
          {
            ...values,
            lineQuantity: formData.value.orderQuantity,
            bizOrderlineNo: '0',
          },
        ],
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
