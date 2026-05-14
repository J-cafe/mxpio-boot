<template>
  <BasicModal
    width="1200px"
    :height="600"
    title="选择批次号"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
    cancelText="关闭"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import type { VxeGridPropTypes } from '@mxpio/components';
  import { executeSearchFormSchema } from './inventoryTask.data';
  import { lotPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'LotSelectModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['select', 'register']);
  const { createMessage } = useMessage();
  const formData = ref<Recordable>({});
  const columns: VxeGridPropTypes.Columns = [
    { type: 'radio', width: 40 },
    {
      title: '序号',
      type: 'seq',
      width: '50',
      align: 'center',
    },
    { title: '仓库', field: 'whCode', formatter: 'dictText', width: 100 },
    { title: '批次号', field: 'lotNo', width: 120 },
    { title: '批次索引号', field: 'subLotIndex', width: 100 },
    { title: '物料编码', field: 'itemCode', width: 100 },
    { title: '物料名称', field: 'itemName', width: 100 },
    { title: '规格型号', field: 'itemSpec', width: 100 },
    { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
    { title: '图号', field: 'drawingNo', width: 100 },
    { title: '库存数量', field: 'quantity', width: 100 },
  ];

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: columns,
    searchFormSchema: executeSearchFormSchema,
    pageApi: lotPage,
    vxeGridOptions: {
      height: 600,
      rowConfig: {
        keyField: 'id',
      },
      proxyConfig: {
        autoLoad: false,
      },
      toolbarConfig: {
        buttons: [],
      },
    },
    module: 'erp',
    filters: () => {
      return {
        'quantity@EQ': 0,
        'itemCode@EQ': formData.value.itemCode,
        'whCode@EQ': formData.value.whCode,
      };
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    formData.value = data.record;
    tableRef.value?.commitProxy('query');
  });

  function handleSubmit() {
    const selectedRows = tableRef.value?.getRadioRecord();
    if (!selectedRows) {
      createMessage.error('请选择批次');
      return;
    }
    emit('select', selectedRows);
    closeModal();
  }
</script>
