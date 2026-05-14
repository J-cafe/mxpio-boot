<template>
  <div class="m-3">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @checkbox-all="checkboxChange"
      @checkbox-change="checkboxChange"
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
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <EqpGroupModal width="1000px" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import { deleteEqpGroup, eqpGroupPage, getEqpGroupMember } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './eqpGroup.data';
  import EqpGroupModal from './EqpGroupModal.vue';
  import ExpandTable from './ExpandTable.vue';

  const componentName = 'EqpGroupList';
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
    pageApi: eqpGroupPage,
    deleteApi: deleteEqpGroup,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'id',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
    module: 'erp',
  });

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await getEqpGroupMember(row.id);
    row.childList = res;
    return res;
  }
</script>
