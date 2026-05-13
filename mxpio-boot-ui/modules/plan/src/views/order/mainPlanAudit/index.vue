<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '审核',
              onClick: handleAudit.bind(null, row),
              auth: authConfig.audit,
              ifShow: row.bpmnStatus === '02',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <MainPlanAuditModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import { mpsTempPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './mainPlanAudit.data';
  import MainPlanAuditModal from './MainPlanAuditModal.vue';

  const componentName = 'MainPlanAuditList';
  defineOptions({ name: componentName });

  const authConfig = {
    audit: `erp:${componentName}:audit`,
  };

  const { gridOptions, registerModal, handleDetail, handleSuccess, openModal } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: mpsTempPageApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        buttons: [],
        import: false,
      },
    },
    module: 'erp',
  });

  async function handleAudit(row: Recordable) {
    try {
      openModal(true, {
        record: row,
        isUpdate: true,
      });
    } catch (error) {
      console.log(error);
    }
  }
</script>
