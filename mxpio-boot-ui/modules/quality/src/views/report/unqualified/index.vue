<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #inspectionBillNo="{ row }">
        <a @click="handleOrder(row)">{{ row.inspectionBillNo }}</a>
      </template>
    </VxeBasicTable>
    <QualityOrderModal @register="registerModal" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { qualityUnqualifiedPageApi, qualityByCodeApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './unqualified.data';
  import QualityOrderModal from '../../order/quality/QualityOrderModal.vue';

  const componentName = 'QualityUnqualifiedList';
  defineOptions({ name: componentName });
  const [registerModal, { openModal }] = useModal();
  const { gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: qualityUnqualifiedPageApi,
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [],
        import: false,
      },
    },
    module: 'erp',
  });

  async function handleOrder(row) {
    try {
      const res = await qualityByCodeApi(row.inspectionBillNo);
      openModal(true, {
        record: res,
        disabled: true,
        isUpdate: true,
      });
    } catch (err) {
      console.error(err);
    }
  }
</script>
