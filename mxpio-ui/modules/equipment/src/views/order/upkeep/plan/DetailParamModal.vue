<template>
  <BasicModal
    width="900px"
    title="选项"
    v-bind="$attrs"
    destroyOnClose
    :showOkBtn="false"
    cancelText="关闭"
    @register="registerModal"
  >
    <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { VxeBasicTable, BasicModal, useModalInner } from '@mxpio/components';
  import type { VxeGridPropTypes } from '@mxpio/components';
  import { useListCrudHook } from '@mxpio/common';
  import { eqpUpkeepPlanParamPageApi } from '@mxpio/bizcommon';

  defineOptions({ name: 'DetailParamModal' });

  const detailId = ref('');

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    detailId.value = data.record.id;
  });

  const columns: VxeGridPropTypes.Columns = [
    {
      title: '序号',
      field: 'num',
    },
    {
      title: '选项值',
      field: 'parameter',
    },
  ];

  const { tableRef, gridOptions } = useListCrudHook({
    componentName: 'DetailParamModal',
    columns,
    pageApi: eqpUpkeepPlanParamPageApi,
    vxeGridOptions: {
      id: 'DetailParamTable',
      height: 350,
      tableClass: '!px-0 !py-0',
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        buttons: [],
        import: false,
        print: false,
        export: false,
        custom: false,
      },
    },
    filters: () => {
      return {
        'detailId@EQ': detailId.value,
      };
    },
    module: 'erp',
  });
</script>
