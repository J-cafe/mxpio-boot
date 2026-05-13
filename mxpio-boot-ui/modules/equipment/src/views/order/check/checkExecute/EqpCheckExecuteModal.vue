<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="点检执行"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs v-if="formDataRef?.eqpStatus === 1">
      <a-tab-pane key="1" tab="点检详情">
        <ExecuteDetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { eqpCheckTaskExecuteApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { Tabs as ATabs } from 'ant-design-vue';
  import ExecuteDetailTable from './ExecuteDetailTable.vue';

  defineOptions({ name: 'EqpCheckExecuteModal' });
  const emit = defineEmits(['success', 'register']);
  const recordRef = ref<Recordable>({});
  const formSchema: FormSchema[] = [
    {
      field: 'eqpStatus',
      label: '开关机状态',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
          options: [
            {
              label: '开机',
              value: 1,
            },
            {
              label: '未开机',
              value: 0,
            },
          ],
        };
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'pic',
      label: '点检照片',
      component: 'Upload',
      componentProps: () => {
        return {
          accept: ['image/*'],
        };
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
  ];
  const tableRef = ref<InstanceType<typeof ExecuteDetailTable>>();

  const { registerForm, registerModal, isDisabled, handleSubmit, formDataRef } = useModalFormCrud({
    title: '点检执行',
    formSchema,
    saveApi: eqpCheckTaskExecuteApi,
    openAfter: (data) => {
      recordRef.value = data.record || {};
    },
    classifyIntoFormData: (values) => {
      let equipmentCheckTaskDetailList: Recordable = {};
      if (values.eqpStatus === 1) {
        equipmentCheckTaskDetailList = tableRef.value?.getData() || [];
      }
      return {
        equipmentCheckTaskDetailList,
        bizNo: recordRef.value.bizNo,
        eqpStatus: values.eqpStatus,
        pic: values.pic,
      };
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
      eqpStatus: 1,
    },
  });
</script>
