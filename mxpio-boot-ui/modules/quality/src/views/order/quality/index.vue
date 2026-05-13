<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '检验数量',
              onClick: handleSample.bind(null, row),
              auth: authConfig.checkQuantity,
              ifShow: !row.checkQuantity,
            },
            {
              label: '检验',
              onClick: handleExecute.bind(null, row),
              auth: authConfig.execute,
              ifShow: !!row.checkQuantity && row.checkStatus !== '3',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '确认',
              onClick: handleJudge.bind(null, row),
              auth: authConfig.judge,
              ifShow: row.checkStatus === '2',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <SalesOrderModal @register="registerModal" @success="handleSuccess" />
    <SampleQuanlityModal @register="registerSampleModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { qualityPageApi, qualityConfigApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './qualityOrder.data';
  import SalesOrderModal from './QualityOrderModal.vue';
  import SampleQuanlityModal from './SampleQuanlityModal.vue';
  // import { useMessage } from '@mxpio/hooks';

  const componentName = 'QualityOrderList';
  defineOptions({ name: componentName });
  // const { createMessage } = useMessage();
  const [registerSampleModal, { openModal: openSampleModal }] = useModal();

  const authConfig = {
    checkQuantity: `erp:${componentName}:checkQuantity`,
    export: `erp:${componentName}:export`,
    execute: `erp:${componentName}:execute`,
    judge: `erp:${componentName}:judge`,
  };

  const { handleEdit, gridOptions, registerModal, openModal, handleDetail, handleSuccess } =
    useListCrudHook({
      componentName,
      columns,
      searchFormSchema,
      pageApi: qualityPageApi,
      configApi: qualityConfigApi,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'bizNo',
        },
        toolbarConfig: {
          buttons: [],
          import: false,
        },
      },
      module: 'erp',
    });

  async function handleExecute(row: Recordable) {
    handleEdit(row);
  }

  async function handleJudge(row: Recordable) {
    openModal(true, {
      record: row,
      isUpdate: true,
      disabled: true,
      judge: true,
    });
  }

  async function handleSample(row: Recordable) {
    openSampleModal(true, { record: row, isUpdate: true });
  }
</script>
