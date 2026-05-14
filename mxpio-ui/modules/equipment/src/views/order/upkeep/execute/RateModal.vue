<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="请填写评价"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs v-if="formDataRef?.isUsable === '1'">
      <a-tab-pane key="1" tab="评价详情">
        <RateDetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { rateEqpUpkeepApi, rateYesEqpUpkeepApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { Tabs as ATabs } from 'ant-design-vue';
  import RateDetailTable from './RateDetailTable.vue';

  defineOptions({ name: 'EqpUpkeepRateModal' });
  const emit = defineEmits(['success', 'register']);
  const recordRef = ref<Recordable>({});
  const formSchema: FormSchema[] = [
    {
      field: 'isUsable',
      label: '是否保养完成',
      component: 'RadioButtonGroup',
      colProps: {
        span: 24,
      },
      componentProps: {
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
      },
      required: true,
    },
    {
      field: 'vetoReason',
      label: '否决原因',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      required: true,
      ifShow: (formData) => {
        return formData.values?.isUsable == '0';
      },
    },
  ];
  const tableRef = ref<InstanceType<typeof RateDetailTable>>();

  const { registerForm, registerModal, isDisabled, handleSubmit, formDataRef } = useModalFormCrud({
    title: '填写保养评价',
    formSchema,
    saveApi: (params: Recordable) => {
      if (params?.isUsable === '0') {
        return rateEqpUpkeepApi(params);
      }
      return rateYesEqpUpkeepApi(params);
    },
    openAfter: (data) => {
      recordRef.value = data.record || {};
    },
    classifyIntoFormData: (values) => {
      if (values?.isUsable === '0') {
        return {
          ...values,
          bizNo: recordRef.value.bizNo,
        };
      }
      let rateDetails: Recordable = tableRef.value?.getData() || [];
      rateDetails.forEach((item) => {
        item.bizNo = recordRef.value.bizNo;
      });
      return rateDetails;
    },
    submitAfter: () => {
      emit('success');
    },
    subTables: [
      {
        initSubData: (data: Recordable) => {
          tableRef.value?.setData(data);
        },
        validate: () => {
          if (formDataRef?.eqpStatus !== 1) return Promise.resolve();
          return tableRef.value?.validate() || Promise.resolve();
        },
        getSubData: () => {
          return tableRef.value?.getData() || {};
        },
      },
    ],
    defaultValues: {
      isUsable: '1',
    },
  });
</script>
