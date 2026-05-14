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
              auth: auth.edit,
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ResModal width="900px" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import { resPage } from '../../api/common';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './res.data';
  import ResModal from './ResModal.vue';

  const componentName = 'ResList';
  defineOptions({ name: componentName });
  const { tableRef, auth, gridOptions, registerModal, handleEdit, handleDetail, handleSuccess } =
    useListCrudHook({
      componentName,
      columns,
      searchFormSchema,
      pageApi: resPage,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'typeCode',
        },
        toolbarConfig: {
          buttons: [],
          tools: [
            {
              toolRender: {
                name: 'ExportButton',
                props: {
                  export: `erp:${componentName}:export`,
                },
              },
            },
          ],
          import: false,
        },
      },
      module: 'erp',
    });
</script>
