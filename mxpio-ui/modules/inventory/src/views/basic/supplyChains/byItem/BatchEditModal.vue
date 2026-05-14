<template>
  <BasicModal
    width="600"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="批量修改供应链"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, FormSchema } from '@mxpio/components';
  import { batchEditByItem } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';

  defineOptions({ name: 'BatchEditModal' });

  const props = defineProps({
    itemCode: {
      type: String,
      default: '',
    },
    pnCodes: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'defaultSupply',
      label: '是否默认',
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
        span: 24,
      },
    },
    {
      field: 'inQualityControl',
      label: '是否检验',
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
        span: 24,
      },
    },
    {
      field: 'hold',
      label: '是否冻结',
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
        span: 24,
      },
    },
    {
      field: 'itemPrice',
      label: '单价',
      component: 'InputNumber',
      componentProps: {
        precision: 2,
      },
      colProps: {
        span: 24,
      },
    },
  ];

  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    formSchema,
    saveApi: (data) => {
      return batchEditByItem(props.itemCode, props.pnCodes, data);
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>
