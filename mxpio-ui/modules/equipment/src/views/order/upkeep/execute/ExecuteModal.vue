<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="填写保养信息"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="1" tab="保养详情">
        <ExecuteDetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { submitEqpUpkeepApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { Tabs as ATabs } from 'ant-design-vue';
  import ExecuteDetailTable from './ExecuteDetailTable.vue';

  defineOptions({ name: 'EqpUpkeepExecuteModal' });
  const emit = defineEmits(['success', 'register']);
  const recordRef = ref<Recordable>({});
  const formSchema: FormSchema[] = [
    {
      field: 'actualHourRation',
      label: '保养工时',
      component: 'InputNumber',
      componentProps: {
        className: 'w-full',
      },
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'attachFileBefore',
      label: '保养前照片',
      component: 'Upload',
      componentProps: () => {
        return {
          accept: ['image/*'],
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'attachFile',
      label: '保养后照片',
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

  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    title: '填写保养信息',
    formSchema,
    saveApi: submitEqpUpkeepApi,
    openAfter: (data) => {
      recordRef.value = data.record || {};
    },
    classifyIntoFormData: (values) => {
      let upkeepTaskDetails: Recordable = tableRef.value?.getData() || [];
      return {
        upkeepTaskDetails,
        bizNo: recordRef.value.bizNo,
        attachFileBefore: values.attachFileBefore,
        attachFile: values.attachFile,
        actualHourRation: values.actualHourRation,
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
          return tableRef.value?.validate() || Promise.resolve();
        },
        getSubData: () => {
          return tableRef.value?.getData() || {};
        },
      },
    ],
  });
</script>
