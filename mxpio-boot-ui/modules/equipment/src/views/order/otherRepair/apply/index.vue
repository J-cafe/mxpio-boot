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
              ifShow: row.repairStatus === '10',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '验收',
              onClick: handleCheck.bind(null, row),
              auth: authConfig.check,
              ifShow: row.repairStatus === '25',
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
              ifShow: row.repairStatus === '10',
            },
          ]"
        />
      </template>
      <template #bpmnSortFlag="{ row }">
        <a-tag v-if="row.bpmnSortFlag === '0'" color="#2db7f5">普通</a-tag>
        <a-tag v-else color="red">紧急</a-tag>
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="维修操作历史">
        <RecordTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
    </a-tabs>
    <OtherRepairApplyModal width="80%" @register="registerModal" @success="handleSuccess" />
    <OtherRepairCheckModal width="600px" @register="registerCheckModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Tag as ATag } from 'ant-design-vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { deleteOtherRepairApi, otherRepairPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './otherRepairApply.data';
  import OtherRepairApplyModal from './OtherRepairApplyModal.vue';
  import OtherRepairCheckModal from './OtherRepairCheckModal.vue';
  import RecordTable from './RecordTable.vue';

  const componentName = 'OtherRepairApplyList';
  defineOptions({ name: componentName });
  const authConfig = {
    check: `erp:${componentName}:check`,
  };
  const currentRow = ref<Recordable>({});
  const [registerCheckModal, { openModal: openCheckModal }] = useModal();

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
    pageApi: otherRepairPageApi,
    deleteApi: deleteOtherRepairApi,
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0',
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    module: 'erp',
  });

  function handleCheck(row) {
    openCheckModal(true, { bizNo: row.bizNo });
  }

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }
</script>
