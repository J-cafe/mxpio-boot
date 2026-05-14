<template>
  <div class="m-3">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @checkbox-change="checkboxChange"
      @checkbox-all="checkboxChange"
    >
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
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              auth: auth.delete,
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ProcInfoModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import { deleteProcGroup, procGroupPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './procGroup.data';
  import ProcInfoModal from './ProcGroupModal.vue';

  const componentName = 'ProcGroupList';
  defineOptions({ name: componentName });
  const {
    auth,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: procGroupPage,
    deleteApi: deleteProcGroup,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'procGroupCode',
      },
    },
    module: 'erp',
  });
</script>
