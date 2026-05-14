<!-- 审核发起单据示例 -->
<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
  <a-tabs>
    <a-tab-pane key="detailTable" tab="明细" forceRender>
      <DetailTable ref="tableRef" />
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts" setup>
  import { BasicForm } from '@mxpio/components';
  import { spSaveApi } from '@mxpio/bizcommon';
  import { useFormCrudHook } from '@mxpio/common';
  import { formSchema } from './saleForecast.data';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'SalesOrderForm' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, isDisabled, setFormData, getFormData, submitForm } = useFormCrudHook({
    formSchema,
    saveApi: spSaveApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      orderType: '01',
      bpmnStatus: '01',
      closeStatus: 'open',
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

  defineExpose({
    setFormData,
    submitForm,
    getFormData,
  });
</script>
