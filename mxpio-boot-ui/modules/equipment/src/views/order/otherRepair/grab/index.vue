<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" @data-change="onDataChange">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '抢单',
              onClick: handleGrab.bind(null, row),
            },
          ]"
        />
      </template>
      <template #bpmnSortFlag="{ row }">
        <a-tag v-if="row.bpmnSortFlag === '0'" color="#2db7f5">普通</a-tag>
        <a-tag v-else color="red">紧急</a-tag>
      </template>
    </VxeBasicTable>
    <OtherRepairApplyModal width="80%" @register="registerModal" />
    <OtherRepairGrabModal width="600px" @register="registerGrabModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Tag as ATag } from 'ant-design-vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { otherRepairPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './grab.data';
  import OtherRepairApplyModal from '../apply/OtherRepairApplyModal.vue';
  import OtherRepairGrabModal from './OtherRepairGrabModal.vue';

  const componentName = 'OtherRepairGrabList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const [registerGrabModal, { openModal: openGrabModal }] = useModal();
  const { tableRef, gridOptions, handleDetail, registerModal, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: otherRepairPageApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
      toolbarConfig: {
        buttons: [],
      },
    },
    filters: {
      'repairStatus@EQ': 10,
    },
    module: 'erp',
  });

  const handleGrab = async (row) => {
    try {
      openGrabModal(true, {
        record: row,
      });
    } catch (error) {
      console.log(error);
    }
  };

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setRadioRow(visibleData[0]);
  }
</script>
