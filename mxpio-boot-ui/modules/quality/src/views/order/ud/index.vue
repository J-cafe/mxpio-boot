<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
              auth: authConfig.edit,
              ifShow: row.bpmnStatus === '01',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '提交',
              ifShow: row.bpmnStatus === '01',
              onClick: handleSubmit.bind(null, row),
              auth: authConfig.submit,
            },
            {
              label: '确认',
              onClick: handleJudge.bind(null, row),
              ifShow: row.bpmnStatus === '02',
              auth: authConfig.judge,
            },
          ]"
        />
      </template>
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <PurchaseQualityModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import { udPageApi, udLineListApi, udSubmitApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './ud.data';
  import PurchaseQualityModal from './QualityUDModal.vue';
  import ExpandTable from './ExpandTable.vue';

  const componentName = 'QualityUDList';
  const authConfig = {
    add: `erp:${componentName}:add`,
    edit: `erp:${componentName}:edit`,
    delete: `erp:${componentName}:delete`,
    import: `erp:${componentName}:import`,
    export: `erp:${componentName}:export`,
    judge: `erp:${componentName}:judge`,
    submit: `erp:${componentName}:submit`,
  };
  defineOptions({ name: componentName });
  const { gridOptions, registerModal, openModal, handleDetail, handleEdit, handleSuccess } =
    useListCrudHook({
      componentName,
      columns,
      authConfig,
      searchFormSchema,
      pageApi: udPageApi,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'bizNo',
        },
        expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
        toolbarConfig: {
          buttons: [],
          import: false,
        },
      },
    });

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await udLineListApi(row.bizNo);
    row.childList = res;
    return res;
  }

  async function handleSubmit(row: Recordable) {
    try {
      const res = await udSubmitApi(row.bizNo);
      if (res) {
        handleSuccess();
      }
    } catch (error) {
      console.error(error);
    }
  }

  async function handleJudge(row: Recordable) {
    openModal(true, {
      record: row,
      isUpdate: true,
      disabled: false,
      judge: true,
    });
  }
</script>
