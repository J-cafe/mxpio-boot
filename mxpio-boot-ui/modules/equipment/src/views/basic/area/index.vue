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
              label: '查看二维码',
              onClick: openQrcodeModal.bind(null, true, row),
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
    <AreaModal width="700px" @register="registerModal" @success="handleSuccess" />
    <AreaQrcodeModal width="500px" @register="registerQrcodeModal" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { deleteAreaApi, areaPageApi, areaChildListApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './area.data';
  import AreaModal from './AreaModal.vue';
  import AreaQrcodeModal from './AreaQrcodeModal.vue';

  const componentName = 'AreaList';
  defineOptions({ name: componentName });
  const [registerQrcodeModal, { openModal: openQrcodeModal }] = useModal();

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
    pageApi: areaPageApi,
    deleteApi: deleteAreaApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'id',
      },
      treeConfig: {
        transform: true,
        rowField: 'id',
        parentField: 'pid',
        lazy: true,
        hasChild: 'hasChild',
        loadMethod: async ({ row }) => {
          // const res = await areaChildListApi(row.id);
          // return res.data || [];
          return await areaChildListApi(row.id);
        },
      },
    },
    module: 'erp',
  });
</script>
