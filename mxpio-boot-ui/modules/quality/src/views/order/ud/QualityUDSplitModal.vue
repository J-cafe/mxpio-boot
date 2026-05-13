<template>
  <BasicModal
    width="1200px"
    title="不良品拆分"
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
  import { positiveNumberPattern } from '@mxpio/bizcommon';

  defineOptions({ name: 'QualityUDSplitModal' });

  const emit = defineEmits(['success', 'register']);

  const formData = ref<Recordable>({});
  const executeformSchema: FormSchema[] = [
    {
      field: 'inspectionBillNo',
      label: '质检单号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'lineNo',
      label: '行号',
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
      field: 'unitCode',
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
      field: 'applyQuantity',
      label: '数量',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'splitQuantity',
      label: '拆分数量',
      component: 'InputNumber',
      colProps: {
        span: 8,
      },
      rules: [
        { pattern: positiveNumberPattern as RegExp, message: '请输入大于0的数字', trigger: 'blur' },
        {
          validator: (_, value) => {
            const { applyQuantity } = getFieldsValue();
            if (value > Number(applyQuantity)) {
              return Promise.reject('不能大于' + applyQuantity);
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
      required: true,
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
    setFieldsValue({
      inspectionBillNo: record.inspectionBillNo,
      lineNo: record.lineNo,
      itemName: record.inspectionBill.itemName,
      itemCode: record.inspectionBill.itemCode,
      itemSpec: record.inspectionBill?.itemSpec || '',
      unitCode: record.inspectionBill?.unitCode || '',
      applyQuantity: record.applyQuantity,
      textMap: record.textMap,
    });
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      closeModal();
      emit('success', values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
