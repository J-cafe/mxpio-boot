<template>
  <BasicModal
    width="800px"
    title="转采购"
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
  import { brlineExe } from '@mxpio/bizcommon';

  defineOptions({ name: 'ToPurchaseModal' });

  const emit = defineEmits(['success', 'register']);

  const supplyList = ref<Recordable[]>([]);
  const lines = ref<Recordable[]>([]);
  const toPurchaseformSchema: FormSchema[] = [
    {
      field: 'pnCode',
      label: '供应商',
      component: 'Select',
      required: true,
      componentProps: {
        onSelect: (value, option) => {
          console.log(value, option);
          setFieldsValue({
            pnName: option?.pnName || '',
            pnAddress: option?.pnAddress || '',
            bizMan: option?.bizMan || '',
            pnTel: option?.pnTel || '',
            pnContacts: option?.pnContacts || '',
          });
        },
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnAddress',
      label: '供应商地址',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnContacts',
      label: '联系人',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'pnTel',
      label: '电话',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'bizMan',
      label: '业务员',

      component: 'UserByDeptSelect',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'mergeItem',
      label: '是否合并相同物料',
      required: true,
      component: 'RadioButtonGroup',
      labelWidth: 130,
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
    {
      field: 'memo',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 12,
      },
    },
  ];
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: toPurchaseformSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    supplyList.value = data.supplyList;
    lines.value = data.lines;
    setFieldsValue({
      mergeItem: '1',
    });
    updateSchema([
      {
        field: 'pnCode',
        componentProps: {
          options: supplyList.value,
          // labelInValue: true,
          fieldNames: {
            label: 'pnName',
            value: 'pnCode',
          },
        },
      },
    ]);
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      const ids: string[] = [];
      lines.value.forEach((item) => {
        ids.push(`${item.bizNo}:${item.lineNo}`);
      });
      await brlineExe({
        ids: ids.join(),
        ...values,
      });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
