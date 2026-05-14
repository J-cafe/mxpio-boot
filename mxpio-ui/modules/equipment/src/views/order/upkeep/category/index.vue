<template>
  <div class="m-3 bg-white">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @current-change="onCurrentChange"
      @data-change="onDataChange"
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
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="保养方案明细">
        <DetailTable :categoryId="currentRow.id" />
      </a-tab-pane>
    </a-tabs>
    <UpkeepCategoryModal width="600px" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import { deleteEqpUpkeepCategoryApi, eqpUpkeepCategoryPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './upkeepCategory.data';
  import UpkeepCategoryModal from './UpkeepCategoryModal.vue';
  import DetailTable from './DetailTable.vue';

  const componentName = 'EqpUpkeepCategoryList';
  defineOptions({ name: componentName });

  const currentRow = ref<Recordable>({});

  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpUpkeepCategoryPageApi,
    deleteApi: deleteEqpUpkeepCategoryApi,
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0',
      rowConfig: {
        keyField: 'id',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    module: 'erp',
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }
</script>
