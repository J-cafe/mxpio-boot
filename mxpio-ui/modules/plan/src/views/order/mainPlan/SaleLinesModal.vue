<template>
  <BasicModal
    width="1200px"
    title="销售单明细"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" :tableClass="tableClass" v-bind="gridOptions" height="400px" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import { saleLinesColumns, saleSearchFormSchema } from './mainPlan.data';
  import { useListCrudHook } from '@mxpio/common';
  import { mpsPoPageApi } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import type { RequestCriteriaParams } from '@mxpio/types';

  const componentName = 'SaleLinesModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);

  let versionCode = ref<string>('');
  const tableClass = '!px-0 !py-0';
  const { createMessage } = useMessage();

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    module: 'erp',
    columns: saleLinesColumns,
    searchFormSchema: saleSearchFormSchema,
    pageApi: async (params?: RequestCriteriaParams) => {
      if (params) {
        params.versionCode = versionCode.value;
      }
      const res = await mpsPoPageApi(params);
      return res;
    },
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
      proxyConfig: {
        autoLoad: false,
      },
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    versionCode.value = data.versionCode;
    tableRef.value?.commitProxy('query');
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectedRows = tableRef.value?.getCheckboxRecords();
      if (!selectedRows || selectedRows.length === 0) {
        createMessage.error('请选择销售单');
        return;
      }
      closeModal();
      emit('success', selectedRows);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
